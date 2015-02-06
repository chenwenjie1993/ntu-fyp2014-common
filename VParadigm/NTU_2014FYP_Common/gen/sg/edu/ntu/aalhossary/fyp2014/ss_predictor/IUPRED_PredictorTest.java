package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class IUPRED_PredictorTest {

	@Test
	public void testIUPRED_Predictor() throws FileNotFoundException {
		
		InputStream is = new FileInputStream("/Users/benkong/documents/fyp/iupred/P53_HUMAN.seq");
		IUPRED_Output test = new IUPRED_Output();
		test.position = "342";
		IUPRED_Predictor predictor = new IUPRED_Predictor();
		try {
			predictor.process(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(test.position, predictor.Pregion.get(183).position);
	}

}
