package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public class Contact {

	private double _penetration;
	private double _restitution;
	private double _friction;
	private PotentialContact _potentialContact;
	private Vector3D _contactNormal;
	private Vector3D _contactPoint;

	/**
	 * 
	 * @param aParticle
	 * @param aVelocity
	 */
	public void updateVelocity(AbstractParticle aParticle, Vector3D aVelocity) {
		// TODO - implement Contact.updateVelocity
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aParticle
	 * @param aAcceleration
	 */
	public void updateAcceleration(AbstractParticle aParticle, Vector3D aAcceleration) {
		// TODO - implement Contact.updateAcceleration
		throw new UnsupportedOperationException();
	}

}