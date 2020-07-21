package scheduler;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.persistence.EntityManager;

import scheduler.model.ScheduledJob;

public class JobScheduler {
	private Timer timer;
	private List<ScheduledJob> jobs;
	public static EntityManager em;

	public JobScheduler(EntityManager em) {
		this.em=em;
		timer = new Timer();
		jobs = new ArrayList<ScheduledJob>();
	}

	public long calculateDelay(ScheduledJob job) {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+3"));
		ZonedDateTime nextRun = ZonedDateTime.ofInstant(job.getNext().toInstant(), ZoneId.of("GMT+3"));

		long delay = 0;

		if (now.toInstant().compareTo(nextRun.toInstant()) < 0) {
			Duration duration = Duration.between(now, nextRun);
			delay = duration.getSeconds() * 1000;
		}

		return delay;
	}

	public void addJob(ScheduledJob scheduledJob) {
		jobs.add(scheduledJob);
		timer.scheduleAtFixedRate(scheduledJob, this.calculateDelay(scheduledJob), scheduledJob.getPeriod() * 1000);
	}

	public void start() {
		for (ScheduledJob job : this.jobs) {
			timer.scheduleAtFixedRate(job, this.calculateDelay(job), job.getPeriod() * 1000);
		}
	}

	public void stop() {
		for (ScheduledJob job : this.jobs) {
			job.cancel();
		}
		timer.cancel();
		timer.purge();
	}

	public void clean() {
		this.stop();
		jobs = new ArrayList<ScheduledJob>();
	}
}
