import java.util.*;

public class waterJugProblem {

	// Create global stacks
	static Stack<jugState> exploredStack = new Stack<jugState>();
	static Stack<jugState> previousStates = new Stack<jugState>();
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean existingPrev = false;
		
		// Input the amount of liquid on each jug
		System.out.print("Enter limit of jug A: ");
		int limitA = scanner.nextInt();
		System.out.print("Enter limit of jug B: ");
		int limitB = scanner.nextInt();
		System.out.print("Enter limit of jug C: ");
		int limitC = scanner.nextInt();
		scanner.close();
		
		// Start the timer to calculate how long the program takes to load
		long startTime = System.nanoTime();
		
		// Adds the root state which is the jugs being empty 
		jugState initialstate = new jugState(0, 0, 0);
		exploredStack.push(initialstate);

		// Since the "previousState" stack starts empty, we want the loop to run once before giving any while condition
		do {
			
			// First we check what the latest state is to get all its neighbours
			jugState latestState = exploredStack.peek();
			
			// The next steps are checking if each possible pour is possible 
			pourJug("a", "b", limitA, limitB, limitC);
			pourJug("a", "c", limitA, limitB, limitC);
			pourJug("b", "a", limitA, limitB, limitC);
			pourJug("b", "c", limitA, limitB, limitC);
			pourJug("c", "a", limitA, limitB, limitC);
			pourJug("c", "b", limitA, limitB, limitC);
			
			// We also check if we can fill any jug 
			if (latestState.getA() < limitA) {fillJug(limitA, "a");}
			if (latestState.getB() < limitB) {fillJug(limitB, "b");}
			if (latestState.getC() < limitC) {fillJug(limitC, "c");}
			
			// Finally we check if we can also empty any jug
			if (latestState.getA() != 0) {emptyJug("a");}
			if (latestState.getB() != 0) {emptyJug("b");}
			if (latestState.getC() != 0) {emptyJug("c");}
			
			// After we have checked for each state, we run through them and check if they already exist in our explored stack
			for (int j=0; j<exploredStack.size(); j++) {
				if (exploredStack.get(j).toString().equals(previousStates.peek().toString())) {
					existingPrev = true;
					break;
				} else {
					existingPrev = false;
				}
			}
			// If they don't exist then we can add it to the explored state and take it off the "previousState" stack
			if (existingPrev == false) {
				exploredStack.push(previousStates.peek());
				previousStates.pop();
			}
		} while (!previousStates.empty()); // End of the loop
		
		
		//This can be used for debugging and checking the states that are generated
		for (int i=0; i<exploredStack.size(); i++) {
			System.out.println("Unique State: " + exploredStack.get(i));
		}
		
		
		// Stop the timer
		long finishTime = System.nanoTime();
		
		// Output the number of states the water jug problem has and also the time it took to load in milliseconds
		System.out.println("Number of States: " + exploredStack.size());
		System.out.println("Time taken to run: " + ((finishTime - startTime)/1000000) + "ms");
	}
	
	public static void fillJug(int numb, String jugLetter) { // Fill Jug function
		// Check what the latest state in the explored stack is
		jugState latestState = exploredStack.peek();
		// Here we create a new jugState so that it doesn't change the state we have just peeked
		jugState state = new jugState(latestState.getA(), latestState.getB(), latestState.getC());
		
		if (jugLetter == "a") {
			state.fillA(numb);
		} else if (jugLetter == "b") {
			state.fillB(numb);
		} else {
			state.fillC(numb);
		}
		
		// What ever state we have filled, if it doesn't exist on either stack, we push it to the "previousStates" as a unique state
		if (existingPrev(state) == true && existingExpl(state) == true) {
			previousStates.push(state);
		}
	}

	public static void emptyJug(String jugLetter) { // Empty Jug function
		// Check what the latest state in the explored stack is
		jugState latestState = exploredStack.peek();
		// Here we create a new jugState so that it doesn't change the state we have just peeked
		jugState state = new jugState(latestState.getA(), latestState.getB(), latestState.getC());
		
		if (jugLetter == "a") {
			state.fillA(0);
		} else if (jugLetter == "b") {
			state.fillB(0);
		} else {
			state.fillC(0);
		}
		
		// What ever state we have emptied, if it doesn't exist on either stack, we push it to the "previousStates" as a unique state
		if (existingPrev(state) == true && existingExpl(state) == true) {
			previousStates.push(state);
		}
	}
	
	public static void pourJug(String jugOne, String jugTwo, int limitA, int limitB, int limitC) { // Pour Jug function
		// Check what the latest state in the explored stack is
		jugState latestState = exploredStack.peek();
		// Here we create a new jugState so that it doesn't change the state we have just peeked
		jugState state = new jugState(latestState.getA(), latestState.getB(), latestState.getC());
		
		int lastA = latestState.getA(); // To make it easier to read I save the previous state into ints
		int lastB = latestState.getB();
		int lastC = latestState.getC();
		
		// Check if the first jug is A
		if (jugOne == "a" && lastA > 0) {
			// Check if the second jug is B
			if (jugTwo == "b") {
				
				// If the limit of B is bigger than the sum of both values in A and B now then we can empty A and pour all of A into B
				if (limitB >= lastA+lastB) {
					state.fillA(0);
					state.fillB(lastA+lastB);
					
				// Otherwise if B is not at it's limit yet, then we pour A to B until B is at its limit
				} else if (lastB < limitB) {
					if (lastA >= limitB-lastB) {
						state.fillA(lastA-(limitB-lastB));
						state.fillB(limitB);
					} else {
						state.fillA(0);
						state.fillB(lastA+lastB);
					}
				}
			}
			// Check if the second jug is C
			if (jugTwo == "c") {
				// If the limit of C is bigger than the sum of both values in A and C now then we can empty A and pour all of A into C
				if (limitC >= lastA+lastC) {
					state.fillA(0);
					state.fillC(lastA+lastC);
					
				// Otherwise if C is not at it's limit yet, then we pour A to C until C is at its limit
				} else if (lastC < limitC) {
					if (lastA >= limitC-lastC) {
						state.fillA(lastA-(limitC-lastC));
						state.fillC(limitC);
					} else {
						state.fillA(0);
						state.fillC(lastA+lastC);
					}
				}
			}
		} 
		
		// We do everything as explained above but with B and C
		else if (jugOne == "b" && lastB > 0) {
			if (jugTwo == "a") {
				if (limitA >= lastB+lastA) {
					state.fillA(lastB+lastA);
					state.fillB(0);
					
				} else if (lastA < limitA) {
					if (lastB >= (limitA-lastA)) {
						state.fillA(limitA);
						state.fillB(lastB-(limitA-lastA));
					} else {
						state.fillA(lastA+lastB);
						state.fillB(0);
					}
				}
			} 
			if (jugTwo == "c") {
				if (limitC >= lastB+lastC) {
					state.fillB(0);
					state.fillC(lastB+lastC);
					
				} else if (lastC < limitC) {
					if (lastB >= (limitC-lastC)) {
						state.fillB(lastB-(limitC-lastC));
						state.fillC(limitC);
					} else {
						state.fillB(0);
						state.fillC(lastB+lastC);
					}
				}
			}
		} else {
			if (lastC > 0 && jugTwo == "a") {
				if (limitA >= lastC+lastA) {
					state.fillA(lastC+lastA);
					state.fillC(0);
					
				} else if (lastA < limitA) {
					if (lastC >= (limitA-lastA)) {
						state.fillA(limitA);
						state.fillC(lastC-(limitA-lastA));
					} else {
						state.fillA(lastA+lastC);
						state.fillC(0);
					}
				}
			} 
			if (lastC > 0 && jugTwo == "b") {
				if (limitB >= lastC+lastB) {
					state.fillB(lastB+lastC);
					state.fillC(0);
					
				} else if (lastB < limitB) {
					if (lastC >= (limitB-lastB)) {
						state.fillB(limitB);
						state.fillC(lastC-(limitB-lastB));
					} else {
						state.fillB(lastB+lastC);
						state.fillC(0);
					}
				}
			}
		}
		
		// What ever state we have poured into, if it doesn't exist on either stack, we push it to the "previousStates" as a unique state
		if (existingPrev(state) == true && existingExpl(state) == true) {
			previousStates.push(state);
		}
	}
	
	public static boolean existingPrev(jugState state) {  // Checking if the state passed exists in the "previousState" stack
		for (int i=0; i<previousStates.size(); i++) {
			if (previousStates.get(i).toString().equals(state.toString())) {
				return false;
			}
		}
		return true;
	}
	public static boolean existingExpl(jugState state) { // Checking if the state passed exists in the explored stack
		for (int i=0; i<exploredStack.size(); i++) {
			if (exploredStack.get(i).toString().equals(state.toString())) {
				return false;
			}
		}
		return true;
	}
}

