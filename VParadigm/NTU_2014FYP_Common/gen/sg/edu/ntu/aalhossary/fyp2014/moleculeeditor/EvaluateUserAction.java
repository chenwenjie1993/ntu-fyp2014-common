package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor;

import java.util.ArrayList;
import java.util.List;

import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.HetatomImpl;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.UserActionType;

public class EvaluateUserAction {

	protected JmolDisplay jmolDisplay;
	
	public EvaluateUserAction(JmolDisplay jmolDisplay) {
		this.jmolDisplay = jmolDisplay;
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
			System.out.println("Entering paste function");
			findSelectedAtom(UserActionType.PASTE);
			return;
		default:
			System.out.println("Entering no function");
		}
	}

	private void findSelectedAtom(UserActionType userAction) {
		ArrayList<Atom> atomList = jmolDisplay.getSelectedAtoms();
		jmolDisplay.selectedTag = userAction;
		if(userAction.compareTo(UserActionType.DELETE)==0 || userAction.compareTo(UserActionType.CUT)==0){
			for(int i=0;i<atomList.size();i++){
				Chain chain = jmolDisplay.getStructure().getChain(atomList.get(i).getChainPosition());
				for(Group g: chain.getAtomGroups()){
					if ( g instanceof org.biojava.bio.structure.AminoAcid ){
						List<org.biojava.bio.structure.Atom> atomsInRes = ((org.biojava.bio.structure.AminoAcid)g).getAtoms();
						for(int j=0;j<atomsInRes.size();j++){
							if(atomsInRes.get(j).getPDBserial() == atomList.get(i).getAtomSeqNum())
								doDelete(atomsInRes, j, atomsInRes.get(j).getPDBserial());
						}
					}
					else if (g instanceof HetatomImpl){
						List<org.biojava.bio.structure.Atom> indivAtom = ((HetatomImpl)g).getAtoms();
						for(int j=0;j<indivAtom.size();j++){
							if(indivAtom.get(j).getPDBserial() == atomList.get(i).getAtomSeqNum())
								doDelete(indivAtom, j, indivAtom.get(j).getPDBserial());
						}
					}				
				}
			}
		}
		else if(userAction.compareTo(UserActionType.PASTE)==0){
			System.out.println("Paste function not done yet");
		}
		else
			System.out.println("Function copied");
	}

	private void doDelete(List<org.biojava.bio.structure.Atom> atom, int pos, int pdBserial) {
		//atom = atom; // set atom in own model to null.
		// reset structure for jmol to display
		//jmolDisplay.setStructure(jmolDisplay.getStructure());
		Structure struct = jmolDisplay.getStructure();
		atom.remove(pos);
		jmolDisplay.evaluateString("refresh"); // refresh not deleting atom without hiding
		// never comment "hide atom" script. atom deleted not tested fully
		//jmolDisplay.evaluateString("hide atomno=" + pdBserial);
		System.out.println("atom #" + pdBserial + " deleted");
	}

	private void doPaste(){
		
	}
}
