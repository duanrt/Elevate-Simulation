package elevatorSimulation;

import java.util.ArrayList;

enum Direction {
	up,
	down,
	idle
}

enum Event {
	door_open,
	door_close
}

public class Elevator {		
	int capacity;
	int numOfPersons;
	int currentFloor;
	int toFloor;
	ArrayList<Integer> floorsSelected;
	int travelTime;
	Direction direction;
	Event event;
	ArrayList<Person> persons;	
	
	public Elevator(int c, int t, Direction d) {
		capacity = 10;
		numOfPersons = 0;
		currentFloor = c;
		toFloor = t;
		travelTime = 5;
		direction = d;
		event = Event.door_close;
		persons = new ArrayList<Person>();
	}

	public void addPerson(Person p) {
		persons.add(p);
		numOfPersons++; 
	}
	
	public int getCurrentFloor() { return currentFloor; }

}
