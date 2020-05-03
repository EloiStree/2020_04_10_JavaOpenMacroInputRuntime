package be.eloistree.openmacroinput.command;

import be.eloistree.openmacroinput.enums.PressType;

public class KeyStrokeCommand extends RobotCommand {

	public String m_javaKeyName;
	public PressType m_pressType;
	public KeyStrokeCommand(String javaKeyName, PressType pressType) {
		super();
		this.m_javaKeyName = javaKeyName;
		this.m_pressType = pressType;
	}
	@Override
	public String toString() {
		return String.format("%s:%s-%s", "Key Stroke:",m_javaKeyName, m_pressType.toString());
	}
}
