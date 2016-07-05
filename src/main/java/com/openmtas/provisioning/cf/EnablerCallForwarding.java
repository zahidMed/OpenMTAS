package com.openmtas.provisioning.cf;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.sip.SipServletRequest;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.openmtas.database.dao.SubscriberDAO;
import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.dispatcher.ExpressionResult;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.provisioning.action.SipProvisioningAction;

import gov.nist.javax.sip.message.SIPResponse;

public class EnablerCallForwarding extends SipProvisioningAction{
	
	String service;
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void doAction(SipServletRequest request, ExpressionResult exp) throws IOException {
		// TODO Auto-generated method stub
		logger.debug("Enable CFU for "+request.getFrom().getURI()+" to "+exp.getNp());
		Subscriber sub=subscriberDAO.getSubscriber(request.getFrom().getURI().toString());
		SubscriberConfiguration conf=((RealSubscriber)sub).getConfiguration(service);
		if(conf==null)
		{
			logger.debug("Configuration not found ");
			conf= new SubscriberConfiguration();
			conf.setSubscriber(sub);
			conf.setName(service);
			conf.setNotifyCaller(true);
			((RealSubscriber)sub).addConfiguration(conf);
			conf.setForwardTo("sip:"+exp.getNp()+"@"+SipUtil.getDomain(request.getFrom().getURI()));
			setExtraConfiguration(conf, exp);
			subscriberDAO.save(conf);
		}
		else{
			conf.setForwardTo("sip:"+exp.getNp()+"@"+SipUtil.getDomain(request.getFrom().getURI()));
			setExtraConfiguration(conf, exp);
			subscriberDAO.update(conf);
		}
		
		request.getSession().createRequest("BYE").send();
	}
	
	protected void setExtraConfiguration(SubscriberConfiguration conf, ExpressionResult exp){
		
	}

}
