package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

/**
 * @author waiyan
 * Handle creating of atoms
 * Solve the acceleration, velocity and position of the atom before/after impact
 */
public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.Atom{

	private double radius;
	private double mass = 0, atomicRadius=0, covalentRadius=0, vdwRadius=0;
	private int valence = 0;
	private String atomicSymbol;
	
	/**
	 * Create an atom given the atomic symbol
	 * @param atomicSymbol
	 * @throws Exception
	 */
	public Atom(String atomicSymbol) throws Exception{
		super();
		this.fetchAtomicData(atomicSymbol);
		this.setMass(this.mass);
		this.setRadius(vdwRadius);
		this.setNetCharge(valence);
		this.boundingPrimitive = new BoundingSphere(radius, position);
		this.atomicSymbol = atomicSymbol;
		this.setElementSymbol(atomicSymbol.toUpperCase());
	}
	
	public Atom(sg.edu.ntu.aalhossary.fyp2014.common.Atom atom) throws Exception{
	//	super();
		this.fetchAtomicData(atom.getElementSymbol().toUpperCase());
		this.setMass(this.mass);
		this.setRadius(vdwRadius);
		this.setNetCharge(valence);
		this.boundingPrimitive = new BoundingSphere(radius, position);
		this.atomicSymbol = atom.getElementSymbol().toUpperCase();
		this.setElementSymbol(atomicSymbol.toUpperCase());
		this.position.x = atom.getCoordinates() [0] * 1e-10;
		this.position.y = atom.getCoordinates() [1] * 1e-10;
		this.position.z = atom.getCoordinates() [2] * 1e-10;
	}
	
	public Vector3D getForceAccumulated(){
		return this.forceAccumulated;
	}
	
	public void setNetCharge(int charge){
		this.netCharge = charge;
	}
	
	private void setRadius (double radius){
		this.radius = radius;
	}
	
	public void setPrintFlag(boolean flag){
	}
	
	/**
	 * Calculate acceleration, velocity and position of an atom after the given duration
	 */
	public void integrate(double duration) {
			
		// Calculate total acceleration without updating the original ( a = F /m )
		Vector3D currentAcceleration = new Vector3D (acceleration.x, acceleration.y, acceleration.z, acceleration.metric);
		currentAcceleration.addScaledVector(forceAccumulated, inverseMass);
		
		Vector3D angularAcceleration = inverseInertiaTensor.transform(torqueAccumulated);
		
		// Update current velocity (v = a*t)
		Vector3D initialVelocity = new Vector3D (velocity.x, velocity.y, velocity.z, velocity.metric);
		velocity.addScaledVector(currentAcceleration, duration);
		velocity.round();
		
		
		// Update current position (s = u*t + 0.5*a*t*t)
		position.addScaledVector(initialVelocity, duration);
		position.addScaledVector(currentAcceleration, duration * duration /2);
		
		
		//Update angular velocity
		rotation.addScaledVector(angularAcceleration, duration);
		orientation.addScaledVector(rotation, duration);
		orientation.normalize();
		
//		Matrix4 transformationMatrix = new Matrix4();
//		transformationMatrix.setOrientationAndPos(orientation, position);
//		position = transformationMatrix.transform(position);
		
		// Clear forces
		clearAccumulator();
		
		// Update the centre of the boundingPrimitive 
		boundingPrimitive.updateCentre(position.x, position.y, position.z, position.metric);
	
	}
	
	/**
	 * Calculate velocity change after impact
	 */
	public Vector3D calculateVelocityChange(AbstractParticle other, double COEFFICIENT_OF_RESTITUTION){
		// v1 = u1*(m1-m2) + 2*m2*u2 / m1+m2
		
		double other_mass = other.getMass();
		Vector3D other_velocity = other.getVelocity();
		Vector3D temp = new Vector3D();
		double mass = 1/this.inverseMass;
		double metricDiff = other_velocity.metric - velocity.metric;
		
		if(metricDiff!=0)
			other_velocity.scale(Math.pow(10, metricDiff));
		

		// 0 for inelastic collisions, 1 for elastic collisions
		temp.x = (COEFFICIENT_OF_RESTITUTION*other_mass*(other_velocity.x-velocity.x) + mass*velocity.x + other_mass*other_velocity.x)/(mass+other_mass);
		temp.y = (COEFFICIENT_OF_RESTITUTION*other_mass*(other_velocity.y-velocity.y) + mass*velocity.y + other_mass*other_velocity.y)/(mass+other_mass);
		temp.z = (COEFFICIENT_OF_RESTITUTION*other_mass*(other_velocity.z-velocity.z) + mass*velocity.z + other_mass*other_velocity.z)/(mass+other_mass);
		temp.metric = velocity.metric;
		
		return temp;
	}
	
	public String getAtomicSymbol() { 
		return atomicSymbol;
	}

	/**
	 * Fetch data from PeriodicTable.com 
	 * Requires JSoup plugin
	 * @param atomicSymbol
	 * @throws Exception
	 */
	private void fetchAtomicData (String atomicSymbol) throws Exception{
	
		double data [] = Init.getAtomicData(atomicSymbol);
		mass = data [0];
		valence = (int) data [1];
		atomicRadius = data [2];
		covalentRadius = data [3];
		vdwRadius = data [4];
		
	}
	
	public Matrix3 getInertiaTensor(Vector3D axisOfRotation){
		
		double x = position.x - axisOfRotation.x;
		double y = position.y - axisOfRotation.y;
		double z = position.z - axisOfRotation.z;
		
		double ixx = mass * (y*y + z*z); double iyy = mass * (x*x + z*z); double izz = mass * (x*x + y*y);
		double ixy = -mass * x * y; double iyx = ixy;
		double ixz = -mass * x * z; double izx = ixz;
		double iyz = -mass * y * z; double izy = iyz;
		Matrix3 inertiaTensor = new Matrix3(ixx,ixy,ixz,iyx,iyy,iyz,izx,izy,izz);
		inertiaTensor.print();
		
		return inertiaTensor;
	}

}
