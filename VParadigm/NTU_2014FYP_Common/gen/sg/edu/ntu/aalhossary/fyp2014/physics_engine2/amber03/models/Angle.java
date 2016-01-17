package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;

public class Angle extends BondedInteraction {
	public double th0, cth;
	
	public Angle(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getAngleParams(query);
		th0 = params.get(0);
		cth = params.get(1);
	}
	
	@Override
	public void updatePotentialEnergy() {
		
	}
	
}
