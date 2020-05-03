package be.eloistree.openmacroinput.command;

import be.eloistree.openmacroinput.enums.PressType;

public class MouseClickCommand extends RobotCommand {

	public enum MouseButton {Left, Middle, Right}
	public MouseButton  m_buttonType;
	public PressType m_pressType;
	public MouseClickCommand(MouseButton buttonType, PressType pressType) {
		super();
		this.m_buttonType = buttonType;
		this.m_pressType = pressType;
	}
	@Override
	public String toString() {
		return String.format("%s:%s-%s", "Mouse Click",m_buttonType.toString(), m_pressType.toString());
	}
}
