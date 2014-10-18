package sg.edu.ntu.aalhossary.fyp2014.physics_engine;

public class ElectricForce {
	private static final double COULOMB_CONSTANT = 8.987551787e+9;
	
	/* Return the ElectricForce experienced by Particle1 as a result of particle2  */
	public static Force getElectricForce(AbstractParticle particle1, AbstractParticle particle2){
	
		
		// F = (k * q1 * q2 / |r| / |r|) * unit_vector_r
		
		//double charge1 = particle1.getCharge();
		//double charge2 = particle2.getCharge();
		
		double charge1 = 0.05;
		double charge2 = 0.05;
		
		// Finding distance between 2 particles
		Vector3D distanceVector = new Vector3D (particle1.getPosition());
		distanceVector.subtract(particle2.getPosition());
		
		// unit vector
		distanceVector.normalize();
		
		// scalar distance
		double scalar_distance = distanceVector.getMagnitude();
		
		if(scalar_distance == 0)
			return new Force(0,0,0);
		
		distanceVector.scale(COULOMB_CONSTANT*charge1*charge2/scalar_distance/scalar_distance);
		return new Force (distanceVector);
	}
}
