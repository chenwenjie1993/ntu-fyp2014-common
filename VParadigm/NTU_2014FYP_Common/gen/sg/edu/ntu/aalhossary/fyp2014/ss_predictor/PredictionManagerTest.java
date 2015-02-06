package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.InputMethodEnum;
import sg.edu.ntu.aalhossary.fyp2014.ss_predictor.PredictorController.PredictorEnum;

public class PredictionManagerTest {

	@Test
	public void testSetPredictorFastaFile() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/iupred/TEST.txt";
		Object input = new File (pathname);
		PredictorEnum predictor = PredictorEnum.IUPRED;
		InputManager im = new InputManager();
		im.setInputMethod(InputMethodEnum.fasta_file);
		//im.setInputMethod(null);
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, im.inputMethod, input);

		if(pm.predictor instanceof IUPRED_Predictor){
			InputStreamReader isr = new InputStreamReader(pm.inputstream);
			BufferedReader br = new BufferedReader(isr);
			String read = br.readLine();
			
			assertEquals("ABCD",read);
		}else{
			assertNull(pm.predictor);
		}
		
	}
	@Test
	public void testSetPredictorFastaString() throws IOException {
		PredictorEnum predictor = PredictorEnum.IUPRED;
		InputManager im = new InputManager();
		im.setInputMethod(InputMethodEnum.fasta_string);
		//im.setInputMethod(null);
		Object input = new String ("ABCD");
		
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, im.inputMethod, input);

		if(pm.predictor instanceof IUPRED_Predictor){
			InputStreamReader isr = new InputStreamReader(pm.inputstream);
			BufferedReader br = new BufferedReader(isr);
			String read = br.readLine();
			assertEquals("ABCD",read);
		}else{
			assertNull(pm.predictor);
		}
		
	}
	@Test
	public void testSetPredictorPDBfile1() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/stride/4HHB.pdb";
		PredictorEnum predictor = PredictorEnum.STRIDE;
		InputManager im = new InputManager();
		im.setInputMethod(InputMethodEnum.pdb_file_stride);
		Object input = new File (pathname);
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, im.inputMethod, input);

		if(pm.predictor instanceof STRIDE_Predictor){
			assertNotNull(pm.predictor);
		}else{
			assertNull(pm.predictor);
		}
		
	}
	@Test
	public void testSetPredictorPDBfile2() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/stride/4HHB.pdb";
		PredictorEnum predictor = PredictorEnum.IUPRED;

		InputManager im = new InputManager();
		im.setInputMethod(InputMethodEnum.pdb_file_iupred);
		Object input = new File (pathname);
		PredictionManager pm = new PredictionManager();
		pm.setPredictor(predictor, im.inputMethod, input);

		if(pm.predictor instanceof IUPRED_Predictor){
			assertNotNull(pm.predictor);
		}else{
			assertNull(pm.predictor);
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
