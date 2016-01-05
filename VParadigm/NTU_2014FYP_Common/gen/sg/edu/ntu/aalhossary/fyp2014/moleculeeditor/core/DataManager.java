package sg.edu.ntu.aalhossary.fyp2014.moleculeeditor.core;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.biojava.bio.structure.Structure;

import sg.edu.ntu.aalhossary.fyp2014.common.Atom;
import sg.edu.ntu.aalhossary.fyp2014.common.Chain;
import sg.edu.ntu.aalhossary.fyp2014.common.Model;
import sg.edu.ntu.aalhossary.fyp2014.common.Molecule;
import sg.edu.ntu.aalhossary.fyp2014.common.Residue;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Vector3D;

public class DataManager {

	private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MoleculeEditor.class.getName());
	private static String newline = "\n";
	private static DecimalFormat d3 = (DecimalFormat)NumberFormat.getInstance(java.util.Locale.UK);
	static{
		d3.setMaximumIntegerDigits(4);
		d3.setMinimumFractionDigits(3);
		d3.setMaximumFractionDigits(3);
	}
	private static DecimalFormat d2 = (DecimalFormat)NumberFormat.getInstance(java.util.Locale.UK);
	static {
		d2.setMaximumIntegerDigits(3);
		d2.setMinimumFractionDigits(2);
		d2.setMaximumFractionDigits(2);
	}

	public static Structure readFile(java.lang.String fileName) {
		Structure struc = null;
		
		if(fileName.endsWith(".pdb") || fileName.endsWith(".pdbqt")){
			struc = FileReader.readPDBFile(fileName); 
		}
		else if(fileName.endsWith(".mol2")){
			struc = FileReader.readMol2File(fileName);
		}
		else if(fileName.endsWith(".cif")){
			struc = FileReader.readMmcifFile(fileName);
		}
		return struc;
	}

	public static void writeFile(File file, UpdateRegistry updateReg) {
		String modelToPDB = modelToPDB(updateReg.getModelList());
		// implement writer to write to file
		FileWriter.writePDBFile(file, modelToPDB);
	}

	public static String modelToPDB(List<Model> models) {

		StringBuffer pdb = new StringBuffer();
		int numModel = models.size();
		// for each model
		for(int i=0;i<numModel;i++){
			pdb.append("MODEL      " + (i+1)+ newline);	// start for each model
			Model currModel = models.get(i);
			int numMole = currModel.getMolecules().size();
			//for each molecule
			for(int m=0;m<numMole;m++){
				Molecule currMole = currModel.getMolecules().get(m);
				int numChain = currMole.getChains().size();
				// for each Chain
				for(int j=0;j<numChain;j++){
					Chain currChain = currMole.getChains().get(j);
					int numResidue = currChain.getResidues().size();
					//for each Residue in Chain
					for(int k=0;k<numResidue;k++){
						Residue currResidue = currChain.getResidues().get(k);
						int numAtom = currResidue.getAtomList().size();
						for(int l=0;l<numAtom;l++){
							Atom currAtom = currResidue.getAtomList().get(l);
							// pdb string for each ATOM
							toPDB(currAtom, pdb);
						}
					}
					
					int numAtom = currChain.getAtoms().size();
					// for HetAtom in Chain
					for(int k=0;k<numAtom;k++){
						Atom currAtom = currChain.getAtoms().get(k);
						// pdb string for each HETATM
						toPDB(currAtom, pdb);
					}
				}
			}
			pdb.append("ENDMDL").append(newline); // end of each model
		}
		return pdb.toString();
	}
	
	// method adapted from biojava FileConvert.class
	public static void toPDB(Atom a, StringBuffer str){
		
		String record = "" ;
		if (a.getParent() instanceof Chain) {
			record = "HETATM";
		} else {
			record = "ATOM  ";
		}
		
		int    seri       = a.getAtomSeqNum()       ;
		String serial     = alignRight(""+seri,5)   ;
		
		String fullname   = alignLeft(a.getSymbol(),3)	;
		Character  altLoc = ' '        			    ;
		
		String resName;
		if(a.getParent()==null)
			resName = "TST";
		else
			resName = a.getParent().getName();
		String leftResName = alignLeft(resName,3);
		String chainID = "";
		if(a.getParent() instanceof Residue){
			chainID = ((Residue)a.getParent()).getParent().getName();
		}
		else{
			chainID = " ";
		}
		String resseq     = alignRight(""+a.getChainSeqNum(),5);
		
		String x          = alignRight(""+d3.format(a.getCoordinates()[0]),8);
		String y          = alignRight(""+d3.format(a.getCoordinates()[1]),8);
		String z          = alignRight(""+d3.format(a.getCoordinates()[2]),8);
		String occupancy  = alignRight(""+d2.format(1.00),6) ;
		String tempfactor = alignRight(""+d2.format(0.00),6);
		
		StringBuffer s = new StringBuffer();
		s.append(record);
		s.append(serial);
		s.append(" ");
		s.append(fullname);
		s.append(altLoc);
		s.append(leftResName);
		s.append(" ");
		s.append(chainID);
		s.append(resseq);
		s.append("    ");
		s.append(x);
		s.append(y);
		s.append(z);
		s.append(occupancy);
		s.append(tempfactor);
		
		String eString;
		if(a.getSymbol()==null)
			eString = " ";
		else
			eString = a.getSymbol().substring(0, 1).toUpperCase();
		str.append(String.format("%-76s%2s", s.toString(),eString));
		str.append(newline);
	}

	// method from biojava FileConvert.class
	private static String alignLeft(String input, int length){
		if(input==null){
			String spaces = "                           " ;
			return spaces.substring(0, length);
		}
		if (input.length() >= length) {
			return input;
		}

		String spaces = "                           " ;
		input += spaces.substring(0, length - input.length() );
		return input;

	}
	
	// method from biojava FileConvert.class
	private static String alignRight(String input, int length){
		if(input==null){
			String spaces = "                           " ;
			return spaces.substring(0, length);
		}
		int n = input.length();
		if ( n >= length)
			return input;

		String spaces = "                           " ;
		int diff = length - n ;
		StringBuffer s = new StringBuffer();

		s.append(spaces.substring(0,diff));
		s.append(input);

		return s.toString();
	}

	public static void readFile(String filePath, UpdateRegistry updateReg) {
		Structure struc = null;
		
		if(filePath.endsWith(".pdb")){
			struc = FileReader.readPDBFile(filePath); 
		}
		
		if(struc==null){
			logger.log(Level.INFO, "Error loading file");
			JOptionPane.showMessageDialog(new JFrame(), "Error loading file.");
		}
		else
			updateReg.loadFileToJmol(struc);
	}

	public static Structure appendModel(String filePath, UpdateRegistry updateRegistry) {
			Structure struc = null;
			if(filePath.endsWith(".pdb")){
				struc = FileReader.readPDBFile(filePath); 
			}
			if(struc==null){
				logger.log(Level.INFO, "Error loading file");
				JOptionPane.showMessageDialog(new JFrame(), "Error loading file.");
				return null;
			}
			else
				return struc;
	}

	public static void toPDB(Atom a, int i, Vector3D position, double scale, StringBuffer str) {
		String record = "" ;
		if (a.getParent() instanceof Chain) {
			record = "HETATM";
		} else {
			record = "ATOM  ";
		}
		
		int    seri       = i       ;
		String serial     = alignRight(""+seri,5)   ;
		
		String fullname   = alignLeft(a.getSymbol(),3)	;
		Character  altLoc = ' '        			    ;
		
		String resName;
		if(a.getParent()==null)
			resName = "TST";
		else
			resName = a.getParent().getName();
		String leftResName = alignLeft(resName,3);
		String chainID = "";
		if(a.getParent() instanceof Residue){
			chainID = ((Residue)a.getParent()).getParent().getName();
		}
		else{
			chainID = " ";
		}
		String resseq     = alignRight(""+a.getChainSeqNum(),5);
		
		String x          = alignRight(""+d3.format(capCoord(position.x*scale)),8);
		String y          = alignRight(""+d3.format(capCoord(position.y*scale)),8);
		String z          = alignRight(""+d3.format(capCoord(position.z*scale)),8);
		String occupancy  = alignRight(""+d2.format(1.00),6) ;
		String tempfactor = alignRight(""+d2.format(0.00),6);
		
		StringBuffer s = new StringBuffer();
		s.append(record);
		s.append(serial);
		s.append(" ");
		s.append(fullname);
		s.append(altLoc);
		s.append(leftResName);
		s.append(" ");
		s.append(chainID);
		s.append(resseq);
		s.append("    ");
		s.append(x);
		s.append(y);
		s.append(z);
		s.append(occupancy);
		s.append(tempfactor);
		
		String eString;
		if(a.getSymbol()==null)
			eString = " ";
		else
			eString = a.getSymbol().substring(0, 1).toUpperCase();
		str.append(String.format("%-76s%2s", s.toString(),eString));
		str.append(newline);
	}
	
	private static double capCoord (double coord){
		if(coord < 0)
			return Math.max(-999.000, coord);
		return Math.min(coord, +999.000);
		
	}
}