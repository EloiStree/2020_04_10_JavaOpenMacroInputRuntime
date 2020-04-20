package be.eloistree.openmacroinput;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import be.eloistree.openmacroinput.OsUtility.OS;


//java -jar C:\..\JarFileName.jar
public class OpenMacroInputJavaRuntime {


		   public  static int port = 2501;
		   public static  ArrayList<String> history= new ArrayList<String>(); 
		   public static  JTextArea jTextArea=null;
		   public static Clipboard clipboard;
		   public static void main(String[] args){

			   println("Open Macro Input (Java)  ");
		
			   if(args.length>0) {
				   print("> Arg");
				   for(int i=0;i<args.length;i++)
					   print(args[i]);

				   println("#####################");
				   if(args.length>1)
				   port = Integer.parseInt(args[0]);
			   }
			   
			   JFrame frame = new JFrame("Open Macro Input (Java Runtime)");
		       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		      
		       try {
					jTextArea = new JTextArea(
					   "IP: "+InetAddress.getLocalHost()+" Port:"+port+"\n"
					   +"Commands (P ress, R elease, S troke) :\n"
					   +"- ks:[keyname:string]:\n"
					   +"- ms:[0,1,2]\n"
					   +"- wh:[wheel:int]\n"
				       +"- mm:[x:int]:[y:int]\n"
				       +"- ct:[text]\n"
					   +"Code: https://github.com/EloiStree/2020_02_09_OpenMacroInput\n"
					   +"Support & contact: https://www.patreon.com/eloistree\n");
					jTextArea.setBackground(new Color(249f/255f, 104f/255f, 84f/255f,1f));
		       
		       frame.getContentPane().add(jTextArea);
		       frame.pack();
		       } catch (UnknownHostException e1) {
		    	   // TODO Auto-generated catch block
		    	   e1.printStackTrace();
		       }
		       frame.setLocationRelativeTo(null);
		       frame.setVisible(true);
		       clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		      Thread t = new Thread(new Runnable(){
		         public void run(){
		            try {
					   Robot robot = new Robot();
		               DatagramSocket server = new DatagramSocket(port);
		               while(true){
		                 
		                  byte[] buffer = new byte[8192];
		                  DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		                  server.receive(packet);
		                  
		                  String str = new String(packet.getData());
//		                  print("Reçu de la part de " + packet.getAddress() 
//		                                    + " sur le port " + packet.getPort() + " : ");
		                  println(str);
		                  history.add(0, str);
		                  if(str.toLowerCase().trim()== "exit" || str.toLowerCase().trim()== "stop")
		                	  System.exit(0);
		                  else if(str.startsWith("ks:")||str.startsWith("kr:")||str.startsWith("kp:")) {
		                	  char pressType = str.charAt(1);
		                	  str=  str.substring(3).trim();
			                	  
		                	  KeyEventInt found = new KeyEventInt();
			                  boolean converted = ConvertStringToKeyEvent(str, found );
			                  if(converted) {
				                    print("Key Found:"+found.value);
				                    if(pressType=='s' || pressType=='p')
							        robot.keyPress(found.value);
				                    if(pressType=='s' || pressType=='r')
							        robot.keyRelease( found.value );
			                	  
			                  }
		                  
		                  } else if(str.startsWith("ct:")) {
		                	  
		                	  char pressType = str.charAt(1);
		                	  str=  str.substring(3).trim();
			                  print("Past Text:"+str.length());
			                  
			                    if(pressType=='t') {
			                    	StringSelection text = new StringSelection(str);
			                    	clipboard.setContents(text,text);
			                    	
			                    }
			                  
			                    
			                    if(OsUtility.getOS()== OS.MAC) {

			                    	robot.keyPress(KeyEvent.VK_META);
			                    	robot.keyPress(KeyEvent.VK_V);
			                    	robot.keyRelease(KeyEvent.VK_V);
			                    	robot.keyRelease(KeyEvent.VK_META);
			                    	
			                    }
			                    else {
			                    	robot.keyPress(KeyEvent.VK_CONTROL);
			                    	robot.keyPress(KeyEvent.VK_V);
			                    	robot.keyRelease(KeyEvent.VK_V);
			                    	robot.keyRelease(KeyEvent.VK_CONTROL);
			                    	
			                    }
			                  }
		                 
		                  else if(str.startsWith("ms:")||str.startsWith("mr:")||str.startsWith("mp:")) {
		                	  char pressType = str.charAt(1);
		                	  str=  str.substring(3).trim();
		                	 int buttonId = -1;
		                	 if(str.charAt(0)=='0')buttonId = InputEvent.BUTTON1_DOWN_MASK;
		                	 else if(str.charAt(0)=='1')buttonId = InputEvent.BUTTON2_DOWN_MASK;
		                	 else if(str.charAt(0)=='2')buttonId = InputEvent.BUTTON3_DOWN_MASK;
		                	 
			                  boolean converted = buttonId>-1;
			                  if(converted) {
				                    print("Mouse click, Found:"+buttonId);
				                    if(pressType=='s' || pressType=='p')
							        robot.mousePress(buttonId);
				                    if(pressType=='s' || pressType=='r')
							        robot.mouseRelease( buttonId );
			                	  
			                  }
		                  
		                  }
		                  
		                  else if(str.startsWith("mm:")) {
		                	  str=  str.substring(3).trim();
		                	  String [] px =str.split(":");
		                	 try {
				                    print("Mouse move, Found:"+ px[0]+" "+px[1]);

		                		 robot.mouseMove(Integer.parseInt(px[0]), Integer.parseInt(px[1]));
		                		 
		                	 }catch(Exception e) {}
		                  }   
		                  else if(str.startsWith("wh:")) {
		                	  str=  str.substring(3).trim();
		                	  try {
			                	int wheelValue = Integer.parseInt(str);
			                	print("Mouse wheel, Found:"+wheelValue);
		                		robot.mouseWheel(wheelValue);
		                		 
		                	  }catch(Exception e) {}
		                  }
		                  
		                  packet.setLength(buffer.length);
		                  if(history.size()>10)
		                	  history.remove(history.size()-1);
		                  if(jTextArea!=null)	
		                	  jTextArea.setText(
		                			  "IP: "+InetAddress.getLocalHost()+" Port:"+port+"\n"+String.join("\n", history));
		                  
		               }
		            } catch (SocketException e) {
		               e.printStackTrace();
		            } catch (IOException e) {
		               e.printStackTrace();
		            } catch (AWTException e) {
				        e.printStackTrace();
					} 
		            //catch (InterruptedException e) {
					//	e.printStackTrace();
					//}
		         }
		      });  
		      
		      t.start();

		   }
		   public static  boolean ConvertStringToKeyEvent(String toConvert, KeyEventInt result) {
			   
			
			   
			   result.value=-1;
			   switch(toConvert) {
			   case "VK_ENTER": result.value = KeyEvent.VK_ENTER ; break;
			   case "VK_BACK_SPACE": result.value = KeyEvent.VK_BACK_SPACE ; break;
			   case "VK_TAB": result.value = KeyEvent.VK_TAB ; break;
			   case "VK_CANCEL": result.value = KeyEvent.VK_CANCEL ; break;
			   case "VK_CLEAR": result.value = KeyEvent.VK_CLEAR ; break;
			   case "VK_SHIFT": result.value = KeyEvent.VK_SHIFT ; break;
			   case "VK_CONTROL": result.value = KeyEvent.VK_CONTROL ; break;
			   case "VK_ALT": result.value = KeyEvent.VK_ALT ; break;
			   case "VK_PAUSE": result.value = KeyEvent.VK_PAUSE ; break;
			   case "VK_CAPS_LOCK": result.value = KeyEvent.VK_CAPS_LOCK ; break;
			   case "VK_ESCAPE": result.value = KeyEvent.VK_ESCAPE ; break;
			   case "VK_SPACE": result.value = KeyEvent.VK_SPACE ; break;
			   case "VK_PAGE_UP": result.value = KeyEvent.VK_PAGE_UP ; break;
			   case "VK_PAGE_DOWN": result.value = KeyEvent.VK_PAGE_DOWN ; break;
			   case "VK_END": result.value = KeyEvent.VK_END ; break;
			   case "VK_HOME": result.value = KeyEvent.VK_HOME ; break;
			   case "VK_LEFT": result.value = KeyEvent.VK_LEFT ; break;
			   case "VK_UP": result.value = KeyEvent.VK_UP ; break;
			   case "VK_RIGHT": result.value = KeyEvent.VK_RIGHT ; break;
			   case "VK_DOWN": result.value = KeyEvent.VK_DOWN ; break; 
			   case "VK_KP_UP": result.value = KeyEvent.VK_KP_UP ; break; 
			   case "VK_KP_DOWN": result.value = KeyEvent.VK_KP_DOWN ; break; 
			   case "VK_KP_LEFT": result.value = KeyEvent.VK_KP_LEFT ; break; 
			   case "VK_KP_RIGHT": result.value = KeyEvent.VK_KP_RIGHT ; break; 
			   case "VK_COMMA": result.value = KeyEvent.VK_COMMA ; break;
			   case "VK_MINUS": result.value = KeyEvent.VK_MINUS ; break;
			   case "VK_PERIOD": result.value = KeyEvent.VK_PERIOD ; break;
			   case "VK_SLASH": result.value = KeyEvent.VK_SLASH ; break;
			   case "VK_0": result.value = KeyEvent.VK_0 ; break;
			   case "VK_1": result.value = KeyEvent.VK_1 ; break;
			   case "VK_2": result.value = KeyEvent.VK_2 ; break;
			   case "VK_3": result.value = KeyEvent.VK_3 ; break;
			   case "VK_4": result.value = KeyEvent.VK_4 ; break;
			   case "VK_5": result.value = KeyEvent.VK_5 ; break;
			   case "VK_6": result.value = KeyEvent.VK_6 ; break;
			   case "VK_7": result.value = KeyEvent.VK_7 ; break;
			   case "VK_8": result.value = KeyEvent.VK_8 ; break;
			   case "VK_9": result.value = KeyEvent.VK_9 ; break;
			   case "VK_SEMICOLON": result.value = KeyEvent.VK_SEMICOLON ; break;
			   case "VK_EQUALS": result.value = KeyEvent.VK_EQUALS ; break;
			   case "VK_A": result.value = KeyEvent.VK_A ; break;
			   case "VK_B": result.value = KeyEvent.VK_B ; break;
			   case "VK_C": result.value = KeyEvent.VK_C ; break;
			   case "VK_D": result.value = KeyEvent.VK_D ; break;
			   case "VK_E": result.value = KeyEvent.VK_E ; break;
			   case "VK_F": result.value = KeyEvent.VK_F ; break;
			   case "VK_G": result.value = KeyEvent.VK_G ; break;
			   case "VK_H": result.value = KeyEvent.VK_H ; break;
			   case "VK_I": result.value = KeyEvent.VK_I ; break;
			   case "VK_J": result.value = KeyEvent.VK_J ; break;
			   case "VK_K": result.value = KeyEvent.VK_K ; break;
			   case "VK_L": result.value = KeyEvent.VK_L ; break;
			   case "VK_M": result.value = KeyEvent.VK_M ; break;
			   case "VK_N": result.value = KeyEvent.VK_N ; break;
			   case "VK_O": result.value = KeyEvent.VK_O ; break;
			   case "VK_P": result.value = KeyEvent.VK_P ; break;
			   case "VK_Q": result.value = KeyEvent.VK_Q ; break;
			   case "VK_R": result.value = KeyEvent.VK_R ; break;
			   case "VK_S": result.value = KeyEvent.VK_S ; break;
			   case "VK_T": result.value = KeyEvent.VK_T ; break;
			   case "VK_U": result.value = KeyEvent.VK_U ; break;
			   case "VK_V": result.value = KeyEvent.VK_V ; break;
			   case "VK_W": result.value = KeyEvent.VK_W ; break;
			   case "VK_X": result.value = KeyEvent.VK_X ; break;
			   case "VK_Y": result.value = KeyEvent.VK_Y ; break;
			   case "VK_Z": result.value = KeyEvent.VK_Z ; break;
			   case "VK_OPEN_BRACKET": result.value = KeyEvent.VK_OPEN_BRACKET ; break;
			   case "VK_BACK_SLASH": result.value = KeyEvent.VK_BACK_SLASH ; break;
			   case "VK_CLOSE_BRACKET": result.value = KeyEvent.VK_CLOSE_BRACKET ; break;
			   case "VK_NUMPAD0": result.value = KeyEvent.VK_NUMPAD0 ; break;
			   case "VK_NUMPAD1": result.value = KeyEvent.VK_NUMPAD1 ; break;
			   case "VK_NUMPAD2": result.value = KeyEvent.VK_NUMPAD2 ; break;
			   case "VK_NUMPAD3": result.value = KeyEvent.VK_NUMPAD3 ; break;
			   case "VK_NUMPAD4": result.value = KeyEvent.VK_NUMPAD4 ; break;
			   case "VK_NUMPAD5": result.value = KeyEvent.VK_NUMPAD5 ; break;
			   case "VK_NUMPAD6": result.value = KeyEvent.VK_NUMPAD6 ; break;
			   case "VK_NUMPAD7": result.value = KeyEvent.VK_NUMPAD7 ; break;
			   case "VK_NUMPAD8": result.value = KeyEvent.VK_NUMPAD8 ; break;
			   case "VK_NUMPAD9": result.value = KeyEvent.VK_NUMPAD9 ; break;
			   case "VK_MULTIPLY": result.value = KeyEvent.VK_MULTIPLY ; break;
			   case "VK_ADD": result.value = KeyEvent.VK_ADD ; break;
			   case "VK_SEPARATOR": result.value = KeyEvent.VK_SEPARATOR ; break;
			   case "VK_SUBTRACT": result.value = KeyEvent.VK_SUBTRACT ; break;
			   case "VK_DECIMAL": result.value = KeyEvent.VK_DECIMAL ; break;
			   case "VK_DIVIDE": result.value = KeyEvent.VK_DIVIDE ; break;
			   case "VK_DELETE": result.value = KeyEvent.VK_DELETE ; break;
			   case "VK_NUM_LOCK": result.value = KeyEvent.VK_NUM_LOCK ; break;
			   case "VK_SCROLL_LOCK": result.value = KeyEvent.VK_SCROLL_LOCK ; break;
			   case "VK_F1": result.value = KeyEvent.VK_F1 ; break;
			   case "VK_F2": result.value = KeyEvent.VK_F2 ; break;
			   case "VK_F3": result.value = KeyEvent.VK_F3 ; break;
			   case "VK_F4": result.value = KeyEvent.VK_F4 ; break;
			   case "VK_F5": result.value = KeyEvent.VK_F5 ; break;
			   case "VK_F6": result.value = KeyEvent.VK_F6 ; break;
			   case "VK_F7": result.value = KeyEvent.VK_F7 ; break;
			   case "VK_F8": result.value = KeyEvent.VK_F8 ; break;
			   case "VK_F9": result.value = KeyEvent.VK_F9 ; break;
			   case "VK_F10": result.value = KeyEvent.VK_F10 ; break;
			   case "VK_F11": result.value = KeyEvent.VK_F11 ; break;
			   case "VK_F12": result.value = KeyEvent.VK_F12 ; break;
			   case "VK_F13": result.value = KeyEvent.VK_F13 ; break;
			   case "VK_F14": result.value = KeyEvent.VK_F14 ; break;
			   case "VK_F15": result.value = KeyEvent.VK_F15 ; break;
			   case "VK_F16": result.value = KeyEvent.VK_F16 ; break;
			   case "VK_F17": result.value = KeyEvent.VK_F17 ; break;
			   case "VK_F18": result.value = KeyEvent.VK_F18 ; break;
			   case "VK_F19": result.value = KeyEvent.VK_F19 ; break;
			   case "VK_F20": result.value = KeyEvent.VK_F20 ; break;
			   case "VK_F21": result.value = KeyEvent.VK_F21 ; break;
			   case "VK_F22": result.value = KeyEvent.VK_F22 ; break;
			   case "VK_F23": result.value = KeyEvent.VK_F23 ; break;
			   case "VK_F24": result.value = KeyEvent.VK_F24 ; break;
			   case "VK_PRINTSCREEN": result.value = KeyEvent.VK_PRINTSCREEN ; break;
			   case "VK_INSERT": result.value = KeyEvent.VK_INSERT ; break;
			   case "VK_HELP": result.value = KeyEvent.VK_HELP ; break;
			   case "VK_META": result.value = KeyEvent.VK_META ; break;
			   case "VK_BACK_QUOTE": result.value = KeyEvent.VK_BACK_QUOTE ; break;
			   case "VK_QUOTE": result.value = KeyEvent.VK_QUOTE ; break; 
			   case "VK_DEAD_GRAVE": result.value = KeyEvent.VK_DEAD_GRAVE ; break;
			   case "VK_DEAD_ACUTE": result.value = KeyEvent.VK_DEAD_ACUTE ; break;
			   case "VK_DEAD_CIRCUMFLEX": result.value = KeyEvent.VK_DEAD_CIRCUMFLEX ; break;
			   case "VK_DEAD_TILDE": result.value = KeyEvent.VK_DEAD_TILDE ; break;
			   case "VK_DEAD_MACRON": result.value = KeyEvent.VK_DEAD_MACRON ; break;
			   case "VK_DEAD_BREVE": result.value = KeyEvent.VK_DEAD_BREVE ; break;
			   case "VK_DEAD_ABOVEDOT": result.value = KeyEvent.VK_DEAD_ABOVEDOT ; break;
			   case "VK_DEAD_DIAERESIS": result.value = KeyEvent.VK_DEAD_DIAERESIS ; break;
			   case "VK_DEAD_ABOVERING": result.value = KeyEvent.VK_DEAD_ABOVERING ; break;
			   case "VK_DEAD_DOUBLEACUTE": result.value = KeyEvent.VK_DEAD_DOUBLEACUTE ; break;
			   case "VK_DEAD_CARON": result.value = KeyEvent.VK_DEAD_CARON ; break;
			   case "VK_DEAD_CEDILLA": result.value = KeyEvent.VK_DEAD_CEDILLA ; break;
			   case "VK_DEAD_OGONEK": result.value = KeyEvent.VK_DEAD_OGONEK ; break;
			   case "VK_DEAD_IOTA": result.value = KeyEvent.VK_DEAD_IOTA ; break;
			   case "VK_DEAD_VOICED_SOUND": result.value = KeyEvent.VK_DEAD_VOICED_SOUND ; break;
			   case "VK_DEAD_SEMIVOICED_SOUND": result.value = KeyEvent.VK_DEAD_SEMIVOICED_SOUND ; break;
			   case "VK_AMPERSAND": result.value = KeyEvent.VK_AMPERSAND ; break;
			   case "VK_ASTERISK": result.value = KeyEvent.VK_ASTERISK ; break;
			   case "VK_QUOTEDBL": result.value = KeyEvent.VK_QUOTEDBL ; break;
			   case "VK_LESS": result.value = KeyEvent.VK_LESS ; break;
			   case "VK_GREATER": result.value = KeyEvent.VK_GREATER ; break;
			   case "VK_BRACELEFT": result.value = KeyEvent.VK_BRACELEFT ; break;
			   case "VK_BRACERIGHT": result.value = KeyEvent.VK_BRACERIGHT ; break;
			   case "VK_AT": result.value = KeyEvent.VK_AT ; break;
			   case "VK_COLON": result.value = KeyEvent.VK_COLON ; break;
			   case "VK_CIRCUMFLEX": result.value = KeyEvent.VK_CIRCUMFLEX ; break;
			   case "VK_DOLLAR": result.value = KeyEvent.VK_DOLLAR ; break;
			   case "VK_EURO_SIGN": result.value = KeyEvent.VK_EURO_SIGN ; break;
			   case "VK_EXCLAMATION_MARK": result.value = KeyEvent.VK_EXCLAMATION_MARK ; break;
			   case "VK_INVERTED_EXCLAMATION_MARK": result.value = KeyEvent.VK_INVERTED_EXCLAMATION_MARK ; break;
			   case "VK_LEFT_PARENTHESIS": result.value = KeyEvent.VK_LEFT_PARENTHESIS ; break;
			   case "VK_NUMBER_SIGN": result.value = KeyEvent.VK_NUMBER_SIGN ; break;
			   case "VK_PLUS": result.value = KeyEvent.VK_PLUS ; break;
			   case "VK_RIGHT_PARENTHESIS": result.value = KeyEvent.VK_RIGHT_PARENTHESIS ; break;
			   case "VK_UNDERSCORE": result.value = KeyEvent.VK_UNDERSCORE ; break;
			   case "VK_WINDOWS": result.value = KeyEvent.VK_WINDOWS ; break;
			   case "VK_CONTEXT_MENU": result.value = KeyEvent.VK_CONTEXT_MENU ; break;
			   case "VK_FINAL": result.value = KeyEvent.VK_FINAL ; break;
			   case "VK_CONVERT": result.value = KeyEvent.VK_CONVERT ; break;
			   case "VK_NONCONVERT": result.value = KeyEvent.VK_NONCONVERT ; break;
			   case "VK_ACCEPT": result.value = KeyEvent.VK_ACCEPT ; break;
			   case "VK_MODECHANGE": result.value = KeyEvent.VK_MODECHANGE ; break;
			   case "VK_ALPHANUMERIC": result.value = KeyEvent.VK_ALPHANUMERIC ; break;
			   case "VK_FULL_WIDTH": result.value = KeyEvent.VK_FULL_WIDTH ; break;
			   case "VK_HALF_WIDTH": result.value = KeyEvent.VK_HALF_WIDTH ; break;
			   case "VK_CODE_INPUT": result.value = KeyEvent.VK_CODE_INPUT ; break;
			   case "VK_INPUT_METHOD_ON_OFF": result.value = KeyEvent.VK_INPUT_METHOD_ON_OFF ; break;
			   case "VK_CUT": result.value = KeyEvent.VK_CUT ; break;
			   case "VK_COPY": result.value = KeyEvent.VK_COPY ; break;
			   case "VK_PASTE": result.value = KeyEvent.VK_PASTE ; break;
			   case "VK_UNDO": result.value = KeyEvent.VK_UNDO ; break;
			   case "VK_AGAIN": result.value = KeyEvent.VK_AGAIN ; break;
			   case "VK_FIND": result.value = KeyEvent.VK_FIND ; break;
			   case "VK_PROPS": result.value = KeyEvent.VK_PROPS ; break;
			   case "VK_STOP": result.value = KeyEvent.VK_STOP ; break;
			   case "VK_COMPOSE": result.value = KeyEvent.VK_COMPOSE ; break;
			   case "VK_ALT_GRAPH": result.value = KeyEvent.VK_ALT_GRAPH ; break;
			   case "VK_BEGIN": result.value = KeyEvent.VK_BEGIN ; break;
			   case "VK_UNDEFINED": result.value = KeyEvent.VK_UNDEFINED ; break;
			   case "KEY_LOCATION_UNKNOWN": result.value = KeyEvent.KEY_LOCATION_UNKNOWN ; break;
			   case "KEY_LOCATION_STANDARD": result.value = KeyEvent.KEY_LOCATION_STANDARD ; break;
			   case "KEY_LOCATION_LEFT": result.value = KeyEvent.KEY_LOCATION_LEFT ; break;
			   case "KEY_LOCATION_RIGHT": result.value = KeyEvent.KEY_LOCATION_RIGHT ; break;
			   case "KEY_LOCATION_NUMPAD": result.value = KeyEvent.KEY_LOCATION_NUMPAD ; break;
		   		

		   		
			   		default:break;
			   }
			   return result.value>-1;
		   }
		   public static synchronized void print(String str){
		      System.out.print(str);
		   }
		   public static synchronized void println(String str){
		      System.err.println(str);
		   }
		
}