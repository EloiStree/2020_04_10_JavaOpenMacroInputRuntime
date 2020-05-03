package be.eloistree.openmacroinput.command;

public class OpenURLCommand extends RobotCommand{

	public String m_url;

	public OpenURLCommand(String url) {
		super();
		this.m_url = url;
	}
	@Override
	public String toString() {
		return String.format("%s:%s", "Open URL",m_url);
	}
}
