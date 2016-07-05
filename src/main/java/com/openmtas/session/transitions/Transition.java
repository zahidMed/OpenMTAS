package com.openmtas.session.transitions;

import java.io.IOException;

import javax.servlet.sip.ServletParseException;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.TooManyHopsException;

import com.openmtas.session.CallContext;
import com.openmtas.session.CallStep;
import com.openmtas.session.state.CallState;

public abstract class Transition extends CallStep{
}
