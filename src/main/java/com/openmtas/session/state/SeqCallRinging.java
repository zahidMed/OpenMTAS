package com.openmtas.session.state;

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

import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallStateFactory;

public class SeqCallRinging extends CallState{

	@Override
	public void onRequest(CallContext context, SipServletRequest request, int method, Object... objs)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Request "+request.getMethod()+" from: "+request.getRemoteAddr()+":"+request.getRemotePort());
		if(method==METHOD_ACK)
		{
			//System.out.println("Method ack");
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
	        	//System.out.println("Ack request created");
	        	//System.out.println(ack);
	        }
			context.setCallState(CallStateFactory.STATE_CALL_ONGOING);
		}
		else if(method==METHOD_CANCEL)
		{
			
			//SipServletRequest linkedRequest=helper.getLinkedSipServletRequest(request);
			context.setLastRequest(request);
			SipServletResponse resp=request.createResponse(SipServletResponse.SC_OK);
			resp.send();
			
			logger.debug("Send request "+resp.getMethod()+" status: "+resp.getStatus()+", to: "+resp.getRemoteAddr()+":"+resp.getRemotePort());
			resp=request.createResponse(SipServletResponse.SC_REQUEST_TERMINATED);
			resp.send();
			
			logger.debug("Send request "+resp.getMethod()+" status: "+resp.getStatus()+", to: "+resp.getRemoteAddr()+":"+resp.getRemotePort());
			
			
			//SipServletRequest  cancelRequest = linkedRequest.createCancel();
			
			B2buaHelper helper = request.getB2buaHelper();
			SipServletRequest cancelRequest = helper.getLinkedSipServletRequest(context.getOriginalRequest()).createCancel();
			cancelRequest.send();
			logger.debug("Send request "+cancelRequest.getMethod()+" to: "+cancelRequest.getRemoteAddr()+":"+cancelRequest.getRemotePort());
			context.setCallState(CallStateFactory.STATE_CALL_CANCELLATION);
		}
	}

	@Override
	public void onResponse(CallContext context, SipServletResponse response, int method)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		logger.debug("Receive Response status: "+response.getStatus()+" from: "+response.getRemoteAddr()+":"+response.getRemotePort());
		if(response.getStatus()==SipServletResponse.SC_OK)
		{
			//if(context.getCalled().)
			SipServletRequest originalRequest =context.getOriginalRequest();
			SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
			//System.out.println("Response created");
			SipUtil.copyMessageContent(response, responseToOriginalRequest);
			responseToOriginalRequest.send();
			logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
			context.setLastResponse(responseToOriginalRequest);
			
		}
		else if(400<=response.getStatus() || response.getStatus()<500)
		{
			SubscriberConfiguration conf=((RealSubscriber)context.getCalled()).getConfiguration(GeneralParameters.CALL_FORWARDING_BUSY);
			if(conf!=null){
				//context.removeSession(response.getSession().getId());
				context.setCallState(CallStateFactory.STATE_INIT_CFB);
				context.onRequest(context.getOriginalRequest(),-1, conf);
			}
			else
			{
				SipServletRequest originalRequest =context.getOriginalRequest();
				SipServletResponse responseToOriginalRequest = originalRequest.createResponse(response.getStatus());
				responseToOriginalRequest.send();
				logger.debug("Send request "+responseToOriginalRequest.getMethod()+" status: "+responseToOriginalRequest.getStatus()+", to: "+responseToOriginalRequest.getRemoteAddr()+":"+responseToOriginalRequest.getRemotePort());
				context.setLastResponse(responseToOriginalRequest);
				context.setCallState(CallStateFactory.STATE_CALL_CANCELLATION);
			}
		}

	}

	@Override
	public void onTimeOut(CallContext context)
			throws IOException, IllegalArgumentException, TooManyHopsException, ServletParseException {
		// TODO Auto-generated method stub
		SubscriberConfiguration conf=((RealSubscriber)context.getCalled()).getConfiguration(GeneralParameters.CALL_FORWARDING_NO_RSPONSE);
		if(conf!=null)
		{
			context.setCallState(CallStateFactory.STATE_INIT_CFNR);
			context.onRequest(context.getOriginalRequest(), -1, conf);
		}
		else
		{
			SipServletRequest originalRequest =context.getOriginalRequest();
			SipServletResponse responseToOriginalRequest = originalRequest.createResponse(SipServletResponse.SC_TEMPORARILY_UNAVAILABLE);
			logger.debug("Cancel Response created");
			responseToOriginalRequest.send();
			
			

			B2buaHelper helper = originalRequest.getB2buaHelper();
			SipServletRequest cancelRequest = helper.getLinkedSipServletRequest(context.getOriginalRequest()).createCancel();
			cancelRequest.send();
			logger.debug("Send request "+cancelRequest.getMethod()+" to: "+cancelRequest.getRemoteAddr()+":"+cancelRequest.getRemotePort());
			context.setCallState(CallStateFactory.STATE_CALL_CANCELLATION);
		}
	}

}
