package com.openmtas.database.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;;

@Entity(name="conference")
public class Conference {


	
	
	@Id
    @Column(name="public_id", unique=true, nullable=false)
    @GeneratedValue(generator="foreignGenerator")
    @GenericGenerator(name="foreignGenerator", strategy="foreign", parameters=@Parameter(name="property", value="subscriber"))
	String publicId;
	
	Boolean activated;
	
	@Column(name="max_number_of_parties")
	Integer maxNumberOfParties;
	
	@OneToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	Subscriber subscriber;

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public Integer getMaxNumberOfParties() {
		return maxNumberOfParties;
	}

	public void setMaxNumberOfParties(Integer maxNumberOfParties) {
		this.maxNumberOfParties = maxNumberOfParties;
	}

	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
}
