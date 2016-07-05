package com.openmtas.session;

import java.io.IOException;

import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TooManyHopsException;

import org.apache.log4j.Logger;

public abstract class CallStep {

	public static final int METHOD_INVITE = 1;
	public static final int METHOD_REFER = 2;
	public static final int METHOD_ACK = 3;
	public static final int METHOD_BYE = 4;
	public static final int METHOD_NOTIFY = 5;
	public static final int METHOD_CANCEL = 6;
	public static final int METHOD_SUBSCRIBE = 7;
	
	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	abstract public void onRequest(CallContext context, SipServletRequest request, int method,Object...objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException;

	abstract public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException;

	abstract public void onTimeOut(CallContext context) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException;
}
