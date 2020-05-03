package be.eloistree.openmacroinput.command;

public class MouseScrollCommand extends RobotCommand {

	public int m_scollForwardCount  ;
	public MouseScrollCommand(int scrollForwardCount) {
		super();
		this.m_scollForwardCount = scrollForwardCount;
	}
	@Override
	public String toString() {
		return String.format("%s:%d", "Mouse Scroll",m_scollForwardCount);
	}
}
