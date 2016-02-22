package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.util.Map;

public class ControllerFactory {
	public static Controller createController(boolean enableUI, Map<String, Object> config) {
		if (enableUI) {
			return new GuiController(config);
		}
		else {
			return new CliController(config);
		}
	}
}
