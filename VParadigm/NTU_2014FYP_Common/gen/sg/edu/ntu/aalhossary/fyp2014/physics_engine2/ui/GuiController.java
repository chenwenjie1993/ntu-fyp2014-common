package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.MolecularSystem;

public class GuiController extends Controller implements EventListener {
	View v;
	
	public GuiController() {
		super();
		v = new View(this);
	}

	@Override
	public void progress(MolecularSystem m) {
		// TODO: update UI
		try {
			v.getMediator().updateViewerCoord(m.particles);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRestart() {
		status = "Restart";
	}

}
