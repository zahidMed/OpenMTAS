package com.openmtas.database.pojo;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("5")
public class SimpleVirtualSubcriber extends VirtualSubscriber{

}
