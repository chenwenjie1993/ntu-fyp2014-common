package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Distance;

public class ElectrostaticPotential extends NonBondedInteraction {
	public static double f = 138.935_485;
	public static double epsilon_r = 1;

	public ElectrostaticPotential(Atom i, Atom j) {
		super(i, j);
	}

	@Override
	public void updatePotentialEnergy() {
		Vector3D dist = Distance.distance3D(i.getPosition(), j.getPosition());
		Vector3D energy = new Vector3D();
		energy.x = f/epsilon_r * i.charge * j.charge / dist.x;
		energy.y = f/epsilon_r * i.charge * j.charge / dist.y;
		energy.z = f/epsilon_r * i.charge * j.charge / dist.z;
		i.potentialEnergy.add(energy);
		j.potentialEnergy.add(energy);
	}
}
