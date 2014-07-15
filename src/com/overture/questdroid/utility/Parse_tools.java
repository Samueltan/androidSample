package com.overture.questdroid.utility;

import java.text.DateFormat;
import java.util.Date;

import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.impl.cookie.BasicClientCookie;

public class Parse_tools {
	public static BasicClientCookie parseRawCookie(String rawCookie) throws Exception {
	    String[] rawCookieParams = rawCookie.split(";");
	
	    String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
	    if (rawCookieNameAndValue.length != 2) {
	        throw new Exception("Invalid cookie: missing name and value.");
	    }
	
	    String cookieName = rawCookieNameAndValue[0].trim();
	    String cookieValue = rawCookieNameAndValue[1].trim();
	    BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);

		
	    String[] rawDateNameAndValue = rawCookieParams[1].split("=");
        String dateName = rawDateNameAndValue[0].trim();
	        

	
        String dateValue = rawDateNameAndValue[1].trim();
	
        if (dateName.equalsIgnoreCase("expires")) {

	        Date expiryDate = DateUtils.parseDate(dateValue,  new String[]{DateUtils.PATTERN_RFC1036});
	        cookie.setExpiryDate(expiryDate);
        }  
        else {
	                throw new Exception("Invalid cookie: invalid attribute name.");
	            }
	    
	
	    return cookie;
	}
}

