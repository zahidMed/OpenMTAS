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

import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public  class InitCallForwardingUnconditionnal extends InitCallForwarding {

	

	

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		SubscriberConfiguration conf=(SubscriberConfiguration) objs[0];
		forwardCall(context, request, conf);
		//context.setOriginalRequest(request);
		context.setCallState(CallStateFactory.STATE_CALL_PROGRESS);
		ServletTimer timer=context.getTimerService().createTimer(request.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,request.getSession().getId());
		context.setTimer(timer);
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}
}
