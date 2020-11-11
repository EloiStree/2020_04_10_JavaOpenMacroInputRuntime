package be.eloistree.openmacroinput;

import java.util.ArrayList;

import be.eloistree.debug.CDebug;
import be.eloistree.openmacroinput.convertiontables.KeyEventAsString;
import be.eloistree.openmacroinput.enums.PressType;

public class KeyEventId {
	 public int m_value;
	 public String m_nameAsString;
	 public String m_nameShortcutAsString;
	 
	 public KeyEventId(String enumName, String shortcutFormat) {
		 m_nameShortcutAsString = shortcutFormat;
		 m_value = KeyEventAsString.ConvertStringToKeyEvent(enumName);
		 m_nameAsString =enumName; 
	 }
	 
	 public int GetId() {return m_value;}
	 public String GetJavaName() {return m_nameAsString; }
	 public String GetShortcutName() {return m_nameShortcutAsString; }
	 public String GetShortcutNameWhen(PressType pressType) {
		 char pType = MyUnicodeChar.stroke;
		 if(pressType== pressType.Press)pType= MyUnicodeChar.press;
		 if(pressType== pressType.Release)pType= MyUnicodeChar.release;
		 return m_nameShortcutAsString+pType; 
	 }
	 
	 public static String [] GetAllEnumNames() {
		 return KeyEventAsString.GetAllEnumNames();
	 }
	 public static boolean IsRegistered(String keyName) {
		 for (int i = 0; i < KeyEventAsString.ALLENUM.length; i++) {
			if(keyName.equalsIgnoreCase(KeyEventAsString.ALLENUM[i]))
				return true;
		}
		 return false;
	 }
	 
	 public static ArrayList<KeyEventId> getKeysFromDefaultTextTable(){
		 
		 return getKeysFromTextTable(DEFAULTNAMETOSHORTCUT);
	 } 
	 public static ArrayList<KeyEventId> getKeysFromTextTable(String text){
		 ArrayList<KeyEventId> result = new ArrayList<KeyEventId>();
		 String name="", shortcut="";
		 String [] lines = text.split("\n");
		 for (int i = 0; i < lines.length; i++) {
			String [] tokens = lines[i].split(":");
			if(tokens.length==2)
			{
				name = tokens[0].trim().toUpperCase();
				shortcut = tokens[1].trim().toUpperCase();
				if(name.length()>0 && shortcut.length()>0) {
					//System.out.println("?: "+name+">"+shortcut);
					if(IsRegistered(name)) {
						
						result .add(new KeyEventId(name, shortcut));
					}
				}
				
			}
		}
		 return result;
	 }
	 public static String GetDefaultKeysShortcutTableAsText() {return DEFAULTNAMETOSHORTCUT; }
	 
	 private static String DEFAULTNAMETOSHORTCUT=""
	 		+ "VK_NUMPAD0: NP0\r\n" + 
	 		"VK_NUMPAD1: NP1\r\n" + 
	 		"VK_NUMPAD2: NP2\r\n" + 
	 		"VK_NUMPAD3: NP3\r\n" + 
	 		"VK_NUMPAD4: NP4\r\n" + 
	 		"VK_NUMPAD5: NP5\r\n" + 
	 		"VK_NUMPAD6: NP6\r\n" + 
	 		"VK_NUMPAD7: NP7\r\n" + 
	 		"VK_NUMPAD8: NP8\r\n" + 
	 		"VK_NUMPAD9: NP9\r\n" + 
	 		"VK_0: 0\r\n" + 
	 		"VK_1: 1\r\n" + 
	 		"VK_2: 2\r\n" + 
	 		"VK_3: 3\r\n" + 
	 		"VK_4: 4\r\n" + 
	 		"VK_5: 5\r\n" + 
	 		"VK_6: 6\r\n" + 
	 		"VK_7: 7\r\n" + 
	 		"VK_8: 8\r\n" + 
	 		"VK_9: 9\r\n" + 
	 		"VK_PLUS: +\r\n" + 
	 		"VK_MINUS: -\r\n" + 
	 		"VK_MULTIPLY: *\r\n" + 
	 		"VK_DIVIDE: /\r\n" + 
	 		"VK_KP_DOWN: .\r\n" + 
	 		"VK_UP: Up\r\n" + 
	 		"VK_LEFT: Left\r\n" + 
	 		"VK_RIGHT: Right\r\n" + 
	 		"VK_DOWN: Down\r\n" + 
	 		"VK_SPACE: Space\r\n" + 
	 		"VK_CONTROL: Ctrl\r\n" + 
	 		"VK_ALT: Alt\r\n" + 
	 		"VK_ALT_GRAPH: AltGr\r\n" + 
	 		"VK_SHIFT: Shift\r\n" + 
	 		"VK_CAPS_LOCK: ShiftLock\r\n" + 
	 		"VK_TAB: Tab\r\n" + 
	 		"VK_ESCAPE: Escape\r\n" + 
	 		"VK_DELETE: Del\r\n" + 
	 		"VK_DELETE: Delete\r\n" + 
	 		"VK_ENTER: Enter\r\n" + 
	 		"VK_BACK_SPACE: BackSpace\r\n" + 
	 		"VK_WINDOWS: Window\r\n" + 
	 		"VK_A: A\r\n" + 
	 		"VK_B: B\r\n" + 
	 		"VK_C: C\r\n" + 
	 		"VK_D: D\r\n" + 
	 		"VK_E: E\r\n" + 
	 		"VK_F: F\r\n" + 
	 		"VK_G: G\r\n" + 
	 		"VK_H: H\r\n" + 
	 		"VK_I: I\r\n" + 
	 		"VK_J: J\r\n" + 
	 		"VK_K: K\r\n" + 
	 		"VK_L: L\r\n" + 
	 		"VK_M: M\r\n" + 
	 		"VK_N: N\r\n" + 
	 		"VK_O: O\r\n" + 
	 		"VK_P: P\r\n" + 
	 		"VK_Q: Q\r\n" + 
	 		"VK_R: R\r\n" + 
	 		"VK_S: S\r\n" + 
	 		"VK_T: T\r\n" + 
	 		"VK_U: U\r\n" + 
	 		"VK_V: V\r\n" + 
	 		"VK_W: W\r\n" + 
	 		"VK_X: X\r\n" + 
	 		"VK_Y: Y\r\n" + 
	 		"VK_Z: Z\r\n" + 
	 		"VK_F1:   F1\r\n" + 
	 		"VK_F2:   F2\r\n" + 
	 		"VK_F3:   F3\r\n" + 
	 		"VK_F4:   F4\r\n" + 
	 		"VK_F5:   F5\r\n" + 
	 		"VK_F6:   F6\r\n" + 
	 		"VK_F7:   F7\r\n" + 
	 		"VK_F8:   F8\r\n" + 
	 		"VK_F9:   F9\r\n" + 
	 		"VK_F10: F10\r\n" + 
	 		"VK_F11: F11\r\n" + 
	 		"VK_F12: F12\r\n" + 
	 		"VK_INSERT   : Insert\r\n" + 
	 		"VK_HOME     : Home\r\n" + 
	 		"VK_PAGE_DOWN: PageDown\r\n" + 
	 		"VK_PAGE_UP  : PageUp\r\n" + 
	 		"VK_PAUSE    : Pause\r\n" + 
	 		"VK_NUM_LOCK : NumLock	";
}



