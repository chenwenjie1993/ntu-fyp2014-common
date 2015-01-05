package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class PredictorController {
	
	public InputManager inputManager = new InputManager();
	public OutputManager outputManager = new OutputManager();
	PredictionManager predictionManager = null;
	
	
	public enum InputMethodEnum {
		pdb, fasta, objects
	}
	InputMethodEnum input;
	
	public enum PredictorEnum{
		STRIDE,IUPRED;
	}
	//
	
	public enum OutputMethodEnum{
		FILE, OBJECTS
	}
	
	
	
	public void setInputMethod(InputMethodEnum input){
		inputManager.setInputMethod(input);
	}
	public void setPredictor(PredictorEnum predictor){
		predictionManager.setPredictor(predictor,inputManager.inputMethod); //does this return a predictor?
	}
	
	public void setOutputMethod(OutputMethodEnum output,PredictorEnum predictor){
	//	outputManager.SetOutputMethod(output, this.setPredictor(predictor));
	
	}
	//TODO make set predictor and setOutput and and other needed setters if needed
	
	public void perform(PredictionManager Predictor, String fileContents) throws IOException{
		predictionManager.process(fileContents);		
	}

	
	
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

	

}