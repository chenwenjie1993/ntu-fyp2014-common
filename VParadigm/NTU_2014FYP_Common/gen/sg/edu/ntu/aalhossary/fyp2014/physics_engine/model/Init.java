package sg.edu.ntu.aalhossary.fyp2014.physics_engine.model;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine.model.Units.DISTANCE;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.model.Units.MASS;


/**
 * @author waiyan
 * Initialization Class for the Physics Engine
 * Periodic Table used for construction of the Atom
 */
public class Init {
	public static double machineEpsilon;
	public static PrintStream originalStream;
	public static PrintStream dummyStream;
	public static String [] periodicTable =  {
		"H" , "HE", "LI", "BE", "B" , "C" , "N" , "O" , "F" , "NE",
		"NA", "MG", "AL", "SI", "P" , "S" , "CL", "AR", "K" , "CA",
		"SC", "TI", "V",  "CR", "MN", "FE", "CO", "NI", "CU", "ZN",
		"GA", "GE", "AS", "SE", "BR", "KR", "RB", "SR", "Y" , "ZR",	
		"NB", "MO", "TC", "RU", "RH", "PD", "AG", "CD", "IN", "SN",	
		"SB", "TE", "I" , "XE", "CS", "BA", "LA", "CE", "PR", "ND",	
		"PM", "SM", "EU", "GD",	"TB", "DY", "HO", "ER",	"TM", "YB",	
		"LU", "HF", "TA", "W" ,	"RE", "OS", "IR", "PT",	"AU", "HG",
		"TL", "PB", "BI", "PO", "AT", "RN", "FR", "RA",	"AC", "TH",	
		"PA", "U" , "NP", "PU",	"AM", "CM", "BK", "CF", "ES", "FM",	
		"MD", "NO", "LR", "RF", "DB", "SG", "BH", "HS", "MT", "DS",	
		"RG", "CN", "UUT", "UUQ", "UUP", "UUH", "UUS", "UUO"
	};
	public static AtomData [] atomicData = new AtomData [periodicTable.length];
	
	private static class AtomData {
		double [] data;
		
		public AtomData(double[] data){
			this.data = data.clone();
		}
	}
	
	
	public static void init(){
		machineEpsilon = calculateMachineEpsilon();
		saveAtomicData(0);	// get H
		saveAtomicData(5);	// get C
		saveAtomicData(6);	// get N
		saveAtomicData(7);	// get O
		
	}
	
	public static double [] getAtomicData (String symbol) throws Exception {

		int index = Arrays.asList(periodicTable).indexOf(symbol);
		
		if(index == -1){
			throw new Exception("Invalid atomic symbol");
		}
		
		if(atomicData[index] == null)
			saveAtomicData(index);
		
		if(atomicData[index] == null)
			return atomicData[0].data;
		
		return atomicData[index].data;
	}

	private static double calculateMachineEpsilon(){
		double machEps = 1.0;
		do{
           machEps /= 2.0;	// operator / is used instead of bit shifting since we do not know the CPU architecture
		} while ((double) (1.0 + (machEps / 2.0)) != 1.0);
        return (machEps);
	}
	
	private static void saveAtomicData(int index){
		
		NumberFormat formatter = new DecimalFormat("000");
		
		String html = "http://periodictable.com/Elements/" + formatter.format(index+1) + "/data.html";
		boolean flag = true;
		
		double mass = 0, valence = 0, atomicRadius = 0, covalentRadius = 0, vdwRadius = 0;
		String symbol = periodicTable[index];
		double [] data = new double [5];
		
		try{
			Document doc = Jsoup.connect(html).timeout(0).get();	// set timeout to zero IMPORTANT!!!
			
			// The website does not provide a specific header div/class id for mining. 
			// Absolute positioning is used. 
			
			Element table1 = doc.select("table").get(7);
			Elements rows1 = table1.select("tr"); 
			
			for (int i=0; i < rows1.size(); i++) {
		        Element row = rows1.get(i);
		        Elements columns = row.select("td");
		        
		        if(columns.first().text().equals("Atomic Weight"))
		        	mass = Double.parseDouble(columns.last().text()) * MASS.amu.value();		//mass in amu, 1 amu = 1.66053892e-24 grams
		        
		        if(columns.first().text().equals("Valence"))
		        	valence = Integer.parseInt(columns.last().text());	
		        
		       // if(columns.size()>1)	output += columns.get(0).text() + ": " + columns.get(1).text() + "\n";
		    }
			
		
			int tableIndex = 9;
			Elements rows2;
	
			do {
			Element table2 = doc.select("table").get(tableIndex);
				rows2 = table2.select("tr");
				flag = !rows2.get(0).select("td").first().text().equals("Classifications");
				tableIndex++;
			} while (flag);
			
			
			for (int j=0; j<rows2.size(); j++){
				Element row = rows2.get(j);
			    Elements columns = row.select("td");
			     
			     // All radii are in pm.
			     
			     if(columns.first().text().equals("Atomic Radius"))
			    	 atomicRadius = Double.parseDouble(columns.last().text().split(" ")[0]) * DISTANCE.pm.value();
			     
			     if(columns.first().text().equals("Covalent Radius"))
			    	 covalentRadius = Double.parseDouble(columns.last().text().split(" ")[0]) * DISTANCE.pm.value();
			     
			     if(columns.first().text().equals("Van der Waals Radius")) {
			    	 vdwRadius = Double.parseDouble(columns.last().text().split(" ")[0]) * DISTANCE.pm.value();	
			    	 break;
			     }
		    }
			
			boolean print_flag = false;
			if(print_flag) {
				System.out.println("Atom " + symbol);
				System.out.println("Mass is " + mass + " kg.");
				System.out.println("Valence is " + valence);
				System.out.println("Atomic radius is " + atomicRadius + " m.");
				System.out.println("Covalent radius is " + covalentRadius + " m.");
				System.out.println("VdW radius is " + vdwRadius + " m.\n");
			}
		}
		catch (IOException e){
			
			System.out.println("INTERNET CONNECTION ERROR WHILE CREATING ATOM  " + periodicTable[index]);
			System.out.println("Using Hydrogen as DEFAULT");
			symbol = "H";
			index = 0;
			mass = 1.00794 * MASS.amu.value();
			valence = 1;
			atomicRadius = 53e-12;
			covalentRadius = 37e-12;
			vdwRadius = 120e-12;
			
		}
		
		data[0] = mass;
		data[1] = valence;
		data[2] = atomicRadius;
		data[3] = covalentRadius;
		data[4] = vdwRadius;
		
		if(data[2] == 0 || data[3]==0 || data[4]==0){
			System.out.println("Unable to fetch the atom data for atom " + symbol);
			System.exit(-1);
		}
		
		atomicData [index] = new AtomData (data) ;
	}
}
