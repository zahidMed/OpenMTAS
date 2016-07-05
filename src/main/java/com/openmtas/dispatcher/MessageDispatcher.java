package com.openmtas.dispatcher;

import java.io.IOException;

import javax.annotation.Resource;
import javax.sdp.SdpParseException;
import javax.servlet.ServletException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.openmtas.database.pojo.AbbreviatedDialingConfiguration;
import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.provisioning.SipProvisioningManager;
import com.openmtas.session.CallContextManager;
import com.openmtas.session.Registrar;
import com.openmtas.session.state.CallState;


@Component("messageDispatcher")
public class MessageDispatcher 
{

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	@Resource(name="callContextManager")
	CallContextManager callContextManager;
	
	@Resource(name="registrar")
	Registrar registrar;
	
	@Resource(name="sipProvisioningManager")
	SipProvisioningManager sipProvisioningManager;
	
	


	public void doInvite(SipServletRequest request) throws ServletException,IOException, IllegalArgumentException {
		logger.debug("Invite from "+request.getFrom()+", to: "+request.getTo());
		Subscriber sub=callContextManager.getSubscriberManagement().getSubscriber(request.getFrom().getURI().toString());
		if(sub==null)
		{
			SipUtil.notFound(request);
			return;
		}
		String to= SipUtil.getUser(request.getTo().getURI());
		ExpressionResult exp=null;
		if(to.indexOf('*')==-1 && to.indexOf('#')==-1)
		{
			callContextManager.onRequest( request, CallState.METHOD_INVITE);
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_GROUP_CALL_PICKUP))!=null){
			callContextManager.forceCFLongNumber(request, exp);
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_GROUP_CALL_PICKUP_SHORT_NUMBER))!=null){
			callContextManager.forceCFShortNumber(request, exp);
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_ABBR_DIALING))!=null){
			
			AbbreviatedDialingConfiguration conf=((RealSubscriber)sub).getAbbreviatedDialing(exp.getAb());
			if(conf!=null)
			{
				request.getTo().setURI(callContextManager.getSipFactory().createURI(conf.getTarget()));
				callContextManager.onRequest( request, CallState.METHOD_INVITE);
			}
			else{
				SipUtil.forbidden(request);
			}
			
		}
		else if((exp=PatternParser.match(to, GeneralParameters.SERVICE_GROUP_SHORT_NUMBER))!=null){
			
			String target=callContextManager.getSubscriberManagement().getSubscriberIdFromShortNumber(request.getFrom().getURI().toString(), exp.getNp());
			if(target!=null)
			{
				request.getTo().setURI(callContextManager.getSipFactory().createURI(target));
				callContextManager.onRequest( request, CallState.METHOD_INVITE);
			}
			else{
				SipUtil.forbidden(request);
			}
			
		}
		else{
			
			sipProvisioningManager.doProvisioing(request);
			
		}
		
	}
	

	public void doNotify(SipServletRequest request) throws ServletException,IOException {
		callContextManager.onRequest( request, CallState.METHOD_NOTIFY);
	}
	public void doRefer(SipServletRequest request) throws ServletException,IOException, IllegalArgumentException {
		callContextManager.onRequest( request, CallState.METHOD_REFER);
	}
	

	public void doAck(SipServletRequest request) throws ServletException, IOException, IllegalArgumentException {
		callContextManager.onRequest( request, CallState.METHOD_ACK);
	}
	
	
	public void doBye(SipServletRequest request) throws ServletException, IOException, IllegalArgumentException {
		callContextManager.onRequest( request, CallState.METHOD_BYE);
	}
	
	
	public void doCancel(SipServletRequest request) throws ServletException, IOException, IllegalArgumentException {
		callContextManager.onRequest( request, CallState.METHOD_CANCEL);
	}
	
	
	public void doRegister(SipServletRequest request) throws ServletException, IOException {
		
		registrar.doRegister(request);
	}
	
	public void doSubscribe(SipServletRequest request) throws ServletException, IOException {
		callContextManager.onRequest( request, CallState.METHOD_SUBSCRIBE);
	}
	
	
	public void doResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException, IllegalArgumentException {
		callContextManager.onResponse(response, 0);
	}
	
	
	public void doProvisionalResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException, IllegalArgumentException {
		
		callContextManager.onResponse(response, 0);
	}
	
	
	public void doErrorResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException, IllegalArgumentException {
		callContextManager.onResponse(response, 0);
		
	}


	public void doSuccessResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException, IllegalArgumentException {
		callContextManager.onResponse(response, 0);
		
	}

	
	public void timeout(ServletTimer timer) {}
}
