package com.openmtas.session.transitions;

import java.io.IOException;
import java.util.List;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TooManyHopsException;
import javax.servlet.sip.UAMode;

import com.openmtas.dispatcher.SipUtil;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class UnHoldProgress extends Transition {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		if(method==METHOD_ACK){
			B2buaHelper helper = request.getB2buaHelper();
	        SipSession peerSession = helper.getLinkedSession(request.getSession());
			List<SipServletMessage> pendingMessages = helper.getPendingMessages(peerSession, UAMode.UAC);
	        SipServletResponse invitePendingResponse = null;
	       
	        for(SipServletMessage pendingMessage : pendingMessages) {
	        	if(((SipServletResponse)pendingMessage).getStatus() == 200) {
	        		invitePendingResponse = (SipServletResponse)pendingMessage;
	        		break;
	        	}
	        }
	        if(invitePendingResponse!=null)
	        {
	        	
	        	SipServletRequest ack=invitePendingResponse.createAck();
	        	ack.send();
	        	logger.debug("Send request "+ack.getMethod()+" to: "+ack.getRemoteAddr()+":"+ack.getRemotePort());
	        	context.setCallState(CallStateFactory.STATE_CALL_ONGOING);
	        }
		}
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		if(response.getStatus()==SipServletResponse.SC_OK){
			SipServletRequest lastReq=context.getLastRequest();
			SipServletResponse resp= lastReq.createResponse(response.getStatus());
			SipUtil.copyMessageContent(response, resp);
			resp.send();
		}
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

}
