package com.jun.tools.toolkit;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class ImgEqualAssert {
	private static String tag = "ImgEqualAssert";

	public static void isImageEqual(String message , String srcImg, String targetImg) {

		int length = 0;

		File f1 = new File(srcImg);
		File f2 = new File(targetImg);
		Assert.assertTrue("测试截图不存在: " + srcImg, f1.exists());
		Assert.assertTrue("期望截图不存在,请检测测试截图是否正确，并将正确图片拷贝到期望截图目录: " + srcImg, f2.exists());
		
		try {
			FileInputStream fs1;
			fs1 = new FileInputStream(f1);
			FileInputStream fs2;
			fs2 = new FileInputStream(f2);
			BufferedInputStream buf1 = new BufferedInputStream(fs1,8192);
			BufferedInputStream buf2 = new BufferedInputStream(fs2,8192);
			byte[] b1 = new byte[buf1.available()];
			byte[] b2 = new byte[buf2.available()];
			if (b1.length != b2.length) {
				
				UnitAssert.fail(message +"测试截图与期望截图的大小不同，测试截图: " + srcImg);
				
			} else {
				length = b1.length;
				for (int i = 0; i < length; i++) {

					if (b1[(int) i] != b2[(int) i]) {
//						System.out.println("图片对比，第 " + i + " 点比较失败");
						UnitAssert.fail(message + "测试截图与期望截图的内容不, 测试截图: " + srcImg);
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			UnitAssert.fail("图片对比异常, " + srcImg);
		}
	}
	

	public static String imageEqual( String srcImg, String targetImg) {

		String  result = null;
		int length = 0;

		File f1 = new File(srcImg);
		File f2 = new File(targetImg);
		if(!f1.exists()){
			result = "测试截图不存在: " + srcImg;
			return result;
		}
		if(!f2.exists()){
			result = "期望截图不存在,请检测测试截图是否正确，并将正确图片拷贝到期望截图目录: " + srcImg;
			return result;
		}
		
		try {
			FileInputStream fs1;
			fs1 = new FileInputStream(f1);
			FileInputStream fs2;
			fs2 = new FileInputStream(f2);
			BufferedInputStream buf1 = new BufferedInputStream(fs1,8192);
			BufferedInputStream buf2 = new BufferedInputStream(fs2,8192);
			byte[] b1 = new byte[buf1.available()];
			byte[] b2 = new byte[buf2.available()];
			if (b1.length != b2.length) {
				
				result = "测试截图与期望截图的大小不同，测试截图: " + srcImg;
				
			} else {
				length = b1.length;
				for (int i = 0; i < length; i++) {

					if (b1[(int) i] != b2[(int) i]) {
						result = "测试截图与期望截图的内容不同, 测试截图: " + srcImg;
						break;
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result = "图片对比异常, " + srcImg;
		}
		
		return result;
	}
	
}
