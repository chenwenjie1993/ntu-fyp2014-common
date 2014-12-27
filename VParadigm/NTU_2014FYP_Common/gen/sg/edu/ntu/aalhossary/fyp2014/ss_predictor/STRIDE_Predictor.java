package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class STRIDE_Predictor extends Structure_based_Prediction {

	public static void Predict(String []args) {
		

	        FileReader input = null;
	        BufferedReader br = null;

	        try {

	            input = new FileReader("/Users/benkong/Documents/FYP/stride/4HHBout");
	            br = new BufferedReader(input);
	            String str;
	          //  Region predicted = new Region();
	            //ArrayList<Region> Pregion = new ArrayList<Region>();
	            while ((str = br.readLine()) != null) {
	            	Pattern p = Pattern.compile("LOC");
	            	Matcher m =p.matcher(str);
	            	if (m.find()){
	            	ArrayList<String> region_list = new ArrayList<String>();
	            	region_list.add(str);
	            	for(String sentence:region_list){
	            		String []splitArray = sentence.split("\\s+");
	            	//	predicted.Rpred(splitArray[1]);
	            		//predicted.Rchain(splitArray[4]);
	            	//	predicted.Rstart(splitArray[2],splitArray[3]);
	            //		predicted.Rend(splitArray[5], splitArray[6]);
	            	//	Pregion.add(predicted);
	            	}
	            //	Pregion.get(0).printRegion();
	            	
	            	}
	   
	            }
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }

	        finally {

	            try {
	                input.close();
	                br.close();
	            }

	            catch (IOException x) {
	                x.printStackTrace();
	            }

	        }
	    }
	}
