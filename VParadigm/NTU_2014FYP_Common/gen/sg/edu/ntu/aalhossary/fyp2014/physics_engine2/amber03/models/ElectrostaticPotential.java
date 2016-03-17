package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.logging.Logger;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class ElectrostaticPotential extends NonBondedInteraction {
	private static final Logger log = Logger.getLogger("main");
	public static double f = 138.935485;
	public static double epsilon_r = 1;

	public ElectrostaticPotential(Atom i, Atom j) {
		super(i, j);
	}

	@Override
	public void calcPotentialEnergyTerm() {
		// distance from i to j
		Vector3D v_ij = Geometry.vector(i.getPosition(), j.getPosition());
		
		double dist = v_ij.getMagnitude();		
		Vector3D force = new Vector3D();
		
		double forceMagnitude = f * i.charge * j.charge / epsilon_r / dist;
		force = v_ij.getUnitVector();
		force.scale(forceMagnitude);
		

//		if (Math.abs(u.x) - 1e-10 > 0) {
//			force.x = - u.x / v_ij.x;
//		}
//		if (Math.abs(u.y) - 1e-10 > 0) {
//			force.y = - u.y / v_ij.y;
//		}
//		if (Math.abs(u.z) - 1e-10 > 0) {
//			force.z = - u.z / v_ij.z;
//		}
		
		i.addForce(force);
		j.addForce(force.getNegativeVector());
		log.info("[E]" + "(" + i.getGUID() + "," + j.getGUID() + ")" + forceMagnitude);
		log.info("[E]" + "(" + i.getGUID() + "," + j.getGUID() + ")" + force.toString());
//		System.out.println(i.getAccumulatedForce());
//		i.potentialEnergy.add(energy);
//		j.potentialEnergy.add(energy.getNegativeVector());
	}
}
