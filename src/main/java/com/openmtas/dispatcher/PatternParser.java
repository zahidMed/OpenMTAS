package com.openmtas.dispatcher;

import javax.servlet.sip.SipURI;

import org.apache.log4j.Logger;

public class PatternParser {

	public static final Logger logger=Logger.getLogger("OpenMTAS");
	
	public static ExpressionResult match(String input,String pattern){
		
		logger.debug("check match for input: "+input+", pattern: "+pattern);
		
		
		ExpressionResult result= new ExpressionResult();
		char[] inChar=input.toCharArray();
		char[] patternChar=pattern.toCharArray();
		int index=0;
		for(int i=0;i<patternChar.length;i++)
		{
			if(patternChar[i]=='N' && i< patternChar.length-1 && patternChar[i+1]=='P'){
				StringBuilder np= new StringBuilder();
				while((i+2>=patternChar.length && index<inChar.length) || (i+2<patternChar.length && patternChar[i+2]!=inChar[index] && index<inChar.length))
				{
					np.append(inChar[index]);
					index++;
				}
				i++;
				result.setNp(np.toString());
				//System.out.println("NP: "+np);
			}
			else if(patternChar[i]=='D' && i< patternChar.length-1 && patternChar[i+1]=='R'){
				StringBuilder dr= new StringBuilder();
				while((i+2>=patternChar.length && index<inChar.length) || (i+2<patternChar.length && patternChar[i+2]!=inChar[index] && index<inChar.length))
				{
					dr.append(inChar[index]);
					index++;
				}
				i++;
				result.setDr(dr.toString());
				//System.out.println("DR: "+dr);
			}
			else if(patternChar[i]=='A' && i< patternChar.length-1 && patternChar[i+1]=='B'){
				StringBuilder ab= new StringBuilder();
				while((i+2>=patternChar.length && index<inChar.length) || (i+2<patternChar.length && patternChar[i+2]!=inChar[index] && index<inChar.length))
				{
					ab.append(inChar[index]);
					index++;
				}
				i++;
				result.setAb(ab.toString());
				//System.out.println("AB: "+ab);
			}
			else if(patternChar[i]=='S' && i< patternChar.length-1 && patternChar[i+1]=='N'){
				StringBuilder ab= new StringBuilder();
				while((i+2>=patternChar.length && index<inChar.length) || (i+2<patternChar.length && patternChar[i+2]!=inChar[index] && index<inChar.length))
				{
					ab.append(inChar[index]);
					index++;
				}
				i++;
				result.setSn(ab.toString());
				//System.out.println("AB: "+ab);
			}
			else if(patternChar[i]==inChar[index]){
				index++;
			}
			else{
				return null;
			}
		}
		return result;
	}
	
	
	
}
