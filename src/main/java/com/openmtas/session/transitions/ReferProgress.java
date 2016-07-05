package com.openmtas.session.transitions;

import java.io.IOException;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.database.pojo.Subscriber;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class ReferProgress extends Transition {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request, method: "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		if(method==METHOD_NOTIFY)
		{
			
			B2buaHelper helper = request.getB2buaHelper();
			SipSession peerSession = helper.getLinkedSession(request.getSession());
	        SipServletRequest referRequest = peerSession.createRequest("NOTIFY");

	        if(request.getHeader("Subscription-State")!=null)
	        	referRequest.setHeader("Subscription-State", request.getHeader("Subscription-State"));
	        if(request.getHeader("Event")!=null)
	        	referRequest.setHeader("Event", request.getHeader("Event"));
	        referRequest.setContentType(request.getContentType());
        
	        SipUtil.copyMessageContent(request, referRequest);
			referRequest.send();
			context.setLastRequest(request);
			logger.debug("Send notify request");
			//context.setCallState(CallStateFactory.STATE_REFER_PROGRESS);			
		}
			
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Receive status "+response.getStatus()+" from: "+response.getRemoteAddr()+":"+response.getRemotePort());
		if(response.getStatus()==202){
			context.getLastRequest().createResponse(response.getStatus()).send();
			logger.debug("Send Accepted response");
			context.setCallState(CallStateFactory.STATE_REFER);
		}
		else if(response.getStatus()>=400){
			context.getLastRequest().createResponse(response.getStatus()).send();
			logger.debug("Send response");
			context.setCallState(CallStateFactory.STATE_CALL_ONGOING);
		}
		
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

}
