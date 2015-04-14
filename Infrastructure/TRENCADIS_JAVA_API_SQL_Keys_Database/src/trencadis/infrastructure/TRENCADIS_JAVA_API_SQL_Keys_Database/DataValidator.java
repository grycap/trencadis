/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trencadis.infrastructure.TRENCADIS_JAVA_API_SQL_Keys_Database;

import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DataValidator {
	
	/**
	 * UUID in DCE security format
	 * @param uuidStr
	 * @return
	 */
	public static boolean validateUUID(String uuidStr) {
		
		int uuidValidLength = 36;
		if (uuidStr.length() != uuidValidLength) return false;
		
		String uuidPatternStr = "^[0-9A-F]{8,8}-[0-9A-F]{4,4}-[0-9A-F]{4,4}-[0-9A-F]{4,4}-[0-9A-F]{12,12}$";
		Pattern pattern = Pattern.compile(uuidPatternStr);
		
    	Matcher matcher = pattern.matcher(uuidStr);
		
    	boolean valid = false;
    	valid = matcher.find();
    	
    	return (((valid)&&(matcher.start()==0)&&(matcher.end()==uuidValidLength))?true:false);
	}
	
	/**
	 * HMAC in base64
	 * @param macStr
	 * @return
	 */
	public static boolean validateMAC(String macStr) {
		
		int macValidLength = 28;
		if (macStr.length() != macValidLength) return false;
		
		String macPatternStr = "^[A-Za-z0-9+/]{27}=$";
		Pattern pattern = Pattern.compile(macPatternStr);
		
    	Matcher matcher = pattern.matcher(macStr);
		
    	boolean valid = false;
    	valid = matcher.find();
    	
    	return (((valid)&&(matcher.start()==0)&&(matcher.end()==macValidLength))?true:false);
	}
	
	/**
	 * Valida las ontologias: al menos debe existir una y los
	 * valores posibles de ontologia son positivos.
	 * @param ontVector
	 * @return
	 */
	public static boolean validateOntology(Vector<Integer> ontVector) {
		
		int ontMinValue = 0;
		if (ontVector.size() < 1) return false;

		Iterator<Integer> it = ontVector.iterator();
		while (it.hasNext()) {
			Integer tmpOnt = (Integer) it.next();
			if (tmpOnt.intValue() < ontMinValue) return false;
		}
    	return true;
	}	
}