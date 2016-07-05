package com.openmtas.session.transitions;

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
import com.openmtas.session.state.CallState;

public class SubscribeProgress extends Transition {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request, method: "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		logger.debug(request);
		if(method==METHOD_NOTIFY){
			B2buaHelper helper = request.getB2buaHelper();
	        SipSession peerSession = helper.getLinkedSession(request.getSession());
	        SipServletRequest forkedRequest = peerSession.createRequest("NOTIFY");
	        SipUtil.copyMessageContent(request, forkedRequest);
			forkedRequest.send();	
			context.setLastRequest(request);
		}
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		SipServletRequest originalRequest =context.getOriginalRequest();
		SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
		responseToOriginalRequest.send();
		logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
		if(response.getStatus()==SipServletResponse.SC_OK)
			context.setCallState(CallStateFactory.STATE_CALL_ONGOING);
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub

	}

}
