package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

public class PredictionManager {
	
	PredictorWrapper predictor = null;

	public void process(){
		predictor.process();
	}
	public void setPredictor(PredictorEnum predictor, InputMethodEnum inputMethod) {
		if(inputMethod == InputMethodEnum.pdb){
			predictor = PredictorEnum.STRIDE;
		}else if(inputMethod == InputMethodEnum.pdb){
			predictor = PredictorEnum.IUPRED;
		}
	}
	
}