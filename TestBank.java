public class TestBank {
	public static void main(String[] args) throws Exception {
		testGivenParameters();

		testOtherParameters();

		testNoCustomers();

		testShortDay();
	}

	private static void testGivenParameters() throws Exception {
		Bank bank = new Bank( /* dayLength(minutes) */ 240, /* tellerIdleMean(minutes) */ 10, /* tellerIdleVar */ 5,
				/* tellerCount */ 2, /* customerArrivalMean(minutes) */ 2, /* customerArrivalVar */ 10,
				/* customerServeTimeMean(minutes) */ 10, /* customerServeTimeVar */ 30, /* samplingRate */ 30);

		bank.start();

		Thread.sleep(40000);

		if (bank.isAlive()) {
			System.err.println("\'testGivenParameters\' does not stop");
			System.exit(1);
		}

	}

	private static void testOtherParameters() throws Exception {
		Bank bank = new Bank( /* dayLength(minutes) */ 210, /* tellerIdleMean(minutes) */ 15, /* tellerIdleVar */ 5,
				/* tellerCount */ 5, /* customerArrivalMean(minutes) */ 2, /* customerArrivalVar */ 5,
				/* customerServeTimeMean(minutes) */ 10, /* customerServeTimeVar */ 30, /* samplingRate */ 30);

		bank.start();

		Thread.sleep(40000);

		if (bank.isAlive()) {
			System.err.println("\'testOtherParameters\' does not stop");
			System.exit(1);
		}

	}

	private static void testNoCustomers() throws Exception {
		// verify that
		// that the tellers are waiting for customers as long as the bank is working.
		// When the working day is finished, the bank is stopped with all the tellers.
		Bank bank = new Bank( /* dayLength(minutes) */ 60, /* tellerIdleMean(minutes) */ 5, /* tellerIdleVar */ 1,
				/* tellerCount */ 5, /* customerArrivalMean(minutes) */ 300, /* customerArrivalVar */ 0,
				/* customerServeTimeMean(minutes) */ 2, /* customerServeTimeVar */ 1, /* samplingRate */ 30);

		bank.start();

		Thread.sleep(20000);

		if (bank.isAlive()) {
			System.err.println("\'testNoCustomers\' does not stop");
			System.exit(1);
		}

	}

	private static void testShortDay() throws Exception {
		// verify that no customers are in the system,
		// that the tellers are waiting for customers,
		// and that the bank is stopped at the end with all the tellers.
		Bank bank = new Bank(/* dayLength(minutes) */ 1, /* tellerIdleMean(minutes) */ 15, /* tellerIdleVar */ 5,
				/* tellerCount */ 5, /* customerArrivalMean(minutes) */ 10, /* customerArrivalVar */ 5,
				/* customerServeTimeMean(minutes) */ 10, /* customerServeTimeVar */ 30, /* samplingRate */ 30);

		bank.start();

		Thread.sleep(5000);
		if (bank.isAlive()) {
			System.err.println("\'testShortDay\' does not stop");
			System.exit(1);
		}

	}
}
