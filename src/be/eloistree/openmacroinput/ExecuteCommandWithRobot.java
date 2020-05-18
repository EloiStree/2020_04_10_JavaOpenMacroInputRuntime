package be.eloistree.openmacroinput;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import be.eloistree.copyfromweb.ImageToClipboard;
import be.eloistree.openmacroinput.OsUtility.OS;
import be.eloistree.openmacroinput.command.CopyPastCommand;
import be.eloistree.openmacroinput.command.CopyPastCommand.Type;
import be.eloistree.openmacroinput.command.EmbraceCommand;
import be.eloistree.openmacroinput.command.EmbracePerLineCommand;
import be.eloistree.openmacroinput.command.ImageURLToClipboardCommand;
import be.eloistree.openmacroinput.command.KeyStrokeCommand;
import be.eloistree.openmacroinput.command.KillTheProgramCommand;
import be.eloistree.openmacroinput.command.MouseClickCommand;
import be.eloistree.openmacroinput.command.MouseMoveCommand;
import be.eloistree.openmacroinput.command.MouseMoveCommand.MoveType;
import be.eloistree.openmacroinput.command.MouseMoveCommand.MoveTypeValue;
import be.eloistree.openmacroinput.convertiontables.KeyEventAsString;
import be.eloistree.openmacroinput.command.MouseScrollCommand;
import be.eloistree.openmacroinput.command.OpenURLCommand;
import be.eloistree.openmacroinput.command.PastCommand;
import be.eloistree.openmacroinput.command.RobotCommand;
import be.eloistree.openmacroinput.command.UnicodeCommand;
import be.eloistree.openmacroinput.command.WindowCmdLineToExecuteCommand;
import be.eloistree.openmacroinput.enums.PressType;
import be.eloistree.openmacroinput.window.CmdUtility;
import be.eloistree.string.StringPlus;

public class ExecuteCommandWithRobot {

	public Robot robot;
	public Clipboard clipboard;
	public Toolkit toolkit;
	public CmdUtility cmdUtility;
	
	public ExecuteCommandWithRobot() {
		
		try {
			robot = new Robot();
			cmdUtility= new  CmdUtility(false, false);
			clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			toolkit = Toolkit.getDefaultToolkit();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void execute(RobotCommand cmd) {
		if( cmd instanceof  PastCommand ) execute((PastCommand)cmd);
		else if( cmd instanceof  KeyStrokeCommand ) execute((KeyStrokeCommand)cmd);
		else if( cmd instanceof  KillTheProgramCommand ) execute((KillTheProgramCommand)cmd);
		else if( cmd instanceof  MouseClickCommand ) execute((MouseClickCommand)cmd);
		else if( cmd instanceof  MouseMoveCommand ) execute((MouseMoveCommand)cmd);
		else if( cmd instanceof  MouseScrollCommand ) execute((MouseScrollCommand)cmd);
		else if( cmd instanceof  OpenURLCommand ) execute((OpenURLCommand)cmd);
		else if( cmd instanceof  EmbraceCommand ) execute((EmbraceCommand)cmd);
		else if( cmd instanceof  CopyPastCommand ) execute((CopyPastCommand)cmd);
		else if( cmd instanceof  EmbracePerLineCommand ) execute((EmbracePerLineCommand)cmd);
		else if( cmd instanceof  UnicodeCommand ) execute((UnicodeCommand)cmd);	
		else if( cmd instanceof  WindowCmdLineToExecuteCommand ) execute((WindowCmdLineToExecuteCommand)cmd);
		else if( cmd instanceof  ImageURLToClipboardCommand ) execute((ImageURLToClipboardCommand)cmd);
		else {
			System.out.println("Command not take in charge: "+cmd);
		}
		
	}
	ImageURLToClipboardCommand c;
	public void execute(ImageURLToClipboardCommand cmd) {
		if(cmd==null) return;
		c=  cmd;
		Thread thread= new Thread() {
		      public void run() {
		  		new ImageToClipboard(c.getUrl());
		        }
		      };
		      thread.start();	
	}
	
	public void execute(UnicodeCommand cmd) {
		if(cmd==null) return;
		PastText(cmd.getUnicodeAsString());
	}
	String s ="";
	public void execute(WindowCmdLineToExecuteCommand cmd) {
		if(cmd==null) return;
		try {
			for (String str : cmd.GetCommandLines()) {
				s =str;
				Thread thread= new Thread() {
				      public void run() {
							cmdUtility.execute(s);
				        }
				      };
				      thread.start();				
			}
		}catch(Exception e) {
			System.err.println("Fail to executre cmd:"+StringPlus.join(" ", cmd.GetCommandLines()));
		}
		
	}
	
	public void execute(KeyStrokeCommand cmd) {
		if(cmd.m_pressType==PressType.Stroke || cmd.m_pressType==PressType.Press ) {
			
			robot.keyPress (GetIdFrom(cmd.m_javaKeyName));
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			if(cmd.m_pressType==PressType.Stroke || cmd.m_pressType==PressType.Release )
			{				
				robot.keyRelease(GetIdFrom(cmd.m_javaKeyName));
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
	}
	
	private int GetIdFrom(String javaKeyName) {
		return KeyEventAsString.ConvertStringToKeyEvent(javaKeyName);
	}
	public void execute(KillTheProgramCommand cmd) {
		System.exit(0);
	}
	
	public void execute(MouseClickCommand cmd) {
		if(cmd.getPressType()==PressType.Stroke || cmd.getPressType()==PressType.Press )
		robot.mousePress((cmd.getButtonJavaId()));
		if(cmd.getPressType()==PressType.Stroke || cmd.getPressType()==PressType.Release )
		robot.mouseRelease((cmd.getButtonJavaId()));
	}
	public int GetIdFrom(MouseClickCommand.MouseButton mouseButtonType) {
		if(mouseButtonType==MouseClickCommand.MouseButton.Left )
			return 0;
		if(mouseButtonType==MouseClickCommand.MouseButton.Middle )
		    return 1;
		if(mouseButtonType==MouseClickCommand.MouseButton.Right )
			return 2;
		return 0;
	}
	public void execute(MouseMoveCommand cmd) {

		 float x_L2R=cmd.m_leftToRight; 
		 float y_B2T=cmd.m_botToTop;
		 Dimension screenSize =toolkit.getScreenSize();
			
		 if(cmd.m_moveTypeValueHorizontal==MoveTypeValue.InPourcent)
			 x_L2R*= screenSize.width;
		 if(cmd.m_moveTypeValueVertical==MoveTypeValue.InPourcent)
			 y_B2T*= screenSize.height;
		 
		 if(cmd.m_moveType==MoveType.Add) {
			 Point p =MouseInfo.getPointerInfo().getLocation();
			 x_L2R+=p.x; 
			 y_B2T+=p.y;
		 }
		  
		  robot.mouseMove((int)x_L2R,(int) y_B2T);
	}
	public void execute(MouseScrollCommand cmd) {
		
		robot.mouseWheel(cmd.m_scollForwardCount);
	}

	public void execute(OpenURLCommand cmd) {
		
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
		    try {
				Desktop.getDesktop().browse(new URI(cmd.m_url));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
public void execute(PastCommand cmd) {
		
		boolean messageWrite = false;
		while (!messageWrite) {
			try {

					PastText(cmd.m_textToPast);

				messageWrite = true;
			} catch (IllegalStateException e) {
				System.out.println("Did not send message... Retry");

			
				messageWrite = false;
			}

		}


		
}

public void execute(EmbracePerLineCommand cmd) {
	
	String text =CopyText();
	String toPast="";
	String [] lines = text.split("\n");
	for (int i = 0; i < lines.length; i++) {
		toPast =
				cmd.m_textLeft +
				lines[i] +
				cmd.m_textRight+
				'\n';
		PastText(toPast);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
	//System.out.println("<"+toPast);
	
}

public void execute(CopyPastCommand cmd) {
	
		try {

			if(cmd.m_copyPastType==Type.Copy)
				CopyText();
			if(cmd.m_copyPastType==Type.Cut)
				CutText();
			if(cmd.m_copyPastType==Type.Past)
				PastText(null);

		} catch (IllegalStateException e) {
			System.out.println("Did not send message... Retry");

		
		}

	


	
}

public void execute(EmbraceCommand cmd) {
	
	boolean messageWrite = false;
	String cut = CutText();
	while (!messageWrite) {
		try {
			
			
			PastText(cmd.m_textLeft);
			PastText(cut);
			PastText(cmd.m_textRight);

			messageWrite = true;
		} catch (IllegalStateException e) {
			System.out.println("Did not send message... Retry");

		
			messageWrite = false;
		} 

	}


	
}
	private void PastText(String txt ) {
		if(txt==null || txt.length()<0) {
			
			try {
				txt = (String) clipboard.getData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		StringSelection text = new StringSelection(txt);
		try {
		    clipboard.setContents(text, text);
		if (OsUtility.getOS() == OS.MAC) {
		
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_V);	
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_META);
		
		} else {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
		
		}
		Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String CopyText() {
	
		if (OsUtility.getOS() == OS.MAC) {
		
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_C);	
				robot.keyRelease(KeyEvent.VK_C);
				robot.keyRelease(KeyEvent.VK_META);
		
		} else {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_C);
				robot.keyRelease(KeyEvent.VK_C);
				robot.keyRelease(KeyEvent.VK_CONTROL);
		
		}
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String txt="";
		try {
			txt = (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return txt;
	}
	private String CutText() {
	
		if (OsUtility.getOS() == OS.MAC) {
		
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_X);	
				robot.keyRelease(KeyEvent.VK_X);
				robot.keyRelease(KeyEvent.VK_META);
		
		} else {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_X);
				robot.keyRelease(KeyEvent.VK_X);
				robot.keyRelease(KeyEvent.VK_CONTROL);
		
		}
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String txt="";
		try {
			txt = (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return txt;
	}
}
