package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine.model.Vector3D;

/**
 * @author Xiu Ting
 *
 */
public class Residue extends sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle {

	protected String name;
	protected int chainSeqNum;
	protected int moleculeSeqNum;
	protected Chain chain;
	protected ArrayList<Atom> atoms;
	protected ArrayList<Integer> representativeCharges;
	protected ArrayList<Vector3D> representativePoints;
	
	public Residue() {
		super();
		name = null;
		atoms = new ArrayList<Atom>();
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Chain getParent(){
		return chain;
	}
	
	public void setParent(Chain chain) {
		this.chain = chain;
	}
	
	public int getResidueSeqNum() {
		return this.chainSeqNum;
	}

	public void setResidueSeqNum(int seqNum) {
		this.chainSeqNum = seqNum;
	}
	
	public int getChainPosition(){
		return chain.position;
	}
	
	public ArrayList<Atom> getAtomList(){
		return atoms;
	}

	public void setAtomList(List<org.biojava.bio.structure.Atom> atoms) {
		AbstractParticle atom;
		for(int i=0;i<atoms.size();i++){
			atom = new Atom();
			((Atom)atom).setParent(this);
			((Atom)atom).setSymbol(atoms.get(i).getName());
			((Atom)atom).setElementSymbol(atoms.get(i).getElement().toString());
			((Atom)atom).setChainSeqNum(chainSeqNum);
			((Atom)atom).setAtomSeqNum(atoms.get(i).getPDBserial());
			((Atom)atom).setCoordinates(atoms.get(i).getCoords());
			this.atoms.add(((Atom)atom));
		}
	}

	public void setAtomHash(HashMap<String, Atom> atomHash, String modelName) {
		for(int i=0;i<atoms.size();i++){
			atomHash.put(modelName+atoms.get(i).atomSeqNum, atoms.get(i));
		}
	}
	
	public String getModelName(){
		return chain.getModelName();
	}
}