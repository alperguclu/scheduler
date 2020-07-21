package scheduler.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the scheduled_job_logs database table.
 * 
 */
@Entity
@Table(name="scheduled_job_logs")
@NamedQuery(name="ScheduledJobLog.findAll", query="SELECT s FROM ScheduledJobLog s")
public class ScheduledJobLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SCHEDULED_JOB_LOGS_ID_GENERATOR", sequenceName="SCHEDULED_JOB_LOGS_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SCHEDULED_JOB_LOGS_ID_GENERATOR")
	private Integer id;

	private String log;

	private Timestamp time;

	//bi-directional many-to-one association to ScheduledJob
	@ManyToOne
	@JoinColumn(name="scheduled_job_id")
	private ScheduledJob scheduledJob;

	public ScheduledJobLog() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLog() {
		return this.log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public ScheduledJob getScheduledJob() {
		return this.scheduledJob;
	}

	public void setScheduledJob(ScheduledJob scheduledJob) {
		this.scheduledJob = scheduledJob;
	}

}