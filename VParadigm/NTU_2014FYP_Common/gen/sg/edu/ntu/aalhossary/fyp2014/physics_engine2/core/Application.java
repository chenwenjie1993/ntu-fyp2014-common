package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TypologyBuilder;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui.*;

public class Application {
	private static boolean enableUI = false;
	private static int totalFrame = 20;

	public static void main(String[] args) {
		String dir = "res/test/amber03/";
   		TypologyBuilder tb = new TypologyBuilder();
   		MolecularSystem m = tb.build(dir);
   		
   		Controller controller = ControllerFactory.createController(enableUI);
   		
   		int frame = 0;
   		while (frame < totalFrame) {
   			if (controller.status == "Restart") {
   				frame = 0;
   				controller.status = "Running";
   				break;
   			}
   			m.updateEnergyPotential();
   			m.updatePosition();
   			controller.progress(m);
   			frame++;
   		}

   	}

}
