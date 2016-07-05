package com.openmtas.session.state;

import java.io.IOException;


import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;

import javax.servlet.sip.TooManyHopsException;


import com.openmtas.database.pojo.Subscriber;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

import gov.nist.javax.sip.message.SIPResponse;

public class Ongoing extends SimpleCallState{


	@Override
	public void onRequest(CallContext context,SipServletRequest request,int method,Object...objs) throws IllegalArgumentException, TooManyHopsException, ServletParseException, IOException {
		logger.debug("Request, method: "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		//End call case
		if(method==METHOD_BYE)
		{
			
			B2buaHelper helper = request.getB2buaHelper();
	        SipSession peerSession = helper.getLinkedSession(request.getSession());
	        SipServletRequest forkedRequest = peerSession.createRequest("BYE");
			forkedRequest.send();	
			context.setLastRequest(request);
			context.setCallState(CallStateFactory.STATE_ENDING);
			ServletTimer timer=context.getTimerService().createTimer(request.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,request.getSession().getId());
			context.setTimer(timer);
			
		}
		//Call Hold case && conference
		else if(method==METHOD_INVITE)
		{
			System.out.println(request);
			String contact=request.getHeader("Contact");
			//Conference
			if(contact!=null && contact.contains("isfocus"))
			{
				B2buaHelper helper = request.getB2buaHelper();
				SipSession peerSession = helper.getLinkedSession(request.getSession());

				SipServletRequest reInviteRequest = peerSession.createRequest("INVITE");

		        SipUtil.copyMessageContent(request, reInviteRequest);
				reInviteRequest.send();
				context.setLastRequest(request);
				context.setCallState(CallStateFactory.STATE_CONFERENCE_PROGRESS);
				ServletTimer timer=context.getTimerService().createTimer(request.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,request.getSession().getId());
				context.setTimer(timer);
				System.out.println(reInviteRequest);
				
			}
			//call hold 
			else if(SipUtil.checkSDAttribute(request,"inactive") || SipUtil.checkSDAttribute(request,"sendonly") || SipUtil.checkSDAttribute(request,"recvonly"))
			{
				logger.debug("Hold request received");
				B2buaHelper helper = request.getB2buaHelper();
				SipSession peerSession = helper.getLinkedSession(request.getSession());
		        SipServletRequest holdRequest = peerSession.createRequest("INVITE");
		        SipUtil.copyMessageContent(request, holdRequest);
				holdRequest.send();
				context.setLastRequest(request);
				context.setCallState(CallStateFactory.STATE_CALL_HOLD_PROGRESS);
				ServletTimer timer=context.getTimerService().createTimer(request.getApplicationSession(),GeneralParameters.TRANSITION_TIMEOUT,false,request.getSession().getId());
				context.setTimer(timer);
			}
			
		}
		// Call transfer (refer) case
		else if(method==METHOD_REFER)
		{
			Subscriber referTo=context.getSubscriberManagement().getSubscriber(request.getAddressHeader("Refer-To").getURI().toString());
			
			if(referTo==null){
				request.createResponse(SipServletResponse.SC_NOT_ACCEPTABLE).send();;
				return;
			}
			String by=request.getAddressHeader("Referred-By").getURI().toString();
			
			context.setReferTo(referTo);
			if(context.getCaller().getPublicId().equals(by))
				context.setReferedBy(context.getCaller());
			else
				context.setReferedBy(context.getCalled());
			
			B2buaHelper helper = request.getB2buaHelper();
			SipSession peerSession = helper.getLinkedSession(request.getSession());
	        SipServletRequest referRequest = peerSession.createRequest("REFER");

	        if(request.getHeader("Event")!=null)
	        	referRequest.setHeader("Event", request.getHeader("Event"));
	        if(request.getHeader("Expires")!=null)
	        	referRequest.setHeader("Expires", request.getHeader("Expires"));
	        if(request.getAddressHeader("Refer-To")!=null)
	        	referRequest.setAddressHeader("Refer-To", request.getAddressHeader("Refer-To"));
	        if(request.getAddressHeader("Referred-By")!=null)
	        	referRequest.setAddressHeader("Referred-By", request.getAddressHeader("Referred-By"));
	        SipUtil.copyMessageContent(request, referRequest);
			referRequest.send();
			context.setLastRequest(request);
			logger.debug("Send refer request");
			context.setCallState(CallStateFactory.STATE_REFER_PROGRESS);

			
			
			
		}
		
	}
	
	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
			
		
	}
	
	
	@Override
	public void onTimeOut(CallContext context) {
		// TODO Auto-generated method stub
		context.setCallState(CallStateFactory.STATE_CALL_ONGOING);
	}

	
}
