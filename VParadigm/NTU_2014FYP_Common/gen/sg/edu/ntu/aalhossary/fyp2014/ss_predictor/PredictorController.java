package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;



public class PredictorController {
	
	
	public InputManager inputManager = new InputManager();
	public OutputManager outputManager = new OutputManager();
	public PredictionManager predictionManager = new PredictionManager();
	
	

	
	public enum InputMethodEnum {
		pdb_string,
		pdb_file_stride,
		pdb_file_iupred,
		fasta_string,
		fasta_file, 
		objects
	}

	
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
		predictionManager.setPredictor(predictor,inputManager.inputMethod,inputManager.input); 
	}
	
	public void setOutputMethod(OutputMethodEnum output, PredictorEnum predictor){
		outputManager.SetOutputMethod(output,predictor);
	
	}
	//TODO make set predictor and setOutput and and other needed setters if needed
	
	
	
	

}