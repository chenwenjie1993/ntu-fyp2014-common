package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.*;

public class LennardJonesPotential extends NonBondedInteraction {

	public double sigma_i, sigma_j, epsilon_i, epsilon_j;

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
		Vector3D dist = Geometry.distance3D(i.getPosition(), j.getPosition());
		Vector3D force = new Vector3D();
		double ci6 = 4 * epsilon_i * Math.pow(sigma_i, 6);
		double ci12 = 4 * epsilon_i * Math.pow(sigma_i, 12);
		double cj6 = 4 * epsilon_j * Math.pow(sigma_j, 6);
		double cj12 = 4 * epsilon_j * Math.pow(sigma_j, 12);
		double cij6 = Math.sqrt(ci6 * cj6);
		double cij12 = Math.sqrt(ci12 * cj12);
		
		force.x = 12 * cij12 / Math.pow(dist.x, 13) - 6 * cij6 / Math.pow(dist.x, 7);
		force.y = 12 * cij12 / Math.pow(dist.y, 13) - 6 * cij6 / Math.pow(dist.y, 7);
		force.z = 12 * cij12 / Math.pow(dist.z, 13) - 6 * cij6 / Math.pow(dist.z, 7);
		
		i.addForce(force);
		j.addForce(force.getNegativeVector());

//		i.potentialEnergy.add(energy.getNegativeVector());
//		j.potentialEnergy.add(energy);
	}


}
