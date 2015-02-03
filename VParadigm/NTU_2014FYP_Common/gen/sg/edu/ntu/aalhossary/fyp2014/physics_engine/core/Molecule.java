package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Chain;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;

public class Molecule extends sg.edu.ntu.aalhossary.fyp2014.common.Molecule{

	// Molecule is made up of chains and interaction
	// Chains are made up of residues and atoms
	// Residues are made up of atoms and bonds (interaction)
	private double radius;

	public Molecule(ArrayList<Atom> atoms){
		
		super();
		Chain chain =  new Chain();
		
		double mass = 0;
		double radius = 0;
		for (Atom atom : atoms) {
			chain.atomSeq.add(atom);
			
			// Calculating total mass
			mass += atom.getMass();
			
			// Calculating radius
			Vector3D pos = atom.getPosition();
			double max_dist = Math.max(Math.abs(pos.z), Math.max(Math.abs(pos.x),Math.abs(pos.y)));
			if(radius < max_dist)
				radius = max_dist;
		}
		this.chains.add(chain);
		
		this.setMass(mass);
		this.molecularMass = (float)mass;
		this.boundingPrimitive = new BoundingSphere(radius, position);
	}
	
	public void setNetCharge(int charge){
		this.netCharge = charge;
	}
	
	public void integrate(double duration) {
			
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
	
	public Vector3D calculateVelocityChange(double other_mass, Vector3D other_velocity, double COEFFICIENT_OF_RESTITUTION){
		// v1 = u1*(m1-m2) + 2*m2*u2 / m1+m2
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
	
	public Model toModel(){
		Model model = new Model();
		
		return null;
	}
}