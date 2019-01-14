package com.supermap.imobile.Tools;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class XMLParser {

	public XMLParser() {
		
	}
	
	public static void parserPull(String path){
		if (path != null) {
			File file = new File(path);
			if(!file.exists())
				return;
			
			FileInputStream inputStream = null;
			XmlPullParser parser = null;
			
			int eventType = -1;
			try {
				
				inputStream = new FileInputStream(file);
				parser = Xml.newPullParser();
				parser.setInput(inputStream, "UTF-8");
				
				eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						System.out.println("Start Document!!");
						break;
					case XmlPullParser.START_TAG:
						System.out.println("Start Tag: " + parser.getName());
						break;
					case XmlPullParser.END_TAG:
						System.out.println("End Tag: " + parser.getName());
						break;
					case XmlPullParser.TEXT:
						System.out.println("Text: " + parser.getText());
						break;
						
					default:
						break;
					}
					
					eventType = parser.next();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * 获取矢量切片配置文件XML文件中个的Bounds和Scales参数
	 * @param path            文件路径
	 * @param boundsIn        存储Bounds参数的数值,长度至少为4
	 * @param scaleLevelsIn   存放比例尺层级的数组列表
	 * @param scalesIn        存放比例尺大小的数组列表
	 */
	public static void parser(String path, String[] boundsIn, ArrayList<String> scaleLevelsIn, ArrayList<String> scalesIn){
		if (path != null ) {
			File file = new File(path);
			if(!file.exists())
				return;
			
			FileInputStream inputStream = null;
			InputStreamReader inputReader = null;
			BufferedReader bufferReader = null;
			
			String[] bounds = null;
			ArrayList<String> scaleLevels = null;
			ArrayList<String> scales = null;
			
			if (boundsIn == null) {
				bounds = new String[4];
			} else {
				bounds = boundsIn;
			}
			if (scaleLevelsIn == null) {
				scaleLevels = new ArrayList<String>();
			} else {
				scaleLevels = scaleLevelsIn;
			}
			if (scalesIn == null) {
				scales = new ArrayList<String>();
			} else {
				scales = scalesIn;
			}
			
			
			
			String key_start = "<sml:";
			String key_end = "</sml:";
			String key_close = ">";
			
			String key_Bounds = "Bounds";
			String key_Left = "Left";
			String key_Top = "Top";
			String key_Right = "Right";
			String key_Bottom = "Bottom";
			
			String key_Scales = "Scales";
			String key_Scale = "Scale";
			String key_Value = "Value";
			String key_Caption = "Caption";
			
			String line = null;
			String value = null;
			int index1 = 0, index2 = 0;
			
			try {
				
				inputStream = new FileInputStream(file);
				inputReader = new InputStreamReader(inputStream, "UTF-8");
				bufferReader = new BufferedReader(inputReader);
				
				while((line = bufferReader.readLine()) != null){
					// 1.解析Bounds
					if(line.startsWith(key_start + key_Bounds)){
						
						while (!line.startsWith(key_end + key_Bounds)) {
							line = bufferReader.readLine();
							
							// bounds的设置顺序与Rectangle2D的参数保持一致，逆时针
							if (line.startsWith(key_start + key_Left)) {
								index1 = line.indexOf(key_close);
								index2 = line.indexOf(key_end);

								value = line.substring(index1 + key_close.length(), index2);
								bounds[0] = value;
							}

							if (line.startsWith(key_start + key_Bottom)) {
								index1 = line.indexOf(key_close);
								index2 = line.indexOf(key_end);

								value = line.substring(index1 + key_close.length(), index2);
								bounds[1] = value;
							}

							if (line.startsWith(key_start + key_Right)) {
								index1 = line.indexOf(key_close);
								index2 = line.indexOf(key_end);

								value = line.substring(index1 + key_close.length(), index2);
								bounds[2] = value;
							}

							if (line.startsWith(key_start + key_Top)) {
								index1 = line.indexOf(key_close);
								index2 = line.indexOf(key_end);

								value = line.substring(index1 + key_close.length(), index2);
								bounds[3] = value;
							}

						}
					}
					
					// 2.解析Scales
					
					if(line.startsWith(key_start + key_Scales)){
						
						while (!line.startsWith(key_end + key_Scales)) {
							line = bufferReader.readLine();
							
							// 解析Scale
							if (line.startsWith(key_start + key_Scale)) {
								
								while (!line.startsWith(key_end + key_Scale)) {
									
									line = bufferReader.readLine();

									if (line.startsWith(key_start + key_Value)) {
										index1 = line.indexOf(key_close);
										index2 = line.indexOf(key_end);

										value = line.substring(index1 + key_close.length(), index2);
										scales.add(value);
									}
									
									if (line.startsWith(key_start + key_Caption)) {
										index1 = line.indexOf(key_close);
										index2 = line.indexOf(key_end);

										value = line.substring(index1 + key_close.length(), index2);
										scaleLevels.add(value);
									}
								}
							}
						}
					}
					
				}
			
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// close io
			finally{
				if(bufferReader != null){
					try {
						bufferReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(inputReader != null){
					try {
						inputReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}

}
