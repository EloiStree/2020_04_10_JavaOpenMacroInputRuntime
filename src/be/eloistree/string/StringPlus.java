package be.eloistree.string;

public class StringPlus {

	public static String join(String between, String [] arrays) {

		if(arrays.length==0)return "";
		if(arrays.length==1)return arrays[0];
		String result ="";
		for (int i = 0; i < arrays.length; i++) {
			
			if(i==0)
				result= arrays[i];
			else 
				result+= between+arrays[i];
			
		}
		
		
		return result;
	}
}
