package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.biojava.bio.structure.Structure;
import org.junit.Test;

import sg.edu.ntu.aalhossary.fyp2014.common.AminoAcid;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Residue;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.DataManager;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;

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
		String pathname = "/Users/benkong/documents/fyp/stride/1PGB.pdb";
		String pdbstring = IOutility.pdbFileTofastaString(pathname);
		String result = ">"+"\n" + "MTYKLILNGKTLKGETTTEAVDAATAEKVFKQYANDNGVDGEWTYDDATKTFTVTE";
		assertEquals(result,pdbstring);
	}

	@Test
	public void testPdbStringTofastaString() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/stride/1PGB.pdb";
		File file = new File(pathname);
		StringBuilder sb = new StringBuilder((int)file.length());
		Scanner input = new Scanner(file);
		String lineSeparator = System.getProperty("line.separator");
		while (input.hasNextLine()){
			sb.append(input.nextLine() + lineSeparator);
		}
		String result = sb.toString();
		//System.out.println(result);
		String test = ">"+"\n" + "MTYKLILNGKTLKGETTTEAVDAATAEKVFKQYANDNGVDGEWTYDDATKTFTVTE";
		String fastastring = IOutility.pdbStringTofastaString(result);
		assertEquals(test,fastastring);
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
		File file = new File("/Users/benkong/documents/fyp/stride/1PGB.pdb");
		Structure struc = DataManager.readFile(file.getAbsolutePath());
		UpdateRegistry updateReg = new UpdateRegistry();
		updateReg.createUserModel(struc);
		ArrayList<Model> models = (ArrayList<Model>)updateReg.getModelList();
		String result = IOutility.ObjectstoFasta(models.get(0).getMolecules().get(0).getChains());
		String test = ">"+"\n" + "MTYKLILNGKTLKGETTTEAVDAATAEKVFKQYANDNGVDGEWTYDDATKTFTVTE";
		assertEquals(test,result);
	}

	@Test
	public void testObjectstoPdbString() {
		File file = new File("/Users/benkong/documents/fyp/stride/1PGB.pdb");
		Structure struc = DataManager.readFile(file.getAbsolutePath());
		UpdateRegistry updateReg = new UpdateRegistry();
		updateReg.createUserModel(struc);
		ArrayList<Model> models = (ArrayList<Model>)updateReg.getModelList();
		String pdbstring = IOutility.ObjectstoPdbString(models);
		assertNotNull(pdbstring);
		
	}
	@Test
	public void testCreateAminoAcids() throws IOException{
//		System.out.println(System.clearProperty("working.dir"));
		String projectFolder=System.clearProperty("user.dir");
//		System.out.println(System.clearProperty("current.dir"));
		String pathname = projectFolder+"/res/4HHB.pdb";
		STRIDE_Predictor sp = new STRIDE_Predictor();
		sp.process(pathname);
		ArrayList<AminoAcid>test = IOutility.createAminoAcid(pathname,sp.Pregion.get(0));
		assertEquals("PRO",test.get(0).getName());
	//	System.out.println(test.get(0).getResidueSeqNum());
	//	System.out.println(test.get(0).getName());
	//	float[] coo = test.get(0).getAtomList().get(0).getCoordinates();
	//	System.out.println(Arrays.toString(coo));
		
	}
	@Test
	public void testCreateResiduesFromModel() throws IOException {
		String pathname = "/Users/benkong/documents/fyp/stride/4HHB.pdb";
		STRIDE_Predictor sp = new STRIDE_Predictor();
		sp.process(pathname);
		File file = new File("/Users/benkong/documents/fyp/stride/4HHB.pdb");
		Structure struc = DataManager.readFile(file.getAbsolutePath());
		UpdateRegistry updateReg = new UpdateRegistry();
		updateReg.createUserModel(struc);
		ArrayList<Model> models = (ArrayList<Model>)updateReg.getModelList();
		
		ArrayList<Residue> test =  IOutility.createObjectsFromModel(sp.Pregion.get(0), models.get(0));
		//assertEquals("THR",test.get(0).getName());
		System.out.println(test.size());
		for(Residue r: test){
			System.out.println(r.getName());
		}
		
	}
	

}
