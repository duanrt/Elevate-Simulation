package elevatorSimulation;

enum Sex {
	male,
	female
}

public class Person {
	Floor fromFloor;
	Floor toFloor;
	Sex sex;
	
	public Person() {
		fromFloor = toFloor = new Floor();
	}
	
	public Person(Floor f, Floor t) {
		fromFloor = f;
		toFloor = t;
	}
	
	public Floor getFromFloor() { return fromFloor; }
	
	public Floor getToFloor() { return toFloor; }
	
	public void printPerson() {
		System.out.println(" Person goes from floor " + fromFloor.number + " to floor " + toFloor.number);
	}
	
	public void setSex(Sex s) {
		sex = s;
	}
}

// can not define as public unless it is in its own file
class Man extends Person {
	Sex sex;
	
	public Man(Floor f, Floor t) {
		super(f, t);
		sex = Sex.male;
	}	
}