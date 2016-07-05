package com.openmtas.session.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class InitCall extends CallState {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Init normal call");
		
		B2buaHelper helper = request.getB2buaHelper();	
		Map<String, List<String>> headers=new HashMap<String, List<String>>();
		List<String> toHeaderSet = new ArrayList<String>();
		toHeaderSet.add(context.getCalled().getPublicId());
		headers.put("To", toHeaderSet);
		
		SipServletRequest inviteRequest = helper.createRequest(request, true,
				headers);
		SipURI sipUri = (SipURI) context.getSipFactory().createURI(((RealSubscriber)context.getCalled()).getSipContact());	
		
		inviteRequest.setRequestURI(sipUri);
		inviteRequest.send();
		
		context.addSession(inviteRequest.getSession().getId());

		context.setOriginalRequest(request);
		SipServletResponse trying = request.createResponse(SipServletResponse.SC_TRYING);		
		trying.send();
		context.setCallState(CallStateFactory.STATE_CALL_PROGRESS);
		ServletTimer timer=context.getTimerService().createTimer(request.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,request.getSession().getId());
		context.setTimer(timer);
		//set call duration before doing CFNR
		
	}

	@Override
	public void onTimeOut(CallContext context) {
		// TODO Auto-generated method stub
		
	}
	

	

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Response for method: "+response.getMethod()+"status "+response.getStatus()+" from: "+response.getRemoteAddr()+":"+response.getRemotePort());
	}

	
	

}
