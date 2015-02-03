package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextField;

import org.biojava.bio.structure.Structure;
import org.jmol.api.JmolViewer;

import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Particle;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.JmolDisplay;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.MenuPanel;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.ToolPanel;

/**
 * @author Xiu Ting
 * The main function of the Molecule Editor.
 * Initialise main display and set up connection
 */
public class MoleculeEditor {

	private static JFrame frame;
	protected static Container contentPane;
	protected static MenuPanel menu;
	protected static ToolPanel toolPanel;
	protected static JmolDisplay jmolPanel;
	protected static UpdateRegistry mediator;
	protected static List<Model> modelList;
	protected static java.util.logging.Logger logger;
	protected static Handler fileHandler;

	public static void main(String[] args) {
		initDisplay();
		createLogger();
		//addResmolScriptPanel();
		mediator = new UpdateRegistry(jmolPanel.getViewer(), toolPanel, modelList);
		jmolPanel.setMediator(mediator);
		//setConnection();
	}
	
	public MoleculeEditor(){
		initJmolDisplay();
		mediator = new UpdateRegistry(jmolPanel.getViewer(), toolPanel, modelList);
		jmolPanel.setMediator(mediator);
	}

	private static void initJmolDisplay(){
		frame = new JFrame();
		//JMenuBar menu = MenuCreator.initMenu(frame);
        //frame.setJMenuBar(menu);
        frame.addWindowListener(new WindowAdapter() {
        	  public void windowClosing(WindowEvent we) {
        		    System.exit(0);
        		  }
        		});
        //contentPane = frame.getContentPane();
        // allocate jmolPanel for display
        jmolPanel = new JmolDisplay();
        jmolPanel.setPreferredSize(new Dimension(500,500));
        
        frame.add(jmolPanel);
        
        //contentPane.add(jmolPanel);
        
        frame.pack();
        frame.setVisible(true);
	}
	
	private static void createLogger(){
		logger = java.util.logging.Logger.getLogger(MoleculeEditor.class.getName());
		try {
			fileHandler = new FileHandler("./res/res/Error.log");
			logger.addHandler(fileHandler);
			fileHandler.setFormatter(new SimpleFormatter());
			PrintStream outPS = new PrintStream(new BufferedOutputStream(new FileOutputStream("./res/res/Error.log")));  // append is true
			System.setErr(outPS);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void addResmolScriptPanel(){
		 Box vBox = Box.createVerticalBox();
	     vBox.add(jmolPanel);
	        
	     JTextField field = new JTextField();
	     field.setMaximumSize(new Dimension(Short.MAX_VALUE,30));
	     field.setText("enter RASMOL like command...");
	     RasmolFieldListener rasmolcmd = new RasmolFieldListener(jmolPanel, field);
	     field.addActionListener(rasmolcmd);
	     vBox.add(field);
	     contentPane.add(field);
	}
	private static void initDisplay() {
		// create frame for the application
		frame = new JFrame();
		jmolPanel = new JmolDisplay();
		menu = new MenuPanel(jmolPanel);
		JMenuBar menuBar = menu.initMenu(frame);
        frame.setJMenuBar(menuBar);
        frame.addWindowListener(new WindowAdapter() {
        	  public void windowClosing(WindowEvent we) {
        		    System.exit(0);
        		  }
        		});
        Container contentPane = frame.getContentPane();
        // allocate jmolPanel for display
        
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
		
		// define the right panel of application
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
	
	public UpdateRegistry getMediator(){
		return mediator;
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
}