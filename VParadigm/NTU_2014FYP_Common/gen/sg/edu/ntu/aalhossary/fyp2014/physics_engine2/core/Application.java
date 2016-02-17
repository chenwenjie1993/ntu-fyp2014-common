package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.topology.TypologyBuilder;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui.*;

public class Application {
	private static boolean enableUI = true;
	private static int totalFrame = 400;
	private static final Logger log = Logger.getLogger("main");

	public static void main(String[] args) {
	    FileHandler fh;  

	    try {
	        fh = new FileHandler("Log.txt");  
	        log.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
	        log.setUseParentHandlers(false);
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();
	    }  
		
		
		log.info("Application starts");
		String dir = "res/test/amber03_two_residue/";
   		TypologyBuilder tb = new TypologyBuilder();
   		MolecularSystem m = tb.build(dir);
   		
   		Controller controller = ControllerFactory.createController(enableUI);
   		
   		int frame = 0;
   		while (frame < totalFrame) {
   			log.info("Frame " + frame);
   			
   			m.updateEnergyPotential();
   			m.integrate();
   			controller.progress(m);
   			frame++;
   		}

   	}

}
