package elevatorSimulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class Simulation extends Thread {
	Building building;
	HashMap<Integer, Integer> personsWaiting; // [ floor : number ]
	HashMap<Integer, Queue<Person>> allPersons; // [ floor : [ persons ] ]

	public Simulation(Building b) {
		building = b;
		personsWaiting = new HashMap<Integer, Integer>();
		allPersons = new HashMap<Integer, Queue<Person>>();
		for (int i = 1; i <= b.getNumOfFloors(); i++) {
			// Queue is interface, need to be implemented as LinkedList, PriorityQueue, etc
			allPersons.put(i, new LinkedList<Person>());
		}
	}
	
	// To synchronize a method, use "public synchronized void printPersonQueue()"
	public void printPersonQueue() {
		while (true) {
			System.out.println("====== Printing persons waiting for elevator ============");
			
			synchronized(this) {  //synchronized block
				for (Map.Entry<Integer, Queue<Person>> e : allPersons.entrySet()) {
					Queue<Person> q = e.getValue();
					for (Person p : q) {
						if (p != null)
							p.printPerson();
					}
				}				
				for (Map.Entry<Integer, Integer> e : personsWaiting.entrySet()) {
					System.out.println("There are " + e.getValue() + " persons waiting at floor " + e.getKey());
				}
			}

			try {
				sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void generatePersons(int floors) {
		while (true) {			
			int floorFrom = (int) (1 + Math.random() * floors);
			int floorTo = (int) (1 + Math.random() * floors);
			if (floorFrom != floorTo) {
				Floor f = new Floor(floorFrom);
				Floor t = new Floor(floorTo);
				
				synchronized(this) {
					Queue<Person> q = allPersons.get(floorFrom);
		            Person p = new Person(f, t);
		            if (floorFrom % 2 == 0) {
		            	p.setSex(Sex.male);
		            }
		            else {
		            	p.setSex(Sex.female);
		            }
		            
		            try {
		            	// Queue methods:  add(), peek(), poll()
		            	q.add(p);
		            } catch (Exception e) {
		            	e.printStackTrace();
		            }
		            
		            // HashMap methods: get(), put()
		            allPersons.put(floorFrom, q);
		            
		            int newValue = (personsWaiting.get(floorFrom) == null ? 0 : personsWaiting.get(floorFrom));		            					
		            personsWaiting.put(floorFrom, newValue + 1 );	
		            
		            System.out.println("Person generated, going from floor " + floorFrom + " to floor " + floorTo);
				}

				try {
					sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}
	}
	
	public boolean peopeWaitingAt(Elevator e, int f) {
		if (personsWaiting == null || personsWaiting.get(f) == null)
			return false;
		
	    if ((personsWaiting.get(f) > 0) && (e.capacity < e.numOfPersons)) {
	    	return true;
	    }
	    return false;
	}
	
	public synchronized void travelTo(Elevator e, Floor to) {
	    if (e.getCurrentFloor() != to.getFloor()) {
	        System.out.println("Elevator is going from floor " + e.getCurrentFloor() + " to " + to.getFloor());
	        int floors = Math.abs(e.getCurrentFloor() - to.getFloor());
	        Direction direction = Direction.up;
	        if (e.getCurrentFloor() > to.getFloor())
	            direction = Direction.down;
	
	        for (int i = 1; i < floors + 1; ++i) {
	            // at each floor, check if there is person waiting and if capacity is full, and then open door to get more people in
	            int next = ((direction == Direction.up) ? (e.getCurrentFloor() + 1) : (e.getCurrentFloor() - 1));
	            if (peopeWaitingAt(e, next)) {
	            	System.out.println("TBD: need stop at floor " + next + " to take people in");
	            	// TBD: need stop and let person waiting come in
	
	            	
	            } else {
	            	if (i != floors)
	            		System.out.println("Elevator is passing floor " + next);
	            	else {
	            		// now arrived at to floor
	            		System.out.println("Elevator is arrived at floor " + to.number);	      
	            	}
	                try {
						sleep((e.travelTime * 1000));
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
	                e.currentFloor = next;
	            }
	        }
	    }
	}

	public boolean noPersonsWaiting() {
		if (allPersons.isEmpty())
			return true;
		
		for (Map.Entry<Integer, Queue<Person>> entry : allPersons.entrySet() ) {
			if (!entry.getValue().isEmpty()) {
				return false; 
			}
		}
		
		return true;
	}
	
	public void start() {
		while (true) {
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			run();
		}
	}
	
	public synchronized void run() {
		Elevator e = building.elevator;
		int nextFloor = e.currentFloor + 1;		

		while (!noPersonsWaiting()) {
			if (nextFloor > building.getNumOfFloors())  // do not go over the floors limit
				nextFloor = 1;

			// find the next floor which has person waiting and closest to current floor
            Queue<Person> q = allPersons.get(nextFloor);
            if (q != null) {
	            while (q != null && q.isEmpty()) {
	                nextFloor += 1;
	                q = allPersons.get(nextFloor);
	            }
	            if (q != null) {
		            Person p = q.peek();
		            e.addPerson(p);
		            
		            // go to the floor to pick up the first person
		            travelTo(e, p.fromFloor);
		            personsWaiting.put(p.fromFloor.number, personsWaiting.get(p.fromFloor.number) - 1);
		            
		            // send the first person to the to floor
		            e.currentFloor = p.fromFloor.number;
		            travelTo(e, p.toFloor);
		            System.out.println("Person who goes from floor " + p.fromFloor.number + " to floor " + p.toFloor.number + " existed the door");
		            e.currentFloor = p.toFloor.number;
		            
		            q.poll();  // do pop()
		            
		            nextFloor = e.currentFloor + 1;
	            }	            
			}
		}
	}
}


public class ElevatorSimulation extends Thread {

	public static void main(String[] args) {
		System.out.println("Simulation of Elevators in the building!");

		Building b = new Building(5);
		Simulation es = new Simulation(b);
		
		Thread t1 = new Thread() {
			public void run() {
				es.generatePersons(es.building.getNumOfFloors());
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				es.printPersonQueue();
			}
		};
		
		t1.start();
		t2.start();
		es.start(); 
		
        try
        {
            t1.join();
            t2.join();
            es.join();
        }
        catch(Exception e)
        {
            System.out.println("Interrupted");
        }
	}
}
