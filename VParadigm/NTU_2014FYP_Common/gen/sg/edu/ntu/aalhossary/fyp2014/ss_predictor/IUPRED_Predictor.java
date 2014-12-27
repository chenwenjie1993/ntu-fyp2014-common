package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Map;
import java.util.Vector;

public class IUPRED_Predictor extends Sequence_based_Prediction {

	public static void Predict(String[]args)throws InterruptedException {
		
			 
			
			
			ProcessBuilder pb = new ProcessBuilder();
			
			pb.directory(new File("/temp/iupred"));
			
			
			String line = null;
			try {

				assert pb.redirectInput() == Redirect.PIPE;
				Vector<String> commands = new Vector<String>();
				commands.add("./iupred");
				commands.add("--");
				commands.add("long");
				Map <String, String> env = pb.environment();
//				env.put("IUPred_PATH", strLine);
				
			/*	Set<String> keySet = environment.keySet();
				for(String key: keySet){
					String value = environment.get(key);
					System.out.println(key+"\t\t:\t"+value);
				}*/
			//	commands.add("./iupred");
			//	commands.add("--");
			//	commands.add("long");
				pb.command(commands);
				pb.redirectOutput(Redirect.PIPE);
				pb.redirectErrorStream(true);
				
				Process p =pb.start();
				try{
					FileInputStream fstream = new FileInputStream("/temp/iupred/P53_HUMAN.seq");
					OutputStream out = p.getOutputStream();
					
					int d;
					while((d = fstream.read()) != -1){
						out.write(d);
					}
					fstream.close();
					out.close();
				}
				catch(FileNotFoundException e){
					
				}
				
				
			
			
				
			
		
				
				InputStream is = p.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null)
						   System.out.println(line);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			
			}
			
			

			
		}
		
	}
