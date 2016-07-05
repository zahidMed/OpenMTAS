package com.openmtas.parameter;

public class GeneralParameters {

	public static long REQUEST_TIMEOUT=20000;
	public static long TRANSITION_TIMEOUT=15000;
	public static String CALL_FORWARDING_UNCONDITIONAL="cfu";
	public static String CALL_FORWARDING_BUSY="cfb";
	public static String CALL_FORWARDING_NO_RSPONSE="cfnr";
	public static String CALL_FORWARDING_UNREACHABLE="cfur";
	
	public static String SIP_URI_REGEX="(sip:.*)@(.*)";
	
	
	
	public static String SERVICE_CFU_ENABELING="*21*NP#";
	public static String SERVICE_CFU_DISABELING="*21#";
	
	public static String SERVICE_CFB_ENABELING="*67*NP#";
	public static String SERVICE_CFB_DISABELING="*67#";
	
	public static String SERVICE_CFNR_ENABELING="*61*NP*DR#";
	public static String SERVICE_CFNR_DISABELING="*61#";
	
	public static String SERVICE_CFUR_ENABELING="*62*NP#";
	public static String SERVICE_CFUR_DISABELING="*62#";
	
	public static String SERVICE_ABBR_DIALING="*AB";
	public static String SERVICE_ABBR_PROV="*51*AB*NP#";
	public static String SERVICE_ABBR_ERASE="*51*AB#";
	public static String SERVICE_ABBR_ERASE_ALL="*51#";
	
	public static String SERVICE_GROUP_SHORT_NUMBER="*#SN";
	public static String SERVICE_GROUP_CALL_PICKUP="**NP";
	public static String SERVICE_GROUP_CALL_PICKUP_SHORT_NUMBER="*#*SN";
	
}
