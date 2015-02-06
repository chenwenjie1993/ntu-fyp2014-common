package sg.edu.ntu.aalhossary.fyp2014.ss_predictor;

public class STRIDE_Output {
	
	 String prediction;
	 String chain;
	 String start;
	 String startpos;
	 String end;
	 String endpos;
	
	 public void Rpred (String Rpred){
		 prediction = Rpred;
	 }
	 public void Rchain (String Rchain){
		 chain = Rchain;
	 }
	 public void Rstart(String Rstart){
		 
		 start = Rstart;
	 }
	 
	 public void RstartPos(String RstartPos){
		 startpos = RstartPos;
	 }
	 public void Rend(String Rend){
		end = Rend;
		 
	 }
	 public void RendPos(String RendPos){
		 endpos = RendPos;
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
