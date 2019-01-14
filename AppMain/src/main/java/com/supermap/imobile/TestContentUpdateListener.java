package com.supermap.imobile;

/**
 * 用于更新测试内容信息
 *
 */
public interface TestContentUpdateListener {

	/**
	 * 用于其他类更新测试信息
	 * @param content
	 */
	public void updateContent(String content);
	
	/**
	 * 测试结束,主要用于其他类中的测试循环结束
	 */
	public void onFinish();
}
