package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.HetatomImpl;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import org.jmol.api.JmolViewer;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.UserActionType;

public class EvaluateUserAction {

	/** value for user functions
	 * 
	 */
	public static final int DELETE = 1;
	public static final int COPY = 2;
	public static final int CUT = 3;
	public static final int PASTE = 4;
	public static final int DRAGATOM = 5;
	public static final int DRAGATOMMINIMIZE = 6;
	public static final int DRAGSELECTED = 7;
	public static final int DRAGSELECTEDMINIMIZE = 8;
	public static final int MINIMIZE = 9;
	
	public static void evaluateAction(UpdateRegistry updateReg, String script) {
		System.out.println("Evaluating script: " + script);
		script = script.toLowerCase();
		String action = script.substring(4, script.length()-1);
		System.out.println("Action:" + action);
		switch(action){
		case "delete":
			System.out.println("Entering delete function");
			findSelectedAtom(updateReg, DELETE);
			return;
		case "copy":
			System.out.println("Entering copy function");
			findSelectedAtom(updateReg, COPY);
			return;
		case "cut":
			System.out.println("Entering cut function");
			findSelectedAtom(updateReg, CUT);
			return;
		case "paste":
			//JmolViewer viewer = jmolDisplay.getViewer();
			//System.out.println(viewer.getAtomPoint3f(10).x+  " "+ viewer.getAtomPoint3f(10).y + " " +viewer.getAtomPoint3f(10).z);
			findSelectedAtom(updateReg, PASTE);
			return;
		case "minimizeonly":
			System.out.println("Entering drag Atom function");
			dragModeSetting(updateReg, MINIMIZE);
			return;
		case "dragatom":
			System.out.println("Entering drag Atom function");
			dragModeSetting(updateReg, DRAGATOM);
			return;
		case "dragminimize":
			dragModeSetting(updateReg, DRAGATOMMINIMIZE);
			return;
		case "dragselected":
			dragModeSetting(updateReg, DRAGSELECTED);
			return;
		case "dragselectedminimize":
			dragModeSetting(updateReg, DRAGSELECTEDMINIMIZE);
			return;
		default:
			return;
		}
	}

	private static void dragModeSetting(UpdateRegistry updateReg, int dragMode) {
			updateReg.dragMode = DRAGATOM;
			if(dragMode==MINIMIZE){
				System.out.println("Evaluating set picking dragatom");
				updateReg.minimizeModel();
			}
			else if(dragMode==DRAGATOM){
				updateReg.atomselected=true;
				System.out.println("Evaluating set picking dragatom");
				updateReg.getViewer().evalString("set picking DRAGATOM");
			}
			else if(dragMode==DRAGSELECTED){
				updateReg.atomselected=false;
				System.out.println("Evaluating set picking dragatom");
				updateReg.getViewer().evalString("set picking DRAGATOM");
			}
			else if(dragMode==DRAGATOMMINIMIZE || dragMode==DRAGSELECTEDMINIMIZE){
				updateReg.atomselected=true;
				System.out.println("Evaluating set picking dragatom");
				updateReg.getViewer().evalString("set picking DRAGATOM");
				updateReg.setMinimizeMode(true);
			}
	}

	public static void findSelectedAtom(UpdateRegistry updateReg, int mode) {
		ArrayList<Atom> atomList = updateReg.getSelectedAtoms();
		int[] atomNo = new int[atomList.size()];
		for(int i=0;i<atomNo.length;i++){
			atomNo[i] = atomList.get(atomNo.length-1-i).getAtomSeqNum();
		}
		int currentModelIndex = updateReg.getViewer().getDisplayModelIndex(); 
		if(mode==DELETE ||mode==CUT||mode==COPY){
			for(int i=0;i<atomList.size();i++){
				String key = atomList.get(i).getModelName()+atomList.get(i).getAtomSeqNum();
				if(mode==CUT || mode==COPY)
					updateReg.copyAtoms(currentModelIndex, key);
				if(mode==DELETE || mode==CUT)
					updateReg.deleteAtoms(currentModelIndex, key, atomList.get(i).getAtomSeqNum());
			}
		}
		else if(mode==PASTE){
			updateReg.pasteSavedAtoms(updateReg.getViewer().getDisplayModelIndex());
		}
		else{
			System.out.println("Invalid Operation");
		}
		//reset model atom seq number will give weird molecule.
		/*for(Atom atm1 : updateReg.getModelList().get(currentModelIndex).getAtomHash().values()){
			for(int i=0;i<atomNo.length;i++){
				if(atm1.getAtomSeqNum()>atomNo[i])
					atm1.setAtomSeqNum((atm1.getAtomSeqNum()-1));
			}
		}*/
	}
}
