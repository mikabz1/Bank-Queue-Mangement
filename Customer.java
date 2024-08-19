import java.util.Queue;

/**
 * Customer - represents a client of the bank.
 * 
 */
public class Customer extends Thread {
	private int custNumber; //the id of the customer
	private Bank bank;
	private int serviceTime;
//	private boolean flag;
//	private int state = 0;
	private int waitTime = 0;
	private int timerStart = 0;

	public Customer(Bank bank,int serviceTime,int custNumber){
		this.custNumber = custNumber;
		this.bank = bank;
		this.serviceTime = serviceTime;
//		flag = false;
	}

	/*
	 * Getters and setters
	 */
	public int getCustNumber() {
		return custNumber;
	}
//	public int getServiceTime(){
//		return serviceTime;
//	}

	/**
	 * run - main thread action
	 */
	public void run() {
		synchronized (this){
			timerStart = bank.getClock().getCurrentTime();
			try {
				notify();
				wait();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		System.out.println("customer num: " + custNumber + " time wait: " + waitTime);
		bank.decreasCusCount();
    }

	/**
	 * serve - simulate the service the customer is getting. This method is called
	 * only by the teller servicing this customer. In addition to holding up the
	 * teller for the duration of the service, it also notifies this customer's
	 * thread that it has been serviced and therefore may terminate.
	 */
	public void serve()  {
		waitTime = timerStart - bank.getClock().getCurrentTime();
		try {
			sleep((long)serviceTime*Bank.TIME_SIMULATION_FACTOR);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
    }

	@Override
	public String toString() {
		return String.valueOf(custNumber);
	}
	
	
} /* class Customer */
