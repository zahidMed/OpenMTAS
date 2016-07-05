package com.openmtas.database.pojo;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("4")
public class CorporateSubscriber extends RealSubscriber{

	
	
	@Column(name="short_number")
	String shortNumber;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "group_id", nullable = false)
	Group group;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getShortNumber() {
		return shortNumber;
	}

	public void setShortNumber(String shortNumber) {
		this.shortNumber = shortNumber;
	}
	
}
