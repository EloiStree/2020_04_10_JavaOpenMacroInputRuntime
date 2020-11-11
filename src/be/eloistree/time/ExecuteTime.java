package be.eloistree.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExecuteTime {
	
	public Calendar m_whenToExecute;
	
	
	public ExecuteTime() {
		 
		nowWithMilliSeconds(0); 
		
	}public ExecuteTime(Calendar whenToExecute) {
		m_whenToExecute = whenToExecute;
		
	}
	public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public String getDescription() {
		return   formatter.format(m_whenToExecute );
		
	}
	
	
	public boolean isItTime(Date startExlusive, Date endExclusive) {
		
		return m_whenToExecute.after(startExlusive) && m_whenToExecute.before(endExclusive);
	}
	public boolean isTimePast( Date now) {

		//System.out.println("DT: "+ getTimeLeft( now));
		return getTimeLeft(now)<=0;
	}
	private long getTimeLeft(Date timeNow) {
		  long lGameDate = m_whenToExecute.getTime().getTime();
		  long lcurrDate = timeNow.getTime();
		  return lGameDate - lcurrDate;
	}


	public static ExecuteTime nowWithMilliSeconds(int milliseconds ) {
	      Calendar cl = Calendar. getInstance();
	      cl.setTime(new Date());
	      cl.add(Calendar.MILLISECOND, milliseconds);
	      return new ExecuteTime(cl);
		
	}
	
	public static ExecuteTime timeOfTheDay(int hours, int minutes, int seconds, int milliseconds ) {
	      Calendar calendar = Calendar. getInstance();
	      calendar.setTime(new Date());
	      calendar.set(Calendar.MILLISECOND, milliseconds);
	        calendar.set(Calendar.SECOND, seconds);
	        calendar.set(Calendar.MINUTE, minutes);
	        calendar.set(Calendar.HOUR_OF_DAY,hours);
	      return new ExecuteTime(calendar);
	}
	public static ExecuteTime fromCalendar(Calendar whenToExecute) { 
		 return new ExecuteTime(whenToExecute);
	}
	
}
