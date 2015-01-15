package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

public class STRIDE_Output {
	
	 String prediction;
	 String chain;
	 String start;
	 String end;
	
	 public void Rpred (String Rpred){
		 prediction = Rpred;
	 }
	 public void Rchain (String Rchain){
		 chain = Rchain;
	 }
	 public void Rstart(String one, String two){
		 
		 start = one.concat(two);
	 }
	 public void Rend(String one, String two){
		end = one.concat(two); 
		 
	 }
	 public void printRegion(){
		 System.out.println("Prediction:"+prediction+"	Chain:"+chain+"		Start:"+start+"		End:"+ end);	 
	 }
	 
	 public String toString(){
		 String string=null;
		 string = "Prediction:"+prediction+"	Chain:"+chain+"		Start:"+start+"		End:"+end+""; 
		 return string;
	 }

}
