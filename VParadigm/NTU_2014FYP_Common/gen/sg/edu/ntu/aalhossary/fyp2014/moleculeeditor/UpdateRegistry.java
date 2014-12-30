package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javajs.util.P3;

import org.biojava.bio.structure.Structure;
import org.jmol.api.JmolViewer;
import org.jmol.java.BS;

import sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle;
import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Vector3D;

public class UpdateRegistry {
	JmolViewer viewer;
	List<Model> modelList;
	DataManager dataMgr;
	private ArrayList<Atom> selectedAtoms;
	
	public UpdateRegistry(JmolViewer viewer, List<Model> models){
		this.viewer = viewer;
		this.modelList = models;
		dataMgr = new DataManager();
	}
	
	/**********************************************************/
	/*** Methods that notify when there's changes in viewer ***/
	/**********************************************************/
	
	public void createUserModel(String fileName) {
		Structure structure = dataMgr.readFile(fileName);
		if(modelList==null)
			modelList = new ArrayList<Model>();
		Model model=null;
		for(int i=0;i<structure.nrModels();i++){
			// for each model, create and add to model list
			model = new Model();
			model.setModelName(structure.getPdbId());
			model.setMolecule(structure);	// set the model
			modelList.add(model);
		}
	}

	public void setSelectedAtoms(BS values) {
		System.out.println("Selected: " + values);
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
		EvaluateUserAction userAction = new EvaluateUserAction(this);
		userAction.evaluateAction(script);
	}
	
	public void atomMoved(BS atomsMoved) {
		// get the list of atoms moved.
		setSelectedAtoms(atomsMoved);
		
		// update the atoms moved in user model.
		for(int i=0;i<selectedAtoms.size();i++){
			int atomIndex = selectedAtoms.get(i).getChainSeqNum()-1;
			double[] coords = {viewer.getAtomPoint3f(atomIndex).x,viewer.getAtomPoint3f(atomIndex).y,viewer.getAtomPoint3f(atomIndex).z};
			selectedAtoms.get(i).setCoordinates(coords);
		}
	}
	
	/*********************************************************/
	/*** Methods that notify when there's changes in model ***/
	/*********************************************************/
	
	// static method to display model from other platform
	public static void displayModels(List<Model> models, JmolViewer viewer){
		// convert model to pdb string
		String pdb = DataManager.modelToPDB(models);
		// pdb to viewer
		viewer.openStringInline(pdb);
	}
	
	public void displayParticles(AbstractParticle p1, AbstractParticle p2){
		DecimalFormat decformat = new DecimalFormat("#.###");
		String[] coord = new String[6];
		double scale = Math.pow(10, 10);
		coord[0] = decformat.format(p1.getPosition().x*scale);
		coord[1] = decformat.format(p1.getPosition().y*scale);
		coord[2] = decformat.format(p1.getPosition().z*scale);
		coord[3] = decformat.format(p2.getPosition().x*scale);
		coord[4] = decformat.format(p2.getPosition().y*scale);
		coord[5] = decformat.format(p2.getPosition().z*scale);
		
		String[] spaces = new String[6];
		for(int i=0;i<coord.length;i++){
			String space = "";
			for(int j=0;j<(8 - coord[i].length());j++){
				space += " ";
			}
			spaces[i] = space;
		}
//							  "         1         2         3         4         5         6         7         8
//							  "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
//		String line1=String.format("HETATM%5d  N   TST A", args)
		Vector3D position1 = p1.getPosition();
		String coords1= String.format("%8.3f%8.3f%8.3f", position1.x,position1.y,position1.z);
		Vector3D position2 = p2.getPosition();
		String coords2= String.format("%8.3f%8.3f%8.3f", position2.x,position2.y,position2.z);
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
		createUserModel("res/temp/temp.pdb");
	}

	public void notifyUpdated(AbstractParticle[] particles){
		List<Atom> atomsUpdated = new ArrayList<Atom>();
		for(int i=0;i<particles.length;i++){
			if(particles[i] instanceof Atom){ // if particle is Atom type
				// get the updated atom from model
				atomsUpdated.add(modelList.get(0).getAtomHash().get(modelList.get(0).getModelName()+((Atom)particles[i]).getChainSeqNum()));
			}
		}
		
		// update atoms in jmol display
		for(int i=0;i<atomsUpdated.size();i++){
			Atom atom = atomsUpdated.get(i);
			float xOffset = (float)atomsUpdated.get(i).getPosition().x - viewer.getAtomPoint3f(atomsUpdated.get(i).getChainSeqNum()-1).x;
			float yOffset = (float)atomsUpdated.get(i).getPosition().y - viewer.getAtomPoint3f(atomsUpdated.get(i).getChainSeqNum()-1).y;
			float zOffset = (float)atomsUpdated.get(i).getPosition().z - viewer.getAtomPoint3f(atomsUpdated.get(i).getChainSeqNum()-1).z;
			viewer.evalString("select atomno="+ atomsUpdated.get(i).getChainSeqNum());
			viewer.evalString("translateSelected {" + xOffset + " " + yOffset + " " + zOffset + "}");
		}
	}

	public ArrayList<Atom> getSelectedAtoms() {
		return selectedAtoms;
	}
}
