package com.openmtas.database.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("1")
public class RealSubscriber extends Subscriber{

	@Column(name="ip")
	String ip;
	
	@Column(name="port")
	int port;
	
	@Column(name="registred")
	boolean registred;
	
	@Column(name="last_update")
	Date lastUpdate;


	@OneToOne(fetch = FetchType.EAGER, mappedBy = "subscriber", cascade = CascadeType.ALL)
	Conference conference;
	
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="subscriber", cascade = CascadeType.ALL)
	Set<SubscriberConfiguration> configs;
	

	@OneToMany(fetch=FetchType.EAGER, mappedBy="subscriber", cascade = CascadeType.ALL)
	Set<AbbreviatedDialingConfiguration> abbrevDialings;
	

	
	
	public void addConfiguration(SubscriberConfiguration config){
		if(configs==null) configs= new HashSet<SubscriberConfiguration>();
		configs.add(config);
		config.setSubscriber(this);
	}
	
	public void addAbbreviatedDialing(AbbreviatedDialingConfiguration config){
		if(abbrevDialings==null) abbrevDialings= new HashSet<AbbreviatedDialingConfiguration>();
		abbrevDialings.add(config);
		config.setSubscriber(this);
	}
	
	public AbbreviatedDialingConfiguration getAbbreviatedDialing(String shortNumber){
		
		if(abbrevDialings!=null)
			for(AbbreviatedDialingConfiguration abbr : abbrevDialings){
				if(shortNumber.equals(abbr.getShortNumber()))
					return abbr;
			}
		return null;
	}
	
	public SubscriberConfiguration getConfiguration(String id){
		if(configs!=null)
			for(SubscriberConfiguration conf: configs){
				if(id.equals(conf.getName()))
					return conf;
			}
		return null;
	}

	
	public String getSipContact() {
		return publicId.replaceAll(SIP_URI_REGEX, "$1")+"@"+ip+":"+port;
	}

	

	public boolean isRegistred() {
		return registred;
	}

	public void setRegistred(boolean registred) {
		this.registred = registred;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}




	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString(){
		return "user publicId: "+publicId+", display name: "+displayName+", ip: "+ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public Conference getConference() {
		return conference;
	}

	public void setConference(Conference conference) {
		this.conference = conference;
	}
	
	public Set<SubscriberConfiguration> getConfigs() {
		return configs;
	}

	public void setConfigs(Set<SubscriberConfiguration> configs) {
		this.configs = configs;
	}

	public Set<AbbreviatedDialingConfiguration> getAbbrevDialings() {
		return abbrevDialings;
	}

	public void setAbbrevDialings(Set<AbbreviatedDialingConfiguration> abbrevDialings) {
		this.abbrevDialings = abbrevDialings;
	}

}
