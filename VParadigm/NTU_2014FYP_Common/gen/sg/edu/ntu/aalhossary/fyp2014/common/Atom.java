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
	protected Interaction interaction;
	protected float[] coordinates;
	protected Particle parent;	// can be Residue or Chain
	protected ArrayList<Bond> bonds;
	protected double vdw_radius;	// waiyan: jmol should have vdw_radius?
	
	public Atom() {
		bonds = new ArrayList<Bond>();
		coordinates = new float[3]; 
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

	public void setBond(Bond bond) {
		bonds.add(bond);
	}
	
	public ArrayList<Bond> getBond(){
		return bonds;
	}
	
	public String getModelName(){
		if(parent instanceof Chain){
			return ((Chain) parent).getModelName();
		}
		else if(parent instanceof Residue){
			return ((Residue) parent).getModelName();
		}
		else{
			return null; // TODO: add in exceptions
		}
	}
	
	public void setVdWRadius(double vdw_radius){
		this.vdw_radius = vdw_radius;
	}
	
	public double getVdWRadius(){
		return vdw_radius;
	}
	
	public float[] getCoordinates(){
		return coordinates;
	}

	public void setCoordinates() {
		if(coordinates[0]==0.00f && coordinates[1]==0.00f && coordinates[2]==0.00f){
		coordinates[0] = (float) position.x;
		coordinates[1] = (float) position.y;
		coordinates[2] = (float) position.z;
		}
	}
	
	public void setCoordinates(double[] coord) {
		coordinates[0] = (float) coord[0];
		coordinates[1] = (float) coord[1];
		coordinates[2] = (float) coord[2];
	}
}