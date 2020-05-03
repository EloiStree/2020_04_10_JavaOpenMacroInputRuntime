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
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import be.eloistree.openmacroinput.OsUtility.OS;
import be.eloistree.openmacroinput.command.RobotCommand;
import be.eloistree.openmacroinput.convertiontables.KeyEventAsString;

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
	public static void main(String[] args) throws IOException {

		
		println("Open Macro Input (Java)  ");
		setPortAndLockerFromMainArgs(args);
		displayFrameToDebugAndCloseProcess(args);
		loadClipboardAsStaticToUse();
		createOrLoadUserShortCutPreference();
		writeListOfKeyAvailaibleAsFileForUser();
		launchThreadThatListenToUDPToCreateQueue();
		executer = new ExecuteCommandWithRobot();
		
		/*
		 * 
		 * 
		 * if (jTextArea != null) jTextArea.setText("IP: " + InetAddress.getLocalHost()
		 * + " Port:" + port + "\n" + String.join("\n", history));
		 * 
		 */

		
		String lastPackage="";
		String lastValidate="";
		String validateHistory="";
		
		
		CommandParser parser= new CommandParser(keysUserShortcut);
		System.out.println("Hey :");
		int packageCount =0;
		while(true) {
			packageCount =getPackageWaitingCount();

			if(packageCount>0) {
				String packageToProcess =dequeueNextPackage();
				setTextDisplayed("Package Waiting:"+packageCount, true);
				System.out.println("Deqeue:"+packageToProcess);
				
				if(locker.length()>0 && !packageToProcess.startsWith(locker) ) {
					System.out.println(String.format("Deny (%s,%s):%s",locker, ""+lockDenyCount, packageToProcess));
					lockDenyCount++;
					packageToProcess="";
				}
				else {
					if(locker.length()>0) {
					
						packageToProcess=packageToProcess.substring(locker.length());	
						System.out.println("After Locker:"+packageToProcess);
					}
				} 				
				lastPackage=packageToProcess;
				if(packageToProcess.length()>0) {
					
					ArrayList<RobotCommand> cmds = parser.getCommandsFrom(packageToProcess);
					System.out.println("Command found:"+cmds.size());
					for (int i = 0; i < cmds.size(); i++) {
						executer.execute(cmds.get(i));
						
						lastValidate= cmds.get(i).toString();
						validateHistory= lastValidate+"\n\r"+validateHistory;
						if(validateHistory.length()>2000)
							validateHistory= validateHistory.substring(0,2000);
						//if(cmds!=null)
							//cmds.get(i).execute();
					}
				}
			
				setTextDisplayed(String.format("Package:\t%s\nValide:\t%s\n\nHistory:\n%s\n",lastValidate, lastValidate, validateHistory),true);
			}
		}
	}

	public static String getTextIpPortDescription() {
		try {
			return "IP: " + InetAddress.getLocalHost() + " Port:" + port + "\n";
		} catch (UnknownHostException e) {
			return "IP: 127.0.0.1 Port:" + port + "\n";
		}
	}
	public static void setTextDisplayed(String text, boolean withIpInfo) {
		if(withIpInfo)
			jTextArea.setText(getTextIpPortDescription()+"\n"+text);
		else 
			jTextArea.setText(text);
		frame.update(frame.getGraphics());
		//jTextArea.update(jTextArea.getGraphics());
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
	private static void launchThreadThatListenToUDPToCreateQueue() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				DatagramSocket server=null;
				try {
					System.out.println("← → ↑ ↓ ↔ ↕");

					server= new DatagramSocket(port);
					while (true) {

						byte[] buffer = new byte[8192];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
						server.receive(packet);

						String str = new String(packet.getData(), StandardCharsets.UTF_8);
						System.out.println("P:"+str);
						addPackageToWait(str);
						packet.setLength(buffer.length); 
					}

				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
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
					+ String.join(" ", args) + "\n" + "Commands (P ress, R elease, S troke) :\n"
					+ "- ks:[keyname:string]:\n" + "- ms:[0,1,2]\n" + "- wh:[wheel:int]\n" + "- mm:[x:int]:[y:int]\n"
					+ "- ct:[text]\n" + "Code: https://github.com/EloiStree/2020_02_09_OpenMacroInput\n"
					+ "Support & contact: https://www.patreon.com/eloistree\n");
			jTextArea.setBackground(new Color(249f / 255f, 104f / 255f, 84f / 255f, 1f));

			frame.getContentPane().add(jTextArea);
			frame.pack();
		} catch (UnknownHostException e1) {
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
		Path fPath = Paths.get("KeyShortcut.txt");
		if (!Files.exists(fPath)) {
			Files.writeString(fPath, KeyEventId.GetDefaultKeysShortcutTableAsText());
			String.join("\n", KeyEventId.GetAllEnumNames());
		}
		return Files.readString(fPath);
	}

	private static void writeListOfKeyAvailaibleAsFileForUser() throws IOException {
		Path fKeyAvailaible = Paths.get("AllStrokableKeys.txt");
		if (!Files.exists(fKeyAvailaible)) {
			Files.writeString(fKeyAvailaible, String.join("\n", KeyEventId.GetAllEnumNames()));
		}
	}

	public static synchronized void print(String str) {
		System.out.print(str);
	}

	public static synchronized void println(String str) {
		System.err.println(str);
	}

}