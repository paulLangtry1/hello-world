package bankAccount;

import java.util.ArrayList;
import javax.swing.border.TitledBorder;



import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;

public class BankAccountGUI extends JFrame{
	
	// Menu structure
	private JMenuBar myBar;
	private JMenu fileMenu, recordMenu;
	private JMenuItem fileLoad, fileSaveAs, removeAccount;
	
	
	//Arraylist of account objects
	private ArrayList <Account> arrData;

	//Table components
	private JTable myTable;
	private MyTableModel tm;
	private JScrollPane myPane;
	
		
	//Form panel components
	private JLabel nameLabel = new JLabel("Name");
    private JTextField nameField = new JTextField(10);
	private JLabel accountNoLabel = new JLabel("Account Number");
	private JTextField accountNoField= new JTextField(10);
	private JLabel balanceLabel = new JLabel("Balance");
	private JTextField balanceField= new JTextField(10);
	private JButton addButton = new JButton("Add Account");
	private JPanel formPnl = new JPanel(); 
	private JPanel tblPnl = new JPanel(); 
	
	//Thread panel
	private JPanel threadPnl = new JPanel();
	private JTextArea threadOutput = new JTextArea();
	private JButton threadButton = new JButton("Thread simulation");
	
	//File variables

	private File file = new File("accounts.dat");	
	private File currentDirectory;  
	
	

	
	public BankAccountGUI(){  
		// Setting up menu
		setUpMenu();

		//create array of account objects and pass to custom TableModel
		arrData = new ArrayList<Account>();
		tm = new MyTableModel(arrData);		
		myTable = new JTable(tm);
		

		
		
		myPane = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		myTable.setSelectionForeground(Color.white);
		myTable.setSelectionBackground(Color.red);
		myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		
		JFileChooser fileChooserfc = new JFileChooser();
		fileChooserfc.setCurrentDirectory(new File(System.getProperty("user.dir")));

		//create form panel
		 createFormPanel();
		 
		//Event Listeners

		 //TODO:1 Add Account Event
		 addButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evt) {
					double balance;
					int accountNo;
					
					Account acc = null;
					try {
						if (nameField.getText().isEmpty() || accountNoField.getText().isEmpty() || balanceField.getText().isEmpty() )
							JOptionPane.showMessageDialog(BankAccountGUI.this, "All textfields must have a value");

						else {
							String name = nameField.getText();
							balance = Double.parseDouble(balanceField.getText());
							accountNo = Integer.parseInt(accountNoField.getText());
							acc = new Account(name, accountNo, balance);
							//customersArea.append(p.toString());
							
							arrData.add(acc);
							tm.fireTableDataChanged();
							nameField.setText("");
							balanceField.setText("");
							accountNoField.setText("");
						}
							
					}
					catch(NumberFormatException ex) {
						JOptionPane.showMessageDialog(BankAccountGUI.this, "balance/account number must be a number");
					}
					
				}
				
			});


		// TODO:2 Save the data to a file (save event handler)
		
		// Use provided function: writeDataFile() to save the data into the file#
		 fileSaveAs.addActionListener(new ActionListener() 
			{			
				public void actionPerformed(ActionEvent e) 
				{
					
					if(arrData.size()<=0)
					{
						JOptionPane.showMessageDialog(BankAccountGUI.this,"You must have content to Save !!");
					}
					else
					{
						int retVal = fileChooserfc.showSaveDialog(BankAccountGUI.this);
						
						if(retVal == JFileChooser.APPROVE_OPTION) 
						{
							currentDirectory = fileChooserfc.getSelectedFile();
							try {
								writeDataFile(file);
							} catch (FileNotFoundException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						BankAccountGUI.this.arrData.clear();
						tm.fireTableDataChanged();
					}
					
					
				}
			});



		// TODO:3 Loading the contents of a file into the table (load event handler)
		// Use provided function: readDataFile() to save the data into the file
		 fileLoad.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					
					
					int returnVal= fileChooserfc.showOpenDialog(BankAccountGUI.this);
					
					
					if (returnVal == JFileChooser.APPROVE_OPTION) 
					{
						File file= fileChooserfc.getSelectedFile();
						try {
							readDataFile(file);
							tm.fireTableDataChanged();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				}
			});

		 
		//TODO: 4 Remove Selected Account Event
		 removeAccount.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e)
				{
					
					int row = myTable.getSelectedRow();
					
					if(row == -1)
					{
						JOptionPane.showMessageDialog(BankAccountGUI.this,"You must Select an Account to Delete");
					}
					else
					{
						arrData.remove(row);
						tm.fireTableRowsDeleted(arrData.size(), arrData.size());
						
						
						
					}
					
					
					
					
				}    	  
			});
		 
		//TODO: 5 Thread Button simulation Event
		 threadButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent evt) 
				{
					
					WithdrawDepositThreads wdt = new WithdrawDepositThreads();//unable to get thread to run 
				
			    }});
		 


		
		
		// Adding menu bar
		
		setJMenuBar(myBar);
		
		//add scrollpane with table
		tblPnl.add(myPane);
		
		//add thread button to thread panel
		threadPnl.add(threadButton);
		threadPnl.add(threadOutput);
		
		//add panel for form
		add(formPnl, BorderLayout.NORTH);
		TitledBorder title = BorderFactory.createTitledBorder("Account Summary");
		tblPnl.setBorder(title);
		add(tblPnl, BorderLayout.CENTER);
		add(threadPnl, BorderLayout.SOUTH);
		
		
		this.setTitle("Bank Account Details");
		this.setVisible(true);
		this.pack();
	} // constructor


	private void setUpMenu() {
		fileLoad = new JMenuItem("Open");
		fileSaveAs = new JMenuItem("Save As");
		
		fileMenu = new JMenu("File");
		fileMenu.add(fileLoad);
		fileMenu.add(fileSaveAs);
		
		removeAccount = new JMenuItem("Remove Account");
		
		recordMenu = new JMenu("Account");
		recordMenu.add(removeAccount);
		
		myBar = new JMenuBar();
		myBar.add(fileMenu);
		myBar.add(recordMenu);

	}

	public void writeDataFile(File f) throws IOException, FileNotFoundException 
	{
		FileOutputStream fo = null;
		ObjectOutputStream os = null;
		
		fo = new FileOutputStream(f);
		os = new ObjectOutputStream(fo);
		os.writeObject(arrData);
		os.close();
	}

	public void readDataFile(File f) throws IOException, ClassNotFoundException 
	{
		FileInputStream fi = null;
		ObjectInputStream in = null;
		ArrayList<Account>arr = null;
		
		fi = new FileInputStream(f);
		in = new ObjectInputStream(fi);
		
		arr = (ArrayList<Account>)in.readObject();
		
		arrData.clear();
		arrData.addAll(arr);
		
		fi.close();
		in.close();
		
	
	}
	
	public void closeDown() {
			System.exit(0);
	}
	
	public void createFormPanel() {
		    formPnl.setLayout(new GridBagLayout());
			TitledBorder title = BorderFactory.createTitledBorder("Add Account");
			formPnl.setBorder(title);
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor =  GridBagConstraints.WEST;
		    c.insets = new Insets(10,0,0,0);  
		    formPnl.add(nameLabel, c);
		    c.gridx = 1;
		    formPnl.add(nameField, c);
			c.gridx = 0;
		    c.gridy = 1;
		    formPnl.add(accountNoLabel, c);
		    c.gridx = 1;
		    formPnl.add(accountNoField,c);
			c.gridx = 0;
		    c.gridy = 2;
		    formPnl.add(balanceLabel,c);
		    c.gridx = 1;
		    formPnl.add(balanceField,c);
			c.gridx = 0;
		    c.gridy = 3;
		    c.gridwidth = 2; 
		    c.fill = GridBagConstraints.NONE;
		    c.anchor =  GridBagConstraints.CENTER; 
		    formPnl.add(addButton, c);
	   }
	
	public static void main (String args[]){
		new BankAccountGUI();
	} // main
} //class