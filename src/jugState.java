import java.util.*;

public class jugState { // jug state is created as a class
	int a,b,c;
	
	public jugState(int a, int b, int c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	// With the getter methods we can get the number in each jug
	public int getA() {
		return a;
	}
	public int getB() {
		return b;
	}
	public int getC() {
		return c;
	}
	
	// With these methods we can fill each jug with what ever number we input
	public void fillA(int limitA) {
		this.a = limitA;
	}
	public void fillB(int limitB) {
		this.b = limitB;
	}
	public void fillC(int limitC) {
		this.c = limitC;
	}
	
	// This toString() can be used to print the class as a string. ex: (1, 2, 3)
	@Override
	public String toString() {
		return ("("+this.getA()+", "+this.getB()+", "+this.getC()+")");
	}
}