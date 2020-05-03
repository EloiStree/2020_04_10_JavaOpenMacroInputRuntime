package be.eloistree.openmacroinput.command;

public class EmbraceCommand extends RobotCommand{

	public String m_textLeft;
	public String m_textRight;
	public EmbraceCommand(String textLeft, String textRight) {
		super();
		this.m_textLeft = textLeft;
		this.m_textRight = textRight;
	}
	 
	@Override
	public String toString() {
		return String.format("%s:%d-%d", "Embrace",m_textLeft.length(), m_textRight.length());
	}
}
