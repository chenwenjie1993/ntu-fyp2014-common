package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;

public class InputManager {
	

	InputMethodEnum inputMethod;
	ArrayList <Object> inputs;
	
	public InputMethodEnum getInputMethod() {
		return inputMethod;
	}

	public void setInputMethod(InputMethodEnum inputMethod) {
		//No need to check here till now. will check in next methods
		this.inputMethod = inputMethod;
	}
	
	public void getInputArraylist(ArrayList<Object> inputs){
		this.inputs = inputs;
		
	}
	/**
	 * @deprecated we may need it later
	 * @param pathname
	 * @return
	 * @throws FileNotFoundException
	 */
	public String readFile(String pathname) throws FileNotFoundException {
		File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());
		Scanner scanner = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");
		try{
			while(scanner.hasNextLine()){
				fileContents.append(scanner.nextLine()).append(lineSeparator);
			}
			return fileContents.toString();
		}finally{
			scanner.close();
		}
	}
	
}