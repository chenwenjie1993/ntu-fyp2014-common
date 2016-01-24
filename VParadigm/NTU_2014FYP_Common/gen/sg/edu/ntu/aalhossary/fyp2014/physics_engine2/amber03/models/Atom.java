package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.AbstractParticle {
	String type;
	double charge;
	
	public Atom(String type, double charge, double mass) {
		super();
   		this.type = type;
   		this.charge = charge;
   		this.setMass(mass);
   	}
	
}
