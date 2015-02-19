package sg.edu.ntu.aalhossary.fyp2014.common;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.BoundingCube;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.BoundingPrimitive;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Matrix3;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Matrix4;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Quaternion;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.World;

public abstract class AbstractParticle implements sg.edu.ntu.aalhossary.fyp2014.common.Particle {
	protected int guid;
	protected Vector3D position;
	protected Vector3D velocity;
	protected Vector3D acceleration;
	protected Vector3D forceAccumulated;
	protected Vector3D torqueAccumulated;
	protected Vector3D velocityAccumulated;
	protected double inverseMass;
	protected Matrix3 inverseInertiaTensor;
	protected Quaternion orientation;
	protected Vector3D rotation;
	protected Matrix4 transformMatrix;
	protected BoundingPrimitive boundingPrimitive;
	protected int netCharge;

	public AbstractParticle() {
		position = new Vector3D(0,0,0);
		velocity = new Vector3D(0,0,0);
		acceleration = new Vector3D(0,0,0);
		forceAccumulated = new Vector3D(0,0,0);
		torqueAccumulated = new Vector3D(0,0,0);
		velocityAccumulated = new Vector3D(0,0,0);
		inverseMass = 0;
		orientation = new Quaternion(0,0,0,0);
		rotation = new Vector3D(0,0,0);
		netCharge = 0;
		guid = World.particleCount;
		World.particleCount ++;
	}
	
	public int getGUID(){
		return guid;
	}

	public Vector3D getPosition() {
		return this.position;
	}

	public void setPosition(double x, double y, double z, int metric) {
		position.x = x; position.y = y; position.z = z; position.metric = metric;
		boundingPrimitive.updateCentre(x, y, z, metric);	
	}
	
	public void setPosition(double x, double y, double z){
		position.x = x; position.y = y; position.z = z;
		boundingPrimitive.updateCentre(x, y, z, position.metric);	
	}
	
	public void movePositionBy (double dist_x, double dist_y, double dist_z, int metric) {
		double metricDiff = metric - position.metric;
		if(metricDiff == 0) {
			position.x += dist_x;
			position.y += dist_y;
			position.z += dist_z;
		}
		else {
			double scale = Math.pow(10, metricDiff);
			position.x += dist_x*scale;
			position.y += dist_y*scale;
			position.z += dist_z*scale;
		}
		boundingPrimitive.updateCentre(position.x, position.y, position.z, position.metric);		
	}
	
	public Vector3D getVelocity() {
		return this.velocity;
	}

	public void setVelocity(double x, double y, double z, int metric) {
		velocity.x = x; velocity.y = y; velocity.z = z; velocity.metric = metric;
	}
	
	public void setVelocity(double x, double y, double z) {
		velocity.x = x; velocity.y = y; velocity.z = z;
	}
	
	public void setVelocityAccumulated() {
		setVelocity(velocityAccumulated.x,velocityAccumulated.y,velocityAccumulated.z);
		velocityAccumulated.clear();
	}

	public void addVelocityAccumulated(double x, double y, double z, int metric) {
		velocityAccumulated.add(new Vector3D(x,y,z,metric));
	}
	
	public void addVelocityAccumulated(double x, double y, double z) {
		velocityAccumulated.x += x; velocityAccumulated.y += y; velocityAccumulated.z += z;
	}
	
	public Vector3D calculateVelocityChange(AbstractParticle other, double COEFFICIENT_OF_RESTITUTION){
		
		if(this instanceof Atom) {
			Atom atom = (Atom) this;
			return atom.calculateVelocityChange(other, COEFFICIENT_OF_RESTITUTION);
		}
		else if (this instanceof Molecule){
			Molecule molecule = (Molecule) this;
			return molecule.calculateVelocityChange(other, COEFFICIENT_OF_RESTITUTION);
		}
		return null;
	}

	public Vector3D getAcceleration() {
		return this.acceleration;
	}

	public void setAcceleration(double x, double y, double z, int metric) {
		acceleration.x = x; acceleration.y = y; acceleration.z = z; acceleration.metric = metric;
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
		
		if(this instanceof Atom) {
			Atom atom = (Atom) this;
			atom.integrate(duration);
		}
		
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
	
	public void setNetCharge(int charge){
		netCharge = charge;
	}
	
	public int getNetCharge(){
		return netCharge;
	}
	
	@Override
	public boolean equals(Object particle) {
	    if (particle == null) 
	        return false;
	    
	    if (getClass() != particle.getClass()) 
	        return false;
	    
	    final AbstractParticle other = (AbstractParticle) particle;
	    
	    //if(this.position != other.position || this.velocity != other.velocity || this.acceleration != other.acceleration || this.inverseMass != other.inverseMass)
	    if(this.guid != other.guid)	
	    	return false;
	    
	    return true;
	}
}