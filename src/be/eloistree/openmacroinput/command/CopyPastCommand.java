package be.eloistree.openmacroinput.command;

public class CopyPastCommand extends RobotCommand{
	public enum Type {Copy, Past, Cut}
	
	public Type m_copyPastType;

	public CopyPastCommand(Type copyPastType) {
		super();
		this.m_copyPastType = copyPastType;
	}
	
	@Override
	public String toString() {
		return String.format("%s:%s", "Copy Past", m_copyPastType.toString());
	}

}
