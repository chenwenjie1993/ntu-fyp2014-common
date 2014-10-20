package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.align.gui.*;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolViewer;
import org.jmol.util.Logger;

import sg.edu.ntu.aalhossary.fyp2014.commonmodel.Model;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
	
	public JmolDisplay() {
		super();
		statusListener = new MyJmolStatusListener(this);
		adapter = new SmarterJmolAdapter();
		Logger.setLogLevel( verbose?Logger.LEVEL_INFO:Logger.LEVEL_ERROR);
		viewer = JmolViewer.allocateViewer(this, adapter);
		viewer.setJmolCallbackListener(statusListener);
		jMolSelectionListener = new JMolSelectionListener();
		viewer.addSelectionListener(jMolSelectionListener);
		viewer.evalString("load menu \"jmol.mnu\"");
	}
	
	/**
	 * 
	 * @param g
	 */
	public void paint(java.awt.Graphics g) {
		getSize(currentSize);
		g.getClipBounds(rectClip);
		viewer.renderScreenImage(g, currentSize.width, currentSize.height);
	}
	
	/**
	 * 
	 * @param rasmolScript
	 */
	public void evaluateString(java.lang.String rasmolScript) {
		viewer.evalString(rasmolScript);
	}
	
	/**
	 * 
	 * @param pdbFile
	 */
	public void openStringInline(java.lang.String pdbFile) {
		viewer.openStringInline(pdbFile);
	}
	
	public JmolViewer getViewer() {
		return this.viewer;
	}
	
	/**
	 * 
	 * @param rasmolScript
	 */
	public void executeCmd(java.lang.String rasmolScript) {
		viewer.evalString(rasmolScript);
		if(rasmolScript.contains("zap")){
			model = null;
			structure = null;
		}
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

	/**
	 * 
	 * @param fullPathName
	 * @param modelName
	 * @param fileName
	 */
	public void notifyNewFileOpen(java.lang.String fullPathName, java.lang.String modelName, java.lang.String fileName) {
		//System.out.println("notifyFileOpen: " + fullPathName + "\n" + fileName);
		structure = DataManager.readFile(fileName);
		if (model==null)
			model = new Model();
		model.setMolecule(structure);
		ToolPanel.makeModelList();
		// check objects
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("test.txt"));
			for(int i=0;i<model.getMolecules().size();i++){
				writer.write("Molecule: " + model.getMolecules().get(i).getName());
				writer.newLine();
				for(int j=0;j<model.getMolecules().get(i).getChains().size();j++){
					writer.write("Chain Name: " + model.getMolecules().get(i).getChains().get(j).getChainName());
					writer.newLine();
					for(int k=0;k<model.getMolecules().get(i).getChains().get(j).getResidues().size();k++){
						writer.write("Residues: " + model.getMolecules().get(i).getChains().get(j).getResidues().get(k).getName() + ", " + model.getMolecules().get(i).getChains().get(j).getResidues().get(k).getResidueSeqNum());
						writer.newLine();
					}
					for(int k=0;k<model.getMolecules().get(i).getChains().get(j).getAtoms().size();k++){
						writer.write("Atom: " + model.getMolecules().get(i).getChains().get(j).getAtoms().get(k).getName() + ", " + model.getMolecules().get(i).getChains().get(j).getAtoms().get(k).getChainSeqNum());
						writer.newLine();
					}
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}