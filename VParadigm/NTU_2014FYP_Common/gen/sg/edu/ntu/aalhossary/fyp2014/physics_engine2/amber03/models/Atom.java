package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.AbstractParticle {
	String type;
	String name;
	double charge;
	
	ArrayList<Atom> neighbor;
//	double mass;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public double getCharge() {
		return charge;
	}
	
	public void setCharge(double charge) {
		this.charge = charge;
	}
	
	public ArrayList<Atom> getNeighbor() {
		return neighbor;
	}
	
	public void addNeighbor(Atom atom) {
		if (neighbor == null) {
			neighbor = new ArrayList<Atom>();
		}
		neighbor.add(atom);
	}
	
//	public double getMass() {
//		return mass;
//	}
//	
//	public void setMass(double mass) {
//		this.mass = mass;
//	}
	
	@Override
	public String toString() {
		return type + " " + this.getPosition();
	}
	
}
