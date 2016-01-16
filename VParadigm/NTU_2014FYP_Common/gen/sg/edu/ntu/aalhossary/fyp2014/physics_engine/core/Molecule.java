package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Chain;
import sg.edu.ntu.aalhossary.fyp2014.common.math.Matrix3;
import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

/**
 * @author waiyan
 * Handle creating of molecules
 * Solve the acceleration, velocity and position of the molecule before/after impact
 */
public class Molecule extends sg.edu.ntu.aalhossary.fyp2014.common.Molecule{

	// Molecule is made up of chains and interaction
	// Chains are made up of residues and atoms
	// Residues are made up of atoms and bonds (interaction)
	private double radius = 0;
	private ArrayList<Atom> atoms = new ArrayList<Atom>();
	
	public Molecule(sg.edu.ntu.aalhossary.fyp2014.common.Molecule mol) throws Exception{
		//TODO copy everything
		
		double mass=0;
		double r  =0 ;	
		
		for(sg.edu.ntu.aalhossary.fyp2014.common.Chain chain: mol.getChains()){
			for (sg.edu.ntu.aalhossary.fyp2014.common.Residue residue: chain.residues) {
				for (sg.edu.ntu.aalhossary.fyp2014.common.Atom atom: residue.getAtomList()){
					Atom newAtom = new Atom(atom);
					atoms.add(newAtom);
					mass += newAtom.getMass();	
				}
			}
		}
		Vector3D position = getCentroid();
		
		for(Atom atom: atoms){
			// Calculating radius
			Vector3D pos = atom.getPosition();
			double maxDistX = Math.abs(Math.abs(pos.x)-Math.abs(position.x));
			double maxDistY = Math.abs(Math.abs(pos.y)-Math.abs(position.y));
			double maxDistZ = Math.abs(Math.abs(pos.z)-Math.abs(position.z));
			double max_dist = Math.max(Math.max(maxDistX, maxDistY), maxDistZ);
			
			if(radius < max_dist)
				radius = max_dist;
			
			BoundingSphere b = (BoundingSphere)(atom.getBoundingPrimitive());
			r = Math.max(r, b.getRadius());
		}
		
		this.setMass(mass);
		this.molecularMass = (float)mass;
		this.boundingPrimitive = new BoundingSphere(radius, position);
		this.position = position;
	}
	
	/**
	 * Construct a molecule given a lsit of atoms
	 * @param atoms
	 */
	public Molecule(ArrayList<Atom> atoms){
		
		super();
		Chain chain =  new Chain();		// for consistency with super class
		this.atoms = atoms;
		double mass = 0;
		
		// Calculating position
		Vector3D position = getCentroid();
		double r  =0 ;	
		for (Atom atom : atoms) {
			
			chain.atomSeq.add(atom);
			
			// Calculating total mass
			mass += atom.getMass();	
			
			// Calculating radius
			Vector3D pos = atom.getPosition();
			double maxDistX = Math.abs(Math.abs(pos.x)-Math.abs(position.x));
			double maxDistY = Math.abs(Math.abs(pos.y)-Math.abs(position.y));
			double maxDistZ = Math.abs(Math.abs(pos.z)-Math.abs(position.z));
			double max_dist = Math.max(Math.max(maxDistX, maxDistY), maxDistZ);
			
			if(radius < max_dist)
				radius = max_dist;
			
			BoundingSphere b = (BoundingSphere)(atom.getBoundingPrimitive());
			r = Math.max(r, b.getRadius());
		}
		
		this.chains.add(chain);
		
		this.setMass(mass);
		this.molecularMass = (float)mass;
		this.boundingPrimitive = new BoundingSphere(radius, position);
		this.position = position;
		
		
	}
	
	/**
	 * Get the centroid position of the molecule
	 * @return position
	 */
	private Vector3D getCentroid(){
		Vector3D pos = new Vector3D();
		for(int i=0; i<atoms.size(); i++){
			pos.add(atoms.get(i).getPosition());
		}
		pos.scale(1.0/atoms.size());
		return pos;
	}
	
	/**
	 * Return the atoms constituting the molecule
	 * @return atoms
	 */
	public ArrayList<Atom> getAtoms(){
		return atoms;
	}
	
	/**
	 * Set the postion of the molecule as the centroid of constituting atoms
	 */
	public void setPositionAsCentroid(){
		this.position = getCentroid();
	}
	
	public void setNetCharge(int charge){
		this.netCharge = charge;
	}
	
	/**
	 * Set the position of the molecule. Also set the velocities of the constituting atoms.
	 */
	public void setPosition(double x, double y, double z, int metric) {
		Vector3D pos = new Vector3D(x, y, z, metric);
		if(this.position.equals(pos))
			return;
		
		pos.subtract(this.position);
		for (Atom a: atoms) 
			a.movePositionBy(pos.x, pos.y, pos.z, metric);
		this.position = new Vector3D (x,y,z,metric);
		this.boundingPrimitive.updateCentre(pos.x, pos.y, pos.z, metric);
	}
	
	/**
	 * Set the position of the molecule. Also set the velocities of the constituting atoms.
	 */
	public void setPosition(double x, double y, double z){
		Vector3D pos = new Vector3D(x, y, z);
		if(this.position.equals(pos))
			return;
		
		pos.subtract(this.position);
	//	pos.round();
		for (Atom a: atoms) 
			a.movePositionBy(pos.x, pos.y, pos.z, position.metric);
		this.position = new Vector3D (x,y,z);
		boundingPrimitive.updateCentre(x, y, z, position.metric);	
	}
	
	/**
	 * Set the velocity of the molecule. Also set the velocities of the constituting atoms.
	 */
	public void setVelocity(double x, double y, double z){
		for (Atom a: atoms)
			a.setVelocity(x, y, z);
		this.velocity = new Vector3D(x,y,z);
	}
	
	/**
	 * Set the velocity of the molecule. Also set the velocities of the constituting atoms.
	 */
	public void setVelocity(double x, double y, double z, int metric){
		for (Atom a: atoms)
			a.setVelocity(x, y, z, metric);
		this.velocity = new Vector3D(x,y,z, metric);
	}
	
	
	
	public void setInverseInertiaTensor(Vector3D axisOfRotation){
		
		double total_ixx = 0, total_ixy = 0, total_ixz = 0;
		double total_iyx = 0, total_iyy = 0, total_iyz = 0;
		double total_izx = 0, total_izy = 0, total_izz = 0;
		
		for(Atom atom: atoms) {
			double x = atom.getPosition().x - axisOfRotation.x;
			double y = atom.getPosition().y - axisOfRotation.y;
			double z = atom.getPosition().z - axisOfRotation.z;
			double mass = atom.getMass();
			total_ixx += mass * (y*y + z*z); 
			total_iyy += mass * (x*x + z*z); 
			total_izz += mass * (x*x + y*y);
			double ixy = -mass * x * y; 
			double ixz = -mass * x * z; 
			double iyz = -mass * y * z; 
			total_ixy += ixy;
			total_iyx += ixy;
			total_ixz += ixz;
			total_izx += ixz;
			total_iyz += iyz;
			total_izy += iyz;
		}
		
		Matrix3 inertiaTensor = new Matrix3(total_ixx,total_ixy,total_ixz,total_iyx,total_iyy,total_iyz,total_izx,total_izy,total_izz);
		inertiaTensor.print();
		super.setInverseInertiaTensor(inertiaTensor);
	}
	
	/**
	 * Calculate acceleration, velocity and position of the molecule after the given duration
	 */
	public void integrate(double duration) {
		
		// Calculate total acceleration without updating the original ( a = F /m )
		Vector3D currentAcceleration = new Vector3D (acceleration.x, acceleration.y, acceleration.z, acceleration.metric);
		currentAcceleration.addScaledVector(forceAccumulated, inverseMass);
		
		// Update current velocity (v = a*t)
		Vector3D initialVelocity = new Vector3D (velocity.x, velocity.y, velocity.z, velocity.metric);
		Vector3D velocity = new Vector3D (this.velocity.x, this.velocity.y, this.velocity.z, this.velocity.metric);
		velocity.addScaledVector(currentAcceleration, duration);
		velocity.round();
		this.setVelocity(velocity.x, velocity.y, velocity.z);
		
		
		// Update current position (s = u*t + 0.5*a*t*t)
		Vector3D position = new Vector3D (this.position.x, this.position.y, this.position.z, this.position.metric);
		position.addScaledVector(initialVelocity, duration);
		position.addScaledVector(currentAcceleration, duration * duration /2);
		this.setPosition(position.x, position.y, position.z);
		
/*		Vector3D angularAcceleration = inverseInertiaTensor.transform(torqueAccumulated);
		rotation.addScaledVector(angularAcceleration, duration);
		orientation.addScaledVector(rotation, duration);
		orientation.normalize();
*/
		// Clear forces
		clearAccumulator();
		
		
			
		/*
		else if(World.simulationLvlAtomic == true) {
			Vector3D totalAcceleration = new Vector3D();
			for (Atom a: atoms){
				// Calculate total acceleration without updating the original ( a = F /m )
				Vector3D currentAcceleration = new Vector3D (acceleration.x, acceleration.y, acceleration.z, acceleration.metric);
				currentAcceleration.addScaledVector(a.getForceAccumulated(), a.getInverseMass());
				totalAcceleration.add(currentAcceleration);
			}
			
			// Update current velocity (v = a*t)
			Vector3D initialVelocity = new Vector3D (velocity.x, velocity.y, velocity.z, velocity.metric);
			Vector3D velocity = new Vector3D (this.velocity.x, this.velocity.y, this.velocity.z, this.velocity.metric);
			velocity.addScaledVector(totalAcceleration, duration);
			velocity.round();
			this.setVelocity(velocity.x, velocity.y, velocity.z);
			
			// Update current position (s = u*t + 0.5*a*t*t)
			Vector3D position = new Vector3D (this.position.x, this.position.y, this.position.z, this.position.metric);
			position.addScaledVector(initialVelocity, duration);
			position.addScaledVector(totalAcceleration, duration * duration /2);
			this.setPosition(position.x, position.y, position.z);
			
			// Clear forces
			clearAccumulator();
			
			// Update the centre of the boundingPrimitive 
		//	boundingPrimitive.updateCentre(position.x, position.y, position.z, position.metric);
		 
		 */
	}
	
	/**
	 * Calculate the velocity change after impact
	 */
	public Vector3D calculateVelocityChange(AbstractParticle other, double COEFFICIENT_OF_RESTITUTION){
		
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
		/*
		if (World.simulationLvlAtomic == true) {
			ArrayList<Atom> atomList1 = this.atoms;
			ArrayList<AbstractParticle[]> potentialContacts = new ArrayList<>();
			Vector3D total_vel = new Vector3D();
			
			if(other instanceof Molecule){
				Molecule molecule = (Molecule) other;
				ArrayList<Atom> atomList2 = molecule.atoms;
				for (Atom a: atomList1){
					for (Atom b: atomList2){
						double scale1 = Math.pow(10, a.getPosition().metric);
						double scale2 = Math.pow(10, b.getPosition().metric);
						double diff_x = a.getPosition().x * scale1 - b.getPosition().x * scale2;
						double diff_y = a.getPosition().y * scale1 - b.getPosition().y * scale2;
						double diff_z = a.getPosition().z * scale1 - b.getPosition().z * scale2;
						double min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
						double r1 = ((BoundingSphere)(a.getBoundingPrimitive())).getRadius();
						double r2 = ((BoundingSphere)(b.getBoundingPrimitive())).getRadius();
						double bond_length = 0.28E-9;
						if(min_dist <= bond_length){
							System.out.println("The two molecules collided." + min_dist);
							AbstractParticle [] potentialContact = {a,b};
							potentialContacts.add(potentialContact);
						}
					}
				}
			}
			
			else if (other instanceof Atom){
				Atom atom = (Atom) other;
				for (Atom a: atomList1){
					double scale1 = Math.pow(10, a.getPosition().metric);
					double scale2 = Math.pow(10, atom.getPosition().metric);
					double diff_x = a.getPosition().x * scale1 - atom.getPosition().x * scale2;
					double diff_y = a.getPosition().y * scale1 - atom.getPosition().y * scale2;
					double diff_z = a.getPosition().z * scale1 - atom.getPosition().z * scale2;
					double min_dist = Math.min(Math.abs(diff_x), Math.min(Math.abs(diff_y), Math.abs(diff_z)));
					double r1 = ((BoundingSphere)(a.getBoundingPrimitive())).getRadius();
					double r2 = ((BoundingSphere)(atom.getBoundingPrimitive())).getRadius();
					double bond_length = 0.28E-9;
					if(min_dist <= bond_length){
						System.out.println("The molecule and the atom collided.");
						AbstractParticle [] potentialContact = {a,atom};
						potentialContacts.add(potentialContact);
					}
				}
				
			}
			
			for (AbstractParticle[] potentialContact : potentialContacts){
				AbstractParticle a1 = potentialContact[0];
				AbstractParticle a2 = potentialContact[1];
				Vector3D v1 = a1.calculateVelocityChange(a2, World.COEFFICENT_OF_RESTITUTION);
				total_vel.add(v1);
			}
			return total_vel;
		}	
		*/
	}
}