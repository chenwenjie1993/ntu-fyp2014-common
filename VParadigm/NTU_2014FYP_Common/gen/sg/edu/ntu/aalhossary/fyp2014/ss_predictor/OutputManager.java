package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.OutputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

public class OutputManager {
	public PredictorController _unnamed_PredictorController_;

	OutputMethodEnum outputMethod;
	PredictorWrapper predictor;
	
	public OutputMethodEnum getOutputMethod() {
		return outputMethod;
	}
	
	public void SetOutputMethod(OutputMethodEnum output, PredictorWrapper predictor){
		if (predictor==null) {
			throw new IllegalStateException("Predictor is not yet set");
		}	
		if(predictor.equals(PredictorEnum.IUPRED)){
			if (output.equals(OutputMethodEnum.FILE)) {
				//ok, do all necessary assignments
				this.outputMethod = output;
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on predictor");
			}
		if(predictor.equals(PredictorEnum.STRIDE)){
			if (output.equals(OutputMethodEnum.FILE)) {
				//ok, do all necessary assignments
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
}