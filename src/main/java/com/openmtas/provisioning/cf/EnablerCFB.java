package com.openmtas.provisioning.cf;



import org.springframework.stereotype.Component;

import com.openmtas.parameter.GeneralParameters;

@Component("enablerCFB")
public class EnablerCFB extends EnablerCallForwarding {

	public EnablerCFB(){
		service=GeneralParameters.CALL_FORWARDING_BUSY;
	}


}
