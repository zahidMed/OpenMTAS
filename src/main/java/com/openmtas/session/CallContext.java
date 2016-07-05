package com.openmtas.session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.sdp.SdpParseException;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.TooManyHopsException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.openmtas.database.dao.SubscriberDAO;
import com.openmtas.database.dao.SubscriberManagement;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.database.pojo.VSToRSLink;
import com.openmtas.database.pojo.VirtualSubscriber;
import com.openmtas.session.state.CallState;
import com.openmtas.session.state.Ringing;


public class CallContext extends GenericCallContext{

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	protected String id;

	SipServletRequest lastRequest;
	SipServletResponse lastResponse;
	
	Subscriber called;
	
	Subscriber referTo;
	Subscriber referedBy;
	
	CallStep callState;
	CallStep olderState;
	//SipFactory sipFactory;
	
	ServletTimer timer;
	Set<String> sessions= new HashSet<String>();
	
	int forwardCount=0;
	
	
	public boolean hasSession(String id){
		return sessions.contains(id);
	}
	
	public boolean addSession(String id){
		return sessions.add(id);
	}
	
	public boolean removeSession(String id){
		return sessions.remove(id);
	}
	
	public void increnemtFrowardCounter(){
		forwardCount++;
	}
	
	public CallContext(CallContextManager manager){
		super(manager);
		setCallState(CallStateFactory.STATE_IDLE);
		
	}
	
	public void setCallState(int state) {
		CallStep callState=CallStateFactory.getInstance().getCallState(state);
		if(callState!=null)
		{
			logger.debug("New call State: "+callState.getClass().getName());
			if(timer!=null){
				timer.cancel();
				timer=null;
			}
			this.callState = callState;
		}
		else
			logger.error("State not found "+state);
	}
	

	
	public void close(){
		logger.debug("Close context");
		if(timer!=null)
			timer.cancel();
		manager.removeCallContext(this);
	}

	
	@Override
	public void onRequest(SipServletRequest request,int method,Object...objects) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException{
		callState.onRequest(this, request, method,objects);
	}
	
	@Override
	public void onResponse(SipServletResponse response,int method) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException{
		callState.onResponse(this, response, method);
	}
	
	
	public void onTimeOut() throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		callState.onTimeOut(this);
	}

	public void forceCallForwarding(SipServletRequest request,String to) throws IllegalArgumentException, TooManyHopsException, ServletParseException, IOException{
		if(getCallState() instanceof Ringing){
			SubscriberConfiguration conf= new SubscriberConfiguration();
			conf.setForwardTo(request.getFrom().getURI().toString());
			request.createResponse(204 /*SipServletResponse.SC_REQUEST_TERMINATED*/).send();
			//request.getSession().createRequest("BYE").send();
			setCallState(CallStateFactory.STATE_INIT_CFNR);
			onRequest(getOriginalRequest(), -1, conf);
		}
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public Subscriber getCalled() {
		return called;
	}

	public void setCalled(Subscriber called) {
		this.called = called;
	}

	public CallStep getCallState() {
		return callState;
	}

	

	public CallStep getOlderState() {
		return olderState;
	}

	public void setOlderState(CallStep olderState) {
		this.olderState = olderState;
	}

	public SubscriberManagement getSubscriberManagement() {
		return manager.getSubscriberManagement();
	}


	
	public SipFactory getSipFactory() {
		return manager.getSipFactory();
	}


//	public B2buaHelper getHelper() {
//		return helper;
//	}
//
//
//	public void setHelper(B2buaHelper helper) {
//		this.helper = helper;
//	}

	public SipServletResponse getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(SipServletResponse lastResponse) {
		this.lastResponse = lastResponse;
	}

	public SipServletRequest getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(SipServletRequest lastRequest) {
		this.lastRequest = lastRequest;
	}


	public TimerService getTimerService() {
		return manager.getTimerService();
	}

	public int getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(int forwardCount) {
		this.forwardCount = forwardCount;
	}

	public ServletTimer getTimer() {
		return timer;
	}

	public void setTimer(ServletTimer timer) {
		this.timer = timer;
	}

	public Subscriber getReferTo() {
		return referTo;
	}

	public void setReferTo(Subscriber referTo) {
		this.referTo = referTo;
	}

	public Subscriber getReferedBy() {
		return referedBy;
	}

	public void setReferedBy(Subscriber referedBy) {
		this.referedBy = referedBy;
	}



	

	
}
