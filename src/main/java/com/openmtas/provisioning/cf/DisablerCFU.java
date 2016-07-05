package com.openmtas.provisioning.cf;


import org.springframework.stereotype.Component;

import com.openmtas.parameter.GeneralParameters;

@Component("disablerCFU")
public class DisablerCFU extends DisablerCallForwarding{

	public DisablerCFU(){
		service=GeneralParameters.CALL_FORWARDING_UNCONDITIONAL;
	}

}
