package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;

public class Bond extends BondedInteraction {
	public double k, b;
	
	public Bond(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
//		db.getBondParams(atoms)
	}

	@Override
	public void updatePotentialEnergy() {
		
	}

}
