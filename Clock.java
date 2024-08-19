/**
 * WorkingHours - a main clock regulating the bank's work day
 * 
 */
public class Clock extends Thread {
	
	private int dayLength; // minutes
	private boolean isWorking;

	public Clock(int dayLength){
		this.dayLength = dayLength;
		isWorking = true;
	}

	public void run() {
		//isWorking = true;
		while (dayLength > 0){
            try {
                sleep(Bank.TIME_SIMULATION_FACTOR);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
			this.dayLength--;
        }
		isWorking = false;
	}

	/*
	 * Getters and setters
	 */
	public boolean isWorking() {
		return isWorking;
	}
	public int getCurrentTime(){
		return this.dayLength;
	}

}
