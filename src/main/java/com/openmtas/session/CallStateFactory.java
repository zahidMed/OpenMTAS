package com.openmtas.session;

import java.util.HashMap;
import java.util.Map;

import com.openmtas.session.state.Ending;
import com.openmtas.session.state.Hold;
import com.openmtas.session.state.Idle;
import com.openmtas.session.state.InitCall;
import com.openmtas.session.state.InitCallForwardingBusy;
import com.openmtas.session.state.InitCallForwardingNoResponse;
import com.openmtas.session.state.InitCallForwardingUnconditionnal;
import com.openmtas.session.state.InitSeqCall;
import com.openmtas.session.state.InitSubscribe;
import com.openmtas.session.state.Ongoing;
import com.openmtas.session.state.Refering;
import com.openmtas.session.state.Ringing;
import com.openmtas.session.transitions.CallCancellation;
import com.openmtas.session.transitions.CallProgress;
import com.openmtas.session.transitions.ConferenceProgress;
import com.openmtas.session.transitions.HoldProgress;
import com.openmtas.session.transitions.ReferProgress;
import com.openmtas.session.transitions.SeqCallPrograss;
import com.openmtas.session.transitions.UnHoldProgress;

public class CallStateFactory {

	public static int STATE_IDLE=1;
	public static int STATE_CALL_PROGRESS=2;
	public static int STATE_RINGING=3;
	
	//public static int STATE_INIT=0;
	public static int STATE_INIT_CALL=4;
	public static int STATE_INIT_CFU=5;
	public static int STATE_INIT_CFB=6;
	public static int STATE_INIT_CFNR=7;
	//public static int STATE_INITIALISATION=0;
	//public static int STATE_PROGRESSING=5;
	public static int STATE_CALL_ONGOING=8;
	public static int STATE_CALL_HOLD_PROGRESS=9;
	public static int STATE_CALL_HOLD=10;
	public static int STATE_CALL_UNHOLD_PROGRESS=11;
	public static int STATE_REFER_PROGRESS=12;
	public static int STATE_REFER=13;
	public static int STATE_ENDING=14;
	public static int STATE_CONFERENCE_PROGRESS=19;
	
	
	
	public static int STATE_INIT_SUBSCRIBE=15;
	public static int STATE_SUBSCRIBE_PROGRSS=19;
	public static int STATE_INIT_CALL_FORK_SEQ=16;
	public static int STATE_PROGRESSL_FORK_SEQ=17;
	public static int STATE_RINGING_FORK_SEQ=18;
	
	
	
	public static int STATE_CALL_CANCELLATION=-1;
	
	private static CallStateFactory instance=null;
	
	Map<Integer,CallStep> states= new HashMap<Integer, CallStep>();
	
	private CallStateFactory(){
		states.put(STATE_IDLE, new Idle());
		states.put(STATE_INIT_CALL, new InitCall());
		states.put(STATE_INIT_CFU, new InitCallForwardingUnconditionnal());
		states.put(STATE_INIT_CFB, new InitCallForwardingBusy());
		states.put(STATE_INIT_CFNR, new InitCallForwardingNoResponse());
		//states.put(STATE_PROGRESSING, new Progressing());
		states.put(STATE_CALL_ONGOING, new Ongoing());
		states.put(STATE_CALL_CANCELLATION, new CallCancellation());
		states.put(STATE_CALL_HOLD_PROGRESS, new HoldProgress());
		states.put(STATE_CALL_HOLD, new Hold());
		states.put(STATE_CALL_UNHOLD_PROGRESS, new UnHoldProgress());
		states.put(STATE_ENDING, new Ending());
		states.put(STATE_CONFERENCE_PROGRESS, new ConferenceProgress());
		
		
		
		states.put(STATE_CALL_PROGRESS, new CallProgress());
		states.put(STATE_RINGING, new Ringing());
		states.put(STATE_REFER_PROGRESS, new ReferProgress());
		states.put(STATE_REFER, new Refering());
		
		states.put(STATE_INIT_SUBSCRIBE, new InitSubscribe());
		
		states.put(STATE_INIT_CALL_FORK_SEQ, new InitSeqCall());
		states.put(STATE_PROGRESSL_FORK_SEQ, new SeqCallPrograss());
		
	}
	
	public static CallStateFactory getInstance(){
		if(instance==null) instance= new CallStateFactory();
		return instance;
	}
	
	public CallStep getCallState(int state){
		return states.get(state);
	}

}
