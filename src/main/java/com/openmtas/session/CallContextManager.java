package com.openmtas.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sdp.SdpParseException;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.TooManyHopsException;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.openmtas.database.dao.SubscriberDAO;
import com.openmtas.database.dao.SubscriberManagement;
import com.openmtas.dispatcher.ExpressionResult;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.servlet.MainServlet;
import com.openmtas.session.state.CallState;
import com.openmtas.session.state.Ringing;

@Component("callContextManager")
@Scope(value="singleton")
public class CallContextManager {
	
	public static String CALL_CONTEXT="CALL_CONTEXT";
	
	public static final Logger logger=Logger.getLogger(CallContextManager.class);

	SipFactory sipFactory;
	TimerService timerService;
//	
//	SipSessionsUtil sipSessionsUtil;
	
	@Resource(name="subscriberManagement")
	SubscriberManagement subscriberManagement;
	
	List<GenericCallContext> contexts= new ArrayList<GenericCallContext>();
	//Map<String,CallContext> contexts= new HashMap<String, CallContext>();
	
	
	
	//CallContext context=null;
	
	public void forceCFLongNumber(SipServletRequest request,ExpressionResult exp) throws IllegalArgumentException, TooManyHopsException, ServletParseException, IOException{
		String to="sip:"+exp.getNp()+"@"+SipUtil.getDomain(request.getFrom().getURI());
		logger.debug("Force call forwarding from "+request.getFrom().getURI()+", to "+to);
		Iterator<GenericCallContext> iter= contexts.iterator();
		while(iter.hasNext()){
			GenericCallContext c=iter.next();
			if(c instanceof CallContext)
			{
				CallContext ctx=(CallContext) c;
				
				if(ctx.getCalled().getPublicId().equals(to))
				{
					logger.debug("Context found");
					ctx.forceCallForwarding(request, to);
					return;
				}
			}
		}
	}
	
	public void forceCFShortNumber(SipServletRequest request,ExpressionResult exp) throws IllegalArgumentException, TooManyHopsException, ServletParseException, IOException{
		String to=getSubscriberManagement().getSubscriberIdFromShortNumber(request.getFrom().getURI().toString(), exp.getSn());
		
		logger.debug("Force call forwarding with short number from "+request.getFrom().getURI()+", to "+to);
		Iterator<GenericCallContext> iter= contexts.iterator();
		while(iter.hasNext()){
			GenericCallContext c=iter.next();
			if(c instanceof CallContext)
			{
				CallContext ctx=(CallContext) c;
				
				if(ctx.getCalled().getPublicId().equals(to))
				{
					logger.debug("Context found");
					ctx.forceCallForwarding(request, to);
					return;
				}
			}
		}
	}
	
	public void onRequest(SipServletRequest request,int method) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException{
		
		logger.debug("Request "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort()+", session "+request.getSession().getId());
		GenericCallContext context=null;
		if(method==CallState.METHOD_INVITE || method==CallState.METHOD_SUBSCRIBE)
		{
			context=getContext(request.getSession().getId());//(CallContext) request.getSession().getAttribute(CALL_CONTEXT);//contexts.get(request.getSession().getId());
			if(context==null)
			{
				context= new CallContext(this);
				context.addSession(request.getSession().getId());
				addCallContext(request.getSession().getId(), context);
			}
			
		}
		else{
			context=getContext(request.getSession().getId());// (CallContext) request.getSession().getAttribute(CALL_CONTEXT);//contexts.get(request.getSession().getId());
			
		}
		if(context!=null)
			context.onRequest(request, method);
		else
			logger.debug("Context not found");
		
	}
	public void onResponse(SipServletResponse response,int method) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException{
		
		logger.debug("Receive "+response.getMethod()+"status "+response.getStatus()+" from: "+response.getRemoteAddr()+":"+response.getRemotePort());
		GenericCallContext context=getContext(response.getSession().getId());//contexts.get(response.getSession().getId());
		if(context!=null)
			context.onResponse( response, method);
		else
			logger.debug("Context not found");
	}
	
	public void timeout(ServletTimer timer) throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException{
		String sId=(String) timer.getInfo();
		GenericCallContext context=getContext(sId);
		if(context!=null)
			context.onTimeOut();
		else
			logger.debug("Context not found");
	}
	
	public GenericCallContext getContext(String id){
		for(GenericCallContext context: contexts){
			if(context.hasSession(id))
				return context;
		}
		return null;
	}
	
//	public CallContext getContext(SipSession session){
//		System.out.println("getContext for session: "+session.getId());
//		CallContext context=(CallContext) session.getAttribute(CALL_CONTEXT);
//		if(context==null){
//			SipSession ss=sipSessionsUtil.getCorrespondingSipSession(session, "Join");
//			if(ss!=null){
//				System.out.println("Join session found "+session.getId()+" AND "+ss.getId());
//				context=(CallContext) ss.getAttribute(CALL_CONTEXT);
//			}
//		}
//		if(context==null){
//			SipSession ss=sipSessionsUtil.getCorrespondingSipSession(session, "Replaces");
//			if(ss!=null){
//				System.out.println("Replaces session found "+session.getId()+" AND "+ss.getId());
//				context=(CallContext) ss.getAttribute(CALL_CONTEXT);
//			}
//		}	
//		return context;
//	}
	void addCallContext(String id,GenericCallContext context ){
		
		contexts.add(context);
	}
	
	void removeCallContext(CallContext context ){
		
		contexts.remove(context);
	}
//	
//	public CallContext getContext(String id){
//		return contexts.get(id);
//	}
	public SipFactory getSipFactory() {
		return sipFactory;
	}
	public void setSipFactory(SipFactory sipFactory) {
		this.sipFactory = sipFactory;
	}

	
//	public SipSessionsUtil getSipSessionsUtil() {
//		return sipSessionsUtil;
//	}
//	public void setSipSessionsUtil(SipSessionsUtil sipSessionsUtil) {
//		this.sipSessionsUtil = sipSessionsUtil;
//	}
	public TimerService getTimerService() {
		return timerService;
	}
	public void setTimerService(TimerService timerService) {
		this.timerService = timerService;
	}
	public SubscriberManagement getSubscriberManagement() {
		return subscriberManagement;
	}
	public void setSubscriberManagement(SubscriberManagement subscriberManagement) {
		this.subscriberManagement = subscriberManagement;
	}
}
