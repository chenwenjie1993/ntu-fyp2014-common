package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.util.ArrayList;

public interface Interaction extends sg.edu.ntu.aalhossary.fyp2014.common.Interaction {
	public Vector3D calculatePotentialEnergy(ArrayList<Particle> particles);
}
