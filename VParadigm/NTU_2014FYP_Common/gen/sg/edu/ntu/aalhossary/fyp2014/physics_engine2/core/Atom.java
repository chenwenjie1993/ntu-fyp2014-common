package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.Atom implements Particle {
	int id;
	String type;
	double charge;
	double mass;
	
	public Atom(int id, String type, double charge, double mass) {
		this.id = id;
		this.type = type;
		this.charge = charge;
		this.mass = mass;
	}
}
