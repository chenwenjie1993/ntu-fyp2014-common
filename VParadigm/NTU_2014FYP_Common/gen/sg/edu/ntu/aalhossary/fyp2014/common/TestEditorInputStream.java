package sg.edu.ntu.aalhossary.fyp2014.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestEditorInputStream {

	/**
	 * @param args
	 */
	public static void main(String [] args){
		String line;
		try {
			Socket socket = new Socket("localhost",8765);
			
			String sline = "";
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        //PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
	        
	        while ((line = fromServer.readLine()) != null) {
	            System.out.println(line + "\n");
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
