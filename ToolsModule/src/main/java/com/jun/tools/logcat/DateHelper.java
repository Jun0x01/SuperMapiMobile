package com.jun.tools.logcat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

	private static long mTimeOffset = 8*60*60*1000;
	
	public DateHelper() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 获取当前时间,格式为"yyy-MM-dd--HH-mm-ss"
	 * @return
	 */
	public static String getDate(){
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd--HH-mm-ss");
		Date date = new Date();
		strDate = format.format(date);
		return strDate;
	}
	
	/**
	 * 获取当前时间,格式为"yyy-MM-dd--HH-mm"
	 * @return
	 */
	public static String getDate1(){
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd--HH-mm");
		Date date = new Date();
		strDate = format.format(date);
		return strDate;
	}
	
	/**
	 * 获取当前时间,格式为"yyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getDate2(){
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
		Date date = new Date();
		strDate = format.format(date);
		return strDate;
	}
	
	/**
	 * 获取当前时间,格式为"yyy-MM-dd"
	 * @return
	 */
	public static String getDate3(){
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		strDate = format.format(date);
		return strDate;
	}
	
	
	/**
	 * 获取当前时间,格式为"MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getTime(){
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
		Date date = new Date();
		strDate = format.format(date);
		return strDate;
	}
	/**
	 * 获取当前时间,格式为"HHmm"
	 * @return
	 */
	public static String getTime1(){
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat("HHmm");
		Date date = new Date();
		strDate = format.format(date);
		return strDate;
	}
	
	/**
	 * 将时间ms转换成字符串,格式为"mm分ss秒"
	 * @param time
	 * @return
	 */
	public static String transferTime(long time){
		if(time<0)
			time=0; // 负值转换结果不对
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat("mm分ss秒");
		Date date = new Date(time - mTimeOffset);
		strDate = format.format(date);
		return strDate;
	}
	
	/**
	 * 将时间ms转换成指定格式字符串,默认格式为"HH时mm分ss秒"
	 * @param time
	 * @return
	 */
	public static String formatTime(long time, String strformat){
		if(time<0)
			time=0; // 负值转换结果不对
		if(strformat == null || strformat.isEmpty()){
			strformat = "HH时mm分ss秒";
		}
		String strDate = null;
		SimpleDateFormat format = new SimpleDateFormat(strformat);
		Date date = new Date(time - mTimeOffset);
		strDate = format.format(date);
		return strDate;
	}
	
}
