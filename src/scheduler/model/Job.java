package scheduler.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.*;


/**
 * The persistent class for the scheduled_jobs database table.
 * 
 */
@Entity
@Table(name="jobs")
@NamedQuery(name="Job.findAll", query="SELECT j FROM Job j")
public class Job implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="JOBS_ID_GENERATOR", sequenceName="JOBS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="JOBS_ID_GENERATOR")
	private Integer id;

	@Column(name="last_fired")
	private Timestamp lastFired;

	private String name;

	private Timestamp next;

	private Long period;
	
	@Column(name="log_enabled")
	private Boolean logEnabled;
	
	private String days;
	
	private Time hour;
	
	public Job() {
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

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public Time getHour() {
		return hour;
	}

	public void setHour(Time hour) {
		this.hour = hour;
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", lastFired=" + lastFired + ", name=" + name + ", next=" + next + ", period=" + period
				+ ", logEnabled=" + logEnabled + ", days=" + days + ", hour=" + hour + "]";
	}
}