package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.typology.TypologyBuilder;

public class Application {

	public static void main(String[] args) {
		String fileName = "res/test/amber03/topol.top";
		TypologyBuilder.build(fileName);
	}

	
}
