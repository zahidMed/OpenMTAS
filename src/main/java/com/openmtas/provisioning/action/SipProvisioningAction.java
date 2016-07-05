package com.openmtas.provisioning.action;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.sip.SipServletRequest;

import org.apache.log4j.Logger;

import com.openmtas.database.dao.SubscriberDAO;
import com.openmtas.dispatcher.ExpressionResult;

public abstract class SipProvisioningAction {

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	@Resource(name = "subscriberDAO")
	protected SubscriberDAO subscriberDAO;
	
	public abstract void doAction(SipServletRequest request,ExpressionResult exp) throws IOException;

	public SubscriberDAO getSubscriberDAO() {
		return subscriberDAO;
	}

	public void setSubscriberDAO(SubscriberDAO subscriberDAO) {
		this.subscriberDAO = subscriberDAO;
	}
}
