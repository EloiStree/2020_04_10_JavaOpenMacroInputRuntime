package be.eloistree.openmacroinput.command;

import java.awt.event.InputEvent;

import be.eloistree.openmacroinput.enums.PressType;

public class MouseClickCommand extends RobotCommand {

	public enum MouseButton {Left, Middle, Right}
	 MouseButton  m_buttonType;
	 PressType m_pressType;
	public MouseClickCommand(MouseButton buttonType, PressType pressType) {
		super();
		this.m_buttonType = buttonType;
		this.m_pressType = pressType;
	}
	public int getButtonId() {
		switch(m_buttonType) {
			case Middle: return 1;
			case Right: return 2;
			default: return 0;
		}
		
	}public int getButtonJavaId() {
		switch(m_buttonType) {
		case Middle: return InputEvent.BUTTON2_DOWN_MASK;
		case Right: return  InputEvent.BUTTON3_DOWN_MASK;
		default: return  InputEvent.BUTTON1_DOWN_MASK;
	}
	
}
	public PressType getPressType() {return m_pressType;}
	public MouseButton getMouseButtonType() {return m_buttonType;}
	
	@Override
	public String toString() {
		return String.format("%s:%s-%s", "Mouse Click",m_buttonType.toString(), m_pressType.toString());
	}
}
