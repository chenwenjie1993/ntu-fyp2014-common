package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.*;

import org.biojava.bio.structure.Group;
import org.biojava.bio.structure.HetatomImpl;

/**
 * @author Xiu Ting
 *
 */
public class Chain extends sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle {

	protected String name;
	protected int position;
	protected ArrayList<Residue> residues;
	protected ArrayList<Atom> atomSeq;
	
	public Chain() {
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
					Atom atom = new Atom();
					atom.setParent(this);
					atom.setSymbol(atomList.get(i).getName());
					atom.setChainSeqNum(((HetatomImpl)g).getResidueNumber().getSeqNum());
					atom.setAtomSeqNum(atomList.get(i).getPDBserial());
					atomSeq.add(atom);
				}
			}
		}
	}

	@Override
	public Atom getAtom(int pos) {
		// search within residue
		for(int i=0;i<residues.size();i++){
			if(residues.get(i).getAtom(pos)!=null)
				return residues.get(i).getAtom(pos);
		}
		// then search within indiv atom in chain
		for(int i=0;i<atomSeq.size();i++){
			if(atomSeq.get(i).atomSeqNum==pos)
				return atomSeq.get(i);
		}
		return null;
	}

}