package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.AbstractParticle {
	String type;
	String name;
	double charge;
	double mass;
	
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
	
	public double getMass() {
		return mass;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	@Override
	public String toString() {
		return type + " " + this.getPosition();
	}
	
}
