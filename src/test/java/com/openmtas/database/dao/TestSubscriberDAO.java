package com.openmtas.database.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.openmtas.database.pojo.Conference;
import com.openmtas.database.pojo.SimpleSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/resources/applicationContext.xml" })
public class TestSubscriberDAO {

	@Resource(name="subscriberManagement")
	SubscriberManagement subscriberManagement;
	
	@Test
	public void testGetSubscriber(){
		Subscriber sub=subscriberManagement.getSubscriber("sip:050505@localhost");
		Assert.assertEquals("sip:050505@localhost", sub.getPublicId());
		//Assert.assertNotNull(sub.getAbbrevDialings());
		//Assert.assertEquals(sub.getAbbreviatedDialing("1").getTarget(),"sip:080811@localhost");
//		Assert.assertEquals(sub.getConfigs().size(),2);
	}
	
	@Test
	public void testSaveSubscriber(){
		SimpleSubscriber sub=new SimpleSubscriber();
		sub.setPublicId("sip:test@localhost");
		sub.setDisplayName("test");
		sub.setIp("127.0.0.1");
		sub.setPort(5090);
		sub.setRegistred(true);
		System.out.println(sub);
		Conference cf= new Conference();
		cf.setActivated(true);
		cf.setMaxNumberOfParties(3);
		
		SubscriberConfiguration conf1= new SubscriberConfiguration();
		conf1.setName("cfb");
		conf1.setCondition("busy");
		conf1.setForwardTo("sip:080811@test.openmtas.com");
		conf1.setNotifyCaller(true);
		sub.addConfiguration(conf1);
		
		SubscriberConfiguration conf2= new SubscriberConfiguration();
		conf2.setName("cfu");
		conf2.setForwardTo("sip:080811@test.openmtas.com");
		conf2.setNotifyCaller(true);
		sub.addConfiguration(conf2);
		
		sub.setConference(cf);
		cf.setSubscriber(sub);
		//subscriberManagement.saveSubscriber(sub);
		
	}



	public SubscriberManagement getSubscriberManagement() {
		return subscriberManagement;
	}

	public void setSubscriberManagement(SubscriberManagement subscriberManagement) {
		this.subscriberManagement = subscriberManagement;
	}
	
}
