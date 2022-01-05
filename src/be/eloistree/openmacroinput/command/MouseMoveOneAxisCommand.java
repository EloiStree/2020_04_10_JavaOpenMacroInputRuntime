package be.eloistree.openmacroinput.command;

import be.eloistree.openmacroinput.command.MouseMoveCommand.MoveType;
import be.eloistree.openmacroinput.command.MouseMoveCommand.MoveTypeValue;

public class MouseMoveOneAxisCommand extends RobotCommand {
	
	public enum MoveAxisType{
		Left2Right,
		Right2Left,
		Bot2Top,
		Top2Bot
		
	}
	public MoveTypeValue m_moveTypeValueVertical;
	public MoveType m_moveType;
	public MoveAxisType m_moveAxisType;
	public float m_axisMoveValue;
	
	public MouseMoveOneAxisCommand(MoveType moveType, MoveTypeValue moveTypeValue,MoveAxisType axis , float value) {
		super();
		this.m_moveType = moveType;
		this.m_moveTypeValueVertical = moveTypeValue;
		this.m_moveAxisType = axis;
		this.m_axisMoveValue = value;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s:%s %s %s", "Mouse Axis Move"
				,m_moveType.toString()
				,m_moveTypeValueVertical
				, m_moveAxisType,m_axisMoveValue);
	}
}
