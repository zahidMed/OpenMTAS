package com.openmtas.database.dao;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.openmtas.database.pojo.Subscriber;

@Component("subscriberDAO")
public class SubscriberDAO {

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;
	
	
	
	public void save(Object obj) {
		
		getSessionFactory().getCurrentSession().save(obj);
	}
	
	
	public void update(Object obj) {
		getSessionFactory().getCurrentSession().update(obj);
	}
	
	public void delete(Object obj) {
		getSessionFactory().getCurrentSession().delete(obj);
	}
	
	
	
	public Subscriber getSubscriber(String publicId) {

		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Subscriber.class)
				.add(Restrictions.eq("publicId", publicId));
		return (Subscriber) c.uniqueResult();
	}
	
	public String getSubscriberIdFromShortNumber(String publicId,String shortNumber) {

		Session session = getSessionFactory().getCurrentSession();
		SQLQuery query=session.createSQLQuery("select s2.public_id from subscriber s1 join subscriber s2 on(s2.group_id=s1.group_id and s2.short_number=:shortNumber) where s1.public_id=:id limit 1");
		query.setString("shortNumber", shortNumber);
		query.setString("id", publicId);
		return (String) query.uniqueResult();
	}
	
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	
}
