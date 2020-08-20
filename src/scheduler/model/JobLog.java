package scheduler.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the scheduled_job_logs database table.
 * 
 */
@Entity
@Table(name="job_logs")
@NamedQuery(name="JobLog.findAll", query="SELECT j FROM JobLog j")
public class JobLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="JOB_LOGS_ID_GENERATOR", sequenceName="JOB_LOGS_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="JOB_LOGS_ID_GENERATOR")
	private Integer id;

	private String log;

	private Timestamp time;

	//bi-directional many-to-one association to ScheduledJob
	@ManyToOne
	@JoinColumn(name="job_id")
	private Job job;

	public JobLog() {
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

	public Job getJob() {
		return this.job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

}