package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class IOutilityTest {

	@Test
	public void testFastafileToInputStream() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/iupred/TEST.txt";
		String input = "ABCD";
		StringBuilder sb = new StringBuilder();
		InputStream is = IOutility.fastafileToInputStream(pathname);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String read = br.readLine();
		sb.append(read);
		String result = sb.toString();
		assertEquals(input,result);
	}

	@Test
	public void testPdbFileTofastaString() {
		String pathname = "/Users/benkong/documents/fyp/stride/4HHB.pdb";
		String pdbstring = IOutility.pdbFileTofastaString(pathname);
		//System.out.println(pdbstring);
	}

	@Test
	public void testPdbStringTofastaString() {
		fail("Not yet implemented");
	}

	@Test
	public void testStringToInputStream() throws IOException {
		String string = "abcd";
		InputStream is = IOutility.stringToInputStream(string);
		InputStreamReader isr = new InputStreamReader(is);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(isr);
		String read = br.readLine();
		sb.append(read);
		String result = sb.toString();
		assertEquals(string,result);
		
	}



	@Test
	public void testObjectstoFasta() {
		fail("Not yet implemented");
	}

	@Test
	public void testObjectstoPdbFile() {
		fail("Not yet implemented");
	}

}
