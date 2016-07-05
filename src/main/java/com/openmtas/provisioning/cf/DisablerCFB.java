package com.openmtas.provisioning.cf;
import org.springframework.stereotype.Component;

import com.openmtas.parameter.GeneralParameters;

@Component("disablerCFB")
public class DisablerCFB extends DisablerCallForwarding {

	public DisablerCFB(){
		service=GeneralParameters.CALL_FORWARDING_BUSY;
	}

}
