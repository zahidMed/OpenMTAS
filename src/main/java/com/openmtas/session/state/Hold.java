package com.openmtas.session.state;

import java.io.IOException;
import java.util.List;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.UAMode;

import com.openmtas.dispatcher.SipUtil;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class Hold extends CallState {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request, method: "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		if(method==METHOD_INVITE){
			
			if(!SipUtil.checkSDAttribute(request,"inactive"))
			{
				//logger.debug("sendrecv request received");
				B2buaHelper helper = request.getB2buaHelper();
				SipSession peerSession = helper.getLinkedSession(request.getSession());
		        SipServletRequest unHoldRequest = peerSession.createRequest("INVITE");
		        SipUtil.copyMessageContent(request, unHoldRequest);
				unHoldRequest.send();	
				context.setLastRequest(request);
				context.setCallState(CallStateFactory.STATE_CALL_UNHOLD_PROGRESS);
			}
			if(SipUtil.checkSDAttribute(request, "inactive"))
			{
				logger.debug("sendrecv request inactive");
			}
			if(SipUtil.checkSDAttribute(request, "sendrecv"))
			{
				logger.debug("sendrecv request sendrecv");
			}
			if(SipUtil.checkSDAttribute(request, "recvonly"))
			{
				logger.debug("recvonly request recvonly");
			}
			if(SipUtil.checkSDAttribute(request, "sendonly"))
			{
				logger.debug("sendonly request sendonly");
			}
				
		}
		else if(method==METHOD_BYE)
		{
			
			B2buaHelper helper = request.getB2buaHelper();
	        SipSession peerSession = helper.getLinkedSession(request.getSession());
	        SipServletRequest forkedRequest = peerSession.createRequest("BYE");
			forkedRequest.send();	
			context.setLastRequest(request);
			context.setCallState(CallStateFactory.STATE_ENDING);
			ServletTimer timer=context.getTimerService().createTimer(request.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,request.getSession().getId());
			context.setTimer(timer);
			
		}
		
			
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
