package scheduler.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import scheduler.ScheduledJob;

public class Tick extends ScheduledJob{	
	
	@Override
	public void run() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());		
		System.out.println("Tick! "+formatter.format(date));
	}

}
