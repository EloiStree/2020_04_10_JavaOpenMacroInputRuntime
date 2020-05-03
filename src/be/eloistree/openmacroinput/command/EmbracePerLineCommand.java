package be.eloistree.openmacroinput.command;

public class EmbracePerLineCommand extends RobotCommand {
	public String m_textLeft="";
	public String m_textRight="";
	public EmbracePerLineCommand(String textLeft, String textRight) {
		super();
		if(textLeft==null)
			textLeft="";
		if(textRight==null)
			textRight="";
		this.m_textLeft = textLeft;
		this.m_textRight = textRight;
	}
	@Override
	public String toString() {
		return String.format("%s:%d-%d", "Embrace Per Line",m_textLeft.length(), m_textRight.length());
	}
}
