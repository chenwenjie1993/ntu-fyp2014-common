package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.IOException;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;
import org.biojava3.structure.StructureIO;

public class PredictionManager {

	PredictorWrapper predictor = null;
	String pathname;

	public void process(String fileContents) throws IOException{
		
			predictor.process(fileContents);
	
	}
	public void setPredictor(PredictorEnum predictor, InputMethodEnum inputMethod) {
		if (inputMethod==null) {
			throw new IllegalStateException("Input Method is not yet set");
		}
		
		switch (inputMethod) {
		case fasta:
			if (predictor.equals(PredictorEnum.IUPRED)) {
				//ok, do all necessary assignments
				this.predictor=new IUPRED_Predictor();
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on inputs");
			}
			break;
		case objects:
			//TODO check
			//TODO prepare (object to PDB or to fasta) pipeline based on your need
			//TODO assign
			break;
		case pdb:
			//TODO check
			if(predictor.equals(PredictorEnum.STRIDE)){
				this.predictor=new STRIDE_Predictor();
			}
			//TODO prepare (PDB to fasta) pipeline (if you need)
			if(predictor.equals(PredictorEnum.IUPRED)){
				this.PDBtoFasta(pathname);
				this.predictor=new IUPRED_Predictor();
			}
			//TODO assign
			break;
		default:
			break;
		}		
		

	}
	
	public String PDBtoFasta (String pathname){
		String fasta = null;
		try{
		Structure s = StructureIO.getStructure("pathname");
        for ( Chain c : s.getChains()) {
            fasta = c.getAtomSequence();
        }
		}catch(Exception e){
			e.printStackTrace();
		}
		return fasta;
	}  
	
	public String ObjectstoFasta(){
		return null;
		
		
	}
}