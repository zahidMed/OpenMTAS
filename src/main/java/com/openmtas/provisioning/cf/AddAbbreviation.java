package com.openmtas.provisioning.cf;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.sip.SipServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.openmtas.database.dao.SubscriberDAO;
import com.openmtas.database.pojo.AbbreviatedDialingConfiguration;
import com.openmtas.database.pojo.RealSubscriber;
import com.openmtas.database.pojo.Subscriber;
import com.openmtas.dispatcher.ExpressionResult;
import com.openmtas.dispatcher.SipUtil;
import com.openmtas.provisioning.action.SipProvisioningAction;


@Component("addAbbreviation")
public class AddAbbreviation extends SipProvisioningAction {

	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void doAction(SipServletRequest request, ExpressionResult exp) throws IOException {
		// TODO Auto-generated method stub
		Subscriber sub=subscriberDAO.getSubscriber(request.getFrom().getURI().toString());
		AbbreviatedDialingConfiguration abbr= ((RealSubscriber)sub).getAbbreviatedDialing(exp.getAb());
		if(abbr==null){
			abbr= new AbbreviatedDialingConfiguration();
			abbr.setShortNumber(exp.getAb());
			abbr.setSubscriber(sub);
			abbr.setTarget("sip:"+exp.getNp()+"@"+SipUtil.getDomain(request.getFrom().getURI()));
			subscriberDAO.save(abbr);
		}
		else{
			abbr.setTarget("sip:"+exp.getNp()+"@"+SipUtil.getDomain(request.getFrom().getURI()));
			subscriberDAO.update(abbr);
		}
		request.getSession().createRequest("BYE").send();
	}

}
