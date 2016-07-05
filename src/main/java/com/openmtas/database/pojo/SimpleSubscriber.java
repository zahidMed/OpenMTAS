package com.openmtas.database.pojo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("3")
public class SimpleSubscriber extends RealSubscriber {

	

	//List<SubscriberConfiguration> subscriberConfigurations;
	
}
