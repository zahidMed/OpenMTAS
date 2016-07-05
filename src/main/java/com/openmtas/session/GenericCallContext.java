package com.openmtas.session;

import java.io.IOException;

import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.database.pojo.Subscriber;

public abstract class GenericCallContext {

	CallContextManager manager;
	SipServletRequest originalRequest;
	Subscriber caller;
	
	
	
	public GenericCallContext(CallContextManager manager) {
		this.manager=manager;
	}

	
	public  abstract void onRequest(SipServletRequest request,int method,Object...objects) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException;
	public abstract void onResponse(SipServletResponse response,int method) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException;
	public abstract void onTimeOut() throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException;
	public abstract boolean hasSession(String id);
	public abstract boolean addSession(String id);
	
	public SipServletRequest getOriginalRequest() {
		return originalRequest;
	}

	public void setOriginalRequest(SipServletRequest originalRequest) {
		this.originalRequest = originalRequest;
	}

	public Subscriber getCaller() {
		return caller;
	}

	public void setCaller(Subscriber caller) {
		this.caller = caller;
	}
}
