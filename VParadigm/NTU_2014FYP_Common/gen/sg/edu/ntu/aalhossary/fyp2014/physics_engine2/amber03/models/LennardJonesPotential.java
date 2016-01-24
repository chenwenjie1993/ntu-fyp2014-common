package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.*;

public class LennardJonesPotential extends NonBondedInteraction {
	public enum AverageType {
		Geometric,
		Arithmetic
	};
	public double sigma_i, sigma_j, epsilon_i, epsilon_j;
	public static AverageType sigmaAverageType = AverageType.Arithmetic;

	public LennardJonesPotential(Atom i, Atom j) {
		super(i, j);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<Double>param_i = db.getVdParams(i.type);
		List<Double>param_j = db.getVdParams(j.type);
		sigma_i = param_i.get(0);
		sigma_j = param_j.get(0);
		epsilon_i = param_i.get(1);
		epsilon_j = param_j.get(1);
	}

	@Override
	public void updatePotentialEnergy() {
		double average_sigma = getAverageSigma();
		double average_epsilon = getAverageEpsilon();
		Vector3D dist = Geometry.distance3D(i.getPosition(), j.getPosition());
		Vector3D energy = new Vector3D();
		double temp_x = average_sigma / dist.x;
		energy.x = 4 * average_epsilon * (Math.pow(temp_x, 12) - Math.pow(temp_x, 6));
		double temp_y = average_sigma / dist.y;
		energy.y = 4 * average_epsilon * (Math.pow(temp_y, 12) - Math.pow(temp_y, 6));
		double temp_z = average_sigma / dist.z;
		energy.z = 4 * average_epsilon * (Math.pow(temp_z, 12) - Math.pow(temp_z, 6));
		// TODO: check correctness
		i.potentialEnergy.add(energy.getNegativeVector());
		j.potentialEnergy.add(energy);
	}

	public double getAverageSigma() {
		if (sigmaAverageType == AverageType.Geometric) {
			return Math.sqrt(sigma_i * sigma_j);
		}
		return 0.5 * (sigma_i + sigma_j);
	}
	
	public double getAverageEpsilon() {
		return Math.sqrt(epsilon_i * epsilon_j);
	}
}
