package sg.edu.ntu.aalhossary.fyp2014.physics_engine2.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.biojava.bio.structure.Structure;
import org.jmol.api.JmolViewer;
import org.jmol.java.BS;
import org.jmol.viewer.Viewer;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine2.core.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Bond;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Molecule;
import sg.edu.ntu.aalhossary.fyp2014.common.math.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.MouseState;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.JmolDisplay;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.ToolPanel;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.World;

/**
 * @author Wenjie
 *
 *	This class acts as the "middle-man" between JmolDisplay 
 *	and back end calculations and object model.
 *	Contains sections:
 *	- Methods called when there's changes to object model
 *	- Methods called when there's changes in JmolDisplay
 *	- Methods called to notify Viewer of new changes to object Model
 *	- private methods used only within class.
 */

public class UpdateRegistry extends sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.UpdateRegistry {
	/*** Initialize protected variables ***/
//	protected MouseState mouseState;
	protected int dragMode;
	protected boolean addMode;
	protected Atom storeAddAtom;
	protected JmolDisplay jmolPanel;
	protected boolean minimizeMode;
	protected static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UpdateRegistry.class.getName());
	
	/*** Initialize private variables ***/
	private Viewer viewer;					// don't allow classes like Minimizer to access viewer directly
	private ToolPanel toolPanel;			// the side panel, KIV
	private List<Model> modelList; 			// don't allow classes to access model objects directly. 
	//DataManager dataMgr;
	private ArrayList<Atom> selectedAtoms;	// only this class will know the list of selected atoms.
	private ArrayList<Atom> savedAtoms;		// only this class will know which atoms are copied/cut.
	
	/********************/
	/*** Constructors ***/
	/********************/
	public UpdateRegistry(JmolDisplay jmolPanel, ToolPanel toolPanel, List<Model> models){
		this.jmolPanel = jmolPanel;
		this.viewer = jmolPanel.getViewer();
		this.modelList = models;
		this.toolPanel = toolPanel;
//		this.mouseState = new MouseState();
		this.dragMode = -1;
		this.addMode = false;
		this.minimizeMode = false;
	}
	
	// wan yan needed this constructor
	public UpdateRegistry(Viewer viewer, List<Model> models){
		this.viewer = viewer;
		this.modelList = models;
//		this.mouseState = new MouseState();
		this.dragMode = -1;
	}
	
	// Wenjie
	public UpdateRegistry(Viewer viewer) {
		this.viewer = viewer;
		this.modelList = null;
		this.dragMode = -1;
		this.mouseState = new MouseState();
	}
	
	// Ben needed this constructor
	public UpdateRegistry(){
		this.viewer = null;
		this.modelList = new ArrayList<Model>();
		this.toolPanel = null;
//		this.mouseState = new MouseState();
		this.dragMode = -1;
	}
	
	/**********************************************************/
	/*** Methods that notify when there's changes in viewer ***/
	/**********************************************************/

	// called by JMolSelectionListener.java to set the selected Atoms list
	public void setSelectedAtoms(BS values) {
		//System.out.println("Selected: " + values);
		if(selectedAtoms==null)
			selectedAtoms = new ArrayList<Atom>();
		else
			selectedAtoms.clear();

		String[] valueGet = values.toString().split("[{ }]");
		if(modelList!=null){
			Model currentModel = modelList.get(viewer.getDisplayModelIndex());
			for(int i=0;i<valueGet.length;i++){
				if(valueGet[i].compareTo("")!= 0){
					// if its list of continuous atoms
					if(valueGet[i].contains(":")){
						int start = Integer.parseInt(valueGet[i].split(":")[0])+1;
						int end = Integer.parseInt(valueGet[i].split(":")[1])+1;
						for(int j=start;j<=end;j++){
							selectedAtoms.add(currentModel.getAtomHash().get(currentModel.getModelName()+j));
						}
					}
					else{
						selectedAtoms.add(currentModel.getAtomHash().get(currentModel.getModelName()+(Integer.parseInt(valueGet[i])+1)));
					}
				}
			}
		}
	}
	
	// called by MyJmolStatusListener.java for scripts that are self-defined (start with "own").
//	public void evaluateUserAction(String script) {
//		EvaluateUserAction.evaluateAction(this, script);
//	}
	
	double[] offset = new double[3];
	protected boolean atomselected = true;
	// method used for group of selected atoms dragging.
	public void atomMoved(BS atomsMoved) {
		// get the list of atoms moved.
		if(atomselected)
			setSelectedAtoms(atomsMoved);
		
		// update the atoms moved in user model.
		for(int i=0;i<selectedAtoms.size();i++){
			int atomIndex = selectedAtoms.get(i).getAtomSeqNum()-1;
			double[] coords = {viewer.ms.at[atomIndex].x,viewer.ms.at[atomIndex].y,viewer.ms.at[atomIndex].z};
			if(coords[0]!=selectedAtoms.get(i).getCoordinates()[0]){
				offset[0] = selectedAtoms.get(i).getCoordinates()[0]- coords[0];
				offset[1] = selectedAtoms.get(i).getCoordinates()[1]- coords[1];
				offset[2] = selectedAtoms.get(i).getCoordinates()[2]- coords[2];
				selectedAtoms.get(i).setCoordinates(coords);
			}
			else{
				coords[0] = selectedAtoms.get(i).getCoordinates()[0]-offset[0];
				coords[1] = selectedAtoms.get(i).getCoordinates()[1]-offset[1];
				coords[2] = selectedAtoms.get(i).getCoordinates()[2]-offset[2];
				viewer.ms.at[atomIndex].x = (float)coords[0]; 
				viewer.ms.at[atomIndex].y = (float)coords[1]; 
				viewer.ms.at[atomIndex].z = (float)coords[2]; 
				selectedAtoms.get(i).setCoordinates(coords);
			}
			
			//System.out.println("COORD:" + selectedAtoms.get(i).getCoordinates()[0]);
		}
	}

	public void atomMoved() {
		for(Atom atm : modelList.get(viewer.getDisplayModelIndex()).getAtomHash().values()){
			for(int i=0; i<viewer.ms.at.length;i++){
				if(atm.getAtomSeqNum()==(i+1)){
					double[] coords = {viewer.ms.at[i].x,viewer.ms.at[i].y,viewer.ms.at[i].z};
					atm.setCoordinates(coords);
				}
			}
		}
	}
	
	// set the model's bonds using Jmol display.
	public void setModelBonds() {
//		int currModel=0, prevModel=0;
//		Atom atom1, atom2;
//		Bond bond; 
//		String index1, index2;
//		ArrayList<Bond> ownbonds = new ArrayList<Bond>();
//		org.jmol.modelset.Bond[] bonds = viewer.ms.bo;
//		for(int i=0;i<bonds.length;i++){
//			prevModel = currModel;
//			currModel = bonds[i].getAtom1().getModelNumber();
//			Model model = modelList.get(currModel-1);
//			if(currModel!=prevModel && i!=0){
//				modelList.get(prevModel-1).setBonds(ownbonds);
//				ownbonds.clear();
//			}
//			index1 = model.getModelName()+bonds[i].getAtom1().atomSite;
//			index2 = model.getModelName()+bonds[i].getAtom2().atomSite;
//			atom1 = model.getAtomHash().get(index1);
//			atom2 = model.getAtomHash().get(index2);
//			if(atom1==null || atom2==null)
//				continue;
//			bond = new Bond(atom1, atom2);
//			ownbonds.add(bond);
//			atom1.setBond(bond);
//			atom2.setBond(bond);
//			if(i==bonds.length-1){
//				modelList.get(currModel-1).setBonds(ownbonds);
//			}
//		}
	}
	
	// every mouse state and detection called here.
	// to cater for future self-created display.
//	public void setMouseState(int x, int y, int mode) {
//		GestureManager.setMouseState(x,y,mode, mouseState);
//		if(viewer.getInMotion(false)){
//			
//			/*String mouseGesture = "";
//			mouseGesture += "Mouse Mode: " + mouseState.getMouseMode() + "\n";
//			mouseGesture += "Mouse Start Position: X-Pos=" + mouseState.getStartPosX() + " Y-Pos=" + mouseState.getStartPosY() + "\n";
//			mouseGesture += "Mouse End Position: X-Pos=" + mouseState.getEndPosX() + " Y-Pos=" + mouseState.getEndPosY() + "\n";*/
//			//System.out.println(mouseGesture);
//			mouseState.clearState();
//		}
//		if(minimizeMode && mode==16640){	// when mouse drag stopped.
//			System.out.println("Entering minimizing stage...");
//			minimizeModel();
//		}
//	}
	
	/*********************************************/
	/*** Methods that notify viewer on changes ***/
	/*********************************************/
	
	// Method to load file from BioJava structure to Jmol Display
//	public void loadFileToJmol(Structure struc) {
//		createUserModel(struc);
//		String pdb = DataManager.modelToPDB(modelList);
//		viewer.openStringInline(pdb);
//		toolPanel.setModelText(modelList);
//		logger.log(Level.INFO, "After load File: ");
//	}
	
	/*********************************************************/
	/*** Methods that notify when there's changes in model ***/
	/*********************************************************/
	
	// static method to display model from other platform
//	public static void displayModels(List<Model> models, JmolViewer jmolViewer){
//		// convert model to pdb string
//		String pdb = DataManager.modelToPDB(models);
//		// pdb to viewer
//		jmolViewer.openStringInline(pdb);
//	}
	
	private double capCoord (double coord){
		if(coord < 0)
			return Math.max(-999.000, coord);
		return Math.min(coord, +999.000);
		
	}
	
	public void display(ArrayList<AbstractParticle> list){
		String pdb = 	"ATOM      1  N   VAL A   1       6.204  16.869   4.854  1.00 49.05           N\n" +
						"ATOM      2  CA  VAL A   1       6.913  17.759   4.607  1.00 43.14           C\n" +
						"ATOM      3  C   VAL A   1       8.504  17.378   4.797  1.00 24.80           C\n" +
						"ATOM      4  O   VAL A   1       8.805  17.011   5.943  1.00 37.68           O\n" +
						"ATOM      5  CB  VAL A   1       6.369  19.044   5.810  1.00 72.12           C\n" +
						"ATOM      6  CG1 VAL A   1       7.009  20.127   5.418  1.00 61.79           C\n" +
						"ATOM      7  CG2 VAL A   1       5.246  18.533   5.681  1.00 80.12           C\n" +
						"ATOM      8  N   LEU A   2       9.096  18.040   3.857  1.00 26.44           N\n" +
						"ATOM      9  CA  LEU A   2      10.600  17.889   4.283  1.00 26.32           C\n" +
						"ATOM     10  C   LEU A   2      11.265  19.184   5.297  1.00 32.96           C\n" +
						"ATOM     11  O   LEU A   2      10.813  20.177   4.647  1.00 31.90           O\n" +
						"ATOM     12  CB  LEU A   2      11.099  18.007   2.815  1.00 29.23           C\n" +
						"ATOM     13  CG  LEU A   2      11.322  16.956   1.934  1.00 37.71           C\n" +
						"ATOM     14  CD1 LEU A   2      11.468  15.596   2.337  1.00 39.10           C\n" +
						"ATOM     15  CD2 LEU A   2      11.423  17.268   0.300  1.00 37.47           C\n" +
						"END";
		for ( AbstractParticle a: list) {
			sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Atom atom = (sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Atom) a;
			System.out.println(atom.getType());
		}
//		String pdb = "MODEL       1\n";
//		for(int i=0; i<list.size();i++){
//			//String index = String.format("%4d", list.get(i).getGUID());
//			String index = String.format("%4d", i);
//			if(list.get(i) instanceof sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Atom){
//				double scale = Math.pow(10, 10 + list.get(i).getPosition().metric);
//				Vector3D position = list.get(i).getPosition();
//				double x = capCoord(position.x*scale);
//				double y = capCoord(position.y*scale);
//				double z = capCoord(position.z*scale);
//				String coords= String.format("%8.3f%8.3f%8.3f", x,y,z);
//				String elementSymbol = String.format("%-4s",((sg.edu.ntu.aalhossary.fyp2014.physics_engine2.amber03.models.Atom)list.get(i)).getType().toUpperCase());
//						
//				pdb += "HETATM "+index+" "+elementSymbol+"         1    "+coords+"  1.00  0.00\n";
//				
//			}
//			else if (list.get(i) instanceof Molecule){
//				Molecule m = (Molecule)list.get(i);
//				if(World.simulationLvlAtomic == true) {
//					for(Atom a: m.getChains().get(0).atomSeq){
//						double scale = Math.pow(10, 10 + list.get(i).getPosition().metric);
//						Vector3D position = a.getPosition();
//						double x = capCoord(position.x*scale);
//						double y = capCoord(position.y*scale);
//						double z = capCoord(position.z*scale);
//						String coords= String.format("%8.3f%8.3f%8.3f",x,y,z);
//						String elementSymbol = String.format("%-4s",a.getElementSymbol().toUpperCase());
//								
//						pdb += "HETATM "+index+" "+elementSymbol+"         1    "+coords+"  1.00  0.00\n";
//						
//					}
//				
//					/*
//					pdb += "HETATM    1 NA   TST A   1       5.000   5.000   5.000  1.00  0.00\n"+
//						   "HETATM    2 CL   TST A   1       6.400   6.400   6.400  1.00  0.00\n"+
//						   "CONECT    1      2\n" +
//						   "CONECT    2      1\n";
//						   */
//				}
//				else {
//					double scale = Math.pow(10, 10 + list.get(i).getPosition().metric);
//					Vector3D position = list.get(i).getPosition();
//					double x = capCoord(position.x*scale);
//					double y = capCoord(position.y*scale);
//					double z = capCoord(position.z*scale);
//					String coords= String.format("%8.3f%8.3f%8.3f",x,y,z);
//					pdb += "HETATM    1 CS   TST A   1    "+coords+"  1.00  0.00\n";
//				}
//			}
//		}
//		pdb += "ENDMDL";
//		
//		System.out.println(pdb);
		
//		if(World.displayUI)
			viewer.openStringInline(pdb);
		
//		try {
//			BufferedWriter writer = new BufferedWriter(new FileWriter("output.pdb"));
//			writer.write(pdb);
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			World.simulationStatus = "restart";
//		}
		
//		if(World.displayUI) {
//			createUserModel(DataManager.readFile("output.pdb"));
//		}
	}
		
	public void notifyUpdated(AbstractParticle[] particles){
		List<Atom> atomsUpdated = new ArrayList<Atom>();
		
		// When the two particles are part of common model
//		if(modelList.size()!=0){
//			for(int i=0;i<particles.length;i++){
//				if(particles[i] instanceof Atom){ // if particle is Atom type
//					// get the updated atom from model
//					atomsUpdated.add(modelList.get(0).getAtomHash().get(modelList.get(0).getModelName()+((Atom)particles[i]).getChainSeqNum()));
//				}
//			}
//			// update atoms in jmol display
//			for(int i=0;i<atomsUpdated.size();i++){
//				Atom atom = atomsUpdated.get(i);
//				float xOffset = (float)atom.getPosition().x - viewer.getAtomPoint3f(atom.getChainSeqNum()-1).x;
//				float yOffset = (float)atom.getPosition().y - viewer.getAtomPoint3f(atom.getChainSeqNum()-1).y;
//				float zOffset = (float)atom.getPosition().z - viewer.getAtomPoint3f(atom.getChainSeqNum()-1).z;
//				viewer.evalString("select atomno="+ atom.getChainSeqNum());
//				viewer.evalString("translateSelected {" + xOffset + " " + yOffset + " " + zOffset + "}");
//			}
//		}
	}

	// delete Atoms from both modelList and viewer.
	public void deleteAtoms(int currentModelIndex, String key, int atomPosition) {
		modelList.get(currentModelIndex).removeAtom(key);
		//modelList.get(currentModelIndex).getAtomHash().remove(key);
		viewer.evalString("delete atomno=" + atomPosition + " && modelindex=" + viewer.getDisplayModelIndex());
		toolPanel.setModelText(modelList);
	}

	// copy atoms into new Atoms object from selected atoms
	public void copyAtoms(int currentModelIndex, String key) {
		if(savedAtoms==null){
			savedAtoms = new ArrayList<Atom>();
		}
		try {
			Atom newAtom = (Atom)modelList.get(currentModelIndex).getAtomHash().get(key).clone();
			savedAtoms.add(newAtom);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		toolPanel.setModelText(modelList);
	}

	// paste copied/cut atoms, not applicable when copied atoms are paste to same model.
//	public void pasteSavedAtoms(int modelIndex) {
//		if(savedAtoms==null)
//			JOptionPane.showMessageDialog(jmolPanel,"You have not copied or cut any atoms to paste!",
//				    "Warning", JOptionPane.WARNING_MESSAGE);
//		else{
//			boolean error = false;
//			for(int i=0;i<savedAtoms.size();i++){
//				for(Atom atm : modelList.get(modelIndex).getAtomHash().values()){
//					if(savedAtoms.get(i).getCoordinates()[0]==atm.getCoordinates()[0] && 
//							savedAtoms.get(i).getCoordinates()[1]==atm.getCoordinates()[1] &&
//							savedAtoms.get(i).getCoordinates()[2]==atm.getCoordinates()[2] ){
//						error = true;
//						break;
//					}
//				}
//				if(error){
//					JOptionPane.showMessageDialog(jmolPanel,"You are trying to paste atoms in location occupied!",
//						    "Warning", JOptionPane.WARNING_MESSAGE);
//					break;
//				}
//				else{
//					addAtom(modelIndex, savedAtoms.get(i));	
//				}
//			}
//			String pdb =DataManager.modelToPDB(modelList);
//			System.out.println(pdb);
//			
//			viewer.openStringInline(pdb);
//			viewer.setCurrentModelIndex(modelIndex);
//		}
//		toolPanel.setModelText(modelList);
//	}

	// add new atom object to modelList. 
	public void addAtom(int modelIndex, Atom atom) {
		modelList.get(modelIndex).setMolecule(atom);
	}

	public void setMinimizeMode(boolean isMinimize) {
		this.minimizeMode = isMinimize;
	}
	
	/**************************************************/
	/*** Get methods to be called by external class ***/
	/**************************************************/
	
	// get the list of selected atoms already generated
	public ArrayList<Atom> getSelectedAtoms() {
		return selectedAtoms;
	}
	
	// from biojava structure, convert to own user model
	public void createUserModel(Structure struc) {
		// read default MMFFtypes for atoms
		
		if(modelList==null)
			modelList = new ArrayList<Model>();
		Model model=null;
		for(int i=0;i<struc.nrModels();i++){
			// for each model, create and add to model list
			model = new Model();
			model.setModelName("Model"+(i+1));
			model.setMolecule(struc.getModel(i));	// set the model
			modelList.add(model);
		}
	}
	
	// get the list of user model
	public List<Model> getModelList() {
		return modelList;
	}

	// define minimizer, for minimizing molecule.
//	public void minimizeModel() {
//		//viewer.ms.setAtomCoord(0, 0.0f, 0.0f, 0.0f);
//		Minimizer minimizer = new Minimizer(this);
//		minimizer.setupMinimizing(modelList.get(viewer.getDisplayModelIndex()));
//		minimizer.startMinimizing();
//		System.out.println(DataManager.modelToPDB(modelList));
//		minimizeMode = false;
//	}
		
	public void updateViewerCoord() {
		for(Atom atm: modelList.get(viewer.getDisplayModelIndex()).getAtomHash().values()){
	    	  viewer.ms.setAtomCoord((atm.getAtomSeqNum()-1), atm.getCoordinates()[0], atm.getCoordinates()[1], atm.getCoordinates()[2]);
	      }
		jmolPanel.getViewer().refresh(3, "minimization step ");
	}

	public JmolViewer getViewer() {
		return viewer;
	}
}
