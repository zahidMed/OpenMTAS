package com.openmtas.provisioning.cf;

import org.springframework.stereotype.Component;


import com.openmtas.parameter.GeneralParameters;



@Component("enablerCFU")
public class EnablerCFU extends EnablerCallForwarding{

	public EnablerCFU(){
		service=GeneralParameters.CALL_FORWARDING_UNCONDITIONAL;
	}
	
	

}
