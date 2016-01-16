package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.Particle;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.Atom implements Particle {
//	int id;
	String type;
	double charge;
	double mass;
	Vector3D potentialEnergy;
	
	public Atom(String type, double charge, double mass) {
//		this.id = id;
   		this.type = type;
   		this.charge = charge;
   		this.mass = mass;
   		this.potentialEnergy = new Vector3D();
   	}

	@Override
	public Vector3D getPotentialEnergy() {
		return potentialEnergy;
   	}
	
}
