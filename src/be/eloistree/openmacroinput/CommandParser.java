package be.eloistree.openmacroinput;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.eloistree.openmacroinput.command.PastCommand;
import be.eloistree.debug.CDebug;
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
import be.eloistree.time.ExecuteTime;





public class CommandParser {
	public class RobotCommandRef {
		public RobotCommand ref;
	}

	public KeyEventIdPool m_userShortcut;

	public CommandParser(KeyEventIdPool keysUserShortcut) {
		m_userShortcut = keysUserShortcut;
	}


	//TO CODE WHEN TIME
	//Backspace↓200  =  Backspace↓ ⌛600 Backspace↑
	String regexLastTime= "\\w["+getTimeAffectedUnicode()+"]\\d";
	//Backspace↓200  =  Backspace↓↑
	String doubleArrow = "\\w["+MyUnicodeChar.press+MyUnicodeChar.release+"]\\d"; 
	//🖱move:lrdt:0.5:0.7 = move mouse at 50% of left to right and 70% of down to top of the screen.
	//🖱left:lrdt:50:60 = left click at 50pixel from left to right and 60 from bot to top
	String regexMouse= MyUnicodeChar.mouse+"\\w:\\w:\\w:\\w";
	//TO CODE WHEN TIME
	
	public ArrayList<RobotCommand> getCommandsFrom(String packageToProcess) {

		
		
		ExecuteTime whenToDo = null;
		if(packageToProcess.indexOf("t:")==0) {
			packageToProcess= packageToProcess.substring(2);
			int endOfValue = packageToProcess.indexOf(':');
			if(endOfValue>0) {
				try {
				String [] tokens = packageToProcess.substring(0,endOfValue).trim().split("-");
				if(tokens.length==4) {

					int hours = Integer.parseInt(tokens[0]);
					int minutes = Integer.parseInt(tokens[1]);
					int seconds = Integer.parseInt(tokens[2]);
					int milliseconds = Integer.parseInt(tokens[3]);
					whenToDo = ExecuteTime.timeOfTheDay(hours, minutes, seconds, milliseconds);
				}
				}catch (Exception e) {}
				

				packageToProcess= packageToProcess.substring(endOfValue+1);
			}
			
		}	
		else if(packageToProcess.indexOf("tms:")==0) {
			packageToProcess= packageToProcess.substring(4);
			int endOfValue = packageToProcess.indexOf(':');
			if(endOfValue>0) {
				try {
					//System.out.println("Hey T:"+packageToProcess.substring(0,endOfValue).trim());
					//System.out.println("Hey D:"+packageToProcess+" -> "+packageToProcess.substring(0,endOfValue).trim());
					int milliseconds = Integer.parseInt(packageToProcess.substring(0,endOfValue).trim());
					whenToDo=	ExecuteTime.nowWithMilliSeconds(milliseconds);
				}catch (Exception e) { 
					
					System.err.println(e.getStackTrace());
				}

				packageToProcess= packageToProcess.substring(endOfValue+1);

				//System.out.println("DDD:"+packageToProcess);
			}
			
			
		}
		
		boolean timeManagedByConversion=false;
		
		ArrayList<RobotCommand> result = new ArrayList<RobotCommand>();
		
		RobotCommandRef robotCommand = new RobotCommandRef();
		
		if (isItSomeShortcutCommandsV2(packageToProcess, result, whenToDo)) {

			timeManagedByConversion=true;

		//	System.out.println(">>TEST   RTERTS>");
		}
		
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

		if(!timeManagedByConversion) {
			
		
			for (int i = 0; i < result.size(); i++) {
				result.get(i).setTimeToExecute(whenToDo);
			}
		}
		return result;
	}

	private String getTimeAffectedUnicode() {
		
		return ""+MyUnicodeChar.press+
				MyUnicodeChar.release+
				MyUnicodeChar.stroke+
				MyUnicodeChar.pressThenRelease+
				MyUnicodeChar.releaseThenPress;
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
	private static String regexCommand = "\\w+["+MyUnicodeChar.press+MyUnicodeChar.release+MyUnicodeChar.stroke+MyUnicodeChar.releaseThenPress+MyUnicodeChar.pressThenRelease+"]";
	private static String regexTime =MyUnicodeChar.sandTime+"[0-9]+";
	private static String regexTimeWatch =MyUnicodeChar.watchTime+"[0-9h:s]+";
	

	private boolean isItSomeShortcutCommandsV2(String packageToProcess, ArrayList<RobotCommand> result, ExecuteTime startPoint) {

		if (!(packageToProcess.startsWith("sc:")))
			return false;
		if (packageToProcess.length() <= 3)
			return false;
		String content = packageToProcess.substring(3);
		//Pattern.compile(String.format("(%s)|(%s)", regexCommand,regexText));

		if(CDebug.use)
		System.out.println(">>>"+content);
		content= replaceComboStrokeByKeyPressions(content);
		if(CDebug.use)
		System.out.println(">>>"+content);

		//ArrayList<String> shortcuts = new ArrayList<String>();//FindArrowShortcutWithText(content);
		ArrayList<String> shortcuts = FindArrowShortcutWithText(content);
		Calendar whenToExecute = Calendar.getInstance();
		//System.out.println(">>TEST GS>"+whenToExecute.getTimeInMillis());
		if(startPoint!=null && startPoint.m_whenToExecute!=null)
			whenToExecute=startPoint.m_whenToExecute;

			for (int i = 0; i < shortcuts.size(); i++) {
				String shortcut = shortcuts.get(i);
				
			
				if(shortcut.matches(regexText))
				{
					PastCommand created = new PastCommand(shortcut.substring(2,shortcut.length()-2));
					created.setTimeToExecute( new ExecuteTime(whenToExecute));
					result.add(created);
					
				}
				
				else if(shortcut.matches(regexCommand)) {
					
					
					ConvertTextToKeyStroke(result, shortcut, whenToExecute);
					
				}else if(shortcut.indexOf(""+MyUnicodeChar.sandTime)>-1) {

					int millisecondExtracted= extractTimeInMS(shortcut);
					whenToExecute = AddMillisecondsToTime(whenToExecute, millisecondExtracted);
					//System.out.println("YYYY "+whenToExecute.getTimeInMillis());
					
				}else if(shortcut.indexOf(""+MyUnicodeChar.watchTime)>-1) {

					int millisecondExtracted= extractTimeInMSFromWatch(shortcut,whenToExecute );
					whenToExecute = AddMillisecondsToTime(whenToExecute, millisecondExtracted);
					
				}
				
				//System.out.println(">>TEST G>"+shortcut);
				
			}

			//System.out.println(">>TEST GE>"+whenToExecute.getTimeInMillis());
	
		return result.size()>0;
	}

	private Calendar AddMillisecondsToTime(Calendar whenToExecute, int millisecondExtracted) {
		Calendar newTime=Calendar.getInstance();
		newTime.setTimeInMillis(whenToExecute.getTimeInMillis()+millisecondExtracted);
		whenToExecute =newTime;
		return whenToExecute;
	}

	
	private int extractTimeInMSFromWatch(String text, Calendar whenToExecute) {
		System.out.println(">>Watch humm>");
		text= text.trim();
		int i = text.indexOf(MyUnicodeChar.watchTime);
		if(i<0)return 0;
		text = text.substring(i+1).trim();
		
		try {
			int hour=0;
			int minute=0;
			int second=0;
			int millisecond=0;
			String [] tokens = text.split("[h:s]");
			if(tokens.length>=1)
				hour =Integer.parseInt(tokens[0]);
			if(tokens.length>=2)
				minute =Integer.parseInt(tokens[1]);
			if(tokens.length>=3)
				second =Integer.parseInt(tokens[2]);
			if(tokens.length>=4)
				millisecond =Integer.parseInt(tokens[3]);
			
			

			
			Calendar d =  Calendar.getInstance();
			d.set(d.get(Calendar.YEAR) ,d.get(Calendar.MONTH), d.get(Calendar.DAY_OF_MONTH),  hour, minute, second);
			d.add(Calendar.MILLISECOND, millisecond);
			
			long ms = daysBetween(whenToExecute, d);
			if(ms<0)
				ms=0;	
			
				System.out.println(">>Watcj Time>"+text+">"+ ms);
		return (int) ms;
		
		
		}catch(NumberFormatException e) {

			return 0;
		}
	}public long daysBetween(Calendar startDate, Calendar endDate) {
	    long end = endDate.getTimeInMillis();
	    long start = startDate.getTimeInMillis();
	    return end-start;
	}

	public int extractTimeInMS(String text) {
		
		text= text.trim();
		int i = text.indexOf(MyUnicodeChar.sandTime);
		if(i<0)return 0;
		text = text.substring(i+1).trim();
		
		try {

			//	System.out.println(">>CCC>"+text+">");
		int time = Integer.parseInt(text);
		return time;
		}catch(NumberFormatException e) {

			return 0;
		}
		
	}
	
	//https://regexr.com/554uh
	private static String comboStroke ="([A-Za-z0-9"+MyUnicodeChar.arrows()+"]+)([\\s]*\\+[\\s]*[A-Za-z0-9"+MyUnicodeChar.arrows()+"]+)+";
	private static Pattern p2 = Pattern.compile(comboStroke);
	private String replaceComboStrokeByKeyPressions(String content) {


		String textProcessed = content;
		int indexStart =0;
		int indexEnd =-1;
		int antiLoop=500;
		String found ="";
		String replacement ="";
		String before ="";
		String result ="";
		
		boolean foundOne= true;
		while(foundOne ) {
			
			  // insert your pattern here
			Matcher m = p2.matcher(textProcessed);
			if (foundOne= m.find()) {
				indexStart = m.start();
				indexEnd = m.end();
				//System.out.println("E"+indexStart+" "+p2.toString());
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
			if(antiLoop<0)
				break;
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

	private void ConvertTextToKeyStroke(ArrayList<RobotCommand> result, String shortcut, Calendar whenToExecute) {
		
		String word = shortcut.substring(0, shortcut.length() - 1);
		char arrow = shortcut.charAt(shortcut.length() - 1);
		//System.out.println(">s: " + word + " " + arrow);
		
		
		
		PressType press = PressType.Stroke;
		
		if (arrow == MyUnicodeChar.press)
			addValideWord(result, whenToExecute, word, PressType.Press);
		else if (arrow == MyUnicodeChar.release)
			addValideWord(result, whenToExecute, word, PressType.Release);
		else if (arrow == MyUnicodeChar.stroke) {

			addValideWord(result, whenToExecute, word, PressType.Press);
			whenToExecute=AddMillisecondsToTime(whenToExecute, 1);
			addValideWord(result, whenToExecute, word, PressType.Release);
		}else if (arrow == MyUnicodeChar.releaseThenPress) {

			addValideWord(result, whenToExecute, word, PressType.Release);
			whenToExecute=AddMillisecondsToTime(whenToExecute, 1);
			addValideWord(result, whenToExecute, word, PressType.Press);
			
		}else if (arrow == MyUnicodeChar.pressThenRelease) {
			addValideWord(result, whenToExecute, word, PressType.Press);
			whenToExecute=AddMillisecondsToTime(whenToExecute, 1);
			addValideWord(result, whenToExecute, word, PressType.Release);
		}


		//System.out.println("AAAA "+whenToExecute.getTimeInMillis());
	
	}

	private void addValideWord(ArrayList<RobotCommand> result, Calendar whenToExecute, String word, PressType press) {
		RobotCommandRef found = new RobotCommandRef();
		AddValideWord(result, word,press, found);
		if(found.ref!=null && whenToExecute!=null) {
			found.ref.setTimeToExecute(new ExecuteTime( whenToExecute));
		}
	}
	
	
	

	private boolean IsDeveloperKeyShortcut(ArrayList<RobotCommand> result, String content, PressType pressType, RobotCommandRef found) {

		content = content.toLowerCase();
		RobotCommand foundValue =null;
		if (content.contentEquals( "copypast")) {
			if (pressType == PressType.Stroke || pressType == PressType.Press )
				foundValue=(new CopyPastCommand(CopyPastCommand.Type.Copy));
			if (pressType == PressType.Stroke || pressType == PressType.Release)
				foundValue=(new CopyPastCommand(CopyPastCommand.Type.Past));
				
		}
		else if (content .contentEquals("cutpast")) {
			if (pressType == PressType.Stroke || pressType == PressType.Press)
				foundValue=(new CopyPastCommand(CopyPastCommand.Type.Cut));
			if (pressType == PressType.Stroke || pressType == PressType.Release)
				foundValue=(new CopyPastCommand(CopyPastCommand.Type.Past));
		}else if (content .contentEquals( "copy")) {
			foundValue=(new CopyPastCommand(CopyPastCommand.Type.Copy));

		}else if (content .contentEquals("cut")) {
			foundValue=(new CopyPastCommand(CopyPastCommand.Type.Cut));

		}else if (content.contentEquals( "past")) {
			foundValue=(new CopyPastCommand(CopyPastCommand.Type.Past));

		}
		else if (content .contentEquals( "ctrlcmd") || content .contentEquals( "ctrlorcmd")) {
			if (OsUtility.getOS() == OS.MAC)
				foundValue=(new KeyStrokeCommand("VK_META",pressType));
			else
				foundValue=(new KeyStrokeCommand("VK_CONTROL",pressType));

		}else if (content .contentEquals( "cmd")) {
			foundValue=(new KeyStrokeCommand("VK_META",pressType));

		}else if (content.contentEquals( "ctrl")) {
			foundValue=(new KeyStrokeCommand("VK_CONTROL",pressType));
		}

		if(foundValue!=null ) {
			result.add(foundValue);
			if(found!=null)
			found.ref = foundValue;			
		}
		return foundValue!=null;
	}

	private boolean IsUserKeyShortcut(ArrayList<RobotCommand> result, String content, PressType pressType, RobotCommandRef found) {
		content = content.toLowerCase();
		for (KeyEventId key : m_userShortcut.getRefToKeys()) {

			if (content.contentEquals(key.GetShortcutName().toLowerCase())) {
				KeyStrokeCommand r = new KeyStrokeCommand(key.GetJavaName(), pressType);
				if(found!=null)
					found.ref =r;
				result.add(r);
				return true;
			}
		}
		return false;
	}

	private boolean IsJavaKeyShortcut(ArrayList<RobotCommand> result, String content, PressType pressType, RobotCommandRef found) {
		content = content.toLowerCase();
		for (String name : KeyEventId.GetAllEnumNames()) {
			if (content.contentEquals(name.toLowerCase())) {
				KeyStrokeCommand r =new KeyStrokeCommand(name, pressType);
				if(found!=null)
					found.ref =r;
				result.add(r);
				return true;
			}
		}
		return false;
	}

	private boolean IsMouseShortcut(ArrayList<RobotCommand> result, String content, PressType pressType, RobotCommandRef found) {
		content = content.toLowerCase();
		RobotCommand foundValue =null;
		if (content.contentEquals( "mouseclick") || content .contentEquals("mouseleftclick") || content .contentEquals( "leftclick")) {
			foundValue=new MouseClickCommand(MouseButton.Left, pressType);

		}
		else if (content .contentEquals( "mouserightclick") || content .contentEquals( "rightclick")) {
			foundValue=new MouseClickCommand(MouseButton.Right, pressType);

		}
		else if (content .contentEquals( "mousemiddleclick") || content .contentEquals( "middleclick")) {
			foundValue=new MouseClickCommand(MouseButton.Middle, pressType);
		}
		
		else if (content .contentEquals("scroll")) {
			result.add(new MouseScrollCommand(pressType == PressType.Press ? -1 : 1));
		}

		if(foundValue!=null) {
			result.add(foundValue);
			if(found!=null)
				found.ref = foundValue;			
		}
		return foundValue!=null;
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

	private static Pattern r =Pattern.compile(String.format("(%s)|(%s)|(%s)|(%s)", regexCommand,regexText,regexTime, regexTimeWatch));
	private static ArrayList<String> FindArrowShortcutWithText(String value) {
		//System.out.println("Hello");
		//System.out.println("Bye");
		ArrayList<String> shortcut = new ArrayList<String>();
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
				

				
				AddValideWord(result, part, PressType.Press, null);
				
			}
			for (int j = parts.length-1; j >=0 ; j--) {
				String part = parts[j];

				AddValideWord(result, part, PressType.Release, null);
				
			}
		}
		
		
		return true;
	}

	private void AddValideWord(ArrayList<RobotCommand> result, String alphaNumWord, PressType pressType, RobotCommandRef found) {
		if (IsDeveloperKeyShortcut(result, alphaNumWord, pressType, found));
		else if (IsMouseShortcut(result, alphaNumWord, pressType, found));
		else if (IsJavaKeyShortcut(result, alphaNumWord, pressType, found));
		else if (IsUserKeyShortcut(result, alphaNumWord, pressType, found));
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
			}catch(Exception e) {

				if(CDebug.use)System.out.println("Can't parse to unicode:"+content);return false;}
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
