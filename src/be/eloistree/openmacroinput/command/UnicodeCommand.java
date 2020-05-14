package be.eloistree.openmacroinput.command;

public class UnicodeCommand extends RobotCommand{

	 int m_unicodeId;
	public UnicodeCommand(int unicodeId) {
		m_unicodeId = unicodeId;
	}
	public String getUnicodeAsString() {
		return Character.toString((char)m_unicodeId);
		
	}
	
	public int GetUnicodeAsInteger() {
		return m_unicodeId;
	}
	@Override
	public String toString() {
		return String.format("%s:%s", "Unicode",getUnicodeAsString());
	}
}
