package com.openmtas.session.transitions;

import java.io.IOException;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.session.CallContext;
import com.openmtas.session.state.CallState;

public class CallCancellation extends Transition{

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method,Object...objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		if(method==METHOD_ACK)
		{
				context.close();
		}
		
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Receive status "+response.getStatus()+" from: "+response.getRemoteAddr()+":"+response.getRemotePort());
		
		if(response.getStatus()==SipServletResponse.SC_REQUEST_TERMINATED){
			context.close();
		}
	}

	@Override
	public void onTimeOut(CallContext context) throws IOException {
		// TODO Auto-generated method stub
		context.close();
	}
	
}
