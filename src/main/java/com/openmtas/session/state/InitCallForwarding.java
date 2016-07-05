package com.openmtas.session.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.session.CallContext;

public abstract class InitCallForwarding extends CallState{

	
	void closeSession(CallContext context, SipServletRequest request) throws IOException
	{
		logger.debug("Close session with "+request.getFrom());
		B2buaHelper helper = request.getB2buaHelper();
		SipServletRequest cancelRequest = helper.getLinkedSipServletRequest(request).createCancel();
		helper.unlinkSipSessions(cancelRequest.getSession());
		context.removeSession(cancelRequest.getSession().getId());
		cancelRequest.send();
		context.removeSession(cancelRequest.getSession().getId());
		logger.debug("Send request "+cancelRequest.getMethod()+" to: "+cancelRequest.getRemoteAddr()+":"+cancelRequest.getRemotePort());
	}
	
	public void forwardCall(CallContext context,SipServletRequest request,SubscriberConfiguration conf) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		logger.debug("Call being forwarded to "+conf.getForwardTo());
		if(conf.getForwardTo()==null)
		{
			logger.debug("forword_to is null for "+conf.getSubscriber().getPublicId());
			SipUtil.notFound(request);
			context.close();
			return;
		}
		
		Subscriber newCalled=context.getSubscriberManagement().getSubscriber(conf.getForwardTo());
		if(newCalled==null){
			logger.debug("Error: forworded end user not found from "+conf.getSubscriber().getPublicId()+" to "+conf.getForwardTo());
			SipUtil.notFound(request);
			context.close();
			return;
		}
		
		SipServletResponse trying = request.createResponse(SipServletResponse.SC_TRYING);		
		trying.send();
		
		SipServletResponse forwarding = request.createResponse(SipServletResponse.SC_CALL_BEING_FORWARDED);		
		forwarding.send();
		
		
		context.setCalled(newCalled);
		

		B2buaHelper helper = request.getB2buaHelper();	
		
		Map<String, List<String>> headers=new HashMap<String, List<String>>();
		List<String> toHeaderSet = new ArrayList<String>();
		toHeaderSet.add(newCalled.getPublicId());
		headers.put("To", toHeaderSet);
		
		SipServletRequest inviteRequest = helper.createRequest(request, true,
				headers);
		SipURI sipUri = (SipURI) context.getSipFactory().createURI(((RealSubscriber)newCalled).getSipContact());	
		inviteRequest.setRequestURI(sipUri);
		inviteRequest.send();
		logger.debug("Send request "+inviteRequest.getMethod()+" to: "+inviteRequest.getRemoteAddr()+":"+inviteRequest.getRemotePort());
		context.addSession(inviteRequest.getSession().getId());
		//context.setOriginalRequest(request);
		context.increnemtFrowardCounter();
		
	}
	
	
}
