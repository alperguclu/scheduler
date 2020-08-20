package scheduler;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.EntityManager;

import scheduler.model.Job;

public class JobScheduler {
	public static Timer timer;
	private List<Job> jobs;
	public static List<ScheduledJob> scheduledJobs = new ArrayList<ScheduledJob>();
	public static EntityManager em;
	private TimerTask dailyJobRunner;

	public JobScheduler(EntityManager em) {
		JobScheduler.em=em;
		timer = new Timer();
		jobs = new ArrayList<Job>();
	}

	public long calculateDelay(Job job) {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+3"));
		ZonedDateTime nextRun = ZonedDateTime.ofInstant(job.getNext().toInstant(), ZoneId.of("GMT+3"));

		long delay = 0;

		if (now.toInstant().compareTo(nextRun.toInstant()) < 0) {
			Duration duration = Duration.between(now, nextRun);
			delay = duration.getSeconds() * 1000;
		}

		return delay;
	}
	
	public static long calculateDelay(LocalTime hour) {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+3"));
		ZonedDateTime nextRun = now.with(hour);

		long delay = 0;

		if (now.toInstant().compareTo(nextRun.toInstant()) < 0) {
			Duration duration = Duration.between(now, nextRun);
			delay = duration.getSeconds() * 1000;
		}

		return delay;
	}

	public void addJob(Job job) {		
		jobs.add(job);

		ScheduledJob scheduledJob = new ScheduledJob();
		scheduledJob.setJob(job);
		
		if(!job.getPeriod().equals(0l)) {
			timer.scheduleAtFixedRate(scheduledJob, this.calculateDelay(job), job.getPeriod() * 1000);
		}
	}

	public void start() {		
		for (ScheduledJob scheduledJob : JobScheduler.scheduledJobs) {
			if(!scheduledJob.getJob().getPeriod().equals(0l)) {
				timer.scheduleAtFixedRate(scheduledJob, this.calculateDelay(scheduledJob.getJob()), scheduledJob.getJob().getPeriod() * 1000);
			}
		}
		
		this.dailyJobRunner = new DailyJobRunner();
		timer.scheduleAtFixedRate(dailyJobRunner, 1000, 900 * 1000);
	}

	public void stop() {
		for (ScheduledJob scheduledJob : JobScheduler.scheduledJobs) {
			scheduledJob.cancel();
		}
		
		this.dailyJobRunner.cancel();
		
		timer.cancel();
		timer.purge();
	}

	public void clean() {
		this.stop();
		jobs = new ArrayList<Job>();
	}
}
