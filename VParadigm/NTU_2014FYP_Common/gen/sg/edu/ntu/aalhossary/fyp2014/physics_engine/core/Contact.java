package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;


public class Contact {
	private ArrayList<AbstractParticle[]> _potentialContact;
	private double _penetration;
	private double _restitution;
	private double _friction;
	private Vector3D _contactNormal;
	private Vector3D _contactPoint;

	public void updateVelocity(AbstractParticle aParticle, Vector3D aVelocity) {
		throw new UnsupportedOperationException();
	}

	public void updateAcceleration(AbstractParticle aParticle, Vector3D aAcceleration) {
		throw new UnsupportedOperationException();
	}
}