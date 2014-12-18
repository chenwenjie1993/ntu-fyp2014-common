package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.userenum;

public enum BondType {
	AMIDE("AMIDE"), ESTER("ESTER"), 
	AMINE("AMINE"), UREA("UREA"), 
	ETHER("ETHER"), OLEFIN("OLEFIN"),
	QUARTERNITRO("QUARTERNITRO"), ARONITRO_ALICARBON("ARONITRO_ALICARBON"), 
	LACNTIRO_ALICARBON("LACNTIRO_ALICARBON"), AROCARBON_AROCARBON("AROCARBON_AROCARBON"), 
	SULPHONAMIDE("SULPHONAMIDE"), COVALENT("COVALENT");
	
	private final String name;
	BondType(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
}