package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputManager {
	public PredictorController _unnamed_PredictorController_;

	public String InputSequence(String pathname) throws FileNotFoundException {
		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");
		try{
			
			while(scanner.hasNextLine()){
				fileContents.append(scanner.nextLine() + lineSeparator);
				
			}
			return fileContents.toString();
		}finally{
			scanner.close();
		}
	}

	public void InputObjects() {
		throw new UnsupportedOperationException();
	}
}