package com.openmtas.session;

import java.util.Iterator;
import java.util.Set;

import com.openmtas.database.pojo.VSToRSLink;

public abstract class MultiSessionsCallContext extends GenericCallContext{

	Set<CallContext> callContexes;
	
	Iterator<VSToRSLink> callForkIterator;
	
	public MultiSessionsCallContext(CallContextManager manager) {
		super(manager);
		// TODO Auto-generated constructor stub
	}

	public Set<CallContext> getCallContexes() {
		return callContexes;
	}

	public void setCallContexes(Set<CallContext> callContexes) {
		this.callContexes = callContexes;
	}

	
	public abstract void invalidateOtherContexts(CallContext context);
	
	

}
