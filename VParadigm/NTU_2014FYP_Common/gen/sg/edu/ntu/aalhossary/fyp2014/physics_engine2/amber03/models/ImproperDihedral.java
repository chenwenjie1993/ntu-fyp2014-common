package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;

public class ImproperDihedral extends Dihedral {
	public double phrase, kd;

	public ImproperDihedral(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getImproperDihedralParams(query);
		phrase = params.get(0);
		kd = params.get(1);
	}

}
