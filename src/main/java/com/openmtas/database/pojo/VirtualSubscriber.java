package com.openmtas.database.pojo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;

@Entity
@DiscriminatorValue("2")
public class VirtualSubscriber extends Subscriber {

	public static int CALL_TYPE_SEQUENCE=0;
	public static int CALL_TYPE_SEMULTANIOUS=1;
	
	Boolean enabled;
	
	@Column(name="call_type")
	Integer callType;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="virtualSubscriber", cascade = CascadeType.ALL)
	@OrderBy("_order")
	List<VSToRSLink> targers;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	

	public Integer getCallType() {
		return callType;
	}

	public void setCallType(Integer callType) {
		this.callType = callType;
	}

	public List<VSToRSLink> getTargers() {
		return targers;
	}

	public void setTargers(List<VSToRSLink> targers) {
		this.targers = targers;
	}
}
