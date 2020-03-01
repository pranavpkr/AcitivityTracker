package com.userexp.main.models;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "emp_activities")
public class EmpActivities {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
		name = "UUID",
		strategy = "org.hibernate.id.UUIDGenerator"
	)
	@Column(name = "id", nullable = false)
	public UUID id;
	public long employee_id;
	public String name;
	public Timestamp start_time;
	public int duration;
  
	public EmpActivities() {
		super();
	}
	
	
	public EmpActivities( long employee_id, String name, Timestamp start_time, int duration) {
		this.employee_id = employee_id;
		this.name = name;
		this.start_time = start_time;
		this.duration = duration;
	}
	
	public EmpActivities( UUID id, long employee_id, String name, Timestamp start_time, int duration) {
		this.id = id;
		this.employee_id = employee_id;
		this.name = name;
		this.start_time = start_time;
		this.duration = duration;
	}
	
	
	
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public long getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(long employee_id) {
		this.employee_id = employee_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getStart_time() {
		return start_time;
	}
	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
}