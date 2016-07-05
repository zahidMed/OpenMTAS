package com.openmtas.database.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name="subscriber")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type",
 discriminatorType=DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class Subscriber {

	public static String SIP_URI_REGEX="(sip:.*)@(.*)";

	@Id
    @Column(name="public_id",unique=true,nullable=false)
	String publicId;
	
	@Column(name="display_name")
	String displayName;
	
	
	@Override
	public boolean equals(Object obj){
		if(obj==null || !(obj instanceof Subscriber)) return false;
		return this.getPublicId().equals(((Subscriber)obj).getPublicId());
	}
	

	
	@Override
	public String toString(){
		return "user publicId: "+publicId+", display name: "+displayName;
	}



	public String getPublicId() {
		return publicId;
	}



	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	


}
