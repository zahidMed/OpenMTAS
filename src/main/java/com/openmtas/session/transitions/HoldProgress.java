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
import com.openmtas.session.state.CallState;

public class HoldProgress extends Transition {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request, method: "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
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
	        	logger.debug("Send Ack request to: "+ack.getRemoteAddr()+":"+ack.getRemotePort());
	        	context.setCallState(CallStateFactory.STATE_CALL_HOLD);
	        	
	        	
	        }
		}
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
			SipServletRequest lastReq=context.getLastRequest();
			SipServletResponse resp= lastReq.createResponse(response.getStatus());
			SipUtil.copyMessageContent(response, resp);
			resp.send();
			if(response.getStatus()!=SipServletResponse.SC_OK)
				context.setCallState(CallStateFactory.STATE_CALL_ONGOING);
		
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		context.setCallState(CallStateFactory.STATE_CALL_ONGOING);
	}

}
