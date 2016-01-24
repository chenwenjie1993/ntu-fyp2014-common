package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

public class ControllerFactory {
	public static Controller createController(boolean enableUI) {
		if (enableUI) {
			return new GuiController();
		}
		else {
			return new CliController();
		}
	}
}
