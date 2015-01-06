package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.IOException;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

public class PredictionManager {

	PredictorWrapper predictor = null;

	public void process() throws IOException{
			predictor.process();
	}

	public void setPredictor(PredictorEnum predictor, InputMethodEnum inputMethod) {
		if (inputMethod==null) {
			throw new IllegalStateException("Input Method is not yet set");
		}
		
		switch (inputMethod) {
		case fasta:
			if (predictor.equals(PredictorEnum.IUPRED)) {
				//OK, do all necessary assignments
				this.predictor=new IUPRED_Predictor();
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
			}
			break;
		case objects:
			//TODO check
			//TODO prepare (object to PDB or to fasta) pipeline based on your need
			//TODO assign
			break;
		case pdb:
			//TODO check
			if(predictor ==PredictorEnum.STRIDE){
				this.predictor=new STRIDE_Predictor();
			}
			//TODO prepare (PDB to FASTA) pipeline (if you need)
			else if(predictor==PredictorEnum.IUPRED){
				this.predictor=new IUPRED_Predictor();
			}
			//TODO assign
			break;
		default:
			break;
		}		
		

	}
	

}