package com.openmtas.provisioning.cf;

import org.springframework.stereotype.Component;

import com.openmtas.parameter.GeneralParameters;

@Component("disablerCFNR")
public class DisablerCFNR extends DisablerCallForwarding {

	public DisablerCFNR(){
		service=GeneralParameters.CALL_FORWARDING_NO_RSPONSE;
	}
}
