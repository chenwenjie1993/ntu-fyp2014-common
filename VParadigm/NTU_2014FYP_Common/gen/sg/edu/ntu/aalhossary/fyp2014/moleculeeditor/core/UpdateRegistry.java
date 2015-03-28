package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.io.BufferedWriter;
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

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Bond;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Molecule;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.JmolDisplay;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.ToolPanel;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Vector3D;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.World;

/**
 * @author Xiu Ting
 *
 *	This class acts as the "middle-man" between JmolDisplay 
 *	and back end calculations and object model.
 *	Contains sections:
 *	- Methods called when there's changes to object model
 *	- Methods called when there's changes in JmolDisplay
 *	- Methods called to notify Viewer of new changes to object Model
 *	- private methods used only within class.
 */

public class UpdateRegistry {
	/*** Initialize protected variables ***/
	protected MouseState mouseState;
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
		this.mouseState = new MouseState();
		this.dragMode = -1;
		this.addMode = false;
		this.minimizeMode = false;
	}
	
	// wan yan needed this constructor
	public UpdateRegistry(Viewer viewer, List<Model> models){
		this.viewer = viewer;
		this.modelList = models;
		this.mouseState = new MouseState();
		this.dragMode = -1;
	}
	
	// Ben needed this constructor
	public UpdateRegistry(){
		this.viewer = null;
		this.modelList = new ArrayList<Model>();
		this.toolPanel = null;
		this.mouseState = new MouseState();
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
	public void evaluateUserAction(String script) {
		EvaluateUserAction.evaluateAction(this, script);
	}
	
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
		int currModel=0, prevModel=0;
		Atom atom1, atom2;
		Bond bond; 
		String index1, index2;
		ArrayList<Bond> ownbonds = new ArrayList<Bond>();
		org.jmol.modelset.Bond[] bonds = viewer.ms.bo;
		for(int i=0;i<bonds.length;i++){
			prevModel = currModel;
			currModel = bonds[i].getAtom1().getModelNumber();
			Model model = modelList.get(currModel-1);
			if(currModel!=prevModel && i!=0){
				modelList.get(prevModel-1).setBonds(ownbonds);
				ownbonds.clear();
			}
			index1 = model.getModelName()+bonds[i].getAtom1().atomSite;
			index2 = model.getModelName()+bonds[i].getAtom2().atomSite;
			atom1 = model.getAtomHash().get(index1);
			atom2 = model.getAtomHash().get(index2);
			if(atom1==null || atom2==null)
				continue;
			bond = new Bond(atom1, atom2);
			ownbonds.add(bond);
			atom1.setBond(bond);
			atom2.setBond(bond);
			if(i==bonds.length-1){
				modelList.get(currModel-1).setBonds(ownbonds);
			}
		}
	}
	
	// every mouse state and detection called here.
	// to cater for future self-created display.
	public void setMouseState(int x, int y, int mode) {
		GestureManager.setMouseState(x,y,mode, mouseState);
		if(viewer.getInMotion(false)){
			
			/*String mouseGesture = "";
			mouseGesture += "Mouse Mode: " + mouseState.getMouseMode() + "\n";
			mouseGesture += "Mouse Start Position: X-Pos=" + mouseState.getStartPosX() + " Y-Pos=" + mouseState.getStartPosY() + "\n";
			mouseGesture += "Mouse End Position: X-Pos=" + mouseState.getEndPosX() + " Y-Pos=" + mouseState.getEndPosY() + "\n";*/
			//System.out.println(mouseGesture);
			mouseState.clearState();
		}
		if(minimizeMode && mode==16640){	// when mouse drag stopped.
			System.out.println("Entering minimizing stage...");
			minimizeModel();
		}
	}
	
	/*********************************************/
	/*** Methods that notify viewer on changes ***/
	/*********************************************/
	
	// Method to load file from BioJava structure to Jmol Display
	public void loadFileToJmol(Structure struc) {
		createUserModel(struc);
		String pdb = DataManager.modelToPDB(modelList);
		viewer.openStringInline(pdb);
		toolPanel.setModelText(modelList);
		logger.log(Level.INFO, "After load File: ");
	}
	
	/*********************************************************/
	/*** Methods that notify when there's changes in model ***/
	/*********************************************************/
	
	// static method to display model from other platform
	public static void displayModels(List<Model> models, JmolViewer jmolViewer){
		// convert model to pdb string
		String pdb = DataManager.modelToPDB(models);
		// pdb to viewer
		jmolViewer.openStringInline(pdb);
	}
	
	private double capCoord (double coord){
		if(coord < 0)
			return Math.max(-999.000, coord);
		return Math.min(coord, +999.000);
	}
	
	public void displayParticles(ArrayList<AbstractParticle> list){
		DecimalFormat decformat = new DecimalFormat("#.###");
		
		
		String[] coord;
		String pdb = "MODEL       1\n";
		for(int i=0; i<list.size();i++){
			String index = String.format("%4d", list.get(i).getGUID());
			if(list.get(i) instanceof Atom){
				double scale = Math.pow(10, 10 + list.get(i).getPosition().metric);
				Vector3D position = list.get(i).getPosition();
				double x = capCoord(position.x*scale);
				double y = capCoord(position.y*scale);
				double z = capCoord(position.z*scale);
				String coords= String.format("%8.3f%8.3f%8.3f", x,y,z);
				String elementSymbol = String.format("%-4s",((Atom)list.get(i)).getElementSymbol().toUpperCase());
						
				pdb += "HETATM "+index+" "+elementSymbol+"         1    "+coords+"  1.00  0.00\n";
				
			}
			else if (list.get(i) instanceof Molecule){
				Molecule m = (Molecule)list.get(i);
				if(World.simulationLvlAtomic == true) {
					for(Atom a: m.getChains().get(0).atomSeq){
						double scale = Math.pow(10, 10 + list.get(i).getPosition().metric);
						Vector3D position = a.getPosition();
						double x = capCoord(position.x*scale);
						double y = capCoord(position.y*scale);
						double z = capCoord(position.z*scale);
						String coords= String.format("%8.3f%8.3f%8.3f",x,y,z);
						String elementSymbol = String.format("%-4s",a.getElementSymbol().toUpperCase());
								
						pdb += "HETATM "+index+" "+elementSymbol+"         1    "+coords+"  1.00  0.00\n";
						
					}
				
					/*
					pdb += "HETATM    1 NA   TST A   1       5.000   5.000   5.000  1.00  0.00\n"+
						   "HETATM    2 CL   TST A   1       6.400   6.400   6.400  1.00  0.00\n"+
						   "CONECT    1      2\n" +
						   "CONECT    2      1\n";
						   */
				}
				else {
					double scale = Math.pow(10, 10 + list.get(i).getPosition().metric);
					Vector3D position = list.get(i).getPosition();
					double x = capCoord(position.x*scale);
					double y = capCoord(position.y*scale);
					double z = capCoord(position.z*scale);
					String coords= String.format("%8.3f%8.3f%8.3f",x,y,z);
					pdb += "HETATM    1 AR   TST A   1    "+coords+"  1.00  0.00\n";
				}
			}
		}
		pdb += "ENDMDL";
		viewer.openStringInline(pdb);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("temp.pdb"));
			writer.write(pdb);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createUserModel(DataManager.readFile("temp.pdb"));
	}
	
	public void displayParticles(AbstractParticle p1, AbstractParticle p2){
		DecimalFormat decformat = new DecimalFormat("#.###");
		/*
		String[] coord = new String[6];
		coord[0] = decformat.format(p1.getPosition().x);
		coord[1] = decformat.format(p1.getPosition().y);
		coord[2] = decformat.format(p1.getPosition().z);
		coord[3] = decformat.format(p2.getPosition().x);
		coord[4] = decformat.format(p2.getPosition().y);
		coord[5] = decformat.format(p2.getPosition().z);
		
		String[] spaces = new String[6];
		for(int i=0;i<coord.length;i++){
			String space = "";
			for(int j=0;j<(8 - coord[i].length());j++){
				space += " ";
			}
			spaces[i] = space;
		}
		*/
//							  "         1         2         3         4         5         6         7         8
//							  "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
		double scale = Math.pow(10, 10);
		Vector3D position1 = p1.getPosition();
		String coords1= String.format("%8.3f%8.3f%8.3f", position1.x*scale,position1.y*scale,position1.z*scale);
		Vector3D position2 = p2.getPosition();
		String coords2= String.format("%8.3f%8.3f%8.3f", position2.x*scale,position2.y*scale,position2.z*scale);
		String p1Properties = "HETATM    1 NA   TST A   1    "+coords1+"  1.00  0.00";
		String p2Properties = "HETATM    2 CL   TST A   2    "+coords2+"  1.00  0.00";

		String pdb = "MODEL       1\n" + p1Properties + '\n' + p2Properties + "\nENDMDL";
		
		viewer.openStringInline(pdb);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("temp.pdb"));
			writer.write(pdb);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createUserModel(DataManager.readFile("res/temp/temp.pdb"));
	}

	public void notifyUpdated(AbstractParticle[] particles){
		List<Atom> atomsUpdated = new ArrayList<Atom>();
		
		// When the two particles are part of common model
		if(modelList.size()!=0){
			for(int i=0;i<particles.length;i++){
				if(particles[i] instanceof Atom){ // if particle is Atom type
					// get the updated atom from model
					atomsUpdated.add(modelList.get(0).getAtomHash().get(modelList.get(0).getModelName()+((Atom)particles[i]).getChainSeqNum()));
				}
			}
			// update atoms in jmol display
			for(int i=0;i<atomsUpdated.size();i++){
				Atom atom = atomsUpdated.get(i);
				float xOffset = (float)atom.getPosition().x - viewer.getAtomPoint3f(atom.getChainSeqNum()-1).x;
				float yOffset = (float)atom.getPosition().y - viewer.getAtomPoint3f(atom.getChainSeqNum()-1).y;
				float zOffset = (float)atom.getPosition().z - viewer.getAtomPoint3f(atom.getChainSeqNum()-1).z;
				viewer.evalString("select atomno="+ atom.getChainSeqNum());
				viewer.evalString("translateSelected {" + xOffset + " " + yOffset + " " + zOffset + "}");
			}
		}
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
	public void pasteSavedAtoms(int modelIndex) {
		if(savedAtoms==null)
			JOptionPane.showMessageDialog(jmolPanel,"You have not copied or cut any atoms to paste!",
				    "Warning", JOptionPane.WARNING_MESSAGE);
		else{
			boolean error = false;
			for(int i=0;i<savedAtoms.size();i++){
				for(Atom atm : modelList.get(modelIndex).getAtomHash().values()){
					if(savedAtoms.get(i).getCoordinates()[0]==atm.getCoordinates()[0] && 
							savedAtoms.get(i).getCoordinates()[1]==atm.getCoordinates()[1] &&
							savedAtoms.get(i).getCoordinates()[2]==atm.getCoordinates()[2] ){
						error = true;
						break;
					}
				}
				if(error){
					JOptionPane.showMessageDialog(jmolPanel,"You are trying to paste atoms in location occupied!",
						    "Warning", JOptionPane.WARNING_MESSAGE);
					break;
				}
				else{
					addAtom(modelIndex, savedAtoms.get(i));	
				}
			}
			String pdb =DataManager.modelToPDB(modelList);
			System.out.println(pdb);
			
			viewer.openStringInline(pdb);
			viewer.setCurrentModelIndex(modelIndex);
		}
		toolPanel.setModelText(modelList);
	}

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
	public void minimizeModel() {
		//viewer.ms.setAtomCoord(0, 0.0f, 0.0f, 0.0f);
		Minimizer minimizer = new Minimizer(this);
		minimizer.setupMinimizing(modelList.get(viewer.getDisplayModelIndex()));
		minimizer.startMinimizing();
		System.out.println(DataManager.modelToPDB(modelList));
		minimizeMode = false;
	}
		
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
