package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Particle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Vector3D;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.Atom implements Particle {
//	int id;
	String type;
	double charge;
	double mass;
	Vector3D energy;
	
	public Atom(String type, double charge, double mass) {
//		this.id = id;
		this.type = type;
		this.charge = charge;
		this.mass = mass;
		this.energy = new Vector3D();
	}
	
	@Override
	public Vector3D getPosition(){
		return null;
	}

	@Override
	public Vector3D getEnergy() {
		return energy;
	}
	
}
