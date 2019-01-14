package com.jun.tools.toolkit;

public class ObjectToString {

	
	public static String arrayToString(int [] data){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("{");
		for(int i=0; data != null && i<data.length; i++){
			strBuilder.append(data[i] + ",");
		}
		if (strBuilder.length() > 1) {
			strBuilder.deleteCharAt(strBuilder.length() -1);
		}
		
		strBuilder.append("}");
		
		return strBuilder.toString();
	}
	
	public static String arrayToString(Object [] data){
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("{");
		for(int i=0; data != null && i<data.length; i++){
			strBuilder.append(data[i].toString() + ",");
		}
		if (strBuilder.length() > 1) {
			strBuilder.deleteCharAt(strBuilder.length() -1);
		}
		
		strBuilder.append("}");
		
		return strBuilder.toString();
	}
}
