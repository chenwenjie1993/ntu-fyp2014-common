package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//stride does not take input from process

public class STRIDE_Predictor extends PredictorWrapper {
	 ArrayList<STRIDE_Output> Pregion = null;

	public void process(String pathname) throws IOException {
		
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(new File("/temp/stride"));
		

		try {
		assert pb.redirectInput() == Redirect.PIPE;
		Vector<String> commands = new Vector<String>();
		commands.add("./stride");
		commands.add(pathname);
		
		pb.command(commands);
		pb.redirectOutput(Redirect.PIPE);
		pb.redirectErrorStream(true);
		
		Process p =pb.start();
		
		InputStream input = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(input));
		String str;
		
        Pregion = new ArrayList<STRIDE_Output>();
       
		
		    while ((str = br.readLine()) != null) {
		 
		    
		    	Pattern pat = Pattern.compile("LOC");
            	Matcher m =pat.matcher(str);
            	if (m.find()){
            	ArrayList<String> region_list = new ArrayList<String>();
            	region_list.add(str);
            	
            	for(String sentence:region_list){
            		STRIDE_Output predicted = new STRIDE_Output();
            		String []splitArray = sentence.split("\\s+");
            		predicted.Rpred(splitArray[1]);
            		predicted.Rchain(splitArray[4]);
            		predicted.Rstart(splitArray[2]);
            		predicted.RstartPos(splitArray[3]);
            		predicted.Rend(splitArray[5]);
            		predicted.RendPos(splitArray[6]);
            		Pregion.add(predicted);
            		}
            	}
            	}
		}catch (IOException e){
			e.printStackTrace();
		}
	
		
	}
}