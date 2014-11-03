package sg.edu.ntu.aalhossary.fyp2014.common;


/**
 * 
 * @author Xiu Ting
 *
 */
public interface Particle {
	public default String getName(){
		return "PARTICLE";
	}
	
	public default Atom getAtom(int atomicNumber){
		return null; //TODO: null or exception
	}
}