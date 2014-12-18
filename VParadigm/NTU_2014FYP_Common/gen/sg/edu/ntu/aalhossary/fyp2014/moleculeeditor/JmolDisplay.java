package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.align.gui.*;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolViewer;
import org.jmol.java.BS;
import org.jmol.util.Logger;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.UserActionType;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Xiu Ting
 * The main display of the application 
 * 
 */
public class JmolDisplay extends JPrintPanel implements ActionListener {

	private static final long serialVersionUID = -4721103453203185678L;
	private org.jmol.api.JmolViewer viewer;
	private org.jmol.api.JmolAdapter adapter;
	private org.jmol.api.JmolStatusListener statusListener;
	protected final java.awt.Dimension currentSize = new Dimension();
	protected java.awt.Rectangle rectClip = new Rectangle();
	protected org.biojava.bio.structure.Structure structure;
	private boolean verbose;
	private JMolSelectionListener jMolSelectionListener;
	protected static Model model;
	protected static ArrayList<Atom> selectedAtoms;
	protected UserActionType selectedTag;
	protected EvaluateUserAction evaluateUserAction;
	
	
	public JmolDisplay() {
		super();
		statusListener = new MyJmolStatusListener(this);
		adapter = new SmarterJmolAdapter();
		evaluateUserAction = new EvaluateUserAction(this);
		Logger.setLogLevel( verbose?Logger.LEVEL_INFO:Logger.LEVEL_ERROR);
		viewer = JmolViewer.allocateViewer(this, adapter);
		viewer.setJmolCallbackListener(statusListener);
		jMolSelectionListener = new JMolSelectionListener();
		viewer.addSelectionListener(jMolSelectionListener);
		viewer.evalString("load menu \"res/res/jmol.mnu\"");	// load own menu when initialising display
	}
	
	public void paint(java.awt.Graphics g) {
		getSize(currentSize);
		g.getClipBounds(rectClip);
		viewer.renderScreenImage(g, currentSize.width, currentSize.height);
	}
	
	// use to refresh display
	public void refreshDisplay(){
		viewer.refresh(3, "");
	}
	
	public void evaluateString(java.lang.String rasmolScript) {
		viewer.evalString(rasmolScript);
	}
	
	public void openStringInline(java.lang.String pdbFile) {
		viewer.openStringInline(pdbFile);
	}
	
	public JmolViewer getViewer() {
		return this.viewer;
	}
	
	public void executeCmd(java.lang.String rasmolScript) {
		viewer.evalString(rasmolScript);
		if(rasmolScript.contains("zap")){
			model = null;
			structure = null;
		}
	}
	
	public Structure getStructure(){
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
		String pdb = structure.toPDB();
		viewer.openStringInline(pdb);		
		evaluateString("save STATE state_1");
	}

	public void clearDisplay() {
		executeCmd("zap;");
		structure = null;
		
		viewer = null;
		adapter = null;
	}
	
	public ArrayList<Atom> getSelectedAtoms() {
		return selectedAtoms;	
	}

	// function called when loading new file command is called
	public void notifyNewFileOpen(java.lang.String fullPathName, java.lang.String modelName, java.lang.String fileName) {
		// read file into structure.
		structure = DataManager.readFile(fullPathName);
		if (model==null)
			model = new Model();
		model.setMolecule(structure);	// set the model
	}

	// selected values start from 0. But atom number on script start from 1. Take note.
		public static void getSelected(BS values) {
			System.out.println("Selected: " + values);
			// clear the list to update latest selected atoms
			if(selectedAtoms==null)
				selectedAtoms = new ArrayList<Atom>();
			else
				selectedAtoms.clear();
			
			String[] valueGet = values.toString().split("[{ }]");
			if(model!=null){
				for(int i=0;i<valueGet.length;i++){
					if(valueGet[i].compareTo("")!= 0){
						// if its list of continuous atoms
						if(valueGet[i].contains(":")){
							int start = Integer.parseInt(valueGet[i].split(":")[0])+1;
							int end = Integer.parseInt(valueGet[i].split(":")[1])+1;
							for(int j=start;j<=end;j++){
								selectedAtoms.add(model.getMolecules().get(0).getAtom(j));
							}
						}
						else{
							selectedAtoms.add(model.getMolecules().get(0).getAtom(Integer.parseInt(valueGet[i])+1));
						}
					}
				}
			}
			System.out.println();
		}
		
		public void evaluateOwnAdditionalFunction(String script){
			evaluateUserAction.evaluateAction(script);		
		}
}