package com.openmtas.session;

import java.io.IOException;

import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TooManyHopsException;

public class SimultaneousSessionsCallContext extends MultiSessionsCallContext{

	public SimultaneousSessionsCallContext(CallContextManager manager) {
		super(manager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onRequest(SipServletRequest request, int method, Object... objects)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasSession(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void invalidateOtherContexts(CallContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeOut() throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addSession(String id) {
		// TODO Auto-generated method stub
		return false;
	}

}
