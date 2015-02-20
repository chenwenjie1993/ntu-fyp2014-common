package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

/**
 * Return the Lennard-Jones potential (need epsilon + bond length)
 * @param particle1
 * @param particle2
 * @return
 */
public class LennardJonesForce implements Force{
	
	private Vector3D lennardJonesForce;
	
	public LennardJonesForce (AbstractParticle particle1, AbstractParticle particle2){
		lennardJonesForce = calculateForce(particle1, particle2);
	}
	
	public Vector3D getForce(){
		return lennardJonesForce;
	}
	
	public Vector3D getNegativeForce(){
		return lennardJonesForce.getNegativeVector();
	}
	
	/**
	 * Calculate the LennardJonesForce between the two particles
	 */
	public Vector3D calculateForce(AbstractParticle particle1, AbstractParticle particle2){
		
		/*** known as theta */
		double epsilon = 0.1e-8;			// depth of potential well
		double bond_length = 0.28E-9;		// bond length for NaCl
		// bond length or vdw radius?

		// Finding distance between 2 particles
		Vector3D distanceVector = new Vector3D (particle1.getPosition());
		distanceVector.subtract(particle2.getPosition());
		
		// scalar distance
		double scalar_distance = distanceVector.getMagnitude();
		
		// unit vector
		distanceVector.normalize();
				
		if(scalar_distance == 0)
			return new Vector3D();
		
		double term2 = Math.pow(bond_length/scalar_distance, 6);
		double term1 = term2 * term2;
		
		distanceVector.scale(4*epsilon*(term1 - term2));
		return distanceVector;
	}
}
