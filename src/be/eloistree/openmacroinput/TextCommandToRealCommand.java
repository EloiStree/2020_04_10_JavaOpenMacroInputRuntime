package be.eloistree.openmacroinput;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import be.eloistree.openmacroinput.enums.PressType;

public class TextCommandToRealCommand {

	public KeyEventIdPool keysShortcut;
	
	public TextCommandToRealCommand ( KeyEventIdPool keysShortcut) {
		
	}
	
}

/**



        result = result.Replace("↕", "↕ ");
        result = result.Replace("↓", "↓ ");
        result = result.Replace("↑", "↑ ");

        result = result.Replace(" ScrollUp↕ ".ToLower(), string.Format("[[wh:{0}]]", 1));
        result = result.Replace(" ScrollDown↕ ".ToLower(), string.Format("[[wh:{0}]]", -1));
        result = result.Replace(" ClickLeft↕ ".ToLower(), "[[ms:0]]");
        result = result.Replace(" ClickRight↕ ".ToLower(), "[[ms:2]]");
        result = result.Replace(" ClickScroll↕ ".ToLower(), "[[ms:1]]");
        result = result.Replace(" ClickLeft↓ ".ToLower(), "[[mp:0]]");
        result = result.Replace(" ClickLeft↑ ".ToLower(), "[[mr:0]]");
        result = result.Replace(" ClickRight↓ ".ToLower(), "[[mp:2]]");
        result = result.Replace(" ClickRight↑ ".ToLower(), "[[mr:2]]");
        result = result.Replace(" ClickScroll↓ ".ToLower(), "[[mp:1]]");
        result = result.Replace(" ClickScroll↑ ".ToLower(), "[[mr:1]]");
        
        
       


*/