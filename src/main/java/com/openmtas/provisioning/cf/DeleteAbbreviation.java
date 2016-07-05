package com.openmtas.provisioning.cf;

import java.io.IOException;

import javax.servlet.sip.SipServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.openmtas.database.pojo.AbbreviatedDialingConfiguration;
import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.dispatcher.ExpressionResult;
import com.openmtas.provisioning.action.SipProvisioningAction;

@Component("deleteAbbreviation")
public class DeleteAbbreviation extends SipProvisioningAction{

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void doAction(SipServletRequest request, ExpressionResult exp) throws IOException {
		// TODO Auto-generated method stub
		Subscriber sub=subscriberDAO.getSubscriber(request.getFrom().getURI().toString());
		AbbreviatedDialingConfiguration abbr= ((RealSubscriber)sub).getAbbreviatedDialing(exp.getAb());
		if(abbr==null){
			subscriberDAO.delete(abbr);
		}
		request.getSession().createRequest("BYE").send();
	}
}
