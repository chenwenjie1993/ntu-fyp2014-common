package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.math.Geometry;

public class ImproperDihedral extends Dihedral {

	public ImproperDihedral(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getImproperDihedralParams(query);
		phi0 = params.get(0);
		kd = params.get(1);
	}

	@Override
	public void updatePotentialEnergy() {
		updateProperties();
		dVdphi = - kd * dp;
	    applyForce();
	}
}
