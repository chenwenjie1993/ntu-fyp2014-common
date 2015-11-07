package sg.edu.ntu.aalhossary.fyp2014.physics_engine.controller;

public class ControllerFactory {

	public static Controller create(boolean enableGUI) {
		return enableGUI? new GuiController() : new CommandLineController();
	}
	
}
