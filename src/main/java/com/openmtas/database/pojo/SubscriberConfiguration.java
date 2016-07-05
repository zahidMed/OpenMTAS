package com.openmtas.database.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="subscriber_configuration")
public class SubscriberConfiguration {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	String name;
	
	@Column(name="cond")
	String condition;
	
	@Column(name="forward_to")
	String forwardTo;
	
	@Column(name="notify_caller")
	Boolean notifyCaller;
	
	Integer duration;
	
	@ManyToOne
	@JoinColumn(name = "subscriber", nullable = false)
	Subscriber subscriber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getForwardTo() {
		return forwardTo;
	}

	public void setForwardTo(String forwardTo) {
		this.forwardTo = forwardTo;
	}

	public Boolean getNotifyCaller() {
		return notifyCaller;
	}

	public void setNotifyCaller(Boolean notifyCaller) {
		this.notifyCaller = notifyCaller;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}
