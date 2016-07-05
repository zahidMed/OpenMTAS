package com.openmtas.database.pojo;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;



@Entity(name="_group")
public class Group {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	String name;
	

	@OneToMany(fetch=FetchType.LAZY, mappedBy="group", cascade = {CascadeType.PERSIST})
	Set<CorporateSubscriber> subscribers;

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

	public Set<CorporateSubscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<CorporateSubscriber> subscribers) {
		this.subscribers = subscribers;
	}
}
