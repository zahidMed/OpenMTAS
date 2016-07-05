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
@Table(name="abbrev_dialing_configuration")
public class AbbreviatedDialingConfiguration {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	@Column(name="short_number")
	String shortNumber;
	
	@Column(name="target")
	String target;
	
	@ManyToOne
	@JoinColumn(name = "subscriber", nullable = false)
	Subscriber subscriber;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShortNumber() {
		return shortNumber;
	}

	public void setShortNumber(String shortNumber) {
		this.shortNumber = shortNumber;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj==null || !(obj instanceof AbbreviatedDialingConfiguration)) return false;
		AbbreviatedDialingConfiguration abbr=(AbbreviatedDialingConfiguration) obj;
		return this.getShortNumber().equals(abbr.getShortNumber()) && getSubscriber().equals(getSubscriber());
	}

}
