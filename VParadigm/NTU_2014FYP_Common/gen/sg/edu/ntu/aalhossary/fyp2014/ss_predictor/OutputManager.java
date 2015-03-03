package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.OutputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

public class OutputManager {

	OutputMethodEnum outputMethod;
	public InputManager inputManager = new InputManager();
	public STRIDE_Predictor sp = new STRIDE_Predictor();
	
	public OutputMethodEnum getOutputMethod() {
		return outputMethod;
	}
	
	public void SetOutputMethod(OutputMethodEnum output, PredictorEnum predictor){//TODO change into enum
		if (predictor==null) {
			throw new IllegalStateException("Predictor is not yet set");
		}	
		else if(predictor==PredictorEnum.IUPRED){
			if (output.equals(OutputMethodEnum.FILE)) {
				//ok, do all necessary assignments
				this.outputMethod = output;
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on predictor");
			}
		if(predictor.equals(PredictorEnum.STRIDE)){
			if (output.equals(OutputMethodEnum.FILE)) {
				this.outputMethod = output;
			}else if(output.equals(OutputMethodEnum.OBJECTS)) {
				//ok, do all necessary assignments
				this.outputMethod = output;
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on predictor");
				}
		
			}
		}
	}
	public void OutputFile(){
		
		
	}
	public void OutputObjects(InputMethodEnum input){
		if(input==InputMethodEnum.pdb_file_iupred ||input==InputMethodEnum.pdb_file_stride ){
		//	IOutility.createObjectsFromPDB(sp.Pregion);
		}else if(input==InputMethodEnum.objects){
			IOutility.createObjectsFromModel(sp.Pregion.get(0), inputManager.input);
		}
	}
}