package scheduler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import scheduler.model.Job;

public class DailyJobRunner extends TimerTask {

	@Override
	public void run() {
		System.out.println("Checking daily jobs...");
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+3"));
		List<Job> jobs = JobScheduler.em.createNamedQuery("Job.findAll", Job.class).getResultList();
		int scheduled = 0;
		
		for(Job job : jobs) {
			if(job.getPeriod().equals(0l)) {
				System.out.println(job);

				List<String> days = Arrays.asList(job.getDays().split(","));
				
				if(days.contains(now.getDayOfWeek().toString())) {
					Long delay = JobScheduler.calculateDelay(job.getHour().toLocalTime());
					
					if(delay>0) {
						
						try {
							Class<?> clazz = Class.forName(job.getName());
							ScheduledJob task = (ScheduledJob) clazz.newInstance();
							task.setJob(job);
							
							JobScheduler.timer.schedule(task, delay);
							scheduled++;
							
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		System.out.println("Daily jobs checked. "+scheduled+" jobs scheduled");
	}

}
