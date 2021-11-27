package elevatorSimulation;

public class Floor {	
	int number;
	public int personsWaiting;
	
	public Floor() {
		number = 1;
		personsWaiting = 0;
	}
	
	public Floor(int n) {
		number = n;
		personsWaiting = 0;
	}
	
	public Floor (int n, int p) {
		number = n;
		personsWaiting = p;
	}
	
	public int getFloor() { return number; }
	
}
