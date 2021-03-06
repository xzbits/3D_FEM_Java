package oofem;

import iceb.jnumerics.*;
import inf.text.ArrayFormat;

public class Element {
	private double area;
	private double eModulus;
	int[] dofNumbers = new int[6];
	private Node n1;
	private Node n2;
	
	public Element(double e, double a, Node n1, Node n2) {
		this.eModulus	= e;
		this.area		= a;
		this.n1 		= n1;
		this.n2			= n2;
	}
	
	public IMatrix computeStiffnessMatrix() {
		IMatrix Ke1 = this.getE1().dyadicProduct(this.getE1());
		IMatrix Ke = new Array2DMatrix(6,6);
		double EAL = this.getArea() * this.getEModulus() / Math.pow(this.getLength(),3);
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Ke.set(i, j, Ke1.get(i, j) * EAL);
			}
		}
		for(int i = 0; i < 3; i++) {
			for(int j = 3; j < 6; j++) {
				Ke.set(i, j, -EAL * Ke1.get(i, j-3));
			}
		}
		for(int i = 3; i < 6; i++) {
			for(int j = 0; j < 3; j++) {
				Ke.set(i, j, -EAL * Ke1.get(i-3, j));
			}
		}
		for(int i = 3; i < 6; i++) {
			for(int j = 3; j < 6; j++) {
				Ke.set(i, j, EAL * Ke1.get(i-3, j-3));
			}
		}
		return Ke;
	}
	
	public void enumerateDOFs() {
		Node node1 = this.getNode1();
		Node node2 = this.getNode2();
		for(int i = 0; i < this.dofNumbers.length; i++) {
			if(i <= 2) {
				this.dofNumbers[i] = node1.getDOFNumbers()[i];
			}
			if(i > 2) {
				this.dofNumbers[i] = node2.getDOFNumbers()[i - 3];
			}
		}
	}
	
	public int[] getDOFNumbers() {
		return this.dofNumbers;
	}
	
	public double computeForce() {
		Vector3D T = this.getE1().multiply(1/ this.getLength());

		double u1 = T.scalarProduct(this.getNode1().getDisplacement());
		double u2 = T.scalarProduct(this.getNode2().getDisplacement());
		
		double strain = 1/ this.getLength() * (u2 - u1);
		double N = this.getEModulus() * this.getArea() * strain;
		return N;
	}
	
	public Vector3D getE1() {
		Vector3D x1 = this.getNode1().getPosition();
		Vector3D x2 = this.getNode2().getPosition();
		Vector3D v12 = new Vector3D(
				x2.getX1()-x1.getX1(), 
				x2.getX2()-x1.getX2(), 
				x2.getX3()-x1.getX3()
				);
		return v12;
	}
	
	public double getLength() {
		//double length = this.getE1().normTwo();
		double length =	Math.sqrt(Math.pow(this.getE1().getX1(),2) + Math.pow(this.getE1().getX2(),2) + Math.pow(this.getE1().getX3(),2));
		return length;
	}
	
	public Node getNode1() {
		return n1;
	}
	
	public Node getNode2() {
		return n2;
	}
	
	public double getArea() {
		return area;
	}
	
	public double getEModulus() {
		return eModulus;
	}
	
	private double[] printPreparation() {
		double[] a = new double[3];
		a[0] = this.getEModulus();
		a[1] = this.getArea();
		a[2] = this.getLength();
		return a;
	}
	
	public void print() {
		System.out.println(ArrayFormat.format(this.printPreparation()));
	}
}
