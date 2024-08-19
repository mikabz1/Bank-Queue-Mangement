import java.util.Queue;

/**
 * Teller - represents a bank clerk
 * 
 */
public class Teller extends Thread {
	private int tellerNumber; //the id of the teller
	private int custNumber; //the id of the last (current) served customer
	private int customersServed; //the number of the customers which are served by the this teller
	private Bank bank;
	private boolean isBussy;
	public Teller(Bank bank,int tellerNumber){
		this.tellerNumber = tellerNumber;
		this.bank = bank;
		customersServed = 0;
		isBussy = false;
	}

	/*
	 * Gettetrs and setters
	 */
	public int getCustNumber() {
		return custNumber;
	}

	public int getTellerNumber() {
		return tellerNumber;
	}

	public boolean isServing() {
		return isBussy;
	}
	
	public boolean isIdle() {
		return false;
	}

	/**
	 * run - main thread action
	 */
	public void run() {
		isBussy = false;
		while (bank.getClock().isWorking()){
			Customer c = null;
			synchronized (bank.getCustomersQueue()){
				try {
					bank.getCustomersQueue().wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				if(!bank.getCustomersQueue().isEmpty()){
					c = bank.getCustomersQueue().poll();
					isBussy = true;
				}
			}
			if (c != null){
				this.custNumber = c.getCustNumber();
				synchronized (c){
					c.notify();
					c.serve();
				}
                try {
                    c.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
				isBussy = false;
				customersServed++;
            }
        }
		while (!bank.getCustomersQueue().isEmpty()){
			Customer c = null;
			synchronized (bank.getCustomersQueue()){
				if(!bank.getCustomersQueue().isEmpty()){
					c = bank.getCustomersQueue().poll();
					isBussy = true;
				}
			}
			if (c != null){
				this.custNumber = c.getCustNumber();
				synchronized (c){
					c.notify();
					c.serve();
				}
				try {
					c.join();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				isBussy = false;
				customersServed++;
			}
		}
		System.out.println("teller no " + tellerNumber + " served " + customersServed + " customers");
	}

}
