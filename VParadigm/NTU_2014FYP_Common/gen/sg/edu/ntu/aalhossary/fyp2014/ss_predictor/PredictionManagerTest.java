package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.Test;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

public class PredictionManagerTest {

	@Test
	public void testSetPredictorFastaFile() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/iupred/TEST.txt";
		PredictorEnum predictor = PredictorEnum.IUPRED;
		InputMethodEnum inputMethod = InputMethodEnum.fasta_file;
		Object input = new File (pathname);
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, inputMethod, input);

		if(pm.predictor instanceof IUPRED_Predictor){
			InputStreamReader isr = new InputStreamReader(pm.inputstream);
			BufferedReader br = new BufferedReader(isr);
			String read = br.readLine();
		//	System.out.println(read);
			System.out.println("True!");
		}else{
			System.out.println("fail!");
		}
		
	}
	@Test
	public void testSetPredictorFastaString() throws IOException {
		PredictorEnum predictor = PredictorEnum.IUPRED;
		InputMethodEnum inputMethod = InputMethodEnum.fasta_string;
		Object input = new String ("ABCD");
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, inputMethod, input);

		if(pm.predictor instanceof IUPRED_Predictor){
			InputStreamReader isr = new InputStreamReader(pm.inputstream);
			BufferedReader br = new BufferedReader(isr);
			String read = br.readLine();
			System.out.println(read);
			System.out.println("True!");
		}else{
			System.out.println("fail!");
		}
		
	}
	@Test
	public void testSetPredictorPDBfile1() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/stride/4HHB.pdb";
		PredictorEnum predictor = PredictorEnum.STRIDE;
		InputMethodEnum inputMethod = InputMethodEnum.pdb_file;
		Object input = new File (pathname);
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, inputMethod, input);
		System.out.println(pm.predictor.getClass());
		if(pm.predictor instanceof STRIDE_Predictor){
			System.out.println(pm.pathname);
			System.out.println("True!");
		}else{
		System.out.println("fail!");
		}
		
	}
	@Test
	public void testSetPredictorPDBfile2() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/stride/4HHB.pdb";
		PredictorEnum predictor = PredictorEnum.IUPRED;
		InputMethodEnum inputMethod = InputMethodEnum.pdb_file;
		Object input = new File (pathname);
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, inputMethod, input);

		if(pm.predictor instanceof IUPRED_Predictor){
			System.out.println("True!");
		}else{
			System.out.println("fail!");
		}

		
	}
	
	/*public void testSetPredictorObjectModel() throws IOException {
		PredictorEnum predictor = PredictorEnum.STRIDE;
		InputMethodEnum inputMethod = InputMethodEnum.objects;
		Object input = new ArrayList<Model>();
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, inputMethod, input);

		if(pm.predictor instanceof STRIDE_Predictor){
			System.out.println("True!");
		}else{
			System.out.println("fail!");
		}
		
	}*/
	
	

}
