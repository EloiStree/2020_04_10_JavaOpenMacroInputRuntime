package be.eloistree.openmacroinput.command;

import be.eloistree.openmacroinput.StringPlus;

public class WindowCmdLineToExecuteCommand extends RobotCommand{
	String [] m_commandLines;
	public WindowCmdLineToExecuteCommand(String []cmdLines) {
		m_commandLines = new String[cmdLines.length];
		for (int i = 0; i < cmdLines.length; i++) {
			m_commandLines[i]=cmdLines[i].trim();
		}
		
	}
	public String[] GetCommandLines() {
		return m_commandLines;
	}
	@Override
	public String toString() {
		return String.format("%s:%s", "CMD",StringPlus.join(" || ", m_commandLines));
	}
}
