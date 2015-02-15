package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava.bio.structure.Structure;
import org.jmol.api.JmolViewer;
import org.jmol.java.BS;
import org.jmol.viewer.ActionManager;
import org.jmol.viewer.Viewer;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Bond;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.ui.ToolPanel;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Vector3D;

public class UpdateRegistry {
	Viewer viewer;
	ToolPanel toolPanel;
	List<Model> modelList;
	DataManager dataMgr;
	private ArrayList<Atom> selectedAtoms;
	protected MouseState mouseState;
	protected int dragMode;
	protected static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MoleculeEditor.class.getName());
	
	public UpdateRegistry(Viewer viewer, ToolPanel toolPanel, List<Model> models){
		this.viewer = viewer;
		this.modelList = models;
		this.toolPanel = toolPanel;
		this.dataMgr = new DataManager();
		this.mouseState = new MouseState();
		this.dragMode = -1;
	}
	
	public UpdateRegistry(Viewer viewer, List<Model> models){
		this.viewer = viewer;
		this.modelList = models;
		this.dataMgr = new DataManager();
		this.mouseState = new MouseState();
		this.dragMode = -1;
	}
	
	public UpdateRegistry(){
		this.viewer = null;
		this.modelList = new ArrayList<Model>();
		this.toolPanel = null;
		this.dataMgr = new DataManager();
		this.mouseState = new MouseState();
		this.dragMode = -1;
	}
	
	/**********************************************************/
	/*** Methods that notify when there's changes in viewer ***/
	/**********************************************************/

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
	
	public void evaluateUserAction(String script) {
		EvaluateUserAction.evaluateAction(this, script);
	}
	
	public void atomMoved(BS atomsMoved) {
		// get the list of atoms moved.
		setSelectedAtoms(atomsMoved);
		
		// update the atoms moved in user model.
		for(int i=0;i<selectedAtoms.size();i++){
			int atomIndex = selectedAtoms.get(i).getAtomSeqNum()-1;
			double[] coords = {viewer.ms.at[atomIndex].x,viewer.ms.at[atomIndex].y,viewer.ms.at[atomIndex].z};
			selectedAtoms.get(i).setCoordinates(coords);
		}
	}

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
			bond = new Bond(atom1, atom2);
			ownbonds.add(bond);
			atom1.setBond(bond);
			atom2.setBond(bond);
			if(i==bonds.length-1){
				modelList.get(currModel-1).setBonds(ownbonds);
			}
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
	
	public void displayParticles(ArrayList<AbstractParticle> list){
		DecimalFormat decformat = new DecimalFormat("#.###");
	
		
		String[] coord;
		String pdb = "MODEL       1\n";
		for(int i=0; i<list.size();i++){
			if(list.get(i) instanceof Atom){
				double scale = Math.pow(10, 10 + list.get(i).getPosition().metric);
				coord = new String[3];
				coord[0] = decformat.format(list.get(i).getPosition().x);
				coord[1] = decformat.format(list.get(i).getPosition().y);
				coord[2] = decformat.format(list.get(i).getPosition().z);
				Vector3D position = list.get(i).getPosition();
				String coords= String.format("%8.3f%8.3f%8.3f", position.x*scale,position.y*scale,position.z*scale);
				String properties = "HETATM    1 CL   TST A   1    "+coords+"  1.00  0.00";
				pdb += properties + '\n';
			}
			else{
				throw new UnsupportedOperationException();
			}
		}
		pdb += "ENDMDL";
		viewer.openStringInline(pdb);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("res/temp/temp.pdb"));
			writer.write(pdb);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		createUserModel(DataManager.readFile("res/temp/temp.pdb"));
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
			BufferedWriter writer = new BufferedWriter(new FileWriter("res/temp/temp.pdb"));
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

	public ArrayList<Atom> getSelectedAtoms() {
		return selectedAtoms;
	}

	public void deleteAtoms(int currentModelIndex, String key, int atomPosition) {
		modelList.get(currentModelIndex).getAtomHash().remove(key);
		viewer.evalString("delete atomno=" + atomPosition + " && modelindex=" + viewer.getDisplayModelIndex());
		toolPanel.setModelText(modelList);
	}

	public void setMouseState(int x, int y, int mode) {
		GestureManager.setMouseState(x,y,mode, mouseState);
		if(viewer.getInMotion(false)){
			String mouseGesture = "";
			mouseGesture += "Mouse Mode: " + mouseState.getMouseMode() + "\n";
			mouseGesture += "Mouse Start Position: X-Pos=" + mouseState.getStartPosX() + " Y-Pos=" + mouseState.getStartPosY() + "\n";
			mouseGesture += "Mouse End Position: X-Pos=" + mouseState.getEndPosX() + " Y-Pos=" + mouseState.getEndPosY() + "\n";
			//System.out.println(mouseGesture);
			mouseState.clearState();
		}
	}

	/******************************************/
	/*** Private methods used in class only ***/
	/******************************************/
	
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
	
	public List<Model> getModelList() {
		return modelList;
	}
}
