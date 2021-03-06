package oofem;

import inf.text.ArrayFormat;

public class Force {
	private double[] components = new double[3];
	public Force(double r1, double r2, double r3) {
		components[0] = r1;
		components[1] = r2;
		components[2] = r3;
	}
	
	public double getComponent(int c) {
		return components[c];
	}
	
	public void print() {
		System.out.println(ArrayFormat.format(this.components));
	}
}
