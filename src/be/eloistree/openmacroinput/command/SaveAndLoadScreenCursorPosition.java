package be.eloistree.openmacroinput.command;

public class SaveAndLoadScreenCursorPosition  extends RobotCommand {
	public SaveAndLoadScreenCursorPosition(String name, ActionType actionType) {
		this.screenPositionName= name;
		this.actionType= actionType;
	}
	public enum ActionType{ Save , Load}
	public String screenPositionName;
	public ActionType actionType;
}
