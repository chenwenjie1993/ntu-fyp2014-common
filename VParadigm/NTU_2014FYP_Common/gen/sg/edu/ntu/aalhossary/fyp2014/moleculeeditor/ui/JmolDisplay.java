package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.align.gui.*;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolSelectionListener;
import org.jmol.api.JmolViewer;
import org.jmol.java.BS;
import org.jmol.modelkit.ModelKitPopup;
import org.jmol.modelkit.ModelKitPopupResourceBundle;
import org.jmol.popup.PopupResource;
import org.jmol.util.Logger;
import org.jmol.viewer.Viewer;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.EvaluateUserAction;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.JMolSelectionListener;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MyJmolStatusListener;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry;
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
	private org.jmol.viewer.Viewer viewer;
	private org.jmol.api.JmolAdapter adapter;
	private org.jmol.api.JmolStatusListener statusListener;
	protected final java.awt.Dimension currentSize = new Dimension();
	protected java.awt.Rectangle rectClip = new Rectangle();
	private boolean verbose;
	private JmolSelectionListener jMolSelectionListener;
	protected static ArrayList<Atom> selectedAtoms;
	protected UserActionType selectedTag;
	protected UpdateRegistry mediator;
	
	
	public JmolDisplay() {
		super();
		statusListener = new MyJmolStatusListener(this);
		adapter = new SmarterJmolAdapter();
		Logger.setLogLevel( verbose?Logger.LEVEL_INFO:Logger.LEVEL_ERROR);
		viewer = (Viewer)JmolViewer.allocateViewer(this, adapter);
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
	
	public Viewer getViewer() {
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
		EvaluateUserAction.evaluateAction(mediator, script);		
	}

	public void setMediator(UpdateRegistry mediator) {
		this.mediator = mediator;
	}
}