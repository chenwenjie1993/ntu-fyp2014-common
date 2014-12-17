package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

public class Force{

	/**
	 * Return VdW force experienced by particle1 due to particle2
	 * @param particle1 (requires BoundingSphere radius)
	 * @param particle2 (requires BoundingSphere radius)
	 * @return VdWForce
	 */
	public static Vector3D getVdWForce(AbstractParticle particle1, AbstractParticle particle2){
		double Hamaker_coefficient = 1e-19; //can be positive or negative in sign depending on the intervening medium?
		
		BoundingPrimitive bp1 = particle1.getBoundingPrimitive();
		BoundingPrimitive bp2 = particle2.getBoundingPrimitive();
		
		if(bp1 instanceof BoundingSphere && bp2 instanceof BoundingSphere){
			BoundingSphere bs1 = (BoundingSphere) bp1;
			BoundingSphere bs2 = (BoundingSphere) bp2;
			double r1 = bs1.getRadius();
			double r2 = bs2.getRadius();
			
			Vector3D distanceVector = new Vector3D(particle1.getPosition());
			distanceVector.subtract(particle2.getPosition());
			double sq_distance = distanceVector.getSquaredMagnitude();
			
			double term1 = 2*r1*r2 / (sq_distance - (r1+r2)*(r1+r2));
			double term2 = 2*r1*r2 / (sq_distance - (r1-r2)*(r1-r2));
			double term3 = Math.log ((sq_distance - (r1+r2)*(r1+r2))/(sq_distance - (r1-r2)*(r1-r2)));
			double VdWScalar = -Hamaker_coefficient/6*(term1 + term2 + term3);
			
			distanceVector.normalize();
			distanceVector.scale(VdWScalar);
			return distanceVector;
		}
		return new Vector3D();
	}
	
 
	/**
	 * Return the ElectricForce experienced by Particle1 due to particle2
	 * @param particle1 (requires atomic charge)
	 * @param particle2 (requires atomic charge)
	 * @return ElectricForce
	 */
	public static Vector3D getElectricForce(AbstractParticle particle1, AbstractParticle particle2){
	
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
				
		if(scalar_distance == 0)
			return new Vector3D();
		
		distanceVector.scale(COULOMB_CONSTANT*charge1*charge2/scalar_distance/scalar_distance);
		return distanceVector;
	}
	
	/**
	 * Return the Lennard-Jones potential (need epsilon + bond length)
	 * @param particle1
	 * @param particle2
	 * @return
	 */
	public static Vector3D getLennardJonesPotential(AbstractParticle particle1, AbstractParticle particle2){
		
		double epsilon = 0.1e-8;					// depth of potential well
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