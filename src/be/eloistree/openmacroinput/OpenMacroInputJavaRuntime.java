package be.eloistree.openmacroinput;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import be.eloistree.openmacroinput.OsUtility.OS;
import be.eloistree.openmacroinput.audio.PlaySoundFromUrl;
import be.eloistree.openmacroinput.command.RobotCommand;
import be.eloistree.openmacroinput.convertiontables.KeyEventAsString;
import be.eloistree.openmacroinput.window.CmdUtility;

import java.io.BufferedReader;
import java.nio.charset.Charset;

//java -jar C:\..\JarFileName.jar
public class OpenMacroInputJavaRuntime {

	public static int port = 2501;
	public static ArrayList<String> history = new ArrayList<String>();
	public static JTextArea jTextArea = null;
	public static Clipboard clipboard;
	public static KeyEventIdPool keysUserShortcut;
	public static String locker="";
	public static long lockDenyCount;
	public static ExecuteCommandWithRobot executer;
	public static String lastPackage="";
	public static String lastValidate="";
	public static String validateHistory="";
	public static String numberInTheQueue="";
	public static boolean recievedPackaged=false;
	public static void main(String[] args) throws IOException {

		///PlaySoundFromUrl.PlayWav("https://www.kozco.com/tech/WAV-MP3.wav");
		println("Open Macro Input (Java)  ");
		setPortAndLockerFromMainArgs(args);
		displayFrameToDebugAndCloseProcess(args);
		loadClipboardAsStaticToUse();
		createOrLoadUserShortCutPreference();
		writeListOfKeyAvailaibleAsFileForUser();
		launchThreadThatListenToUDPToCreateQueue();
		StartRefreshUIThread();
		executer = new ExecuteCommandWithRobot();
		
		

		
		
		
		CommandParser parser= new CommandParser(keysUserShortcut);
		System.out.println("Hey :");
		int packageCount =0;
		while(true) {
			packageCount =getPackageWaitingCount();
			numberInTheQueue = ""+packageCount;
			if(packageCount>0) {
				if(recievedPackaged==false)
				recievedPackaged=true;
				String packageToProcess =dequeueNextPackage();
				System.out.println("Deqeue:"+packageToProcess.trim());
				
				if(locker.length()>0 && !packageToProcess.startsWith(locker) ) {
					System.out.println(String.format("Deny (%s,%s):%s",locker, ""+lockDenyCount, packageToProcess));
					lockDenyCount++;
					packageToProcess="";
				}
				else {
                                    
                                    
					if(locker.length()>0) {
					
						packageToProcess=packageToProcess.substring(locker.length());	
						System.out.println("After Locker:"+packageToProcess.trim());
					}
				} 				
				lastPackage=packageToProcess;
				if(packageToProcess.length()>0) {
					
					ArrayList<RobotCommand> cmds = parser.getCommandsFrom(packageToProcess);
					System.out.println("Command found:"+cmds.size());
					for (int i = 0; i < cmds.size(); i++) {
						
						try {
							
						executer.execute(cmds.get(i));
						}catch(Exception e) {
							System.out.print("Fail to execute:"+cmds.get(i).toString()+"\n"+e.getStackTrace());

							OpenMacroInputJavaRuntime. DisplayException(e);
						}
						
						lastValidate= cmds.get(i).toString();
						validateHistory= lastValidate+"\n\r"+validateHistory;
						if(validateHistory.length()>2000)
							validateHistory= validateHistory.substring(0,2000);
						//if(cmds!=null)
							//cmds.get(i).execute();
					}
				}
			
				//setTextDisplayed(String.format("Package:\t%s\nValide:\t%s\n\nHistory:\n%s\n",lastValidate, lastValidate, validateHistory),true);
			}
		}
	}
	public static void DisplayException(Exception stack ) {
		
		DisplayException(stack.getMessage(), 10000);
	}

	public static void DisplayException(Exception stack, long sleepTime ) {
		
		DisplayException(stack.getMessage(), sleepTime);
	}
	public static String exceptionHappened="";
	public static void DisplayException(String stack, long sleepTime ) {
		exceptionHappened= "EXCEPTION:+\\n\"+ stack";
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
			OpenMacroInputJavaRuntime. DisplayException(e);
		}
		exceptionHappened="";
		
		
	}
	public static String getTextIpPortDescription() {
		try {
			return "IP: " + InetAddress.getLocalHost() + " Port:" + port + "\n";
		} catch (UnknownHostException e) {
			OpenMacroInputJavaRuntime. DisplayException(e);
			return "IP: 127.0.0.1 Port:" + port + "\n";
		}
	}
	public static void setTextDisplayed(String text, boolean withIpInfo) {
		if(withIpInfo)
			jTextArea.setText(getTextIpPortDescription()+getPatreonSupportLink()+"\n"+text.trim());
		else 
			jTextArea.setText(text.trim());
		frame.update(frame.getGraphics());
		//jTextArea.update(jTextArea.getGraphics());
	}
	private static String getPatreonSupportLink() {
		return "Hope this tool is helping you. "+MyUnicodeChar.youRock+" \n"
	+"https://eloistree.page/donation\n";
	}

	private static void createOrLoadUserShortCutPreference() throws IOException {
		String keysShortcutTable = GetWantedShortCutOfUserFromFile();
		keysUserShortcut = new KeyEventIdPool(keysShortcutTable);
	}

        
        
        
	public static synchronized int getPackageWaitingCount() {return m_receivedPackage.size();}
	public static synchronized String dequeueNextPackage(){ return m_receivedPackage.remove(0);}
	public static LinkedList<String> m_receivedPackage = new LinkedList<String>();
	public static synchronized void addPackageToWait(String text) {
		m_receivedPackage.add(text);
		
	}
	
	private static void StartRefreshUIThread() {
		Thread t = new Thread(new Runnable() {
			public void run() {

				try {
				while (true) {
					if(recievedPackaged) {
						
						if(exceptionHappened.length()>0) {
							setTextDisplayed(exceptionHappened, true);
						}
						else {
							setTextDisplayed(String.format("Package (%s):\t%s\nValide:\t%s\n\nHistory:\n%s\n",
									OpenMacroInputJavaRuntime. numberInTheQueue,
									OpenMacroInputJavaRuntime. lastValidate,
									OpenMacroInputJavaRuntime. lastValidate, 
									OpenMacroInputJavaRuntime. validateHistory),true);
							
						}
					}
					Thread.sleep(500);}
					
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		});
		t.start();
		
	}
	
	private static void launchThreadThatListenToUDPToCreateQueue() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				DatagramSocket server=null;
				try {
					System.out.println(""+MyUnicodeChar.press+MyUnicodeChar.release+MyUnicodeChar.stroke+MyUnicodeChar.youRock+MyUnicodeChar.split);

					server= new DatagramSocket(port);
					while (true) {

						byte[] buffer = new byte[8192];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
						server.receive(packet);

                        Charset cd =Charset.forName("UTF-8");

						String str = new String(packet.getData(), cd);
						System.out.println("P("+m_receivedPackage.size()+"):"+str );
						addPackageToWait(str);
						packet.setLength(buffer.length); 
					}

				} catch (SocketException e) {
					OpenMacroInputJavaRuntime.DisplayException(e);
					e.printStackTrace();
				} catch (IOException e) {
					OpenMacroInputJavaRuntime. DisplayException(e);
					e.printStackTrace();
				}
				finally{

					if(server!=null)
						server.close();
				}
			}

		});

		t.start();
	}

	private static void loadClipboardAsStaticToUse() {
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public static JFrame frame;
	private static void displayFrameToDebugAndCloseProcess(String[] args) {
		frame = new JFrame("Open Macro Input (Java Runtime)");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			jTextArea = new JTextArea("IP: " + InetAddress.getLocalHost() + " Port:" + port + "\n" + "Args: "
					+ StringPlus.join(" ", args) + "\n"+getPatreonSupportLink()+ "\n" + "Commands (P ress, R elease, S troke) :\n"
					+ "- ks:[keyname:string]:\n" + "- ms:[0,1,2]\n" + "- wh:[wheel:int]\n" + "- mm:[x:int]:[y:int]\n"
					+ "- ct:[text]\n" + "Code: https://github.com/EloiStree/2020_02_09_OpenMacroInput\n"
					);
			jTextArea.setBackground(new Color(249f / 255f, 104f / 255f, 84f / 255f, 1f));

			frame.getContentPane().add(jTextArea);
			frame.pack();
		} catch (UnknownHostException e1) {
			OpenMacroInputJavaRuntime. DisplayException(e1);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static void setPortAndLockerFromMainArgs(String[] args) {
		if (args.length > 0) {
			print("> Arg");
			for (int i = 0; i < args.length; i++)
				print(args[i]);

			println("#####################");
			if (args.length >= 1)
				port = Integer.parseInt(args[0]);
			if (args.length >= 2)
				locker = args[1];
		}
	}

	private static String GetWantedShortCutOfUserFromFile() throws IOException {
            String path ="KeyShortcut.txt";
                File f = new File(path);
		if (!f.exists()) {
			
			writeFile(path, KeyEventId.GetDefaultKeysShortcutTableAsText());
			//StringPlus.join("\n", KeyEventId.GetAllEnumNames());
		}
		return readFile(path);
	}

	private static void writeListOfKeyAvailaibleAsFileForUser() throws IOException {
            String path ="AllStrokableKeys.txt";
                File f = new File(path);
		if (!f.exists()) {
			writeFile(path, StringPlus.join("\n", KeyEventId.GetAllEnumNames()));
		}
	}
	
	public static void writeFile(String absolutePath, String text) {
		BufferedWriter writer = null;
		try
		{
		    writer = new BufferedWriter( new FileWriter( absolutePath));
		    writer.write( text);

		}
		catch ( IOException e)
		{
			OpenMacroInputJavaRuntime. DisplayException(e);
		}
		finally
		{
		    try
		    {
		        if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
				OpenMacroInputJavaRuntime. DisplayException(e);
		    }
		}
		
	}
	
	public static String readFile(String absolutePath ) {
           String content = "";
	
            		BufferedReader reader = null;
		try
		{
		    reader = new BufferedReader( new FileReader( absolutePath));
                    int data = reader.read();
                    while (data>-1)
                    {
                        char c = (char) data;
                        content+=c;
                        data = reader.read();

                    }
                    
		}
		catch ( IOException e)
		{
			OpenMacroInputJavaRuntime. DisplayException(e);
		}
		finally
		{
		    try
		    {
		        if ( reader != null)
		        reader.close( );
		    }
		    catch ( IOException e)
		    {
				OpenMacroInputJavaRuntime. DisplayException(e);
		    }
		}

	    return content;
	}

	public static synchronized void print(String str) {
		System.out.print(str);
	}

	public static synchronized void println(String str) {
		System.err.println(str);
	}

}