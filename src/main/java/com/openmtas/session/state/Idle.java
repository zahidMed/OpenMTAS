package com.openmtas.session.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.database.pojo.VirtualSubscriber;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;


public class Idle extends CallState {

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request, method: "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		if(method==METHOD_INVITE)
		{
			
			Subscriber caller= context.getSubscriberManagement().getSubscriber(request.getFrom().getURI().toString());
			
			Subscriber called=context.getSubscriberManagement().getSubscriber(request.getTo().getURI().toString());

			context.setCaller(caller);
			context.setCalled(called);
			context.setOriginalRequest(request);
			if(called!=null && (called instanceof RealSubscriber))
			{
				SubscriberConfiguration conf=((RealSubscriber)called).getConfiguration(GeneralParameters.CALL_FORWARDING_UNCONDITIONAL);
				if(conf==null)
				{
					context.setCallState(CallStateFactory.STATE_INIT_CALL);
					context.onRequest(request, method);
				}
				else
				{
					context.setCallState(CallStateFactory.STATE_INIT_CFU);
					context.onRequest(request, method,conf);
				}
				//set timeout
				
			}
			else if(called !=null && (called instanceof VirtualSubscriber)){
				System.out.println("************************ holley**********************");
				context.setCallState(CallStateFactory.STATE_INIT_CALL_FORK_SEQ);
				context.onRequest(request, method);
			}
			else{
				SipUtil.notFound(request);
				context.close();
			}
		}
		else if(method==METHOD_SUBSCRIBE)
		{
			
			Subscriber caller= context.getSubscriberManagement().getSubscriber(request.getFrom().getURI().toString());
			
			Subscriber called=context.getSubscriberManagement().getSubscriber(request.getTo().getURI().toString());

			context.setCaller(caller);
			context.setCalled(called);
			context.setOriginalRequest(request);
			if(called!=null && (called instanceof RealSubscriber))// && !request.getFrom().equals(request.getTo()))
			{
				
				context.setCallState(CallStateFactory.STATE_INIT_SUBSCRIBE);
				context.onRequest(request, method);
				
			}
			else{
				SipUtil.notFound(request);
				context.close();
			}
		}
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		
	}

}
