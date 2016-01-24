package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;

public abstract class Dihedral extends BondedInteraction {
	public Atom i, j, k, l;
	Vector3D v_ji, v_jk, v_lk;
	
	public Dihedral(List<Atom> atoms) {
		super(atoms);
	}

	public abstract void updatePotentialEnergy();

}
