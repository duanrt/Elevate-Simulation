package elevatorSimulation;

public class Building {
	private int numOfFloors;
	Elevator elevator;
	
	public Building(int n) {
		numOfFloors = n;
		elevator = new Elevator(1, 1, Direction.idle);
		System.out.println("Elevator is generated. Idle at floor 1");
	}
	
	public Building (int n, Elevator e) {
		numOfFloors = n;
		elevator = e;
	}
	
	public int getNumOfFloors() {
		return numOfFloors;
	}
}
