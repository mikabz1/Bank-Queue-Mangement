import java.util.Set;

/**
 * SampleQueues - periodically wake up and sample the Bank's queues for the
 * purpose of gathering statistics.
 * 
 */
public class Sampler extends Thread {
	private Bank bank;
	private double rate;
	private int samples;

	/**
	 * Constructor -
	 *
	 * @param bank
	 *            - the instance of the Bank
	 * @param rate
	 *            - the period to wait between samples(min)
	 */
	public Sampler(Bank bank, double rate) {
		this.bank = bank;
		this.rate = rate;
		this.samples = 0;
	}

	public void run() {
		while (bank.getClock().isWorking() || bank.isActive()) {
			try {
				sleep((long) rate * Bank.TIME_SIMULATION_FACTOR);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(sample());
		}
		System.out.println("The number of samples is " + samples);
	}

	/**
	 * sampleQueues - collects statistical data from the queues
	 *
	 * @return
	 */
	private String sample() {
		StringBuffer sb = new StringBuffer();
		Set<Teller> tellers = bank.getTellers();
		sb.append("Sample:  " + bank.getCustomersQueue() + " customers are waiting.\n");
		for (Teller t : tellers) {
			sb.append("Sample: Teller " + t.getTellerNumber() + " is ");
			if (t.isIdle())
				sb.append("idle");
			else {
				sb.append("active");
				if (t.isServing())
					sb.append(", currently serving customer " + t.getCustNumber());
			}
			sb.append("\n");
		}

		samples++;

		return (sb.toString());
	}

} /* class SampleQueues */
