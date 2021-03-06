package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui.*;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util.LogFormatter;

public class Application {
	private static boolean enableUI = true;
	private static int totalFrame = 500000;
	private static String dir = "res/amber03_test/";
	private static Map<String, Object> config = new HashMap<>();
	private static final Logger log = Logger.getLogger("main");
	
	private static void init() {
	    FileHandler fh;

		try {
	        fh = new FileHandler("Log.gro");
	        fh.setFormatter(new LogFormatter());
	        log.addHandler(fh);
//	        SimpleFormatter formatter = new SimpleFormatter();  
//	        fh.setFormatter(formatter);
	        log.setUseParentHandlers(false);
	        
	    } catch (SecurityException e) {
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();
	    }
		
		config.put("frame", totalFrame);
		config.put("dir", dir);
		config.put("name", "4C");
		config.put("timeDelta", 0.0002);
		config.put("forceField", "Amber03");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Bond", true);
		params.put("Angle", true);
		params.put("ProperDihedral", false);
		params.put("ImproperDihedral", false);
		params.put("Electrostatic", false);
		params.put("LennardJones", false);
		
		config.put("ffParams", params);
		
//		log.info("Application starts");
		
	}

	public static void main(String[] args) {
		init();
	    
   		Controller controller = ControllerFactory.createController(enableUI, config);
		controller.start();
   	}

}
