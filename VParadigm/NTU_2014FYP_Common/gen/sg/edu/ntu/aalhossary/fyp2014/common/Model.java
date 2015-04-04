package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.biojava.bio.structure.Structure;

public class Model {

	protected String modelName = null;
	protected Structure structure;
	protected ArrayList<Molecule> molecules;
	protected HashMap<String, Atom> atomHash;
	protected Bond[] bonds;
	
	public Model() {
		molecules = new ArrayList<Molecule>();
		atomHash = new HashMap<String,Atom>();
	}
	
	public ArrayList<Molecule> getMolecules() {
		return this.molecules;
	}

	public void setMolecules(ArrayList<Molecule> molecules) {
		this.molecules = molecules;
	}

	/**
	 * needs to change the name to something more descriptive
	 * 
	 * @param list
	 * @deprecated will be renamed
	 */
	public void setMolecule(List<org.biojava.bio.structure.Chain> list) {
		AbstractParticle molecule;
		molecule = new Molecule();
		molecules.add(((Molecule)molecule));
		((Molecule)molecule).setName(list.get(0).getParent().getPdbId());
		((Molecule)molecule).setParent(this);
		((Molecule)molecule).setChains(list);
		((Molecule)molecule).setAtomHash(atomHash, modelName);
	}
	
	public void setMolecule(Atom atm){
		AbstractParticle molecule;
		molecule = new Molecule();
		((Molecule)molecule).setName("");
		((Molecule)molecule).setParent(this);
		((Molecule)molecule).setChain(atm);
		((Molecule)molecule).setAtomHash(atomHash, modelName);
		molecules.add(((Molecule)molecule));
	}
	
	public void setModelName(String name){
		modelName = name;
	}

	public java.lang.String getModelName() {
		return this.modelName;
	}

	public Object[] getModelDetailList() {
		ArrayList<String> modelList = new ArrayList<String>();
		for(int i=0;i<molecules.size();i++){
			Molecule molecule = molecules.get(i);
			modelList.add(molecule.getName());
			for(int j=0;j<molecule.getChains().size();j++){
				Chain chain = molecule.getChains().get(j);
				modelList.add(chain.getName());
			}
		}
		return modelList.toArray();
	}
	
	public HashMap<String, Atom> getAtomHash(){
		return atomHash;
	}
	
	public void setBonds(ArrayList<Bond> ownbonds){
		bonds = new Bond[ownbonds.size()];
		for(int i=0;i<ownbonds.size();i++){
			bonds[i] = ownbonds.get(i);
		}
	}
	
	public Bond[] getBonds(){
		return bonds;
	}

	public void removeAtom(String key) {
		Atom atm = atomHash.get(key);
		int atomno = atm.getAtomSeqNum();
		if(atm.getParent() instanceof AminoAcid){
			AminoAcid res = (AminoAcid)atm.getParent();
			for(int i=0;i<res.getAtomList().size();i++){
				if(atm.getAtomSeqNum()==res.getAtomList().get(i).getAtomSeqNum()){
					atomHash.remove(key);
					res.getAtomList().remove(i);
				}
			}
		}
		else if(atm.getParent() instanceof Chain){
			Chain chain = (Chain)atm.getParent();
			for(int i=0;i<chain.getAtoms().size();i++){
				if(atm.getAtomSeqNum()==chain.getAtoms().get(i).getAtomSeqNum()){
					atomHash.remove(key);
					chain.getAtoms().remove(i);
				}
			}
		}
	}
}