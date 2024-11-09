package be.eloistree.openmacroinput;

import java.util.ArrayList;

public class IntegerCommandPool {
	public ArrayList<IntegerToCommand> keys = new ArrayList<IntegerToCommand>();
	
	public IntegerCommandPool(String poolFromText) {
		
		if(poolFromText==null  || poolFromText.length()<=0)
			keys=IntegerToCommand.getKeysFromDefaultTextTable();
		else
			keys=IntegerToCommand.getKeysFromTextTable(poolFromText);	
	}
	public String [] getAllCommands() {
		String [] result = new String[ keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			result [i]= keys.get(i).getCommand();
		}
		return result;
		
	}
	public String [] getAllNameRegistered() {
		String [] result = new String[ keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			result [i]= keys.get(i).getIntegerAsString();
		}
		return result;
		
	}
	public ArrayList<IntegerToCommand> getRefToKeys() {
		return keys;
	}
	public ArrayList<String> getCommandsFromIntegerName(String integerName) {		
	  integerName=	integerName.trim();
	  //System.out.println("ll "+integerName);
	  ArrayList<String> cmds = new ArrayList<String>();

	  //System.out.println("ll "+cmds);
	  for(int i=0; i<keys.size(); i++) {
		  
	  	if(integerName.equalsIgnoreCase(keys.get(i).m_nameAsString)){
	  		cmds.add(keys.get(i).m_command);
	  		//System.out.println("lm "+keys.get(i));
	  	}
	  }
	  		
	  return cmds;
	}
}
