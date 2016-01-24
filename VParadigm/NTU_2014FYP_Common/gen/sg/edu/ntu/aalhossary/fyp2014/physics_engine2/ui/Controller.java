package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;

public abstract class Controller {
	public String status = "Running";
	
	public abstract void progress(MolecularSystem m);
}
