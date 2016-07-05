package com.openmtas.session;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

import org.springframework.stereotype.Component;

import com.openmtas.database.dao.SubscriberManagement;
import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;

@Component("registrar")
public class Registrar {

	
	@Resource(name="subscriberManagement")
	SubscriberManagement subscriberManagement;
	
	public void doRegister(SipServletRequest request) throws ServletException, IOException {
			
			System.out.println("Registrar::doRegister from "+request.getFrom().toString());
			RealSubscriber sub=(RealSubscriber) subscriberManagement.getSubscriber(request.getFrom().getURI().toString());
			if(sub!=null)
			{
				System.out.println("Registrar::doRegister update subscriber IP: "+request.getRemoteAddr()+", Port: "+request.getRemotePort());
				sub.setPort(request.getRemotePort());
				sub.setIp(request.getRemoteAddr());
				subscriberManagement.updateSubscriber(sub);
			}
			SipServletResponse trying = request.createResponse(SipServletResponse.SC_OK);		
			trying.send();
		}

	public SubscriberManagement getSubscriberManagement() {
		return subscriberManagement;
	}

	public void setSubscriberManagement(SubscriberManagement subscriberManagement) {
		this.subscriberManagement = subscriberManagement;
	}
}
