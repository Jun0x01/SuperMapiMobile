
/**
 * 
 * Logcat 获取应用日志
 * 
 * @author Xingjun
 * 2016.08.18
 *
 */
package com.jun.tools.logcat;


/*
1.设置权限 READ_LOGS
2.使用方法一：
  1)注册服务LogcatService
  2)使用LogcatServiceManager启动服务
3.使用方法二：
       使用LogcatSaveManager, 根据情况，创建线程进行保存
4.获取日志的方法参考LogcatHelper
  保存日志时，做了时间过滤，参考LogcatSaveManager, 根据情况处理

*/