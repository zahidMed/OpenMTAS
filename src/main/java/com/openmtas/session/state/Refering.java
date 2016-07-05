package com.openmtas.session.state;

import java.io.IOException;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.dispatcher.SipUtil;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class Refering extends CallState {

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

	        referRequest.setHeader("Subscription-State", request.getHeader("Subscription-State"));
	        referRequest.setHeader("Event", request.getHeader("Event"));
	        referRequest.setContentType(request.getContentType());
        
	        SipUtil.copyMessageContent(request, referRequest);
			referRequest.send();
			context.setLastRequest(request);
			logger.debug("Send notify request");
			//context.setCallState(CallStateFactory.STATE_REFER_PROGRESS);			
		}
		else if(method==METHOD_BYE)
		{
			
			B2buaHelper helper = request.getB2buaHelper();
	        SipSession peerSession = helper.getLinkedSession(request.getSession());
	        SipServletRequest forkedRequest = peerSession.createRequest("BYE");
			forkedRequest.send();	
			context.setLastRequest(request);			
			context.setCallState(CallStateFactory.STATE_ENDING);	
		}
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Receive status "+response.getStatus()+" from: "+response.getRemoteAddr()+":"+response.getRemotePort());
		{
			context.getLastRequest().createResponse(response.getStatus()).send();
			logger.debug("Send response"+response.getStatus());
		}
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub

	}

}
