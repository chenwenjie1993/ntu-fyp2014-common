package sg.edu.ntu.aalhossary.fyp2014.physics_engine.model;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;

/**
 * Return VdW force experienced by particle1 due to particle2
 * @param particle1 (requires BoundingSphere radius)
 * @param particle2 (requires BoundingSphere radius)
 * @return VdWForce
 */
public class VdWForce implements Force{
	
	private Vector3D vdwForce;
	
	public VdWForce (AbstractParticle particle1, AbstractParticle particle2){
		vdwForce = calculateForce(particle1, particle2);
	}
	
	public Vector3D getForce(){
		return vdwForce;
	}
	
	public Vector3D getNegativeForce(){
		return vdwForce.getNegativeVector();
	}
	
	/**
	 * Calculate the vdW force between the two particles
	 */
	public Vector3D calculateForce(AbstractParticle particle1, AbstractParticle particle2){
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
	
}
