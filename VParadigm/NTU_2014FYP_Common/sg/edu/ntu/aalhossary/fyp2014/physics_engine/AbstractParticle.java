package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public abstract class AbstractParticle {

	protected double inverseMass;
	protected Vector3D position;
	protected Vector3D velocity;
	protected Vector3D acceleration;
	protected Vector3D forceAccumulated;
	protected Vector3D torqueAccumulated;
	protected Matrix3 inverseInertiaTensor;
	protected Quaternion orientation;
	protected Vector3D rotation;
	protected Matrix4 transformMatrix;
	protected BoundingPrimitive boundingPrimitive;

	public double getInverseMass() {
		return this.inverseMass;
	}

	public Vector3D getPosition() {
		return this.position;
	}

	public Vector3D getVelocity() {
		return this.velocity;
	}

	public Vector3D getAcceleration() {
		return this.acceleration;
	}

	public void setInverseInertiaTensor(Matrix3 inverseInertiaTensor) {
		this.inverseInertiaTensor = inverseInertiaTensor;
	}

	public Quaternion getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
	}

	public Vector3D getRotation() {
		return this.rotation;
	}

	public void setRotation(Vector3D rotation) {
		this.rotation = rotation;
	}

	public AbstractParticle() {
		// TODO - implement AbstractParticle.AbstractParticle
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPosition(double x, double y, double z) {
		// TODO - implement AbstractParticle.setPosition
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setVelocity(double x, double y, double z) {
		// TODO - implement AbstractParticle.setVelocity
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setAcceleration(double x, double y, double z) {
		// TODO - implement AbstractParticle.setAcceleration
		throw new UnsupportedOperationException();
	}

	public double getMass() {
		// TODO - implement AbstractParticle.getMass
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param mass
	 */
	public void setMass(double mass) {
		// TODO - implement AbstractParticle.setMass
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param duration
	 */
	public void integrate(double duration) {
		// TODO - implement AbstractParticle.integrate
		throw new UnsupportedOperationException();
	}

	public void clearAccumulator() {
		// TODO - implement AbstractParticle.clearAccumulator
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param force
	 */
	public void addForce(Vector3D force) {
		// TODO - implement AbstractParticle.addForce
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aTorque
	 */
	public void addTorque(Vector3D aTorque) {
		// TODO - implement AbstractParticle.addTorque
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aM
	 * @param aPosition
	 * @param aOrientation
	 */
	public void calculateTransformMatrix(Matrix4 aM, Vector3D aPosition, Quaternion aOrientation) {
		// TODO - implement AbstractParticle.calculateTransformMatrix
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aWorldCor
	 * @param aQ
	 * @param aBodyCor
	 * @param aRotMatrix
	 */
	public void calculateInertiaTensor(Matrix3 aWorldCor, Quaternion aQ, Matrix3 aBodyCor, Matrix4 aRotMatrix) {
		// TODO - implement AbstractParticle.calculateInertiaTensor
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aForce
	 * @param aPoint
	 */
	public void addForceAtPoint(Vector3D aForce, Vector3D aPoint) {
		// TODO - implement AbstractParticle.addForceAtPoint
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param aForce
	 * @param aPoint
	 */
	public void addForceAtBodyPoint(Vector3D aForce, Vector3D aPoint) {
		// TODO - implement AbstractParticle.addForceAtBodyPoint
		throw new UnsupportedOperationException();
	}

}