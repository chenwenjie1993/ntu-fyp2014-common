package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

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

	protected UpdateRegistry mediator;
	
	public EvaluateUserAction(UpdateRegistry mediator) {
		this.mediator = mediator;
	}

	public void evaluateAction(String script) {
		System.out.println("Evaluating script: " + script);
		script = script.toLowerCase();
		String action = script.substring(4, script.length()-1);
		switch(action){
		case "delete":
			System.out.println("Entering delete function");
			findSelectedAtom(UserActionType.DELETE);
			return;
		case "copy":
			System.out.println("Entering copy function");
			findSelectedAtom(UserActionType.COPY);
			return;
		case "cut":
			System.out.println("Entering cut function");
			findSelectedAtom(UserActionType.CUT);
			return;
		case "paste":
			//JmolViewer viewer = jmolDisplay.getViewer();
			//System.out.println(viewer.getAtomPoint3f(10).x+  " "+ viewer.getAtomPoint3f(10).y + " " +viewer.getAtomPoint3f(10).z);
			//findSelectedAtom(UserActionType.PASTE);
			return;
		default:
			System.out.println("Entering no function");
		}
	}

	private void findSelectedAtom(UserActionType userAction) {
		ArrayList<Atom> atomList = mediator.getSelectedAtoms();
		if(userAction.compareTo(UserActionType.DELETE)==0 || userAction.compareTo(UserActionType.CUT)==0){
			for(int i=0;i<atomList.size();i++){
				String key = atomList.get(i).getModelName();
				int currentModelIndex = mediator.viewer.getDisplayModelIndex(); 
				mediator.modelList.get(currentModelIndex).getAtomHash().remove(key);
				mediator.viewer.evalString("delete atomno=" + (atomList.get(i).getAtomSeqNum()));
			}
		}
		else if(userAction.compareTo(UserActionType.PASTE)==0){
			System.out.println("Paste function not done yet");
		}
		else
			System.out.println("Function copied");
	}
}
