package sg.edu.ntu.aalhossary.fyp2014.physics_engine.application;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine.controller.*;


class Application {
	public static boolean enableGUI = false;
	public static String outputFilePath = "output.txt";
	
	public static void main(String[] args) {
		Controller controller = ControllerFactory.create(enableGUI);
		controller.start();
	}
}
