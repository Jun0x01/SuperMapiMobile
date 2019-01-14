package com.jun.tools.logcat;

import java.io.IOException;
import java.util.ArrayList;

public class RuntimeHelper {

	public RuntimeHelper() {
		
	}

	public static Process execute(ArrayList<String> args){
		Process process = null;
		String[] commandArray = args.toArray(new String[args.size()]);
		try {
			process = Runtime.getRuntime().exec(commandArray);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return process;
	}
}
