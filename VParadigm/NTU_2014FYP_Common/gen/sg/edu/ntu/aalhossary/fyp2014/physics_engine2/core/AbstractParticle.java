package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public class AbstractParticle extends sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle {
	
	public Vector3D getAccumulatedForce() {
		return forceAccumulated;
	}
	
	public Vector3D getAccumulatedAcceleration() {
		Vector3D a = new Vector3D();
		a.addScaledVector(a, inverseMass);
		return a;
	}
}
