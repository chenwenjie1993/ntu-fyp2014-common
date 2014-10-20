package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public class ForceRegistry {

	private java.util.HashMap<AbstractParticle, java.util.ArrayList<Force>> registrations = new HashMap<AbstractParticle,ArrayList<Force>>();

	/**
	 * 
	 * @param abstractParticle
	 * @param force
	 */
	public void add(AbstractParticle abstractParticle, Force force) {
		// TODO - implement ForceRegistry.add
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param abstractParticle
	 * @param force
	 */
	public void remove(AbstractParticle abstractParticle, Force force) {
		// TODO - implement ForceRegistry.remove
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param abstractParticle
	 */
	public void removeAllForceFrom(AbstractParticle abstractParticle) {
		// TODO - implement ForceRegistry.removeAllForceFrom
		throw new UnsupportedOperationException();
	}

	public void clear() {
		// TODO - implement ForceRegistry.clear
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param abstractParticle
	 */
	public void updateForce(AbstractParticle abstractParticle) {
		// TODO - implement ForceRegistry.updateForce
		throw new UnsupportedOperationException();
	}

	public void updateAllForces() {
		// TODO - implement ForceRegistry.updateAllForces
		throw new UnsupportedOperationException();
	}

}