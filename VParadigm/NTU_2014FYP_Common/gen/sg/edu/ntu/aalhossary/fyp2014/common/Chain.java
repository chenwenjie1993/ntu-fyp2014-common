package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.*;

import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.HetatomImpl;

/**
 * @author Xiu Ting
 *
 */
public class Chain extends sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle {

	public String name;
	public int position;
	public ArrayList<Residue> residues;
	public ArrayList<Atom> atomSeq;
	public Molecule molecule;
	
	public Chain() {
		super();
		residues = new ArrayList<Residue>();
		atomSeq = new ArrayList<Atom>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setChainPosition(int pos){
		this.position = pos;
	}
	
	public int getChainPosition(){
		return position;
	}

	public void setChainName(String chainID) {
		this.name = chainID;
	}
	
	public ArrayList<Residue> getResidues() {
		return this.residues;
	}

	public java.util.ArrayList<Atom> getAtoms() {
		return this.atomSeq;
	}

	public void setChainSequence(org.biojava.bio.structure.Chain chain) {
		Residue res;
		for(Group g : chain.getAtomGroups()){
			if ( g instanceof org.biojava.bio.structure.AminoAcid ){
				res = new AminoAcid();
				res.setParent(this);
				res.setName(((org.biojava.bio.structure.AminoAcid)g).getPDBName());
				res.setResidueSeqNum(((org.biojava.bio.structure.AminoAcid)g).getResidueNumber().getSeqNum());
				((AminoAcid) res).setAminoChar(((org.biojava.bio.structure.AminoAcid)g).getAminoType());
				res.setAtomList(((org.biojava.bio.structure.AminoAcid)g).getAtoms());
				residues.add(res);
			}
			else if(g instanceof HetatomImpl){
				List<org.biojava.bio.structure.Atom> atomList = ((HetatomImpl)g).getAtoms();
				for(int i=0;i<atomList.size();i++){
					AbstractParticle atom = new Atom();
					((Atom)atom).setParent(this);
					((Atom)atom).setSymbol(atomList.get(i).getName());
					((Atom)atom).setElementSymbol(atomList.get(i).getElement().toString());
					((Atom)atom).setChainSeqNum(((HetatomImpl)g).getResidueNumber().getSeqNum());
					((Atom)atom).setAtomSeqNum(atomList.get(i).getPDBserial());
					((Atom)atom).setCoordinates(atomList.get(i).getCoords());
					atomSeq.add(((Atom)atom));
				}
			}
		}
	}
	
	public Molecule getParent(){
		return molecule;
	}
	
	public void setParent(Molecule molecule){
		this.molecule = molecule;
	}

	public void setAtomHash(HashMap<String, Atom> atomHash, String modelName) {
		for(int i=0;i<residues.size();i++){
			residues.get(i).setAtomHash(atomHash, modelName);
		}
		for(int i=0;i<atomSeq.size();i++){
			atomHash.put(modelName+atomSeq.get(i).atomSeqNum, atomSeq.get(i));
		}
	}

	public String getModelName() {
		return molecule.getParent().modelName;
	}

	public void setChainSequence(Atom atm) {
		atm.setParent(this);
		atm.setChainSeqNum(atomSeq.size()+1);
		int atmNum = 0;
		Model parentModel = getParent().getParent();
		for(int j=0;j<parentModel.getMolecules().size();j++){
			Molecule parent =parentModel.getMolecules().get(j); 
			for(int i=0;i<parent.getChains().size();i++){
				Chain chain = parent.getChains().get(i);
				if(chain.getResidues()!=null && chain.getResidues().size()>0){
					for(Residue res : chain.getResidues()){
						atmNum += res.getAtomList().size();
					}
				}
				if(chain.getAtoms()!=null && chain.getAtoms().size()>0){
					atmNum += chain.getAtoms().size();
				}
			}
		}
		atm.setAtomSeqNum((atmNum+1));
		atomSeq.add(atm);
	}

}