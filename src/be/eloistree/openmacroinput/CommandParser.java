package be.eloistree.openmacroinput;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.eloistree.openmacroinput.command.PastCommand;
import be.eloistree.openmacroinput.OsUtility.OS;
import be.eloistree.openmacroinput.command.CopyPastCommand;
import be.eloistree.openmacroinput.command.EmbraceCommand;
import be.eloistree.openmacroinput.command.EmbracePerLineCommand;
import be.eloistree.openmacroinput.command.ImageURLToClipboardCommand;
import be.eloistree.openmacroinput.command.KeyStrokeCommand;
import be.eloistree.openmacroinput.command.KillTheProgramCommand;
import be.eloistree.openmacroinput.command.MouseClickCommand;
import be.eloistree.openmacroinput.command.MouseScrollCommand;
import be.eloistree.openmacroinput.command.OpenURLCommand;
import be.eloistree.openmacroinput.command.MouseClickCommand.MouseButton;
import be.eloistree.openmacroinput.command.MouseMoveCommand.MoveType;
import be.eloistree.openmacroinput.command.MouseMoveCommand.MoveTypeValue;
import be.eloistree.openmacroinput.command.MouseMoveCommand;
import be.eloistree.openmacroinput.command.RobotCommand;
import be.eloistree.openmacroinput.command.UnicodeCommand;
import be.eloistree.openmacroinput.command.WindowCmdLineToExecuteCommand;
import be.eloistree.openmacroinput.enums.PressType;





public class CommandParser {
	public class RobotCommandRef {
		public RobotCommand ref;
	}

	public KeyEventIdPool m_userShortcut;

	public CommandParser(KeyEventIdPool keysUserShortcut) {
		m_userShortcut = keysUserShortcut;
	}

	public ArrayList<RobotCommand> getCommandsFrom(String packageToProcess) {
		ArrayList<RobotCommand> result = new ArrayList<RobotCommand>();
		RobotCommandRef robotCommand = new RobotCommandRef();
		
		if (isItSomeShortcutCommandsV2(packageToProcess, result)) {}
		else if (isItCopyPastCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} 
		else if (isItClipboardCommand(packageToProcess, result)) {}
		else if (isItKeyStrokeCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItUnicodeCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItMouseClickCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItMouseMoveCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItMouseScrollCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItExitOrStopCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		}  else if (isItOpenWebPageCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItEmbraceCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItEmbracePerLineCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		}else if (isItImageToClipboardCommand(packageToProcess, robotCommand)) {
			result.add(robotCommand.ref);
		} else if (isItWindowCommandLineCommand(packageToProcess, result)) {
			
		}else if (isItKeyComboStrokeCommand(result,packageToProcess )) {
			
		}
		
		return result;
	}

	private boolean isItEmbraceCommand(String packageToProcess, RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("em:")))
			return false;
		if (packageToProcess.length() <= 3)
			return false;
		String content = packageToProcess.substring(3);
		String[] tokens = content.split(""+MyUnicodeChar.split);
		if (tokens.length == 2) {
			robotCommand.ref = new EmbraceCommand(tokens[0], tokens[1]);
			return true;
		} else {

			tokens = content.split(":");
			if (tokens.length == 2) {
				robotCommand.ref = new EmbraceCommand(tokens[0], tokens[1]);
				return true;
			}
		}
		return false;

	}

	private boolean isItEmbracePerLineCommand(String packageToProcess, RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("empl:")))
			return false;
		if (packageToProcess.length() <= 5)
			return false;
		String content = packageToProcess.substring(5);
		String[] tokens = content.split(""+MyUnicodeChar.split);
		if (tokens.length == 2) {
			robotCommand.ref = new EmbracePerLineCommand(tokens[0], tokens[1]);
			return true;
		} else {

			tokens = content.split(":");
			if (tokens.length == 2) {
				robotCommand.ref = new EmbracePerLineCommand(tokens[0], tokens[1]);
				return true;
			}
		}
		return false;

	}

	//NEED TO BE IMPROVE BUT I DONT MASTER ENOUGH REGEX
	private static String regexText = "\\[\\[[^\\[\\]]+\\]\\]";
	private static String regexCommand = "\\w+["+MyUnicodeChar.press+MyUnicodeChar.release+MyUnicodeChar.stroke+"]";

	private boolean isItSomeShortcutCommandsV2(String packageToProcess, ArrayList<RobotCommand> result) {
		if (!(packageToProcess.startsWith("sc:")))
			return false;
		if (packageToProcess.length() <= 3)
			return false;
		String content = packageToProcess.substring(3);
		//Pattern.compile(String.format("(%s)|(%s)", regexCommand,regexText));

		System.out.println(">>>"+content);
		content= replaceComboStrokeByKeyPressions(content);
		System.out.println(">>>"+content);
		
			ArrayList<String> shortcuts = FindArrowShortcutWithText(content);
			for (int i = 0; i < shortcuts.size(); i++) {
				String shortcut = shortcuts.get(i);
				
				if(shortcut.matches(regexText))
				{
					result.add(new PastCommand(shortcut.substring(2,shortcut.length()-2)));
				}else if(shortcut.matches(regexCommand)) {
					
					
					ConvertTextToKeyStroke(result, shortcut);
				}
				
			}
	
	
		return false;
	}

	//https://regexr.com/554uh
	private static String comboStroke ="([A-Za-z0-9"+MyUnicodeChar.arrows()+"]+)([\\s]*\\+[\\s]*[A-Za-z0-9"+MyUnicodeChar.arrows()+"]+)+";
	private String replaceComboStrokeByKeyPressions(String content) {
		String textProcessed = content;
		int indexStart =0;
		int indexEnd =-1;
		int antiLoop=500;
		String found ="";
		String replacement ="";
		String before ="";
		String result ="";
		
		while(indexStart>-1 && antiLoop>0) {
			
			Pattern p = Pattern.compile(comboStroke);  // insert your pattern here
			Matcher m = p.matcher(textProcessed);
			if (m.find()) {
				indexStart = m.start();
				indexEnd = m.end();
				before = textProcessed.substring(0,indexStart);
				found = textProcessed.substring(indexStart, indexEnd);
				//System.out.println("Found>>"+found);
				replacement =parseComboToShortCut(found);
				//System.out.println("Replace by>>"+replacement);
				  
				textProcessed = textProcessed.substring(indexEnd+1);
				result += before+replacement;
				//result += " "+before+" "+replacement+" ";
				}
		
			
			antiLoop--;
		}
		if(result.length()<=0)
			result =content;
		//System.out.println("Result>>"+result);
		return result;
	}

	private String parseComboToShortCut(String found) {
		String [] parts = found.split("\\+");
		String result ="";
		if(parts.length<=0) return found;
		
		for (int j = 0; j < parts.length; j++) {
			 parts[j] = parts[j].replaceAll("[^A-Za-z0-9]","").trim();
		}
		
		for (int j = 0; j < parts.length; j++) {
			result += parts[j]+MyUnicodeChar.press+" ";
		}
		for (int j = parts.length-1; j >=0 ; j--) {
			result += parts[j]+MyUnicodeChar.release+" ";
			
		}
		return result;
	}

	private void ConvertTextToKeyStroke(ArrayList<RobotCommand> result, String shortcut) {
		
		String word = shortcut.substring(0, shortcut.length() - 1);
		char arrow = shortcut.charAt(shortcut.length() - 1);
		//System.out.println(">s: " + word + " " + arrow);
		PressType press = PressType.Stroke;
		if (arrow == MyUnicodeChar.press)
			press = PressType.Press;
		else if (arrow == MyUnicodeChar.release)
			press = PressType.Release;

		AddValideWord(result, word,press);
	}
	
	

	private boolean IsDeveloperKeyShortcut(ArrayList<RobotCommand> result, String content, PressType pressType) {

		content = content.toLowerCase();
		
		if (content.contentEquals( "copypast")) {
			if (pressType == PressType.Stroke || pressType == PressType.Press )
				result.add(new CopyPastCommand(CopyPastCommand.Type.Copy));
			if (pressType == PressType.Stroke || pressType == PressType.Release)
				result.add(new CopyPastCommand(CopyPastCommand.Type.Past));
			return true;
		}
		else if (content .contentEquals("cutpast")) {
			if (pressType == PressType.Stroke || pressType == PressType.Press)
				result.add(new CopyPastCommand(CopyPastCommand.Type.Cut));
			if (pressType == PressType.Stroke || pressType == PressType.Release)
				result.add(new CopyPastCommand(CopyPastCommand.Type.Past));
			return true;
		}else if (content .contentEquals( "copy")) {
				result.add(new CopyPastCommand(CopyPastCommand.Type.Copy));
			return true;
		}else if (content .contentEquals("cut")) {
				result.add(new CopyPastCommand(CopyPastCommand.Type.Cut));
			return true;
		}else if (content.contentEquals( "past")) {
				result.add(new CopyPastCommand(CopyPastCommand.Type.Past));
			return true;
		}
		else if (content .contentEquals( "ctrlcmd") || content .contentEquals( "ctrlorcmd")) {
			if (OsUtility.getOS() == OS.MAC)
				result.add(new KeyStrokeCommand("VK_META",pressType));
			else
				result.add(new KeyStrokeCommand("VK_CONTROL",pressType));
			return true;
		}else if (content .contentEquals( "cmd")) {
			result.add(new KeyStrokeCommand("VK_META",pressType));
			return true;
		}else if (content.contentEquals( "ctrl")) {
			result.add(new KeyStrokeCommand("VK_CONTROL",pressType));
			return true;
		}

		return false;
	}

	private boolean IsUserKeyShortcut(ArrayList<RobotCommand> result, String content, PressType pressType) {
		content = content.toLowerCase();
		for (KeyEventId key : m_userShortcut.getRefToKeys()) {

			if (content.contentEquals(key.GetShortcutName().toLowerCase())) {
				result.add(new KeyStrokeCommand(key.GetJavaName(), pressType));
				return true;
			}
		}
		return false;
	}

	private boolean IsJavaKeyShortcut(ArrayList<RobotCommand> result, String content, PressType pressType) {
		content = content.toLowerCase();
		for (String name : KeyEventId.GetAllEnumNames()) {
			// System.out.print(name);
			if (content.contentEquals(name.toLowerCase())) {
				result.add(new KeyStrokeCommand(name, pressType));
				return true;
			}
		}
		return false;
	}

	private boolean IsMouseShortcut(ArrayList<RobotCommand> result, String content, PressType pressType) {
		content = content.toLowerCase();
		if (content.contentEquals( "mouseclick") || content .contentEquals("mouseleftclick") || content .contentEquals( "leftclick")) {
			result.add(new MouseClickCommand(MouseButton.Left, pressType));
			return true;
		}
		if (content .contentEquals( "mouserightclick") || content .contentEquals( "rightclick")) {
			result.add(new MouseClickCommand(MouseButton.Right, pressType));
			return true;
		}
		if (content .contentEquals( "mousemiddleclick") || content .contentEquals( "middleclick")) {
			result.add(new MouseClickCommand(MouseButton.Middle, pressType));
			return true;
		}
		if (content.contentEquals( "doubleclick") || content .contentEquals("doubleleftclick")) {

			result.add(new MouseClickCommand(MouseButton.Left, PressType.Stroke));
			result.add(new MouseClickCommand(MouseButton.Left, PressType.Stroke));
			return true;
		}
		if (content .contentEquals( "doublerightclick")) {

			result.add(new MouseClickCommand(MouseButton.Right, PressType.Stroke));
			result.add(new MouseClickCommand(MouseButton.Right, PressType.Stroke));
			return true;
		}
		if (content .contentEquals("scroll")) {
			result.add(new MouseScrollCommand(pressType == PressType.Press ? -1 : 1));
			return true;
		}

		return false;
	}
	/*
	private static ArrayList<String> FindArrowShortcut(String value) {
		ArrayList<String> shortcut = new ArrayList<String>();
		String pattern = "\\w+["+MyUnicodeChar.press+MyUnicodeChar.relase+MyUnicodeChar.stroke+"]";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(value);
		while (m.find()) {
			System.out.println("Found value: " + m.group(0));
			shortcut.add(m.group(0));
		}
		return shortcut;
	}//*/

	private static ArrayList<String> FindArrowShortcutWithText(String value) {
		ArrayList<String> shortcut = new ArrayList<String>();
		Pattern r =Pattern.compile(String.format("(%s)|(%s)", regexCommand,regexText));
		Matcher m = r.matcher(value);
		while (m.find()) {
			shortcut.add(m.group(0));
		}
		return shortcut;
	}

	private boolean isItMouseMoveCommand(String packageToProcess, RobotCommandRef robotCommand) {
		
		
		if (!(packageToProcess.startsWith("mm:") || packageToProcess.startsWith("ma:")))
			return false;
		if (packageToProcess.length() <= 3)
			return false;

		MouseMoveCommand.MoveType moveTypeValue = MoveType.Set;
		if (packageToProcess.charAt(1) == 'a')
			moveTypeValue = MoveType.Add;

		String content = packageToProcess.substring(3).trim();
		content= content.replace("px", "");
		content= content.replace("PX", "");
		String[] tokens = content.split(":");
		if (tokens.length != 2)
			return false;

		float xPx = 0;
		float yPx = 0;
		MouseMoveCommand.MoveTypeValue xType, yType;
		try {
			xType = tokens[0].indexOf("p") > 0 || tokens[0].indexOf("%") > 0 ? MoveTypeValue.InPourcent : MoveTypeValue.InPixel;
			yType = tokens[1].indexOf("p") > 0 || tokens[0].indexOf("%") > 0? MoveTypeValue.InPourcent : MoveTypeValue.InPixel;
			tokens[0] = tokens[0].replace("p", "");
			tokens[1] = tokens[1].replace("p", "");
			tokens[0] = tokens[0].replace("%", "");
			tokens[1] = tokens[1].replace("%", "");
			xPx = Float.parseFloat(tokens[0]);
			yPx = Float.parseFloat(tokens[1]);
		} catch (Exception e) {
			return false;
		}

		robotCommand.ref = new MouseMoveCommand(moveTypeValue, xPx, xType, yPx, yType);
		return true;
	}

	private boolean isItMouseScrollCommand(String packageToProcess, RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("wh:")))
			return false;
		if (packageToProcess.length() <= 3)
			return false;

		try {

			int wheel = Integer.parseInt(packageToProcess.substring(3).trim());
			robotCommand.ref = new MouseScrollCommand(wheel);

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private boolean isItMouseClickCommand(String packageToProcess, RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("ms:") || packageToProcess.startsWith("mr:")
				|| packageToProcess.startsWith("mp:")))
			return false;
		if (packageToProcess.length() <= 3)
			return false;
		char c = packageToProcess.charAt(1);
		PressType pt = PressType.Stroke;
		if (c == 'r')
			pt = PressType.Release;
		else if (c == 'p')
			pt = PressType.Press;

		String info = packageToProcess.substring(3).trim();
		MouseClickCommand.MouseButton button = MouseButton.Left;
		if (info.charAt(0) == '0' || info.charAt(0) == 'l')
			button = MouseButton.Left;
		else if (info.charAt(0) == '1' || info.charAt(0) == 'm')
			button = MouseButton.Middle;
		else if (info.charAt(0) == '2' || info.charAt(0) == 'r')
			button = MouseButton.Right;
		else
			return false;
		robotCommand.ref = new MouseClickCommand(button, pt);

		return true;
	}

	private boolean isItKeyStrokeCommand(String packageToProcess, RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("ks:") || packageToProcess.startsWith("kr:")
				|| packageToProcess.startsWith("kp:")))
			return false;
		if (packageToProcess.length() <= 3)
			return false;
		char c = packageToProcess.charAt(1);
		PressType pt = PressType.Stroke;
		if (c == 'r')
			pt = PressType.Release;
		else if (c == 'p')
			pt = PressType.Press;
		String name = packageToProcess.substring(3).trim();
		robotCommand.ref = new KeyStrokeCommand(name, pt);
		return true;
	}

	private boolean isItKeyComboStrokeCommand(ArrayList<RobotCommand> result, String packageToProcess) {
		if (!(packageToProcess.startsWith("ksc:") ))
			return false;
		if (packageToProcess.length() <= 4)
			return false;

		String content = packageToProcess.substring(4).trim();
		if(content.trim().length()<=0) 
			return false;
		String [] lines = content.split("["+MyUnicodeChar.split+"|>&]");
		if(lines.length<=0) return false;
		
		for (int i = 0; i < lines.length; i++) {
			String [] parts = lines[i].split("\\+");
			if(parts.length<=0) return false; 
			for (int j = 0; j < parts.length; j++) {
				 parts[j] = parts[j].replaceAll("[^A-Za-z0-9]","").trim();
			}
			for (int j = 0; j < parts.length; j++) {
				String part = parts[j];
				

				
				AddValideWord(result, part, PressType.Press);
				
			}
			for (int j = parts.length-1; j >=0 ; j--) {
				String part = parts[j];

				AddValideWord(result, part, PressType.Release);
				
			}
		}
		
		
		return true;
	}

	private void AddValideWord(ArrayList<RobotCommand> result, String alphaNumWord, PressType pressType) {
		if (IsDeveloperKeyShortcut(result, alphaNumWord, pressType));
		else if (IsMouseShortcut(result, alphaNumWord, pressType));
		else if (IsJavaKeyShortcut(result, alphaNumWord, pressType));
		else if (IsUserKeyShortcut(result, alphaNumWord, pressType));
	}
	
	private boolean isItExitOrStopCommand(String packageToProcess, RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("exit") || packageToProcess.startsWith("stop")))
			return false;
		if (!(packageToProcess.toLowerCase().trim() == "exit" || packageToProcess.toLowerCase().trim() == "stop"))
			return false;
		robotCommand.ref = new KillTheProgramCommand();
		return true;
	}

	private boolean isItCopyPastCommand(String packageToProcess, RobotCommandRef cmdOut) {
		if (!(packageToProcess.toLowerCase().startsWith("past:")))
			return false;
		if (packageToProcess.length() < 6)
			return false;

		cmdOut.ref = new PastCommand(packageToProcess.substring(5));
		return true;
	}
	private boolean isItClipboardCommand(String packageToProcess, ArrayList<RobotCommand> result) {
		if (!(packageToProcess.toLowerCase().startsWith("clipboard:")))
			return false;
		if (packageToProcess.length() <= 10)
			return false;
		
		String content = packageToProcess.substring(10).toLowerCase().trim();
		if(content.contentEquals("copypast")) {
			result.add( new CopyPastCommand(CopyPastCommand.Type.Copy) );	
			result.add( new CopyPastCommand(CopyPastCommand.Type.Past) );			
		} 	
		else if(content.contentEquals("cutpast")) {
			result.add( new CopyPastCommand(CopyPastCommand.Type.Cut) );
			result.add( new CopyPastCommand(CopyPastCommand.Type.Past) );			
		}
		else if(content.contentEquals("cut")) {
			result.add( new CopyPastCommand(CopyPastCommand.Type.Cut) );		
		}	
		else if(content.contentEquals("past")) {
			result.add( new CopyPastCommand(CopyPastCommand.Type.Past) );			
		}
		else if(content.contentEquals("copy")) {
			result.add( new CopyPastCommand(CopyPastCommand.Type.Copy) );
		}
		return true;
	}

	
	private boolean isItOpenWebPageCommand(String packageToProcess, RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("url:")))
			return false;
		if (packageToProcess.length() <= 4)
			return false;

		robotCommand.ref = new OpenURLCommand(packageToProcess.substring(4).trim());
		return true;
	}
	private boolean isItUnicodeCommand(String packageToProcess,  RobotCommandRef robotCommand) {
		if (!(packageToProcess.toLowerCase().startsWith("unicode:")))
			return false;
		if (packageToProcess.length() <= 8)
			return false;
		
		String content = packageToProcess.substring(8).toLowerCase().trim();
		boolean parseSucceed=false;
		int unicodeId;
		if(content.toLowerCase().indexOf("u+")>-1|| content.toLowerCase().indexOf("0x")>-1)
		{
			try {
			
			String hex = content.substring(2);
			unicodeId = Integer.parseInt(hex, 16);
			parseSucceed=true;
			//System.out.println("<d>"+hex+"-"+unicodeId);
			}catch(Exception e) {System.out.println("Can't parse to unicode:"+content); return false;}
		}
		else {
			
			try {
				
				unicodeId = Integer.parseInt(content);
				parseSucceed=true;
			}catch(Exception e) {System.out.println("Can't parse to unicode:"+content);return false;}
		}
		if(!parseSucceed)
			return false;
		robotCommand.ref = new UnicodeCommand(unicodeId);
		return true;
	}
	
	private boolean isItWindowCommandLineCommand(String packageToProcess, ArrayList<RobotCommand> result) {
		if (!(packageToProcess.startsWith("cmd:")))
			return false;
		if (packageToProcess.length() <= 4)
			return false;
		String content = packageToProcess.substring(4);
		result.add(new WindowCmdLineToExecuteCommand(content.split(""+MyUnicodeChar.split)));

		return true;
	}
	private boolean isItImageToClipboardCommand(String packageToProcess,  RobotCommandRef robotCommand) {
		if (!(packageToProcess.startsWith("img2clip:")))
			return false;
		if (packageToProcess.length() <= 9)
			return false;
		String content = packageToProcess.substring(9);
		robotCommand.ref= new ImageURLToClipboardCommand(content);

		return true;
	}
	
}
