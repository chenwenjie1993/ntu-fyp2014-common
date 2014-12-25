package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;


import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.DISTANCE;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.MASS;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.Atom implements ActiveParticle{

	private double radius; 
	private double mass = 0, atomicRadius=0, covalentRadius=0, vdwRadius=0;
	private int valence = 0;
	
	public Atom(String atomicNumber){
		super();
		this.fetchAtomicData(atomicNumber);
		this.setMass(this.mass);
		this.setRadius(vdwRadius);
		this.setNetCharge(valence);
		this.boundingPrimitive = new BoundingSphere(radius, position);
	}
	
	public void setNetCharge(int charge){
		this.netCharge = charge;
	}
	
	private void setRadius (double radius){
		this.radius = radius;
	}

	private void fetchAtomicData (String atomicNumber){
		String html = "http://periodictable.com/Elements/" + atomicNumber + "/data.html";
		String output = "";
		boolean flag = true;
		
		try{
			Document doc = Jsoup.connect(html).timeout(0).get();	// set timeout to zero IMPORTANT!!!
			
			// The website does not provide a specific header div/class id for mining. 
			// Absolute positioning is used. 
			
			Element table1 = doc.select("table").get(7);
			Elements rows1 = table1.select("tr"); 
			
			for (int i=0; i < rows1.size(); i++) {
		        Element row = rows1.get(i);
		        Elements columns = row.select("td");
		        
		        if(columns.first().text().equals("Atomic Weight"))
		        	mass = Double.parseDouble(columns.last().text()) * MASS.amu.value();		//mass in amu, 1 amu = 1.66053892e-24 grams
		        
		        if(columns.first().text().equals("Valence"))
		        	valence = Integer.parseInt(columns.last().text());	
		        
		        if(columns.first().text().equals("NFPA Label") && columns.get(1).text().equals(""))
		        	flag = false;
		        
		       // if(columns.size()>1)	output += columns.get(0).text() + ": " + columns.get(1).text() + "\n";
		    }
			
			Element table2;
			if(flag)
				table2 = doc.select("table").get(9);
			else
				table2 = doc.select("table").get(10);
			Elements rows2 = table2.select("tr");
			
			for (int j=0; j<rows2.size(); j++){
				Element row = rows2.get(j);
			    Elements columns = row.select("td");
			     
			     // All radii are in pm.
			     
			     if(columns.first().text().equals("Atomic Radius"))
			    	 atomicRadius = Double.parseDouble(columns.last().text().split(" ")[0]) * DISTANCE.pm.value();
			     
			     if(columns.first().text().equals("Covalent Radius"))
			    	 covalentRadius = Double.parseDouble(columns.last().text().split(" ")[0]) * DISTANCE.pm.value();
			     
			     if(columns.first().text().equals("Van der Waals Radius")) {
			    	 vdwRadius = Double.parseDouble(columns.last().text().split(" ")[0]) * DISTANCE.pm.value();	
			    	break;
			     }
			     
			     // disable break and enable the following code for full data from web
			 //  if(columns.size()>1)	output += columns.get(0).text() + ": " + columns.get(1).text() + "\n";
			     
			}
			
			System.out.println(output);
			System.out.println("Mass is " + mass + " kg.");
			System.out.println("Valence is " + valence);
			System.out.println("Atomic radius is " + atomicRadius + " m.");
			System.out.println("Covalent radius is " + covalentRadius + " m.");
			System.out.println("VdW radius is " + vdwRadius + " m.");
		}
		catch (IOException e){
			System.out.println("ERROR: " + e.getMessage());
		}
	}
	

	@Override
	public float getStretchibility() {
		return 0;
	}
}
