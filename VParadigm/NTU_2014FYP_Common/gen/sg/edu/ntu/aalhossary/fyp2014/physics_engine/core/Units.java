package sg.edu.ntu.aalhossary.fyp2014.physics_engine.core;

import java.math.BigInteger;

public class Units {
	public enum DISTANCE {
		m (1e0), mm (1e-3), μm (1e-6), nm (1e-9), A(1e-10), pm (1e-12), fm(1e-15), am (1e-18);
		private double value;
		
		private DISTANCE (double value){
			this.value = value;
		}
		
		double value(){
			return value;
		}
		
		double convert(DISTANCE from, DISTANCE to){
			return from.value/to.value;
		}
	}
	
	public enum MASS {
		amu (1.66053892e-27), g (1e-3), kg (1e0);
		private double value;
		
		private MASS (double value){
			this.value = value;
		}
		
		double value(){
			return value;
		}
	}

	public enum TIME {
		s (1e0), ms (1e-3), μs (1e-6), ns (1e-9), ps (1e-12), fs (1e-15), as (1e-18), zs(1e-21);
		private double value;
		
		private TIME (double value){
			this.value = value;
		}
		
		double value(){
			return value;
		}
	}
}
