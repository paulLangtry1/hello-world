package bankAccount;

public class WithdrawDepositThreads implements Runnable
{
	
	Account acc = new Account();
	
	 public static void main(String[] args) {
	        WithdrawDepositThreads wpt = new WithdrawDepositThreads();
	        Thread t1 = new Thread(wpt, "Withdrawing");
	        Thread t2 = new Thread(wpt, "Depositing");
	        
	        t1.start();
	        t2.start();
	       
	    }

	public WithdrawDepositThreads() 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() 
	{
		for (int i = 0; i < 2; i++) {
	        try {
	            
	            acc.withdraw(100);
	            try {
	                Thread.sleep(200);
	            } catch (InterruptedException ex) {
	               
	            }
	            if (acc.getBalance() < 0) {
	                System.out.println("account is overdrawn!");
	            }
	            acc.deposit(200);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    System.out.println("Final Acc balance is Rs." + acc.getBalance());
	}
}

