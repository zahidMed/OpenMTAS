package com.openmtas.database.pojo;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="vn_destination")
public class VSToRSLink {


	@EmbeddedId
	private VSToRSId id;
	
	@ManyToOne
    @JoinColumn(name="virtual_number",insertable=false,updatable=false)
	VirtualSubscriber virtualSubscriber;

	public VSToRSId getId() {
		return id;
	}

	public void setId(VSToRSId id) {
		this.id = id;
	}

	public VirtualSubscriber getVirtualSubscriber() {
		return virtualSubscriber;
	}

	public void setVirtualSubscriber(VirtualSubscriber virtualSubscriber) {
		this.virtualSubscriber = virtualSubscriber;
	}
}
