package com.openmtas.session.transitions;

import java.io.IOException;

import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class SeqCallPrograss extends Transition {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Receive "+response.getMethod()+"status "+response.getStatus()+" from: "+response.getRemoteAddr()+":"+response.getRemotePort());
		if(response.getStatus()==SipServletResponse.SC_RINGING || response.getStatus()==SipServletResponse.SC_SESSION_PROGRESS)
		{
			
			SipServletRequest originalRequest =context.getOriginalRequest();
			SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
			
			responseToOriginalRequest.send();
			logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
			context.setLastResponse(responseToOriginalRequest);
			context.setCallState(CallStateFactory.STATE_RINGING);
			
			long timeOut=GeneralParameters.REQUEST_TIMEOUT;
			ServletTimer timer=context.getTimerService().createTimer(originalRequest.getApplicationSession(),timeOut,false,originalRequest.getSession().getId());
			context.setTimer(timer);
		}
		else if(400<=response.getStatus() || response.getStatus()<600)
		{
			
				context.setCallState(CallStateFactory.STATE_INIT_CALL_FORK_SEQ);
				context.onRequest(context.getOriginalRequest(),METHOD_INVITE);
		}
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub

	}

}
