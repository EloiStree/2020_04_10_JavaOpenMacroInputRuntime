package be.eloistree.openmacroinput;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

	
	public static void main(String[] args) {
		
		  String value ="A↕B↓C↑ BDD↓CEEE↑";
	      
		  
		  ArrayList<String> shortcut  =FindArrowShortcut(value);
		
		
	}

	private static ArrayList<String> FindArrowShortcut(String value) {
		  ArrayList<String> shortcut = new ArrayList<String>();
		  String pattern = "\\w+[↕↓↑]";
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(value);
	      while (m.find( )) {
	         //System.out.println("Found value: " + m.group(0) );
	         shortcut.add(m.group(0));
	      }
	      return shortcut;
	}
	
}
