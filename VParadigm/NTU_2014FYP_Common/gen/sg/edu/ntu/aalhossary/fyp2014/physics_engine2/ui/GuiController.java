package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;

public class GuiController extends Controller implements EventListener {

	@Override
	public void progress(MolecularSystem m) {
		
	}

	@Override
	public void onRestart() {
		status = "Restart";
	}

}
