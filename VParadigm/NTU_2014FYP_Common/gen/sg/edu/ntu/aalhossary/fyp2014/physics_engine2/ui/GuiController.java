package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.util.Map;

public class GuiController extends Controller implements EventListener {
	View v;
	
	public GuiController(Map<String, Object> config) {
		super(config);
		v = new View(this, config);
	}

	@Override
	public void start() {
		super.start();
		
		while (currentFrame < totalFrame ) {
			sim.nextFrame(m);
			try {
				v.getMediator().updateViewerCoord(m.particles);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			currentFrame++;
			// Delay simulation
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void onRestart() {
		status = "Running";
		currentFrame = 0;
	}

}
