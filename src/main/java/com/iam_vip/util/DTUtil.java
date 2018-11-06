/**
 * 
 */
package com.iam_vip.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public final class DTUtil {
	
	public static final String FMT_A1 = "yyyy-MM-dd_HH-mm-ss";
	public static final String FMT_A2 = "yyyyMMdd-HHmmss";
	public static final String FMT_A3 = "yyyyMMdd_HHmmss";
	public static final String FMT_A4 = "yyyyMMddHHmmss";
	
	public static final String FMT_AS1 = "yyyy-MM-dd_HH-mm-ss_SSS";
	public static final String FMT_AS2 = "yyyyMMdd-HHmmss-SSS";
	public static final String FMT_AS3 = "yyyyMMdd_HHmmss_SSS";
	public static final String FMT_AS4 = "yyyyMMddHHmmssSSS";

	/**
	 * 
	 */
	private DTUtil() {
	}

	public static final DateFormat getFmt(String fmt) {
		return new SimpleDateFormat(fmt);
	}

	public static final String formatDT(String fmt, Date date) {
		return getFmt(fmt).format(date);
	}

	public static final Date parseDT(String fmt, String date) {
		try {
			return getFmt(fmt).parse(date);
		}
		catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	
	
	
	
	public static final String formatDT(String fmt) {
		return formatDT(fmt, new Date());
	}
	
	
	
	
	/**
	 * yyyy-MM-dd_HH-mm-ss 
	 */
	public static final String formatA() {
		return formatA(1);
	}

	/**
	 * 1: yyyy-MM-dd_HH-mm-ss <br/>
	 * 2: yyyyMMdd-HHmmss <br/>
	 * 3: yyyyMMdd_HHmmss <br/>
	 * 4: yyyyMMddHHmmss <br/>
	 * @param n: the item to choose
	 */
	public static final String formatA( int n ) {
		switch ( n ) {
			case 2: return formatDT( FMT_A2 );
			case 3: return formatDT( FMT_A3 );
			case 4: return formatDT( FMT_A4 );
			case 1: default: return formatDT( FMT_A1 );
		}
	}
	
	/**
	 * yyyy-MM-dd_HH-mm-ss_SSS
	 */
	public static final String formatAS() {
		return formatAS(1);
	}
	
	/**
	 * 1: yyyy-MM-dd_HH-mm-ss_SSS <br/>
	 * 2: yyyyMMdd-HHmmss-SSS <br/>
	 * 3: yyyyMMdd_HHmmss_SSS <br/>
	 * 4: yyyyMMddHHmmssSSS <br/>
	 * @param n: the item to choose
	 */
	public static final String formatAS( int n ) {
		switch ( n ) {
		case 2: return formatDT( FMT_AS2 );
		case 3: return formatDT( FMT_AS3 );
		case 4: return formatDT( FMT_AS4 );
		case 1: default: return formatDT( FMT_AS1 );
		}
	}
	

}
