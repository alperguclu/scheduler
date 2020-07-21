package scheduler.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.*;

import scheduler.JobScheduler;

import java.util.Date;
import java.util.TimerTask;


/**
 * The persistent class for the scheduled_jobs database table.
 * 
 */
@Entity
@Table(name="scheduled_jobs")
@NamedQuery(name="ScheduledJob.findAll", query="SELECT s FROM ScheduledJob s")
public class ScheduledJob extends TimerTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SCHEDULED_JOBS_ID_GENERATOR", sequenceName="SCHEDULED_JOBS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SCHEDULED_JOBS_ID_GENERATOR")
	private Integer id;

	@Column(name="last_fired")
	private Timestamp lastFired;

	private String name;

	private Timestamp next;

	private Long period;
	
	@Column(name="log_enabled")
	private Boolean logEnabled;
	
	public ScheduledJob() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getLastFired() {
		return this.lastFired;
	}

	public void setLastFired(Timestamp lastFired) {
		this.lastFired = lastFired;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getNext() {
		return this.next;
	}

	public void setNext(Timestamp next) {
		this.next = next;
	}

	public Long getPeriod() {
		return this.period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}

	public Boolean getLogEnabled() {
		return logEnabled;
	}

	public void setLogEnabled(Boolean logEnabled) {
		this.logEnabled = logEnabled;
	}

	public void log() {
		if(this.getLogEnabled()) {
			ScheduledJobLog sjl = new ScheduledJobLog();
			sjl.setScheduledJob(this);
			sjl.setTime(this.lastFired);
			sjl.setLog("Job: "+this.getName()+ " finished at "+this.lastFired);
			JobScheduler.em.persist(sjl);			
		}
	}

	public void done() {
		JobScheduler.em.getTransaction().begin();
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+3"));
		this.lastFired = Timestamp.from(now.toInstant());
		this.next = Timestamp.from(now.plusSeconds(this.period).toInstant());
		JobScheduler.em.getTransaction().commit();
	}

	@Override
	public void run() {
		try {
			Class<?> clazz = Class.forName(this.getName());
			ScheduledJob task = (ScheduledJob) clazz.newInstance();
			
			task.run();

			this.done();
			this.log();
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}