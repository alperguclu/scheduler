package scheduler;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimerTask;

import scheduler.model.Job;
import scheduler.model.JobLog;

public class ScheduledJob extends TimerTask  {
	protected Job job;
	
	public Job getJob() {
		return this.job;
	}
	
	public void setJob(Job job) {
		this.job=job;
	}
	
	public void log() {
		if(job.getLogEnabled()) {
			JobScheduler.em.getTransaction().begin();
			
			JobLog sjl = new JobLog();
			sjl.setJob(job);
			sjl.setTime(job.getLastFired());
			sjl.setLog("Job: "+job.getName()+ " finished at "+job.getLastFired());
			JobScheduler.em.persist(sjl);	
			
			JobScheduler.em.getTransaction().commit();
		}
	}

	public void done() {
		JobScheduler.em.getTransaction().begin();
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+3"));
		job.setLastFired(Timestamp.from(now.toInstant()));
		
		if(!job.getPeriod().equals(0l)) {
			job.setNext(Timestamp.from(now.plusSeconds(job.getPeriod()).toInstant()));
		}

		JobScheduler.em.getTransaction().commit();
	}

	@Override
	public void run() {
		try {
			Class<?> clazz = Class.forName(job.getName());
			ScheduledJob task = (ScheduledJob) clazz.newInstance();
			
			task.setJob(job);
			task.run();
			
			task.done();
			task.log();
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
