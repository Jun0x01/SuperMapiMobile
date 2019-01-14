package com.jun.tools.AppManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	public static BufferedReader execute_Reader(String argsStr){
		String[] argsArray = argsStr.split("\\s+"); // 多次匹配空格
		ArrayList<String> args = new ArrayList<>();
		for(int i=0; i<argsArray.length; i++){
			args.add(argsArray[i]);
		}
		BufferedReader reader = null;
		return  execute_Reader(args);
	}


	public static BufferedReader execute_Reader(ArrayList<String> args){
		BufferedReader reader = null;

		Process process = execute(args);
		if(process != null){
			int size = 1024 * 4;
			InputStreamReader inReader = new InputStreamReader(process.getInputStream());
			reader = new BufferedReader(inReader,size);

			process.destroy();
		}
		return  reader;
	}
}
