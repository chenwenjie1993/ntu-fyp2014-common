package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;


import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.DISTANCE;
import sg.edu.ntu.aalhossary.fyp2014.physics_engine.core.Units.MASS;

public class Atom extends sg.edu.ntu.aalhossary.fyp2014.common.Atom implements ActiveParticle{

	private double radius;
	private boolean print_flag = false;
	private double mass = 0, atomicRadius=0, covalentRadius=0, vdwRadius=0;
	private int valence = 0;
	private String atomicSymbol;
	
	public Atom(String atomicSymbol) throws Exception{
		super();
		this.fetchAtomicData(atomicSymbol);
		this.setMass(this.mass);
		this.setRadius(vdwRadius);
		this.setNetCharge(valence);
		this.boundingPrimitive = new BoundingSphere(radius, position);
		this.atomicSymbol = atomicSymbol;
		
	}
	
	public void setNetCharge(int charge){
		this.netCharge = charge;
	}
	
	private void setRadius (double radius){
		this.radius = radius;
	}
	
	public void setPrintFlag(boolean flag){
		this.print_flag = flag;
	}
	
	public void integrate(double duration) {
			
		// Calculate total acceleration without updating the original ( a = F /m )
		Vector3D currentAcceleration = new Vector3D (acceleration.x, acceleration.y, acceleration.z, acceleration.metric);
		currentAcceleration.addScaledVector(forceAccumulated, inverseMass);
		
		// Update current velocity (v = a*t)
		Vector3D initialVelocity = new Vector3D (velocity.x, velocity.y, velocity.z, velocity.metric);
		velocity.addScaledVector(currentAcceleration, duration);
		
		// Update current position (s = u*t + 0.5*a*t*t)
		position.addScaledVector(initialVelocity, duration);
		position.addScaledVector(currentAcceleration, duration * duration /2);
		
		// Clear forces
		clearAccumulator();
		
		// Update the centre of the boundingPrimitive 
		boundingPrimitive.updateCentre(position.x, position.y, position.z, position.metric);
	
	}
	
	public Vector3D calculateVelocityChange(double other_mass, Vector3D other_velocity, double COEFFICIENT_OF_RESTITUTION){
		// v1 = u1*(m1-m2) + 2*m2*u2 / m1+m2
		Vector3D temp = new Vector3D();
		double mass = 1/this.inverseMass;
		double metricDiff = other_velocity.metric - velocity.metric;
		
		if(metricDiff!=0)
			other_velocity.scale(Math.pow(10, metricDiff));
		

		// 0 for inelastic collisions, 1 for elastic collisions
		temp.x = (COEFFICIENT_OF_RESTITUTION*other_mass*(other_velocity.x-velocity.x) + mass*velocity.x + other_mass*other_velocity.x)/(mass+other_mass);
		temp.y = (COEFFICIENT_OF_RESTITUTION*other_mass*(other_velocity.y-velocity.y) + mass*velocity.y + other_mass*other_velocity.y)/(mass+other_mass);
		temp.z = (COEFFICIENT_OF_RESTITUTION*other_mass*(other_velocity.z-velocity.z) + mass*velocity.z + other_mass*other_velocity.z)/(mass+other_mass);
		temp.metric = velocity.metric;
		
		return temp;
	}
	
	public String getAtomicSymbol() { 
		return atomicSymbol;
	}

	private void fetchAtomicData (String atomicSymbol) throws Exception{
	
		int index = Arrays.asList(Init.periodicTable).indexOf(atomicSymbol);
		
		if(index == -1){
			throw new Exception("Invalid atomic symbol");
		}
		
		NumberFormat formatter = new DecimalFormat("000");
		
		String html = "http://periodictable.com/Elements/" + formatter.format(index+1) + "/data.html";
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
			if(print_flag) {
				System.out.println(output);
				System.out.println("Mass is " + mass + " kg.");
				System.out.println("Valence is " + valence);
				System.out.println("Atomic radius is " + atomicRadius + " m.");
				System.out.println("Covalent radius is " + covalentRadius + " m.");
				System.out.println("VdW radius is " + vdwRadius + " m.");
			}
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
