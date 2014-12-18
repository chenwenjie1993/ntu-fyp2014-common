package sg.edu.ntu.aalhossary.fyp2014.common;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum.AtomType;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.AbstractParticle {

	protected java.lang.String name;
	protected java.lang.String symbol;
	protected double atomicMass;
	protected float atomicNum;
	protected int ionCharge;
	protected int chainSeqNum;	// position of atom in specific chain
	protected int atomSeqNum;	// position of atom in entire molecule (pdb serial #)
	protected double[] coordinates;
	protected Interaction interaction;
	protected Particle parent;	// can be Residue or Chain
	protected ArrayList<Bond> bonds;
	protected double vdw_radius;	// waiyan: jmol should have vdw_radius?
	
	public Atom() {
		bonds = new ArrayList<Bond>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(AtomType atomType) {
		this.name = atomType.getAtomName();
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public void setSymbol(String symbol){
		symbol = symbol.toUpperCase();
		this.symbol = symbol;
		// when setting symbol of atom, set the name, atomic mass and atomic number
		boolean found=false;
		for(AtomType a: AtomType.values()){
			if(a.toString().compareTo(symbol.substring(0, 1))==0){
				setName(a);
				setAtomicMass(a);
				setAtomicNumber(a);
				found=true;
			}
		}
		if(!found){
			System.out.println("[" + symbol + "] symbol is not in AtomType.java, please add or check for error");
		}
	}
	
	public Particle getParent(){
		return parent;
	}
	
	public void setParent(Particle chain) {
		this.parent = chain;
	}
	
	private void setAtomicMass(AtomType atomType) {
		atomicMass = atomType.getAtomicMass();
	}

	private void setAtomicNumber(AtomType atomType) {
		atomicNum = atomType.getAtomicNum();
	}

	public int getChainSeqNum() {
		return this.chainSeqNum;
	}

	public void setChainSeqNum(int chainSeqNum) {
		this.chainSeqNum = chainSeqNum;
	}
	
	public int getAtomSeqNum() {
		return this.atomSeqNum;
	}

	public void setAtomSeqNum(int atomSeqNum) {
		this.atomSeqNum = atomSeqNum;
	}
	
	public int getChainPosition(){
		if(parent instanceof Chain){
			return ((Chain) parent).position;
		}
		else if(parent instanceof Residue){
			return ((Residue) parent).getChainPosition();
		}
		else{
			return -1; // TODO: add in exceptions
		}
	}

	public double[] getAtomCoordinates(){
		return coordinates;
	}
	
	public void setCoordinates(double[] coords) {
		coordinates = coords;
	}

	public void setBond(Bond bond) {
		bonds.add(bond);
	}
	
	public ArrayList<Bond> getBond(){
		return bonds;
	}
	
	public void setVdWRadius(double vdw_radius){
		this.vdw_radius = vdw_radius;
	}
	
	public double getVdWRadius(){
		return vdw_radius;
	}
}