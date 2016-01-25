package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class ElectrostaticPotential extends NonBondedInteraction {
	public static double f = 138.935485;
	public static double epsilon_r = 1;

	public ElectrostaticPotential(Atom i, Atom j) {
		super(i, j);
	}

	@Override
	public void updatePotentialEnergy() {
		// distance from i to j
		Vector3D dist3D = Geometry.distance3D(i.getPosition(), j.getPosition());
		Vector3D energy = new Vector3D();
		energy.x = f * i.charge * j.charge / epsilon_r / dist3D.x;
		energy.y = f * i.charge * j.charge / epsilon_r / dist3D.y;
		energy.z = f * i.charge * j.charge / epsilon_r / dist3D.z;
		
		Vector3D force = new Vector3D();
		force.x = - energy.x / dist3D.x;
		force.y = - energy.y / dist3D.y;
		force.z = - energy.z / dist3D.z;
		
		i.addForce(force);
		j.addForce(force.getNegativeVector());
//		System.out.println(force);
//		i.potentialEnergy.add(energy);
//		j.potentialEnergy.add(energy.getNegativeVector());
	}
}
