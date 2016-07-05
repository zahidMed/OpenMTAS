package com.openmtas.database.dao;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;

@Component("subscriberManagement")
@Transactional
public class SubscriberManagement {

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	@Resource(name = "subscriberDAO")
	SubscriberDAO subscriberDAO;
	
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public Subscriber getSubscriber(String publicId){
		logger.debug("get subscriber "+publicId);
		return subscriberDAO.getSubscriber(publicId);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void saveSubscriber(Subscriber sub){
		subscriberDAO.save(sub);
		if(sub instanceof RealSubscriber)
		{
			RealSubscriber rs=(RealSubscriber) sub;
			if(rs.getConfigs()!=null)
				for(SubscriberConfiguration conf: rs.getConfigs())
					subscriberDAO.save(conf);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void updateSubscriber(Subscriber sub){
		subscriberDAO.update(sub);
	}

	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public String getSubscriberIdFromShortNumber(String publicId,String shortNumber) {
		logger.debug("resolve group short number for "+publicId+", "+shortNumber);
		return subscriberDAO.getSubscriberIdFromShortNumber(publicId, shortNumber);
	}
	public SubscriberDAO getSubscriberDAO() {
		return subscriberDAO;
	}

	public void setSubscriberDAO(SubscriberDAO subscriberDAO) {
		this.subscriberDAO = subscriberDAO;
	}
}
