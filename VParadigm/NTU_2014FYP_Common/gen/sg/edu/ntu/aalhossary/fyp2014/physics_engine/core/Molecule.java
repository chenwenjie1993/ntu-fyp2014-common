package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Chain;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;

public class Molecule extends sg.edu.ntu.aalhossary.fyp2014.common.Molecule{

	// Molecule is made up of chains and interaction
	// Chains are made up of residues and atoms
	// Residues are made up of atoms and bonds (interaction)
	private double radius = 0;
	private ArrayList<Atom> atoms;
	
	public Molecule(ArrayList<Atom> atoms){
		
		super();
		Chain chain =  new Chain();		// for consistency with super class
		this.atoms = new ArrayList<>();
		double mass = 0;
		
		// Calculating position
		Vector3D position = getCentroid(atoms);
					
		for (Atom atom : atoms) {
			this.atoms.add(atom);
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
		}
		
		this.chains.add(chain);
		
		this.setMass(mass);
		this.molecularMass = (float)mass;
		this.boundingPrimitive = new BoundingSphere(radius, position);
		this.position = position;
	}
	
	public Vector3D getCentroid(ArrayList<Atom> particles){
		Vector3D pos = particles.get(0).getPosition();
		int i = 1;
		for(i=1; i<particles.size(); i++){
			pos.add(particles.get(i).getPosition());
		}
		pos.scale(1/i);
		return pos;
	}
	
	public void setNetCharge(int charge){
		this.netCharge = charge;
	}
	
	public void setPosition(double x, double y, double z, int metric) {
		Vector3D pos = new Vector3D(x, y, z, metric);
		pos.subtract(this.position);
		for (Atom a: atoms) 
			a.movePositionBy(pos.x, pos.y, pos.z, metric);
		this.position = pos;
		this.boundingPrimitive.updateCentre(pos.x, pos.y, pos.z, metric);
	}
	
	public void setPosition(double x, double y, double z){
		Vector3D pos = new Vector3D(x, y, z);
		pos.subtract(this.position);
		for (Atom a: atoms) 
			a.movePositionBy(pos.x, pos.y, pos.z, position.metric);
		this.position = pos;
		boundingPrimitive.updateCentre(x, y, z, position.metric);	
	}
	
	public void integrate(double duration) {
			
		if(World.simulationLvlAtomic == false) {
			// Calculate total acceleration without updating the original ( a = F /m )
			Vector3D currentAcceleration = new Vector3D (acceleration.x, acceleration.y, acceleration.z, acceleration.metric);
			currentAcceleration.addScaledVector(forceAccumulated, inverseMass);
			
			// Update current velocity (v = a*t)
			Vector3D initialVelocity = new Vector3D (velocity.x, velocity.y, velocity.z, velocity.metric);
			velocity.addScaledVector(currentAcceleration, duration);
			
			// Update current position (s = u*t + 0.5*a*t*t)
			position.addScaledVector(initialVelocity, duration);
			position.addScaledVector(currentAcceleration, duration * duration /2);
			
			// Clear forces
			clearAccumulator();
			
			// Update the centre of the boundingPrimitive 
			boundingPrimitive.updateCentre(position.x, position.y, position.z, position.metric);
		}
		else {
			Vector3D totalAcceleration = new Vector3D();
			for (Atom a: atoms){
				// Calculate total acceleration without updating the original ( a = F /m )
				Vector3D currentAcceleration = a.getAcceleration();
				currentAcceleration.addScaledVector(a.getForceAccumulated(), a.getInverseMass());
				totalAcceleration.add(currentAcceleration);
				
				// Clear forces
				clearAccumulator();
			}
			
			// Update current velocity (v = a*t)
			Vector3D initialVelocity = new Vector3D (velocity.x, velocity.y, velocity.z, velocity.metric);
			velocity.addScaledVector(totalAcceleration, duration);
			
			// Update current position (s = u*t + 0.5*a*t*t)
			position.addScaledVector(initialVelocity, duration);
			position.addScaledVector(totalAcceleration, duration * duration /2);
			
			// Update the centre of the boundingPrimitive 
			boundingPrimitive.updateCentre(position.x, position.y, position.z, position.metric);
		}
	}
	
	public Vector3D calculateVelocityChange(AbstractParticle other, double COEFFICIENT_OF_RESTITUTION){
		
		if(World.simulationLvlAtomic == false) {
			
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
		else {
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
							System.out.println("The two particles collided." + min_dist);
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
						System.out.println("The two particles collided." + min_dist);
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
	}
}