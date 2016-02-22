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
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core.GestureManager;
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
	}
	
	// set the model's bonds using Jmol display.
	public void setModelBonds() {
	}

	
	private double capCoord (double coord){
		if(coord < 0)
			return Math.max(-999.000, coord);
		return Math.min(coord, +999.000);
		
	}
	
	public void display(ArrayList<AbstractParticle> list){

//		String gro = 	"HEMOGLOBIN (DEOXY) (ALPHA CHAIN);; HEMOGLOBIN (DEOXY) (BETA CHAIN);" +
//						"   38" +
//						"    1VAL      N    1   0.620   1.687   0.485" +
//						"    1VAL     H1    2   0.641   1.610   0.425" +
//						"    1VAL     H2    3   0.634   1.659   0.581" +
//						"    1VAL     H3    4   0.525   1.715   0.472" +
//						"    1VAL     CA    5   0.691   1.776   0.461" +
//						"    1VAL     HA    6   0.671   1.775   0.363" +
//						"    1VAL     CB    7   0.637   1.904   0.581" +
//						"    1VAL     HB    8   0.634   1.943   0.673" +
//						"    1VAL    CG1    9   0.701   2.013   0.542" +
//						"    1VAL   HG11   10   0.677   2.089   0.602" +
//						"    1VAL   HG12   11   0.799   1.996   0.546" +
//						"    1VAL   HG13   12   0.674   2.035   0.448" +
//						"    1VAL    CG2   13   0.525   1.853   0.568" +
//						"    1VAL   HG21   14   0.459   1.902   0.626" +
//						"    1VAL   HG22   15   0.496   1.859   0.472" +
//						"    1VAL   HG23   16   0.528   1.757   0.596" +
//						"    1VAL      C   17   0.850   1.738   0.480" +
//						"    1VAL      O   18   0.881   1.701   0.594" +
//						"    2LEU      N   19   0.910   1.804   0.386" +
//						"    2LEU      H   20   0.870   1.852   0.307" +
//						"    2LEU     CA   21   1.060   1.789   0.428" +
//						"    2LEU     HA   22   1.079   1.703   0.476" +
//						"    2LEU     CB   23   1.110   1.801   0.282" +
//						"    2LEU    HB1   24   1.199   1.846   0.282" +
//						"    2LEU    HB2   25   1.045   1.856   0.230" +
//						"    2LEU     CG   26   1.132   1.696   0.193" +
//						"    2LEU     HG   27   1.125   1.695   0.293" +
//						"    2LEU    CD1   28   1.147   1.560   0.234" +
//						"    2LEU   HD11   29   1.162   1.502   0.153" +
//						"    2LEU   HD12   30   1.225   1.551   0.296" +
//						"    2LEU   HD13   31   1.064   1.530   0.281" +
//						"    2LEU    CD2   32   1.142   1.727   0.030" +
//						"    2LEU   HD21   33   1.158   1.641  -0.019" +
//						"    2LEU   HD22   34   1.057   1.768  -0.001" +
//						"    2LEU   HD23   35   1.218   1.790   0.013" +
//						"    2LEU      C   36   1.127   1.918   0.530" +
//						"    2LEU    OC1   37   1.182   1.914   0.613" +
//						"    2LEU    OC2   38   1.081   2.018   0.465" +
//						"   6.31500   8.35900   5.30868   0.00000   0.00000   0.00000   0.00000  -0.87313   0.00000";
		

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

	public void updateViewerCoord(List<AbstractParticle> particles) {
		
		for(AbstractParticle p: particles){
			Vector3D position = p.getPosition();
	    	viewer.ms.setAtomCoord((p.getGUID()-1), (float)position.x*10, (float)position.y*10, (float)position.z*10);
	    }
		viewer.refresh(3, "Simulation");
	}
	
	public void setMouseState(int x, int y, int mode) {
	}

}
