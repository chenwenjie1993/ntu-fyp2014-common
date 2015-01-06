package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.IOException;


public class PredictorController {
	
	public InputManager inputManager = new InputManager();
	public OutputManager outputManager = new OutputManager();
	public PredictionManager predictionManager = new PredictionManager();

	
	public enum InputMethodEnum {
		pdb, fasta, objects
	}
	InputMethodEnum input;
	
	public enum PredictorEnum{
		STRIDE,IUPRED;
	}
	
	
	public enum OutputMethodEnum{
		FILE, OBJECTS
	}
	
	public void setInputMethod(InputMethodEnum input){
		inputManager.setInputMethod(input);
	}
	public void setPredictor(PredictorEnum predictor){
		predictionManager.setPredictor(predictor,inputManager.inputMethod); 
	}
	
	public void setOutputMethod(OutputMethodEnum output, PredictorEnum predictor){
		outputManager.SetOutputMethod(output,predictor);
	
	}
	//TODO make set predictor and setOutput and and other needed setters if needed
	
	public void perform(/*PredictionManager Predictor, String fileContents*/) throws IOException{
		predictionManager.process(/*fileContents*/);		
	}

	

}