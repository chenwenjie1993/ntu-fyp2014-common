package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models;

public class ElectrostaticPotential extends NonBondedInteraction {
	public static double f = 138.935_485;
	public static double er = 1;

	public ElectrostaticPotential(Atom i, Atom j) {
		super(i, j);
	}

	@Override
	public void updatePotentialEnergy() {
		
	}
	
}
