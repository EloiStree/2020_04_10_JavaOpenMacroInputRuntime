package be.eloistree.openmacroinput;

import java.util.ArrayList;

public class KeyEventIdPool {

	public ArrayList<KeyEventId> keys = new ArrayList<KeyEventId>();
	public KeyEventIdPool(String shortcutTable) {
		
		if(shortcutTable==null  || shortcutTable.length()<=0)
			keys=KeyEventId.getKeysFromDefaultTextTable();
		else
			keys=KeyEventId.getKeysFromTextTable(shortcutTable);	
	}
	public String [] getAllShortcutRegistered() {
		String [] result = new String[ keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			result [i]= keys.get(i).GetShortcutName();
		}
		return result;
		
	}
	public String [] getAllNameRegistered() {
		String [] result = new String[ keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			result [i]= keys.get(i).GetShortcutName();
		}
		return result;
		
	}
	public ArrayList<KeyEventId> getRefToKeys() {
		return keys;
	}
}
