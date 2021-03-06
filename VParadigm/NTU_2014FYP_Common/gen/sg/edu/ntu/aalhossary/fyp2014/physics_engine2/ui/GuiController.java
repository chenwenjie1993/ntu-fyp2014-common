package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

public class GuiController extends Controller implements EventListener {
	View v;
	
	public GuiController(Map<String, Object> config) {
		super(config);
		config.put("pdb", m.getPDB());
		v = new View(this, config);
	}

	@Override
	public void start() {
		super.start();
		status = RUNNING;
		long startTime = System.currentTimeMillis();
		// main loop
		while (currentFrame < totalFrame) {
			if (status == STOPPED || status == PAUSED) {
				break;
			}
			
			if (currentFrame % m.getUpdateRlistStep() == 0) {
				m.initRlist();
			}
			
//			if (status == PAUSED) {
//				// on hold when application is not running
//				System.out.println("Pending...");
//				try {
//					Thread.sleep(1000);
//					continue;
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
			
			currentFrame++;
			System.out.println("Frame " + currentFrame);
			
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
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) );
	}

	@Override
	public void onRestart() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	System.out.println("Simulation restarts...");
        		status = STOPPED;
        		currentFrame = 0;
        		v.reload();
        		init();
        		v.disableConfig();
        		status = RUNNING;
        		start();
            }
        }, "Restart");
		t.start();
	}
	
	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void onStop() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	System.out.println("Simulaton stops.");
        		currentFrame = 0;
        		status = STOPPED;
            }
        }, "Stop");
		t.start();
	}

	@Override
	public void onPause() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	System.out.println("Simulaton pauses.");
        		status = PAUSED;
            }
        }, "Pause");
		t.start();
	}

	@Override
	public void onResume() {
		Thread t = new Thread(new Runnable(){
            public void run(){
            	if (status == STOPPED) {
        			return;
        		}
        		System.out.println("Simulaton resumes.");
        		m.setTimeDelta((Double) config.get("timeDelta"));
        		start();
            }
        }, "Resume");
		t.start();
	}

}
