package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum;

public enum AtomType {
	H("Hydrogen", 1.008, 1), C("Carbon", 12.011, 6), 
	N("Nitrogen", 14.007, 7), O("Oxygen", 15.999, 8), 
	F("Fluorine", 18.9984, 9), P("Phosphorus", 30.9738, 15),
	S("Sulfur", 32.06, 16), I("Iodine", 126.9045, 53);
	
	private final String name;
	private final double atomicMass;
	private final int atomicNum;
	
	AtomType(String name, double atomicMass, int atomicNum){
		this.name = name;
		this.atomicMass = atomicMass;
		this.atomicNum = atomicNum;
	}
	
	public String getAtomName(){
		return name;
	}
	
	public double getAtomicMass(){
		return atomicMass;
	}
	
	public int getAtomicNum(){
		return atomicNum;
	}
}
