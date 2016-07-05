package com.openmtas.provisioning.cf;

import org.springframework.stereotype.Component;

import com.openmtas.database.pojo.SubscriberConfiguration;
import com.openmtas.dispatcher.ExpressionResult;
import com.openmtas.parameter.GeneralParameters;

@Component("enablerCFNR")
public class EnablerCFNR extends EnablerCallForwarding {

	public EnablerCFNR(){
		service=GeneralParameters.CALL_FORWARDING_NO_RSPONSE;
	}
	
	protected void setExtraConfiguration(SubscriberConfiguration conf, ExpressionResult exp){
		if(exp.getDr()!=null)
			conf.setDuration(Integer.parseInt(exp.getDr()));
	}
}
