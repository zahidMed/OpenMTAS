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

public class CallProgress extends Transition {

	public CallProgress(){
		
	}

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
			//System.out.println("Response created");
			responseToOriginalRequest.send();
			logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
			context.setLastResponse(responseToOriginalRequest);
			context.setCallState(CallStateFactory.STATE_RINGING);
			SubscriberConfiguration conf=((RealSubscriber)context.getCalled()).getConfiguration(GeneralParameters.CALL_FORWARDING_NO_RSPONSE);
			long timeOut=GeneralParameters.REQUEST_TIMEOUT;
			if(conf!=null && conf.getDuration()!=null)
				timeOut=conf.getDuration()*1000;
			ServletTimer timer=context.getTimerService().createTimer(originalRequest.getApplicationSession(),timeOut,false,originalRequest.getSession().getId());
			context.setTimer(timer);
		}
		else if(400<=response.getStatus() || response.getStatus()<500)
		{
			SubscriberConfiguration conf=((RealSubscriber)context.getCalled()).getConfiguration(GeneralParameters.CALL_FORWARDING_BUSY);
			if(conf!=null){
				//context.removeSession(response.getSession().getId());
				context.setCallState(CallStateFactory.STATE_INIT_CFB);
				context.onRequest(context.getOriginalRequest(),-1, conf);
			}
			else
			{
				SipServletRequest originalRequest =context.getOriginalRequest();
				SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
				responseToOriginalRequest.send();
				logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
				context.setLastResponse(responseToOriginalRequest);
				context.setCallState(CallStateFactory.STATE_CALL_CANCELLATION);
				ServletTimer timer=context.getTimerService().createTimer(response.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,context.getOriginalRequest().getSession().getId());
				context.setTimer(timer);
			}
		}
		else if(500<=response.getStatus() || response.getStatus()<600)
		{
			SubscriberConfiguration conf=((RealSubscriber)context.getCalled()).getConfiguration(GeneralParameters.CALL_FORWARDING_UNREACHABLE);
			if(conf!=null){
				//context.removeSession(response.getSession().getId());
				context.setCallState(CallStateFactory.STATE_INIT_CFB);
				context.onRequest(context.getOriginalRequest(),-1, conf);
			}
			else
			{
				SipServletRequest originalRequest =context.getOriginalRequest();
				SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
				responseToOriginalRequest.send();
				logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
				context.setLastResponse(responseToOriginalRequest);
				context.setCallState(CallStateFactory.STATE_CALL_CANCELLATION);
				ServletTimer timer=context.getTimerService().createTimer(response.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,context.getOriginalRequest().getSession().getId());
				context.setTimer(timer);
			}
		}
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub

	}

}
