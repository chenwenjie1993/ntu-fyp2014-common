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
		next();
	}
	
	public void next() {
		if (currentFrame < totalFrame) {
			sim.nextFrame(m);
			try {
				v.getMediator().updateViewerCoord(m.particles);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			currentFrame++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			next();
		}
		else {
			System.out.println("Simulation ends.");
			return;
		}
	}

	@Override
	public void onRestart() {
		System.out.println("Simulation restarting...");
		status = "Running";
		currentFrame = 0;
		start();
	}
	
	@Override
	public String getStatus() {
		return status;
	}

}
