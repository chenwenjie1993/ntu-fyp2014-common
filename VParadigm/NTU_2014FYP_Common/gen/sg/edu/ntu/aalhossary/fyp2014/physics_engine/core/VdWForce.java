package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;



public class VdWForce {

	public static Force getVdWForce (AbstractParticle particle1, AbstractParticle particle2){
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
			return new Force(distanceVector);
		}
		return new Force();
	}
}
