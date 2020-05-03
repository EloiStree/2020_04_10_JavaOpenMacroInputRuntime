package be.eloistree.openmacroinput.command;

public class MouseMoveCommand extends RobotCommand {

	public enum MoveTypeValue{InPixel, InPourcent}
	public enum MoveType{Set, Add}
	public MoveTypeValue m_moveTypeValueHorizontal;
	public MoveTypeValue m_moveTypeValueVertical;
	public MoveType m_moveType;
	public float m_leftToRight;
	public float m_botToTop;
	
	public MouseMoveCommand(MoveType moveType, float leftToRight,MoveTypeValue moveTypeHorizontal, float botToTop,MoveTypeValue moveTypeVertical) {
		super();
		this.m_moveType = moveType;
		this.m_moveTypeValueHorizontal = moveTypeHorizontal;
		this.m_leftToRight = leftToRight;
		this.m_moveTypeValueVertical = moveTypeVertical;
		this.m_botToTop = botToTop;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s:%s %s%s-%s%s", "Mouse Move",m_moveType.toString(),m_leftToRight, m_moveTypeValueHorizontal.toString(),m_botToTop, m_moveTypeValueVertical.toString());
	}
	
}
