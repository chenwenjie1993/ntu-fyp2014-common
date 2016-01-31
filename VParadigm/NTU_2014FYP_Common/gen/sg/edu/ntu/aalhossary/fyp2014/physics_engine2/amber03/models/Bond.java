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
//		System.out.println(query.toString());
		b0 = params.get(0);
		kb = params.get(1);
	}

	@Override
	public void updatePotentialEnergy() {
		Vector3D dist3D = Geometry.vector(atoms.get(0).getPosition(), atoms.get(1).getPosition());
		
		Vector3D bond = dist3D.getUnitVector();
		bond.scale(b0);
		
//		Vector3D energy = new Vector3D();
		Vector3D force = new Vector3D();
		double d_x = dist3D.x - bond.x;
		force.x = - kb * d_x;
//		energy.x = kb * d_x * d_x;
		
		double d_y = dist3D.y - bond.y;
		force.y = - kb * d_y;
//		energy.y = kb * d_y * d_y;
		
		double d_z = dist3D.z - bond.z;
		force.z = - kb * d_z;
//		energy.z = kb * d_z;
		
//		atoms.get(0).potentialEnergy.add(energy);
		atoms.get(0).addForce(force);
//		atoms.get(1).potentialEnergy.add(energy.getNegativeVector());
		atoms.get(1).addForce(force.getNegativeVector());
//		System.out.println("Force: " + force.toString());
		log.info("[B] " + "(" + atoms.get(0).getGUID() + "," + atoms.get(1).getGUID() + ")" + force.toString());
	}

}
