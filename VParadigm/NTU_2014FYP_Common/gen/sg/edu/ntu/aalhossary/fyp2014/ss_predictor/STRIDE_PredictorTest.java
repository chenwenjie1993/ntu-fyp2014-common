package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class STRIDE_PredictorTest {

	@Test
	public void test() throws IOException {
		STRIDE_Output test = new STRIDE_Output();
		test.start = "ALA142";
		String pathname = "/Users/benkong/Documents/FYP/stride/4HHB.pdb";
		STRIDE_Predictor predictor = new STRIDE_Predictor();
		predictor.process(pathname);
		assertEquals(test.start, predictor.Pregion.get(0).start);
	}

}
