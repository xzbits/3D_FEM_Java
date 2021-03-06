package oofem;

import inf.v3d.obj.*;
import inf.v3d.view.*;
import iceb.jnumerics.*;
public class Visualizer {
	private double displacementScale;
	private double constraintSymbolScale;
	private double forceSymbolScale;
	private double forceSymbolRadius;
	private Structure struct;
	private Viewer v;
	
	public Visualizer(Structure struc, Viewer v) {
		this.struct = struc;
		this.v = v;
	}
	
	public void setConstraintSymbolScale(double constraintSymbolScale) {
		this.constraintSymbolScale = constraintSymbolScale;
	}
	
	public void setForceSymbolScale(double forceSymbolScale) {
		this.forceSymbolScale = forceSymbolScale;
	}
	
	public void setForceSymbolRadius(double forceSymbolRadius) {
		this.forceSymbolRadius = forceSymbolRadius;
	}
	
	public void drawElements() {
		CylinderSet Elements = new CylinderSet();
		for(int i = 0; i < this.struct.getNumberOfElements(); i++) {
			Element e = this.struct.getElement(i);
			Node n1 = e.getNode1();
			Node n2 = e.getNode2();
			double r = Math.sqrt(e.getArea()/ Math.PI);
			
			Elements.addCylinder(n1.getPosition().toArray(), n2.getPosition().toArray(), r);
		}
		v.addObject3D(Elements);
	}
	
	public void drawConstraints() {
		for(int i = 0; i < this.struct.getNumberOfNodes(); i++) {
			Node n = this.struct.getNode(i);
			Constraint cons = n.getConstraint();
			if(cons != null) {
				for(int j = 0; j < 3; j++) {
					Cone cone = new Cone();
					int[] u = {0, 0, 0};
					u[j] = 1;
					if(n.getConstraint().isFree(j) == false) {
						cone.setCenter(n.getPosition().getX1(),
								n.getPosition().getX2(),
								n.getPosition().getX3());
						cone.setDirection(u[0], u[1], u[2]);
						cone.setRadius(this.forceSymbolRadius);
						v.addObject3D(cone);
					}
				}
			}
		}
	}
	
	public void drawElementForces() {
		for(int i = 0; i < this.struct.getNumberOfNodes(); i++) {
			Arrow arrow = new Arrow();
			Node n = this.struct.getNode(i);
			Force f = n.getForce();
			if(f != null) {
				Vector3D coor = new Vector3D(n.getPosition());
				Vector3D force = new Vector3D(f.getComponent(0), f.getComponent(1), f.getComponent(2));
				force = force.multiply(this.forceSymbolScale);
				Vector3D forceScale = new Vector3D(coor.add(force));
				arrow.setPoint1(coor.toArray());
				arrow.setPoint2(forceScale.toArray());
				arrow.setRadius(this.forceSymbolRadius);
				arrow.setColor("red");
				v.addObject3D(arrow);
			}
		}
	}
	
	public void drawDisplacements() {
		CylinderSet newElements = new CylinderSet();
		for (int i = 0; i < this.struct.getNumberOfElements(); i++) {
			Element e = this.struct.getElement(i);

			Vector3D coor = e.getNode1().getPosition();
			Vector3D u1 = e.getNode1().getDisplacement().multiply(this.displacementScale);
			Vector3D point1 = coor.add(u1);

			Vector3D v2 = e.getNode2().getPosition();
			Vector3D u2 = e.getNode2().getDisplacement().multiply(this.displacementScale);
			Vector3D point2 = v2.add(u2);

			double radius = Math.sqrt(e.getArea() / Math.PI);
			newElements.addCylinder(point1.toArray(), point2.toArray(), radius);
			newElements.setColor("yellow");
		}
		v.addObject3D(newElements);
	}
}
