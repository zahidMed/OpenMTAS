package com.openmtas.database.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VSToRSId implements Serializable{

	@Column(name="virtual_number")
	String vn;
	
	@Column(name="destination")
	String targer;
	
	@Column(name="_order")
	Integer order;

	public String getVn() {
		return vn;
	}

	public void setVn(String vn) {
		this.vn = vn;
	}

	public String getTarger() {
		return targer;
	}

	public void setTarger(String targer) {
		this.targer = targer;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
