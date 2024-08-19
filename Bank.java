import java.util.*;

/**
 * Bank - launches the simulation and keeps feeding it with new customers
 * 
 */
public class Bank extends Thread {
	/*
	 * Determines the ratio between the simulated time and running time.
	 * Specifically, this number states how many milliseconds should the program
	 * wait to simulate one minute, if periods in the constructor to Bank are
	 * specified in minutes. Thus, if 'TIME_SIMULATION_FACTOR' is set to 1, a
	 * service time of 1 minutes will be simulated as 1 milliseconds, and a service
	 * time of 10 minutes will be simulated as 10 milliseconds.
	 * 
	 * A good value is 60, making the simulation clock run 1000 faster than the
	 * processes it simulates. Then a service time of 1 minutes(=60 seconds) will be
	 * simulated as 60 milliseconds. Simulating an 8-hour day should take about 30
	 * seconds, since 8 hours/1000 = 30 seconds.
	 */
	public static final int TIME_SIMULATION_FACTOR = 60;
	
	private static Random random = new Random();

	private Queue<Customer> queue;
	
	
	/*
	 * custCount - number of active customers. It is incremented each time Bank
	 * launches one, and is decremented whenever a customer terminates. It is used
	 * to determine if there is still any activity in the bank.
	 */
	private int custCount;

	/*
	 * statistical parameters relating to customers:
	 */
	private double custArrivalMean;
	private double custArrivalVar;
	private double custServeTimeMean;
	private double custServeTimeVar;


	/*
	 * tellers - set of tellers in the bank
	 */
	private Set<Teller> tellers;

	/*
	 * statistical parameters relating to tellers:
	 */
	private int tellerCount;
	private double tellerIdleMean;
	private double tellerIdleVar;

	/*
	 * variables related to the sampling task
	 */
	private Sampler sampler;
	private int samplingRate;

	/*
	 * parameters relating to the clock and working hours
	 */
	private int dayLength; // the dayLength (minutes)
	private Clock clock;

	/**
	 * 
	 * Constructor - for bank: sets up all time distributions for various simulation
	 * aspects, such as customer arrival rate and teller work cycle. All periods are
	 * stated in minutes unless otherwise specified.
	 *
	 * @param dayLength
	 *            	- length of work day (minutes)
	 *  
	 * @param tellerIdleMean, tellerIdleVar
	 *  				- mean and variance of teller's idle time
	 *  
	 * @param tellerCount
	 *            	- number of tellers to simulate
	 * 
	 * @param custArrivalMean, custArrivalVar
	 *			  	- mean and variance of customer inter-arrival periods
	 * 
	 * @param custServeTimeMean, custServeTimeVar
	 * 				- mean and variance of duration of customer's required service
	 * 
	 * @param samplingRate
	 *           	- delay between samples taken by the observer
	 */
	public Bank(int dayLength, double tellerIdleMean, double tellerIdleVar, int tellerCount, double custArrivalMean,
			double custArrivalVar, double custServeTimeMean, double custServeTimeVar, int samplingRate) {
		this.dayLength = dayLength;

		this.tellerCount = tellerCount;
		this.custArrivalMean = custArrivalMean;
		this.custArrivalVar = custArrivalVar;
		this.custServeTimeMean = custServeTimeMean;
		this.custServeTimeVar = custServeTimeVar;
		this.samplingRate = samplingRate;

		this.sampler = new Sampler(this,samplingRate);
		this.clock = new Clock(dayLength);
		tellers = new HashSet<>();
		queue = new LinkedList<>();
		custCount = 0;
	}


	/**
	 * gaussian - compute a random number drawn from a normal (Gaussian)
	 * distribution
	 *
	 * @param periodMean
	 *            - the mean of the distribution
	 * @param periodVar
	 *            - the variance of the distribution
	 * @return
	 */
	public static int gaussian(double periodMean, double periodVar) {
		double period = 0;
		while (period < 1)
			period = periodMean + periodVar * random.nextGaussian();
		return ((int) (period));
	}

	/*
	 * Getters and Setters
	 */
	public void decreasCusCount(){
		this.custCount--;
	}

	public Set<Teller> getTellers() {
		return tellers;
	}

	public Clock getClock() {
		return clock;
	}
	public Queue<Customer> getCustomersQueue() {
		return queue;
	}
	
	
	public boolean isActive() {
		return ((custCount > 0) || (tellers.size() > 0));
	}

	/**
	 * printParams - print the simulation parameters
	 *
	 */
	public void printParams() {
		StringBuffer sb = new StringBuffer();

		sb.append("Bank Simulation\t\t (all periods in minutes)\n");
		sb.append("\tLength of work day:     " + dayLength / 60 + " hours)\n");
		sb.append("\tNumber of tellers:      " + tellerCount + "\n");
		sb.append("\tTeller idle period:     " + tellerIdleMean + " +/- " + tellerIdleVar + "\n");
		sb.append("\tCustomer arrival rate:  " + custArrivalMean + " +/- " + custArrivalVar + "\n");
		sb.append("\tCustomer service time:  " + custServeTimeMean + " +/- " + custServeTimeVar + "\n");
		sb.append("\tQueue sampling rate :   every " + samplingRate + "\n");

		System.out.println(sb.toString());
	}

	/**
	 * run - main thread action
	 */
	public void run() {
		clock.start();
		for(int i = 1;i <= tellerCount;i++){
			tellers.add(new Teller(this,i));
		}
		for(Teller t:tellers){
			t.start();
		}
		sampler.start();
		int customeCounter = 1;
		while(clock.isWorking()) {
			try {
				Thread.sleep((long) gaussian(custArrivalMean, custArrivalVar)*TIME_SIMULATION_FACTOR);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			if(!clock.isWorking()){
				synchronized (getCustomersQueue()) {
					getCustomersQueue().notifyAll();
				}
				break;
			}
			Customer c = new Customer(this, gaussian(custServeTimeMean, custServeTimeVar),customeCounter++);
			c.start();
			synchronized (c){
				try {
					c.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
            synchronized (getCustomersQueue()) {
				queue.add(c);
				getCustomersQueue().notifyAll();
			}
			custCount++;
		}
		for(Teller t:tellers){
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
		tellers.clear();
	}

} /* class Bank */
