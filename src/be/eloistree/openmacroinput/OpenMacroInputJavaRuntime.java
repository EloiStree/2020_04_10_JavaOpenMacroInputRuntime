package be.eloistree.openmacroinput;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.Color;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import be.eloistree.debug.CDebug;
import be.eloistree.openmacroinput.command.RobotCommand;

import java.io.BufferedReader;
import java.nio.charset.Charset;
//java -jar C:\..\JarFileName.jar
public class OpenMacroInputJavaRuntime {
	
	//public static int threadMainPriority= Thread.NORM_PRIORITY;
		//public static int threadUDPPriority= Thread.NORM_PRIORITY;
		public static int threadMainPriority= Thread.MIN_PRIORITY;
		public static int threadUDPPriority= Thread.MIN_PRIORITY;
	public static int port = 2501;
	public static ArrayList<String> history = new ArrayList<String>();
	//public static JTextArea jTextArea = null;
	public static Clipboard clipboard;
	public static KeyEventIdPool keysUserShortcut;
	public static String locker = "";
	public static long lockDenyCount;
	public static ExecuteCommandWithRobot executer;
	public static String lastPackage = "";
	public static String lastValidate = "";
	public static String validateHistory = "";
	public static String numberInTheQueue = "";
	public static boolean recievedPackaged = false;
	public static ArrayList<RobotCommand> executeLaterStack = new ArrayList<RobotCommand>();

	public static boolean useConsole = true;
	public static boolean useRefreshConsoleState = false;
	public static boolean keepThreadAlive=true;

	public static String lastVersionDate="2022-01-05";
	public static void main(String[] args) throws IOException, InterruptedException {

		/// PlaySoundFromUrl.PlayWav("https://www.kozco.com/tech/WAV-MP3.wav");
		println("Open Macro Input (Java)  ");
		setPortAndLockerFromMainArgs(args);
		displayFrameToDebugAndCloseProcess(args);
		loadClipboardAsStaticToUse();
		createOrLoadUserShortCutPreference();
		writeListOfKeyAvailaibleAsFileForUser();
		createTheBatchFileForWindow();
		launchThreadThatListenToUDPToCreateQueue();
		if (useRefreshConsoleState)
			StartRefreshUIThread();
		executer = new ExecuteCommandWithRobot();

		Thread.currentThread().setPriority(threadMainPriority);
		
		CommandParser parser = new CommandParser(keysUserShortcut);
		int packageCount = 0;
		while (true) {
			packageCount = getPackageWaitingCount();
			numberInTheQueue = "" + packageCount;
			if (packageCount > 0) {
				if (recievedPackaged == false)
					recievedPackaged = true;
				String packageToProcess = dequeueNextPackage();

				if(CDebug.use) {
					System.out.println("Deqeue:" + packageToProcess.trim());
					System.out.println("InWaiting:" + executeLaterStack.size());
				}
				if (locker.length() > 0 && !packageToProcess.startsWith(locker)) {
					if (CDebug.use)
						System.out.println(
								String.format("Deny (%s,%s):%s", locker, "" + lockDenyCount, packageToProcess));
					lockDenyCount++;
					packageToProcess = "";
				} else {

					if (locker.length() > 0) {

						packageToProcess = packageToProcess.substring(locker.length());

						if (CDebug.use)
							System.out.println("After Locker:" + packageToProcess.trim());
					}
				}
				// long a = ZonedDateTime.now().toInstant().toEpochMilli();
				lastPackage = packageToProcess;

				if (packageToProcess.length() > 0) {

					ArrayList<RobotCommand> cmds = parser.getCommandsFrom(packageToProcess);
					// System.out.println("Command found:"+cmds.size());
					for (int i = 0; i < cmds.size(); i++) {

						if (cmds.get(i).hasExecuteTime()) {
							executeLaterStack.add(cmds.get(i));
						} else {
							ExecuteCommand(cmds.get(i));
						}

						lastValidate = cmds.get(i).toString();
						// validateHistory= lastValidate+"\n\r"+validateHistory;
						// if(validateHistory.length()>2000)
						// validateHistory= validateHistory.substring(0,2000);

					}
					// long b = ZonedDateTime.now().toInstant().toEpochMilli();
					// System.out.println("M: " + (b-a));
				}

				// setTextDisplayed(String.format("Package:\t%s\nValide:\t%s\n\nHistory:\n%s\n",lastValidate,
				// lastValidate, validateHistory),true);

			}

			for (int i = 0; i < executeLaterStack.size(); i++) {
				if (executeLaterStack.get(i).isCommandReadyToBeExecuted()) {
					ExecuteCommand(executeLaterStack.get(i));
					hasBeenExecuted.add(executeLaterStack.get(i));
				}
			}
			for (int i = 0; i < hasBeenExecuted.size(); i++) {
				executeLaterStack.remove(hasBeenExecuted.get(i));
			}
			hasBeenExecuted.clear();
			Thread.sleep(1);

		}

		
	}
	private static void createTheBatchFileForWindow() {
		String path = "StartJOMI.bat";
		File f = new File(path);
		if (!f.exists()) {
			writeFile(path, "Title \"OMI (Java Runtime)\"\r\n"
					+ "java -jar -Xmx1g -Xms64m jomi.jar 2501\r\n"
					+ "#pause");
		}
		
		path = "StopJOMI.bat";
		f = new File(path);
		if (!f.exists()) {
			writeFile(path, "wmic Path win32_process Where \"CommandLine Like '%%JOMI.jar%%'\" Call Terminate\r\n"
					+ "timeout 10");

			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			    try {
					Desktop.getDesktop().browse(new URI("https://openmacroinput.page.link/jomifirstrun"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			keepThreadAlive=false;
			
			System.exit(0);
		}

	}
	protected void finalize() throws Throwable {

		keepThreadAlive=false;
		System.out.println("Finalized");
	}
	public static LinkedList<RobotCommand> hasBeenExecuted = new LinkedList<RobotCommand>();

	private static void ExecuteCommand(RobotCommand cmd) {
		try {

			executer.execute(cmd);
			// System.out.print("Executed:"+cmd.toString());
		} catch (Exception e) {
			if (CDebug.use)
				System.out.print("Fail to execute:" + cmd.toString() + "\n" + e.getStackTrace());

			// OpenMacroInputJavaRuntime. DisplayException(e);
		}
	}

	public static void DisplayException(Exception stack) {

		DisplayException(stack.getMessage(), 10000);
	}

	public static void DisplayException(Exception stack, long sleepTime) {

		DisplayException(stack.getMessage(), sleepTime);
	}

	public static String exceptionHappened = "";

	public static void DisplayException(String stack, long sleepTime) {
		exceptionHappened = "EXCEPTION:+\\n\"+ stack";
		// try {
		// Thread.sleep(sleepTime);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// OpenMacroInputJavaRuntime. DisplayException(e);
		// }
		exceptionHappened = "";

	}

	public static String getTextIpPortDescription() {
		try {
			return "IP: " + InetAddress.getLocalHost() + " Port:" + port + "\n";
		} catch (UnknownHostException e) {
			OpenMacroInputJavaRuntime.DisplayException(e);
			return "IP: 127.0.0.1 Port:" + port + "\n";
		}
	}

	public static void setTextDisplayed(String text, boolean withIpInfo) {
		if (useConsole) {

			if (withIpInfo)
				System.out.println(getTextIpPortDescription() + getSupportLink() + "\n" + text.trim());
			else
				System.out.println(text.trim());
		} else {
		//	if (withIpInfo)
			//		jTextArea.setText(getTextIpPortDescription() + getSupportLink() + "\n" + text.trim());
			//else
			//	jTextArea.setText(text.trim());

		}

		// frame.update(frame.getGraphics());
		// jTextArea.update(jTextArea.getGraphics());
	}

	private static String getSupportLink() {
		return "Hope this tool is helping you. " + MyUnicodeChar.youRock + " \n" + "https://openmacroinput.com\n";
	}

	private static void createOrLoadUserShortCutPreference() throws IOException {
		String keysShortcutTable = GetWantedShortCutOfUserFromFile();
		keysUserShortcut = new KeyEventIdPool(keysShortcutTable);
	}

	public static synchronized int getPackageWaitingCount() {
		return m_receivedPackage.size();
	}

	public static synchronized String dequeueNextPackage() {
		return m_receivedPackage.remove(0);
	}

	public static LinkedList<String> m_receivedPackage = new LinkedList<String>();

	public static synchronized void addPackageToWait(String text) {
		m_receivedPackage.add(text);

	}

	private static void StartRefreshUIThread() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				// try {
				while (true) {
					if (useRefreshConsoleState) {

						if (recievedPackaged) {

							if (exceptionHappened.length() > 0) {
								setTextDisplayed(exceptionHappened, true);
							} else {
								setTextDisplayed(String.format("Package (%s):\t%s\nValide:\t%s\n\nHistory:\n%s\n",
										OpenMacroInputJavaRuntime.numberInTheQueue,
										OpenMacroInputJavaRuntime.lastValidate, OpenMacroInputJavaRuntime.lastValidate,
										OpenMacroInputJavaRuntime.validateHistory), true);

							}
						}
						try {
							Thread.currentThread().sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				// }
				// catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

			}

		});
		t.setPriority(threadUDPPriority);
		t.start();

	}
	

	private static void launchThreadThatListenToUDPToCreateQueue() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				DatagramSocket server = null;
				try {

					if (CDebug.use)
						System.out.println("" + MyUnicodeChar.press + MyUnicodeChar.release + MyUnicodeChar.stroke
								+ MyUnicodeChar.youRock + MyUnicodeChar.split);

					server = new DatagramSocket(port);
					byte[] buffer = new byte[8192];
					Charset cd = Charset.forName("UTF-8");
					String str = "";
					while (keepThreadAlive) {

						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
						server.receive(packet);
						str = new String(packet.getData(), cd);

						if (CDebug.use)
							System.out.println("P(" + m_receivedPackage.size() + "):" + str);
						addPackageToWait(str);
						packet.setLength(buffer.length);
						buffer= new byte[8192];
						//System.out.println("-To-");
					}

				} catch (SocketException e) {
					OpenMacroInputJavaRuntime.DisplayException(e);
					e.printStackTrace();
					if (server != null)
						server.close();
					keepThreadAlive=false;
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.exit(0);
				} catch (IOException e) {
					OpenMacroInputJavaRuntime.DisplayException(e);
					e.printStackTrace();
					if (server != null)
						server.close();
					keepThreadAlive=false;
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.exit(0);
				} finally {

					
				}
			}

		});

		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
	}

	private static void loadClipboardAsStaticToUse() {
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	//public static JFrame frame;

	private static void displayFrameToDebugAndCloseProcess(String[] args) {

		try {
			String textInitToDisplay = "IP: " + InetAddress.getLocalHost() + " Port:" + port + "\n" + "Args: "
					+ StringPlus.join(" ", args) + "\n" + getSupportLink() + "\n"
					+ "Commands (P ress, R elease, S troke) :\n" + "- ks:[keyname:string]:\n" + "- ms:[0,1,2]\n"
					+ "- wh:[wheel:int]\n" + "- mm:[x:int]:[y:int]\n" + "- ct:[text]\n"
					+ "Code: https://openmacroinput.page.link/jomicode\n"
					+ "Support the project: https://openmacroinput.page.link/support\n"
					+ "Hello World Tutorial: https://openmacroinput.page.link/hellojomi\n"
					+ "Discord: https://eloistree.page.link/discord\n"
					+ "Version: "+lastVersionDate+"\n\n"
					+ "The application is running...\n(Close it when you done with it)\n"
					;

			if (useConsole) {
				System.out.println("Open Macro Input (Java Runtime)");
				System.out.println(textInitToDisplay);

			} else {

				//	frame = new JFrame("Open Macro Input (Java Runtime)");
				//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				//jTextArea = new JTextArea();
				//jTextArea.setBackground(new Color(249f / 255f, 104f / 255f, 84f / 255f, 1f));

				//frame.getContentPane().add(jTextArea);
				//frame.pack();

				//frame.setLocationRelativeTo(null);
				//frame.setVisible(true);
			}
		}

		catch (Exception e1) {
			OpenMacroInputJavaRuntime.DisplayException(e1);
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		String path = "KeyShortcut.txt";
		File f = new File(path);
		if (!f.exists()) {

			writeFile(path, KeyEventId.GetDefaultKeysShortcutTableAsText());
			// StringPlus.join("\n", KeyEventId.GetAllEnumNames());
		}
		return readFile(path);
	}

	private static void writeListOfKeyAvailaibleAsFileForUser() throws IOException {
		String path = "AllStrokableKeys.txt";
		File f = new File(path);
		if (!f.exists()) {
			writeFile(path, StringPlus.join("\n", KeyEventId.GetAllEnumNames()));
		}
	}

	public static void writeFile(String absolutePath, String text) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(absolutePath));
			writer.write(text);

		} catch (IOException e) {
			OpenMacroInputJavaRuntime.DisplayException(e);
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				OpenMacroInputJavaRuntime.DisplayException(e);
			}
		}

	}

	public static String readFile(String absolutePath) {
		String content = "";

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(absolutePath));
			int data = reader.read();
			while (data > -1) {
				char c = (char) data;
				content += c;
				data = reader.read();

			}

		} catch (IOException e) {
			OpenMacroInputJavaRuntime.DisplayException(e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				OpenMacroInputJavaRuntime.DisplayException(e);
			}
		}

		return content;
	}

	public static synchronized void print(String str) {

		if (CDebug.use)
			System.out.print(str);
	}

	public static synchronized void println(String str) {

		if (CDebug.use)
			System.err.println(str);
	}

}