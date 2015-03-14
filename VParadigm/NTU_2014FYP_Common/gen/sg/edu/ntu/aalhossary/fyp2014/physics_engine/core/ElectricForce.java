package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

/**
 * @author waiyan
 * Return the ElectricForce experienced by Particle1 due to particle2
 * @param particle1 (requires atomic charge)
 * @param particle2 (requires atomic charge)
 * @return ElectricForce
 */
public class ElectricForce implements Force{
	
	private Vector3D electricForce;
	
	public ElectricForce (AbstractParticle particle1, AbstractParticle particle2){
		electricForce = calculateForce(particle1, particle2);
	}
	
	public Vector3D getForce(){
		return electricForce;
	}
	
	public Vector3D getNegativeForce(){
		return electricForce.getNegativeVector();
	}
	
	/**
	 * Calculate the electric force between the two particles
	 */
	public Vector3D calculateForce (AbstractParticle particle1, AbstractParticle particle2){
	
		double COULOMB_CONSTANT = 8.987551787e+9;
		double CHARGE_IN_COULOMB = 1.602e-19;
		
		double charge1 = CHARGE_IN_COULOMB * particle1.getNetCharge();
		double charge2 = CHARGE_IN_COULOMB * particle2.getNetCharge();
		
		// Finding distance between 2 particles
		Vector3D distanceVector = new Vector3D (particle1.getPosition());
		distanceVector.subtract(particle2.getPosition());
	
		// scalar distance
		double scalar_distance = distanceVector.getMagnitude();
		
		// unit vector
		distanceVector.normalize();
		
		if(particle1 instanceof Atom == false || particle2 instanceof Atom == false)
			return new Vector3D();
		
		Atom a1 = (Atom) particle1;
		Atom a2 = (Atom) particle2;
		
		if((a1.getAtomicSymbol().equals(a2.getAtomicSymbol()) && a1.getAtomicSymbol().equals("C"))||scalar_distance == 0)
			return new Vector3D();
		
		distanceVector.scale(COULOMB_CONSTANT*charge1*charge2/scalar_distance/scalar_distance);
		return distanceVector;
	}
}
