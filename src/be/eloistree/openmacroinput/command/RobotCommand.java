package be.eloistree.openmacroinput.command;

import java.util.Calendar;
import java.util.Date;

import be.eloistree.time.ExecuteTime;

public abstract class RobotCommand {

	private ExecuteTime m_whenToExecute=null;

	public void setTimeToExecute(ExecuteTime whenToDo) {
		m_whenToExecute = whenToDo;
	}
	public boolean hasExecuteTime() {
		return m_whenToExecute!=null;
	}

	public boolean isCommandReadyToBeExecuted(Date timeNow) {
		if(!hasExecuteTime())
			return true;
		return m_whenToExecute.isTimePast(timeNow);
		
	}
	public boolean isCommandReadyToBeExecuted() {
		if(!hasExecuteTime())
			return true;
		return isCommandReadyToBeExecuted(new Date());
		
	}
	 public Calendar getAsCalendarCopy() {
		 if(m_whenToExecute!=null && m_whenToExecute.m_whenToExecute!=null)
			 return m_whenToExecute.m_whenToExecute; 
		 else 
			 return null;
		 };
	

}
