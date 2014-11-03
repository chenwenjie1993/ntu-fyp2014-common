package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

public abstract class AbstractParticle implements sg.edu.ntu.aalhossary.fyp2014.common.Particle {
	protected Vector3D position;
	protected Vector3D velocity;
	protected Vector3D acceleration;
	protected Vector3D forceAccumulated;
	protected Vector3D torqueAccumulated;
	protected double inverseMass;
	protected Matrix3 inverseInertiaTensor;
	protected Quaternion orientation;
	protected Vector3D rotation;
	protected Matrix4 transformMatrix;
	protected BoundingPrimitive boundingPrimitive;

	public AbstractParticle() {
		position = new Vector3D(0,0,0);
		velocity = new Vector3D(0,0,0);
		acceleration = new Vector3D(0,0,0);
		forceAccumulated = new Vector3D(0,0,0);
		torqueAccumulated = new Vector3D(0,0,0);
		inverseMass = 0;
		orientation = new Quaternion(0,0,0,0);
		rotation = new Vector3D(0,0,0);
	}

	public Vector3D getPosition() {
		return this.position;
	}

	public void setPosition(double x, double y, double z) {
		position.x = x; position.y = y; position.z = z;
	}

	public Vector3D getVelocity() {
		return this.velocity;
	}

	public void setVelocity(double x, double y, double z) {
		velocity.x = x; velocity.y = y; velocity.z = z;
	}

	public Vector3D getAcceleration() {
		return this.acceleration;
	}

	public void setAcceleration(double x, double y, double z) {
		acceleration.x = x; acceleration.y = y; acceleration.z = z;
	}
	
	public double getMass() {
		return 1/this.inverseMass;
	}
	
	public void setMass (double mass){
		if(mass==0) 
			inverseMass = 0;
		else
			inverseMass = 1/mass;
	}

	public double getInverseMass() {
		return this.inverseMass;
	}
	
	public void integrate(double duration) {
		
		// Calculate total acceleration without updating the original ( a = F /m )
		Vector3D currentAcceleration = new Vector3D (acceleration.x, acceleration.y, acceleration.z);
		currentAcceleration.addScaledVector(forceAccumulated, inverseMass);
		
		// Update current velocity (v = a*t)
		Vector3D initialVelocity = new Vector3D (velocity.x, velocity.y, velocity.z);
		velocity.addScaledVector(currentAcceleration, duration);
		
		// Update current position (s = u*t + 0.5*a*t*t)
		position.addScaledVector(initialVelocity, duration);
		position.addScaledVector(currentAcceleration, duration * duration /2);
		
		// Clear forces
		clearAccumulator();
	}
	

	public void setInverseInertiaTensor(Matrix3 aInertia) {
		
	}

	public void clearAccumulator() {
		forceAccumulated.clear();
		torqueAccumulated.clear();
	}

	public void addForce(Vector3D force) {
		forceAccumulated.add(force);
	}
	
	public void addForceAtPoint(Vector3D force, Vector3D point) {
		
		forceAccumulated.add(force);
	    
		// Convert to coordinates relative to center of mass.
	    Vector3D dist = point;
	    dist.subtract(position);
	    
	    Vector3D torque = new Vector3D();
	    torque = dist.getCrossProduct(force);
	    torqueAccumulated.add(torque);    
	}
	
	public void addTorque(Vector3D torque) {
		torqueAccumulated.add(torque);    
	}

	public void calculateTransformMatrix(Matrix4 aM, Vector3D aPosition, Quaternion aOrientation) {
		throw new UnsupportedOperationException();
	}

	public void calculateInertiaTensor(Matrix3 aWorldCor, Quaternion aQ, Matrix3 aBodyCor, Matrix4 aRotMatrix) {
		throw new UnsupportedOperationException();
	}


	public Vector3D getRotation() {
		return this.rotation;
	}

	public void setRotation(Vector3D aRotation) {
		this.rotation = aRotation;
	}

	public Quaternion getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Quaternion aOrientation) {
		this.orientation = aOrientation;
	}
	
	public BoundingPrimitive getBoundingPrimitive(){
		return this.boundingPrimitive;
	}
}