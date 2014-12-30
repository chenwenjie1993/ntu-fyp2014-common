package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.align.gui.*;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolSelectionListener;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
	//protected org.biojava.bio.structure.Structure structure;
	private boolean verbose;
	private JmolSelectionListener jMolSelectionListener;
	//protected static List<Model> modelList;
	protected static ArrayList<Atom> selectedAtoms;
	protected UserActionType selectedTag;
	protected EvaluateUserAction evaluateUserAction;
	protected UpdateRegistry mediator;
	
	
	public JmolDisplay() {
		super();
		statusListener = new MyJmolStatusListener(this);
		adapter = new SmarterJmolAdapter();
		evaluateUserAction = new EvaluateUserAction(mediator);
		Logger.setLogLevel( verbose?Logger.LEVEL_INFO:Logger.LEVEL_ERROR);
		viewer = JmolViewer.allocateViewer(this, adapter);
		viewer.setJmolCallbackListener(statusListener);
		jMolSelectionListener = new JMolSelectionListener(this);
		viewer.addSelectionListener(jMolSelectionListener);
		viewer.setMenu("res/res/jmol.mnu", true); // load own menu when initialising display
	}
	
	public void paint(java.awt.Graphics g) {
		getSize(currentSize);
		g.getClipBounds(rectClip);
		viewer.renderScreenImage(g, currentSize.width, currentSize.height);
	}
	
	public void openStringInline(java.lang.String pdbFile) {
		viewer.openStringInline(pdbFile);
	}
	
	public JmolViewer getViewer() {
		return this.viewer;
	}
	
	public UpdateRegistry getMediator(){
		return this.mediator;
	}
	
	public void executeCmd(java.lang.String rasmolScript) {
		viewer.evalString(rasmolScript);
	}

	public void clearDisplay() {
		executeCmd("zap;");
		
		viewer = null;
		adapter = null;
	}
	
	public ArrayList<Atom> getSelectedAtoms() {
		return selectedAtoms;	
	}

	public void evaluateUserFunction(String script){
		evaluateUserAction.evaluateAction(script);		
	}

	public void setMediator(UpdateRegistry mediator) {
		this.mediator = mediator;
	}
}