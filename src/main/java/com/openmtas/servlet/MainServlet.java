package com.openmtas.servlet;

/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.sdp.SdpParseException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.sip.B2buaHelper;
import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.ServletTimer;
import javax.servlet.sip.SipFactory;
import javax.servlet.sip.SipServlet;
import javax.servlet.sip.SipServletContextEvent;
import javax.servlet.sip.SipServletListener;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipSession;
import javax.servlet.sip.SipSessionsUtil;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.TimerListener;
import javax.servlet.sip.TimerService;
import javax.servlet.sip.TooManyHopsException;

import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.message.B2buaHelperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.openmtas.database.pojo.Subscriber;
import com.openmtas.dispatcher.MessageDispatcher;
import com.openmtas.session.CallContext;
import com.openmtas.session.CallContextManager;
import com.openmtas.session.state.CallState;

public class MainServlet extends SipServlet implements SipServletListener, TimerListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3226786164606188941L;
	Logger logger=Logger.getLogger("OpenMTAS");

	
	
	
	@Autowired
	CallContextManager callContextManager;
	
	@Autowired
	MessageDispatcher messageDispatcher;
	
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		logger.debug("Initialize SIP Servlet");
		super.init(servletConfig);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
	    		servletConfig.getServletContext());
		callContextManager.setSipFactory((SipFactory) getServletContext().getAttribute(
				SipServlet.SIP_FACTORY));
		callContextManager.setTimerService((TimerService) getServletContext().getAttribute(
				SipServlet.TIMER_SERVICE));
		
	}
	
	@Override
	protected void doInvite(SipServletRequest request) throws ServletException,IOException {
		messageDispatcher.doInvite(request);
	}
	
	@Override
	protected void doNotify(SipServletRequest request) throws ServletException,IOException {
		messageDispatcher.doNotify(request);
	}
	
	@Override
	protected void doRefer(SipServletRequest request) throws ServletException,IOException {
		messageDispatcher.doRefer(request);
	}
	
	@Override
	protected void doAck(SipServletRequest request) throws ServletException, IOException {
		messageDispatcher.doAck(request);
	}
	
	@Override
	protected void doBye(SipServletRequest request) throws ServletException, IOException {
		messageDispatcher.doBye(request);
	}
	
	@Override
	protected void doCancel(SipServletRequest request) throws ServletException, IOException {
		messageDispatcher.doCancel(request);
	}
	
	@Override
	protected void doRegister(SipServletRequest request) throws ServletException, IOException {
		messageDispatcher.doRegister(request);

	}
	
	@Override
	protected void doSubscribe(SipServletRequest request) throws ServletException, IOException {
		System.out.println(request);
		messageDispatcher.doSubscribe(request);

	}
	
	
	@Override
	protected void doResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
		messageDispatcher.doResponse(response);
	}
	
	@Override
	protected void doProvisionalResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
		messageDispatcher.doProvisionalResponse(response);
		
	}
	
	@Override
	protected void doErrorResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
		messageDispatcher.doErrorResponse(response);
		
	}
	@Override
	protected void doSuccessResponse(SipServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
		messageDispatcher.doSuccessResponse(response);
		
	}
	
	


	public CallContextManager getCallContextManager() {
		return callContextManager;
	}

	public void setCallContextManager(CallContextManager callContextManager) {
		this.callContextManager = callContextManager;

	}
	
	
	public void timeout(ServletTimer timer) {
		// TODO Auto-generated method stub
		try {
			callContextManager.timeout(timer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TooManyHopsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServletParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public void servletInitialized(SipServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	public MessageDispatcher getMessageDispatcher() {
		return messageDispatcher;
	}

	public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}
}
