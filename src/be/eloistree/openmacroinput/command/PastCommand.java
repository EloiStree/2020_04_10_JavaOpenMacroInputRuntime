package be.eloistree.openmacroinput.command;

public class PastCommand extends RobotCommand{
	
	public String m_textToPast;

	public PastCommand(String textToPast) {
		super();
		this.m_textToPast = textToPast;
	}
	@Override
	public String toString() {
		return String.format("%s %d: %s", "Past",m_textToPast.length(), Clamp(10,m_textToPast));
	}
	public String Clamp(int lenghtAllow, String text) {
		if(text.length()<=lenghtAllow)
			return text;
		return text.substring(0,lenghtAllow-1)+"...";
		
		
	}
}
