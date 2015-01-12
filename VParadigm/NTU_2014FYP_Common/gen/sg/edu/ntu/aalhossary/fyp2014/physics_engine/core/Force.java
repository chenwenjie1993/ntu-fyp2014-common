package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public interface Force{

	public Vector3D calculateForce (AbstractParticle particle1, AbstractParticle particle2);
	public Vector3D getNegativeForce();
	public Vector3D getForce();
}