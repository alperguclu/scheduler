package scheduler.jobs;

import java.text.SimpleDateFormat;
import java.util.Date;

import scheduler.ScheduledJob;

public class Delay extends ScheduledJob{	
	
	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());	
		System.out.println("Delay! "+formatter.format(date));
		
		super.done();
		super.log();
	}

}
