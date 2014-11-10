package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextField;

import org.biojava.bio.structure.Structure;
import org.jmol.api.JmolViewer;

public class MoleculeEditor {

	private static Structure structure = null;
	private static JFrame frame;
	public static ToolPanel toolPanel;
	private static JmolDisplay jmolPanel;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		initDisplay();
		setConnection();
	}

	private static void initDisplay() {
		frame = new JFrame();
		JMenuBar menu = MenuCreator.initMenu(frame);
        frame.setJMenuBar(menu);
        frame.addWindowListener(new WindowAdapter() {
        	  public void windowClosing(WindowEvent we) {
        		    System.exit(0);
        		  }
        		});
        Container contentPane = frame.getContentPane();
        jmolPanel = new JmolDisplay();
        //setConnection();
        jmolPanel.setPreferredSize(new Dimension(500,500));
        
        Box vBox = Box.createVerticalBox();
        vBox.add(jmolPanel);
        
        JTextField field = new JTextField();
		field.setMaximumSize(new Dimension(Short.MAX_VALUE,30));
		field.setText("enter RASMOL like command...");
		RasmolFieldListener rasmolcmd = new RasmolFieldListener(jmolPanel, field);
		field.addActionListener(rasmolcmd);
		vBox.add(field);
		
		Box hBox = Box.createHorizontalBox();
		hBox.add(vBox);
		
		toolPanel = new ToolPanel(jmolPanel);
		toolPanel.setMaximumSize(new Dimension(250,Short.MAX_VALUE));
		hBox.add(toolPanel);
 
        contentPane.add(hBox);
        
        
        
        frame.pack();
        frame.setVisible(true);
        
	}

		
	public JmolViewer getViewer() {
		return jmolPanel.getViewer();
	}


	static class ApplicationCloser extends WindowAdapter {

		/**
		 * 
		 * @param e
		 */
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}

		ApplicationCloser() {
			// TODO - implement ApplicationCloser.ApplicationCloser
			throw new UnsupportedOperationException();
		}

	}
	
	private static void setConnection(){
		jmolPanel.setConnection();	
	}
}