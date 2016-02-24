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
		status = "Running";
		
		// main loop
		while (true) {
			if (status.equals("Pending")) {
				// on hold when application is not running
				System.out.println("Pending...");
				try {
					Thread.sleep(1000);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			currentFrame++;
			System.out.println("Frame " + currentFrame);
			if (currentFrame < totalFrame) {
				m.nextFrame();
				// update Jmol viewer
				try {
					v.getMediator().updateViewerCoord(m.particles);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				// pause 1s for each frame
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Simulation ends.");
				status = "Pending";
			}

		}
	}

	@Override
	public void onRestart() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	System.out.println("Simulation restarts.");
        		status = "Pending";
        		currentFrame = 0;
        		v.reload();
        		buildTopology();
        		status = "Running";
            }
        }, "Restart");
		t.start();
	}
	
	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void onConfigurationChange() {
		
	}

	@Override
	public void onStop() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	System.out.println("Simulaton stops.");
        		currentFrame = 0;
        		status = "Pending";
            }
        }, "Stop");
		t.start();
	}

	@Override
	public void onPause() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	System.out.println("Simulaton pauses.");
        		status = "Pending";
            }
        }, "Pause");
		t.start();
	}

	@Override
	public void onResume() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	if (status.equals("Running")) {
        			return;
        		}
        		System.out.println("Simulaton resumes.");
        		status = "Running";
            }
        }, "Resume");
		t.start();
	}

}
