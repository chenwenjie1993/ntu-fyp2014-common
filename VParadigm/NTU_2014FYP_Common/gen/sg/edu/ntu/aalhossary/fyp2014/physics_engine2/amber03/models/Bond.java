package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

public class Bond implements BondedInteraction {
	public Atom i, j;
	
	public Bond(Atom i, Atom j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public void updatePotentialEnergy() {
	}

}
