package be.eloistree.openmacroinput.command;

import from.web.code.StringUnicodeEncoderDecoder;

public class UnicodeCommand extends RobotCommand{

	 int m_unicodeId;
	public UnicodeCommand(int unicodeId) {
		m_unicodeId = unicodeId;
	}
	public String getUnicodeAsString() {
		
		return StringUnicodeEncoderDecoder.decodeUnicodeSequenceToString( "\\u" + Integer.toHexString(m_unicodeId) );
		 
		
	}
	
	public int GetUnicodeAsInteger() {
		return m_unicodeId;
	}
	@Override
	public String toString() {
		return String.format("%s:%s", "Unicode",getUnicodeAsString());
	}
}
