package be.eloistree.openmacroinput.command;

import java.util.ArrayList;

public class IntegerRobotCommand extends RobotCommand{

	public String m_integerToCall;
	public ArrayList<String> m_commandsToCall;
	
	public IntegerRobotCommand(String integerToCall) {
		super();
		this.m_integerToCall= integerToCall;
		
	}	
	public IntegerRobotCommand(String integerToCall, ArrayList<String>cmds ) {
		super();
		this.m_integerToCall= integerToCall;
		this.m_commandsToCall = cmds;
		
	}
	
	
	@Override
	public String toString() {
		return String.format("%s %d: %s", "Integer",m_integerToCall.length(), m_commandsToCall.size());
	}
	
}