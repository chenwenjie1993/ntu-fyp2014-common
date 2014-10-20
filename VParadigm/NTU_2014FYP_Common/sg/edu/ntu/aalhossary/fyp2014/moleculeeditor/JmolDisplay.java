package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.biojava.bio.structure.align.gui.*;
import java.awt.event.*;

public class JmolDisplay extends JPrintPanel implements ActionListener {

	private static final long serialVersionUID = -4721103453203185678L;
	private org.jmol.api.JmolViewer viewer;
	private org.jmol.api.JmolAdapter adapter;
	private org.jmol.api.JmolStatusListener statusListener;
	final java.awt.Dimension currentSize = new Dimension();
	final java.awt.Rectangle rectClip = new Rectangle();
	public org.biojava.bio.structure.Structure structure;
	private boolean verbose;
	private JMolSelectionListener jMolSelectionListener;
	public Model model;

	public org.jmol.api.JmolViewer getViewer() {
		return this.viewer;
	}

	public void setStructure(org.biojava.bio.structure.Structure structure) {
		this.structure = structure;
	}

	public JmolDisplay() {
		// TODO - implement JmolDisplay.JmolDisplay
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param g
	 */
	public void paint(java.awt.Graphics g) {
		// TODO - implement JmolDisplay.paint
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param rasmolScript
	 */
	public void evaluateString(java.lang.String rasmolScript) {
		// TODO - implement JmolDisplay.evaluateString
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param pdbFile
	 */
	public void openStringInline(java.lang.String pdbFile) {
		// TODO - implement JmolDisplay.openStringInline
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param rasmolScript
	 */
	public void executeCmd(java.lang.String rasmolScript) {
		// TODO - implement JmolDisplay.executeCmd
		throw new UnsupportedOperationException();
	}

	public void clearDisplay() {
		// TODO - implement JmolDisplay.clearDisplay
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param fullPathName
	 * @param modelName
	 * @param fileName
	 */
	public void notifyNewFileOpen(java.lang.String fullPathName, java.lang.String modelName, java.lang.String fileName) {
		// TODO - implement JmolDisplay.notifyNewFileOpen
		throw new UnsupportedOperationException();
	}

}