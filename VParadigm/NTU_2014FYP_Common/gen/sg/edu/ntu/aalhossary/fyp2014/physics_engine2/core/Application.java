package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TypologyBuilder;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui.*;

public class Application {
	private static String status;
	private static boolean enableUI = false;
	private int totalFrame = 1000;

	public static void main(String[] args) {
		String dir = "res/test/amber03/";
   		TypologyBuilder tb = new TypologyBuilder();
   		MolecularSystem m = tb.build(dir);
   		m.calculateParticleEnergyPotential();
   		
   		
   		Controller controller = ControllerFactory.createController(enableUI);
   		
   		status = "Running";
//   		for (int i=0; i<TOTAL_FRAME; i++) {
//   			controller.showProgress();
//   		}
   		for (Particle p: m.particles) {
   			System.out.println(p.getPotentialEnergy().toString());
   		}
   	}

}
