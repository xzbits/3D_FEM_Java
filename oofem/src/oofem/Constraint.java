package oofem;

import inf.text.ArrayFormat;

public class Constraint {
	
	private boolean[] free = new boolean[3];

	public Constraint (boolean u1, boolean u2, boolean u3) {
		free[0] = u1;
		free[1] = u2;
		free[2] = u3;
	}
	
	public boolean isFree(int c) {
		return free[c];
	}
	
	private String[] printPreparation() {
		String[] a = new String[3];
		for(int i = 0; i < free.length; i++) {
			if(this.isFree(i) == false) {
				a[i] = "fixed";
			} else {
				a[i] = "free";
			}
		}
		return a;
	}
	
	public void print() {
		System.out.println(ArrayFormat.format(this.printPreparation()));
		}
}
