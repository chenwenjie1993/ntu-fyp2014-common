package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IUPRED_Predictor extends PredictorWrapper {
	ArrayList<IUPRED_Output> Pregion = null;
	
	public void process(InputStream is)throws IOException {
			
			ProcessBuilder pb = new ProcessBuilder();
			pb.directory(new File("/temp/iupred"));
			String line = null;
			try {

				assert pb.redirectInput() == Redirect.PIPE;
				Vector<String> commands = new Vector<String>();
				commands.add("./iupred");
				commands.add("--");
				commands.add("long");
	//			Map <String, String> env = pb.environment();
//				env.put("IUPred_PATH", strLine);
				
			/*	Set<String> keySet = environment.keySet();
				for(String key: keySet){
					String value = environment.get(key);
					System.out.println(key+"\t\t:\t"+value);
				}*/
				pb.command(commands);
				pb.redirectOutput(Redirect.PIPE);
				pb.redirectErrorStream(true);
				
				Process p =pb.start();
				try{
					OutputStream out = p.getOutputStream();
					
					int d;
					while((d = is.read()) != -1){
						out.write(d);
					}
					is.close();
					out.close();
				}
				catch(FileNotFoundException e){
					
				}
				InputStream is1 = p.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is1));
				Pregion =new ArrayList <IUPRED_Output>();
					while ((line = br.readLine()) != null){
						Pattern pat = Pattern.compile("[ ]{2,}");
		            	Matcher m =pat.matcher(line);
		            	if(m.find()){
		            		ArrayList<String> region_list = new ArrayList<String>();
		            		region_list.add(line);
		            		for(String s:region_list){
		            		IUPRED_Output predicted = new IUPRED_Output();
		            		String []splitArray = s.split("\\s+");     		
		            		predicted.outputposition(splitArray[1]);
		            		predicted.aminoacid(splitArray[2]);
		            		predicted.outputscore(splitArray[3]);
		            		Pregion.add(predicted);	
		            		}
		            		
		            	} 
		            
					}	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}
			
			

			
		}
		
	
	}
