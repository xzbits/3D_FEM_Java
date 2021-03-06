package oofem;

import java.util.ArrayList;
import iceb.jnumerics.*;
import iceb.jnumerics.lse.GeneralMatrixLSESolver;
import iceb.jnumerics.lse.ILSESolver;
import inf.text.ArrayFormat;

public class Structure {
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Element> elements = new ArrayList<Element>();
	
	public Node addNode(double x1, double x2, double x3) {
		this.nodes.add(new Node(x1, x2, x3));
		return this.getNode(this.getNumberOfNodes() -1);
	}
	
	public Element addElement(double e, double a, int n1, int n2) {
		this.elements.add(new Element(e, a, this.getNode(n1), this.getNode(n2)));
		return this.getElement(this.getNumberOfElements() -1);
	}
	
	public int getNumberOfNodes() {
		return this.nodes.size();
	}
	
	public Node getNode(int id) {
		return this.nodes.get(id);
	}
	
	public int getNumberOfElements() {
		return this.elements.size();
	}
	
	public Element getElement(int id) {
		return this.elements.get(id);
	}
	
	public void printStructure() {
		
	}
	
	private int enumerateDOFs() {
		int start = 0;
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			start = this.nodes.get(i).enumerateDOFs(start);
		}
		for(int i = 0; i < this.getNumberOfElements(); i++) {
			this.elements.get(i).enumerateDOFs();
		}
		return start;
	}
	
	private void assembleLoadVector(double[] rGlobal) {
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			int[] dof = this.getNode(i).getDOFNumbers();
			Force f = this.getNode(i).getForce();
			if(f != null) {
				for(int j =0; j < 3; j++) {
					int rIndex = dof[j];
					if(rIndex != -1) {
						rGlobal[rIndex] = rGlobal[rIndex] + f.getComponent(j);
					}
				}
			}
		}
	}
	
	private void assembleStiffnessMatrix(IMatrix kGlobal) {
		for(int i = 0; i < this.getNumberOfElements(); i++) {
			int[] Edof = this.getElement(i).getDOFNumbers();
			IMatrix Ke = this.getElement(i).computeStiffnessMatrix();
			for(int j = 0; j < 6; j++) {
				for(int k = 0; k < 6; k++) {
					int matrixIndex1 = Edof[j];
					int matrixIndex2 = Edof[k];
					if(matrixIndex1 != -1 && matrixIndex2 != -1) {
						kGlobal.add(matrixIndex1, matrixIndex2, Ke.get(j, k));
					}
				}
			}
		}
	}
	
	
	public void solve() {
		int Nequ = this.enumerateDOFs();
		ILSESolver sol = new GeneralMatrixLSESolver();
		QuadraticMatrixInfo info = sol.getAInfo();
		
		info.setSize(Nequ);
		sol.initialize();
		
		IMatrix Kg = sol.getA();
		double[] Fg = new double[Nequ];
		
		this.assembleStiffnessMatrix(Kg);
		this.assembleLoadVector(Fg);
		
		try {
			sol.solve(Fg);
		} catch (SolveFailedException e) {
			System.out.println(e.getMessage());
		}
		selectDisplacements(Fg);
	}
	
	private void selectDisplacements(double[] uGlobal) {
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			int[] dof = this.getNode(i).getDOFNumbers();
			double[] dispNode = new double[3];
			
			for(int j = 0; j < 3; j++) {
				if(dof[j] == -1) {
					dispNode[j] = 0;
				} else {
					dispNode[j] = uGlobal[dof[j]];
				}
			}
			this.getNode(i).setDisplacement(dispNode);
		}
	}
	
	public void printResults() {
		System.out.println("Listing structure");
		System.out.println("Nodes");
		String[] a = new String[] {"idx","x1","x2","x3"};
		System.out.println(ArrayFormat.format(a));
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
		System.out.print(i + " ");
		this.getNode(i).print();
		}
		
		System.out.println();
		
		System.out.println("Constraints");
		String[] b = new String[] {"node","u1","u2","u3"};
		System.out.println(ArrayFormat.format(b));
		for (int i = 0; i < this.getNumberOfNodes(); i++) {
			System.out.print(i + " ");
			this.getNode(i).getConstraint().print();
			}
		
		System.out.println();
		
		System.out.println("Forces");
		String[] c = new String[] {"node","r1","r2","r3"};
		System.out.println(ArrayFormat.format(c));
		for (int i = 0; i < this.getNumberOfNodes(); i++) {
			System.out.print(i + " ");
			this.getNode(i).getForce().print();
			}
		
		System.out.println();
		
		System.out.println("Elements");
		String[] d = new String[] {"idx","E","A","L"};
		System.out.println(ArrayFormat.format(d));
		for (int i = 0; i < this.getNumberOfElements(); i++) {
			System.out.print(i + " ");
			this.getElement(i).print();
			}
		
		System.out.println();
		System.out.println();
		
		System.out.println("Listing analysis results");
		
		System.out.println();
		
		System.out.println("Displacement");
		String[] e = new String[] {"node","u1","u2","u3"};
		System.out.println(ArrayFormat.format(e));
		for(int i = 0; i < this.getNumberOfNodes(); i++) {
			System.out.print(i + " ");
			System.out.println(this.getNode(i).getDisplacement());
		}

		System.out.println();
		
		System.out.println("Element forces");
		String[] f = new String[] {"elem","force"};
		System.out.println(ArrayFormat.format(f));
		for(int i = 0; i < this.getNumberOfElements(); i++) {
			System.out.print(i + " ");
			System.out.println(this.getElement(i).computeForce());
		}
	}
}






















