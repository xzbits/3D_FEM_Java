package oofem;

import iceb.jnumerics.*;
import inf.text.ArrayFormat;

public class Node {
	//Node properties
	private double[] coordinates	= new double[3];
	private int[] dofNumbers 		= new int[3];
	private Constraint cons 		= new Constraint(true, true, true);
	private Force forces 			= new Force(0,0,0);
	double[] displacement 	= new double[3];
	//Node constructor
	public Node(double x1, double x2, double x3) {
		this.coordinates[0] = x1;
		this.coordinates[1] = x2;
		this.coordinates[2] = x3;
	}
	
	//Set CONSTRAINT for a specific node
	public void setConstraint(Constraint c) {
		this.cons=c;
	}
	
	//Call vector CONSTRAINT of a specific node
	public Constraint getConstraint() {
		return this.cons;
	}
	
	//Set FORCE for a specific node
	public void setForce(Force f) {
		this.forces = f;
	}
	
	//Call vector FORCE of a specific node
	public Force getForce() {
		return this.forces;
	}
	
	//enumerate DOF
	public int enumerateDOFs(int start) {
		double u1 = this.getDisplacement().getX1();
		double u2 = this.getDisplacement().getX2();
		double u3 = this.getDisplacement().getX3();
		for(int i=0; i < dofNumbers.length; i++) {
			if(this.getConstraint().isFree(i)==false || u1!=0 || u2!=0 || u3!=0) {
			dofNumbers[i] = -1;
		} else 
			dofNumbers[i] = start++;		
		}
		return start;
	}
	
	//Call DOF numbers
	public int[] getDOFNumbers() {
		return dofNumbers;
	}
	
	//Get the Vector3D of node coordinates
	public Vector3D getPosition() {
		Vector3D v3 = new Vector3D(this.coordinates[0],this.coordinates[1],this.coordinates[2]);
		return v3;
	}
	
	//Set DISPLACEMENT for node
	public void setDisplacement(double[] u) {
		this.displacement = u;
	}
	
	//Call Vector3D of NODE'S DISPLACEMENT
	public Vector3D getDisplacement() {
		Vector3D v3dis = new Vector3D(this.displacement[0],this.displacement[1],this.displacement[2]);
		return v3dis;
	}
	
	public void print() {
		System.out.println(ArrayFormat.format(this.coordinates));
	}
}
