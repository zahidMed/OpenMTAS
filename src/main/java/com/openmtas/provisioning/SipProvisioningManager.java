package com.openmtas.provisioning;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipURI;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.openmtas.database.dao.SubscriberManagement;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.dispatcher.ExpressionResult;
import com.openmtas.dispatcher.PatternParser;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.provisioning.action.SipProvisioningAction;

import gov.nist.javax.sip.message.SIPResponse;

@Component("sipProvisioningManager")
public class SipProvisioningManager {

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	@Resource(name="subscriberManagement")
	SubscriberManagement subscriberManagement;
	
	@Autowired
	ApplicationContext applicationContext;
	
	public void doProvisioing(SipServletRequest request) throws ServletException,IOException 
	{
		String to= SipUtil.getUser(request.getTo().getURI());
		logger.debug("perform provisioning operation "+to+" for "+request.getFrom());
		
		request.createResponse(SIPResponse.OK).send();
		ExpressionResult exp=null;
		String bean=null;
		
		if((exp=PatternParser.match(to, GeneralParameters.SERVICE_CFU_ENABELING))!=null)
		{
			bean="enablerCFU";
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_CFU_DISABELING))!=null){
			bean="disablerCFU";
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_CFB_ENABELING))!=null){
			bean="enablerCFB";
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_CFB_DISABELING))!=null){
			bean="disablerCFB";
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_CFNR_ENABELING))!=null){
			bean="enablerCFNR";
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_CFNR_DISABELING))!=null){
			bean="disablerCFNR";
		}
		else{
			logger.debug("Service not found: "+to);
		}
		
		if(bean!=null){
			SipProvisioningAction action=(SipProvisioningAction) applicationContext.getBean(bean);
			if(action!=null)
				action.doAction(request, exp);
		}
	}
	
	

	public SubscriberManagement getSubscriberManagement() {
		return subscriberManagement;
	}

	public void setSubscriberManagement(SubscriberManagement subscriberManagement) {
		this.subscriberManagement = subscriberManagement;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	

	
	
}
