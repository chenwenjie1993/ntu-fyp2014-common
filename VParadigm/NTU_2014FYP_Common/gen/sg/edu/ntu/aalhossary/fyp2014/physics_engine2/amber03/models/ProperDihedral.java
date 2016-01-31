package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TopologyDatabase;

public class ProperDihedral extends Dihedral {
	double mult;

	public ProperDihedral(List<Atom> atoms) {
		super(atoms);
		TopologyDatabase db = TopologyDatabase.getInstance();
		List<String> query = new ArrayList<String>();
		for (Atom atom : atoms) {
			query.add(atom.type);
		}
		List<Double> params = db.getProperDihedralParams(query);
		phi0 = params.get(0);
		kd = params.get(1);
		mult = params.get(2);
	}

	@Override
	public void updatePotentialEnergy() {
		updateProperties();
		
		double mdphi, sdphi;
		mdphi = mult * phi - phi0;
		sdphi = Math.sin(mdphi);
		dVdphi = - kd * mult * sdphi;
		
		applyForce();
	}
}
