package com.openmtas.dispatcher;

import java.io.IOException;
import java.util.Vector;

import javax.sdp.MediaDescription;
import javax.sdp.SdpException;
import javax.sdp.SdpFactory;
import javax.sdp.SdpParseException;
import javax.sdp.SessionDescription;
import javax.servlet.sip.SipServletMessage;
import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.SipServletResponse;
import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;

import org.apache.log4j.Logger;

import com.openmtas.parameter.GeneralParameters;

import gov.nist.javax.sdp.fields.AttributeField;

public class SipUtil {

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	public static String getUser(URI uri){
		if(uri.isSipURI()){
			return ((SipURI) uri).getUser();
		}
		return uri.toString().replaceAll(GeneralParameters.SIP_URI_REGEX, "$1");
	}
	
	public static String getDomain(URI uri){
		if(uri.isSipURI()){
			return ((SipURI) uri).getHost();
		}
		return uri.toString().replaceAll(GeneralParameters.SIP_URI_REGEX, "$2");
	}
	public static void notFound(SipServletRequest request) throws IOException{
		logger.debug("Send not found response to: "+request.getFrom());
		SipServletResponse notFound = request.createResponse(SipServletResponse.SC_NOT_FOUND);		
		notFound.send();
	}
	
	public static void forbidden(SipServletRequest request) throws IOException{
		logger.debug("Send forbidden response to: "+request.getFrom());
		SipServletResponse forbidden = request.createResponse(SipServletResponse.SC_FORBIDDEN);		
		forbidden.send();
	}
	

	public static boolean checkSDAttribute(SipServletMessage msg,String name)
	{
		logger.debug("Check Message body for the attribute: "+name);
		if(msg==null || msg.getContentLength()==0) return false;
		SdpFactory sdpFactory =SdpFactory.getInstance();
    	
		try {
			SessionDescription sd= sdpFactory
					.createSessionDescription(new String(msg.getRawContent()));
	        //logger.debug("Info: "+sd.getInfo().getValue());
	        Vector<MediaDescription>  attr=sd.getMediaDescriptions(false);
	        if(attr==null) return false;
		    //MediaDescription m=(MediaDescription) attr.get(0);
		    for(MediaDescription m : attr)
			    for(Object obj: m.getAttributes(false)) {
			    	 if (obj instanceof AttributeField) {
			    		 AttributeField f=(AttributeField) obj;
			    		 if(name.equals(f.getName()))
			    				 return true;
			    	 }
			    }   	
		  
		} catch (SdpParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SdpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return false;
	}
	
	
	public static void copyMessageContent(SipServletMessage source, SipServletMessage dest) throws IOException {
		logger.debug("start copy");
		if (source.getContentLength() > 0) {
			dest.setContent(source.getContent(), source.getContentType());
			String enc = source.getCharacterEncoding();
			if (enc != null && enc.length() > 0) {
				dest.setCharacterEncoding(enc);
			}
		}
		logger.debug("end copy");
	}
}
