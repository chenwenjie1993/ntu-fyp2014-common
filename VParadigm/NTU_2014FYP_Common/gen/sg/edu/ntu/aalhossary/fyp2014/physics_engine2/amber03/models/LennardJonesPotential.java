package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;
import java.util.logging.Logger;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.*;

public class LennardJonesPotential extends NonBondedInteraction {

	public double sigma_i, sigma_j, epsilon_i, epsilon_j;
	private static final Logger log = Logger.getLogger("main");

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
		Vector3D v_ij = Geometry.vector(i.getPosition(), j.getPosition());
		double dist = v_ij.getMagnitude();
		
		double ci6 = 4 * epsilon_i * Math.pow(sigma_i, 6);
		double ci12 = 4 * epsilon_i * Math.pow(sigma_i, 12);
		double cj6 = 4 * epsilon_j * Math.pow(sigma_j, 6);
		double cj12 = 4 * epsilon_j * Math.pow(sigma_j, 12);
		double cij6 = Math.sqrt(ci6 * cj6);
		double cij12 = Math.sqrt(ci12 * cj12);
		
		log.info("[DIST]" + "(" + i.getGUID() + "," + j.getGUID() + ")" + dist);
		log.info("[SIGMA]" + "(" + i.getGUID() + "," + j.getGUID() + ")" + 0.5 * (sigma_i + sigma_j));
		
		double forceMagnitude = 12 * cij12 / Math.pow(dist, 13) - 6 * cij6 / Math.pow(dist, 7);
		
		Vector3D force = v_ij.getUnitVector();
		force.scale(forceMagnitude);
		
		i.addForce(force);
		j.addForce(force.getNegativeVector());
//		log.info("[L] " + "(" + i.getGUID() + "," + j.getGUID() + ")" + forceMagnitude);
		log.info("[L] " + "(" + i.getGUID() + "," + j.getGUID() + ")" + force.toString());
//		System.out.println(i.getAccumulatedForce());
//		i.potentialEnergy.add(energy.getNegativeVector());
//		j.potentialEnergy.add(energy);
	}


}
