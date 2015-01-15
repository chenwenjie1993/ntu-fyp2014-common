package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.Chain;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

public class PredictionManager {

	PredictorWrapper predictor = null;
	InputStream inputstream;
	String pathname;
	

	
	public void process() throws IOException{
		
			if (this.predictor instanceof IUPRED_Predictor){
				predictor.process(this.inputstream);
			}else if(this.predictor instanceof STRIDE_Predictor){
				predictor.process(this.pathname);
			}
		}
	
	@SuppressWarnings("unchecked")
	public void setPredictor(PredictorEnum predictor, InputMethodEnum inputMethod,ArrayList<Object> inputs) {
		if (inputMethod==null) {
			throw new IllegalStateException("Input Method is not yet set");
		}
		switch (inputMethod) {
		case fasta_file:
			if (predictor.equals(PredictorEnum.IUPRED)) {
				for(Object o:inputs){
					if(o instanceof File){
						String filepath = ((File)o).getAbsolutePath();
						inputstream = IOutility.fastafileToInputStream(filepath);
						this.predictor=new IUPRED_Predictor();
					}
				}
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
			}
		case fasta_string:
			if (predictor.equals(PredictorEnum.IUPRED)) {
				for(Object o:inputs){
					if(o instanceof String){	
						inputstream = IOutility.stringToInputStream((String)o);
						this.predictor=new IUPRED_Predictor();
					}
				}
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
			}
			
			break;
		case objects:
			//TODO prepare (object to PDB or to FASTA) pipeline based on your need
			if(predictor==PredictorEnum.STRIDE){
				for(Object o:inputs){
					if(o instanceof ArrayList<?>){
						if(((ArrayList<?>)o).get(0)instanceof Model){
							IOutility.ObjectstoPdbFile((ArrayList<Model>)o);
							this.predictor=new STRIDE_Predictor();
						}
				}
				
				}
			}
			//TODO prepare (PDB to FASTA) pipeline (if you need)
			else if(predictor==PredictorEnum.IUPRED){
				for(Object o:inputs){
					if(o instanceof ArrayList<?>){
						if(((ArrayList<?>)o).get(0)instanceof Chain){
							String fasta = IOutility.ObjectstoFasta((ArrayList<Chain>)o);
							inputstream = IOutility.stringToInputStream(fasta);
							this.predictor=new IUPRED_Predictor();	
							}
						}
					}
				} else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
				}
			
			break;
		case pdb_file://do not have to read file to input stream
			if(predictor==PredictorEnum.STRIDE){
				for(Object o :inputs){
					if(o instanceof File){
						pathname = ((File)o).getAbsolutePath();
						this.predictor=new STRIDE_Predictor();
					}
				}
			}
			//TODO prepare (PDB to FASTA) pipeline (if you need)
			else if(predictor==PredictorEnum.IUPRED){
				for(Object o: inputs){
					if(o instanceof File){
						String filepath = ((File)o).getAbsolutePath();
						String fasta = IOutility.pdbFileTofastaString(filepath);
						inputstream = IOutility.stringToInputStream(fasta);
						this.predictor=new IUPRED_Predictor();
					}
				}
			}
		case pdb_string:
			//TODO prepare (PDB to FASTA) pipeline (if you need)
			 if(predictor==PredictorEnum.IUPRED){
				 for(Object o :inputs){
					 if(o instanceof String){	
						 String fastaString = IOutility.pdbStringTofastaString((String)o);
						 inputstream = IOutility.stringToInputStream(fastaString);
						 this.predictor=new IUPRED_Predictor();
					}
				 }
			 }		 
			break;
		default:
			break;
		}		
		

	}
	

}