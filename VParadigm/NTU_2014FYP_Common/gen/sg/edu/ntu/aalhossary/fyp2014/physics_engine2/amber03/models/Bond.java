package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class Bond extends BondedInteraction {
	public double b0, kb;
	private static final Logger log = Logger.getLogger("main");
	
	public Bond(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getBondParams(query);
		System.out.println(query.toString());
		b0 = params.get(0);
		kb = params.get(1);
	}

	@Override
	public void updatePotentialEnergy() {
		Atom atom1 = atoms.get(0);
		Atom atom2 = atoms.get(1);
		Vector3D v_ij = Geometry.vector(atom1.getPosition(), atom2.getPosition());
		
		Vector3D bond = v_ij.getUnitVector();
		bond.scale(b0);
		
//		Vector3D energy = new Vector3D();
		Vector3D force = new Vector3D();
		double d_x = v_ij.x - bond.x;
		force.x = - kb * d_x;
//		energy.x = kb * d_x * d_x;
		
		double d_y = v_ij.y - bond.y;
		force.y = - kb * d_y;
//		energy.y = kb * d_y * d_y;
		
		double d_z = v_ij.z - bond.z;
		force.z = - kb * d_z;
//		energy.z = kb * d_z;
		
//		atoms.get(0).potentialEnergy.add(energy);
		atom1.addForce(force.getNegativeVector());
//		atoms.get(1).potentialEnergy.add(energy.getNegativeVector());
		atom2.addForce(force);
//		System.out.println("Force: " + force.toString());
		log.info("[B] " + "(" + atom1.getGUID() + "," + atom2.getGUID() + ")" + force.toString());
	}

}
