package com.openmtas.provisioning.cf;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.openmtas.database.dao.SubscriberDAO;
import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.dispatcher.ExpressionResult;
import com.openmtas.parameter.GeneralParameters;
import com.openmtas.provisioning.action.SipProvisioningAction;

import gov.nist.javax.sip.message.SIPResponse;

public class DisablerCallForwarding extends SipProvisioningAction {

	String service;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void doAction(SipServletRequest request, ExpressionResult exp) throws IOException {
		// TODO Auto-generated method stub
		logger.debug("Disable CF for " + request.getFrom().getURI() + " to " + exp.getNp());
		SipServletResponse resp = request.createResponse(SIPResponse.OK);
		resp.setContent(getContent(request.getSession().getId()), "application/vnd.3gpp.ussd+xm");
		resp.send();
		Subscriber sub = subscriberDAO.getSubscriber(request.getFrom().getURI().toString());
		SubscriberConfiguration conf = ((RealSubscriber)sub).getConfiguration(service);
		if (conf != null) {

			subscriberDAO.delete(conf);
		}
		SipServletRequest rq = request.getSession().createRequest("BYE");
		rq.setContent(getContent(request.getSession().getId()), "application/vnd.3gpp.ussd+xm");
		rq.send();
	}

	String getContent(String id) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<ussd-data> " + "<language>en</language> "
				+ "<ussd-string> " + "     Hello, your credit is $175.50. Thanks for your query.  "
				+ "     We are happy to assist. Your operator " + "</ussd-string> " + "</ussd-data> ";
	}

}
