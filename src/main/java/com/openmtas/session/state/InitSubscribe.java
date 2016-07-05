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
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class InitSubscribe extends CallState{

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request, method: "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		logger.debug(request);
		if(method==METHOD_SUBSCRIBE)
		{
			B2buaHelper helper = request.getB2buaHelper();	
			Map<String, List<String>> headers=new HashMap<String, List<String>>();
			List<String> toHeaderSet = new ArrayList<String>();
			toHeaderSet.add(context.getCalled().getPublicId());
			headers.put("To", toHeaderSet);
			
			SipServletRequest subscribeRequest = helper.createRequest(request, true,
					headers);
			SipURI sipUri = (SipURI) context.getSipFactory().createURI(((RealSubscriber)context.getCalled()).getSipContact());	
			
			subscribeRequest.setRequestURI(sipUri);
			subscribeRequest.send();
			logger.debug("Send subscribe request");
			logger.debug(subscribeRequest);
			context.addSession(subscribeRequest.getSession().getId());
			context.setOriginalRequest(request);
		}
		
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		SipServletRequest originalRequest =context.getOriginalRequest();
		SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
		responseToOriginalRequest.send();
		if(response.getStatus()==SipServletResponse.SC_OK)
			context.setCallState(CallStateFactory.STATE_SUBSCRIBE_PROGRSS);
		logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

}
