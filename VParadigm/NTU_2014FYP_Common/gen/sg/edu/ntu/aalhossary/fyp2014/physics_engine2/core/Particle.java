package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public interface Particle extends sg.edu.ntu.aalhossary.fyp2014.common.Particle {
	public Vector3D getPosition();
	public Vector3D getPotentialEnergy();
}
