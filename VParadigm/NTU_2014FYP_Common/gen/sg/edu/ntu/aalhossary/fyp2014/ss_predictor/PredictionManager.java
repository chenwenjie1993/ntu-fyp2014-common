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
	
	public void setPredictor(PredictorEnum predictor, InputMethodEnum inputMethod,Object input) {
		if (inputMethod==null) {
			throw new IllegalStateException("Input Method is not yet set");
		}
		switch (inputMethod) {
		case fasta_file:
			if (predictor.equals(PredictorEnum.IUPRED)) {
					if(input instanceof File){
						String filepath = ((File)input).getAbsolutePath();
						inputstream = IOutility.fastafileToInputStream(filepath);
						this.predictor=new IUPRED_Predictor();
				}
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
			}
			break;
		case fasta_string:
			if (predictor.equals(PredictorEnum.IUPRED)) {
					if(input instanceof String){	
						inputstream = IOutility.stringToInputStream((String)input);
						this.predictor=new IUPRED_Predictor();
				}
			} else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
			}
			
			break;
		case objects:
			//TODO prepare (object to PDB or to FASTA) pipeline based on your need
			if(predictor==PredictorEnum.STRIDE){
					if(input instanceof ArrayList<?>){
						if(((ArrayList<?>)input).get(0)instanceof Model){
							IOutility.ObjectstoPdbString((ArrayList<Model>)input);
							this.predictor=new STRIDE_Predictor();
						}		
				}else{
					throw new IllegalArgumentException("invalid input");
				}
			}
			//TODO prepare (PDB to FASTA) pipeline (if you need)
			else if(predictor==PredictorEnum.IUPRED){
					if(input instanceof ArrayList<?>){
						if(((ArrayList<?>)input).get(0)instanceof Chain){
							String fasta = IOutility.ObjectstoFasta((ArrayList<Chain>)input);
							inputstream = IOutility.stringToInputStream(fasta);
							this.predictor=new IUPRED_Predictor();	
							}
					}
				} else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
				}
			
			break;
		case pdb_file_stride://do not have to read file to input stream
			if(predictor==PredictorEnum.STRIDE){
					if(input instanceof File){
						pathname = ((File)input).getAbsolutePath();
						this.predictor=new STRIDE_Predictor();
					}
			}else{
				throw new IllegalArgumentException("Can't instantiate instance based on input");
				}
			break;
		case pdb_file_iupred:	
			//TODO prepare (PDB to FASTA) pipeline (if you need)
			if(predictor==PredictorEnum.IUPRED){
					if(input instanceof File){
						String filepath = ((File)input).getAbsolutePath();
						String fasta = IOutility.pdbFileTofastaString(filepath);
						System.out.println(fasta);
						inputstream = IOutility.stringToInputStream(fasta);
						this.predictor=new IUPRED_Predictor();
					}
			}else {
				throw new IllegalArgumentException("Can't instantiate instance based on input");
				}
			break;
		case pdb_string:
			//TODO prepare (PDB to FASTA) pipeline (if you need)
			 if(predictor==PredictorEnum.IUPRED){
					 if(input instanceof String){	
						 String fastaString = IOutility.pdbStringTofastaString((String)input);
						 inputstream = IOutility.stringToInputStream(fastaString);
						 this.predictor=new IUPRED_Predictor();
					}
			 }	else {
					throw new IllegalArgumentException("Can't instantiate instance based on input");
					}	 
			break;
			
		default:
			break;
		}		
		

	}
	

}