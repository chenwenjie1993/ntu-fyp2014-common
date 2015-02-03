package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.util.ArrayList;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Bond;

public class AtomToMMType {

	public static String setMMType(Atom atom) {
		switch(atom.getElementSymbol()){
		case "N" : if(atom.getBond().size()==4)
					return "NR+";
		case "C" : return checkCarbonAtomBond(atom);
		case "O" : return checkOxygenAtomBond(atom);
		case "H" : return checkHydrogenAtomBond(atom);
		}
		return null;
		
	}

	private static String checkOxygenAtomBond(Atom atom) {
		ArrayList<Bond>bonds= atom.getBond();
		int countO=0, countC=0, countN=0, countH=0;
		if(bonds.size()==2){
			for(int i=0;i<2;i++){
				String atm1 = bonds.get(i).getAtom1().getElementSymbol();
				String atm2 = bonds.get(i).getAtom2().getElementSymbol();
				if((atm1.compareTo("O")==0 && atm2.compareTo("H")==0)||
					(atm1.compareTo("H")==0 && atm2.compareTo("O")==0)){
						countH++;
				}
				else if((atm1.compareTo("O")==0 && atm2.compareTo("C")==0)||
						(atm1.compareTo("C")==0 && atm2.compareTo("O")==0)){
					countC++;
				}
				else if((atm1.compareTo("O")==0 && atm2.compareTo("N")==0)||
						(atm1.compareTo("N")==0 && atm2.compareTo("O")==0)){
					countN++;
				}
				
				if(countH==2)
					return "OR";
				else if(countC==1 && (bonds.get(i).getAtom1().getmmType()==3||bonds.get(i).getAtom2().getmmType()==3))
					return "OC=O";
				else
					return "ON=O";
			}
		}
		if(bonds.size()==1){
			for(int i=0;i<1;i++){
				String atm1 = bonds.get(i).getAtom1().getElementSymbol();
				String atm2 = bonds.get(i).getAtom2().getElementSymbol();
				if(((atm1.compareTo("O")==0 && atm2.compareTo("C")==0)||
						(atm1.compareTo("C")==0 && atm2.compareTo("O")==0))&&
						(bonds.get(i).getAtom1().getmmType()==34||bonds.get(i).getAtom2().getmmType()==34)){
					return "O=C";
				}
				else if(((atm1.compareTo("O")==0 && atm2.compareTo("N")==0)||
						(atm1.compareTo("N")==0 && atm2.compareTo("O")==0))){
						return "O=CN";
				}
			}
		}
		return "O=C";
	}

	private static String checkHydrogenAtomBond(Atom atom) {
		ArrayList<Bond>bonds= atom.getBond();
		for(int i=0;i<1;i++){
			String atm1 = bonds.get(i).getAtom1().getElementSymbol();
			String atm2 = bonds.get(i).getAtom2().getElementSymbol();
			if((atm1.compareTo("C")==0 && atm2.compareTo("H")==0)||
				(atm1.compareTo("H")==0 && atm2.compareTo("C")==0)){
				return"HC";
			}
			else if(((atm1.compareTo("H")==0 && atm2.compareTo("N")==0)||
					(atm1.compareTo("N")==0 && atm2.compareTo("H")==0))&&
					(bonds.get(i).getAtom1().getmmType()==34||bonds.get(i).getAtom2().getmmType()==34)){
				return "HNR+";
			}
			else if(((atm1.compareTo("H")==0 && atm2.compareTo("O")==0)||
					(atm1.compareTo("O")==0 && atm2.compareTo("H")==0))){
				if((bonds.get(i).getAtom1().getmmType()==6||bonds.get(i).getAtom2().getmmType()==6))
					return "HOR";
				else
					return "HO";
			}
			
		}
		return "HNR+";
	}

	private static String checkCarbonAtomBond(Atom atom) {
		ArrayList<Bond>bonds= atom.getBond();
		int countO=0, countC=0, countN=0, countH=0;
		if(bonds.size()==3){
			for(int i=0;i<3;i++){
				String atm1 = bonds.get(i).getAtom1().getElementSymbol();
				String atm2 = bonds.get(i).getAtom2().getElementSymbol();
				if(atm1.compareTo("C")==0 && atm2.compareTo("C")==0){
					countC++;
				}
				else if((atm1.compareTo("C")==0 && atm2.compareTo("O")==0)||
						(atm1.compareTo("O")==0 && atm2.compareTo("C")==0)){
					countO++;
				}
				else if((atm1.compareTo("C")==0 && atm2.compareTo("H")==0)||
						(atm1.compareTo("H")==0 && atm2.compareTo("C")==0)){
					countH++;
				}
				else if((atm1.compareTo("C")==0 && atm2.compareTo("N")==0)||
						(atm1.compareTo("N")==0 && atm2.compareTo("C")==0)){
					countN++;
				}
				
				if(countC==3)
					return "C=C";
				else if((countC==1 && countO==1 && countN==1))
					return "C=ON";
				else if((countC==2 && countO==1)||(countH==1 && countO==1 && countC==1))
					return "C=OR";
				else if(countO==2)
					return "COO";
				else if(countO==1)
					return "C=O";
			}
		}
		if(bonds.size()==4){
			for(int i=0;i<4;i++){
				return "CR";
			}
		}
		return null;
	}

}
