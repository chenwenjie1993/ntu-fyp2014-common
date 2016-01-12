package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core;

import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.util.*;

public class Application {

	public static void main(String[] args) {
		
	}

	public void read() {
		String fileName = "res/amber03/ffbonded.itp";
		List<String> fileAsList = FileReader.readFile(fileName);
		int bondtypes = fileAsList.indexOf("[ bondtypes ]");
	}
}
