package com.supermap.imobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.Map;
import com.supermap.mapping.MapControl;

public class ToolKit {

	private Workspace m_Workspace;

	private WorkspaceConnectionInfo m_ConnectionInfo;
	private Datasources m_Datasources;
	private Map m_Map;
	private MapControl m_MapControl;

	public void openMap(MapControl mMapControl) {
		m_Map = mMapControl.getMap();
		m_Map.setWorkspace(m_Workspace);
		m_Map.refresh();
	}
	
	public static Workspace newWorkspace(String file, WorkspaceType type) {
		Workspace workspace = new Workspace();
		WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
		info.setServer(file);
		info.setType(type);
		workspace.open(info);
		return workspace;
	}
	
	//打开数据源的集合
	public Datasources openDatasources(){
		m_Datasources = m_Workspace.getDatasources();
		return m_Datasources;
	}
	
	//打开数据源
	public static Datasource openDatasource(Workspace workspace, String server,EngineType type){
		DatasourceConnectionInfo info = getDatasourceConnectionInfo(workspace, server, type, false);
		return workspace.getDatasources().open(info);
	}
	
	public static Datasource openDatasource(Workspace workspace, String server,EngineType type, boolean isReadOnly){
		DatasourceConnectionInfo info = getDatasourceConnectionInfo(workspace, server, type, isReadOnly);
		return workspace.getDatasources().open(info);
	}
	
	private static DatasourceConnectionInfo getDatasourceConnectionInfo(Workspace workspace, String server,EngineType type, boolean isReadOnly){
		DatasourceConnectionInfo info = new DatasourceConnectionInfo();
		info.setServer(server);
		
		info.setEngineType(type);
		if(type == EngineType.OGC){
			info.setDriver("WMTS");
		}	
//		info.setReadOnly(isReadOnly);
		Datasources datasources = workspace.getDatasources();
		
		//避免数据源打不开的情况
		String dsName = "testDsName";
		int suffix = 1;
		String name = dsName;
		while(datasources.get(name) != null){
			name = dsName + suffix;
			suffix++;
		}
		info.setAlias(name);
		
		return info;
	}

	//释放工作空间
	public void disposeWorkspace(){
		if(m_MapControl != null){
			m_MapControl.dispose();
			m_MapControl = null;
		}
		
		if(m_ConnectionInfo != null){
			m_ConnectionInfo.dispose();
			m_ConnectionInfo = null;
		}
		
		if(m_Workspace != null){
			m_Workspace.close();
			m_Workspace.dispose();
			m_Workspace = null;
		}
	}
	
	public static Dataset getExpectedDt(Datasource ds, String dtName, Dataset backup){
		Dataset expectedDt = null;
		if(ds.getDatasets().contains(dtName)){
			expectedDt = ds.getDatasets().get(dtName);
		}
		else if (backup != null){
			expectedDt = ds.copyDataset(backup, dtName, backup.getEncodeType());
		}
		return expectedDt;
	}
	
	public static Dataset getTempDatasetByCopy(Dataset oldDataset, Datasource des_ds){
		String del_dtname = "del" + oldDataset.getName();
		if(des_ds.getDatasets().contains(del_dtname)){
			des_ds.getDatasets().delete(del_dtname);
		}
		return des_ds.copyDataset(oldDataset, del_dtname, oldDataset.getEncodeType());
	}
	
	


	public static int dip2px(Context contex,float dipValue){ 
        final float scale = contex.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	}
	
	public static int sp2px(Context contex,float spVale){ 
        final float scale = contex.getResources().getDisplayMetrics().scaledDensity; 
        return (int)(spVale * scale + 0.5f); 
	} 
	
	 /** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public static void copyFile(String oldPath, String newPath) { 
       try { 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1444];  
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
           } 
       } 
       catch (Exception e) { 
           e.printStackTrace(); 
       } 
   } 
   /** 
     * 复制整个文件夹内容 
     * @param oldPath String 原文件路径 如：c:/fqf 
     * @param newPath String 复制后路径 如：f:/fqf/ff 
     * @return boolean 
     */ 
   public static void copyFolder(String oldPath, String newPath) { 
       try { 
    	   File file = new File(newPath) ;
			if(!file.exists()){
				file.mkdir();
			}

			delAllFile(newPath);
			
           File a=new File(oldPath); 
           String[] files=a.list(); 
           File temp=null; 
           for (int i = 0; i < files.length; i++) { 
               if(oldPath.endsWith(File.separator)){ 
                   temp=new File(oldPath+files[i]); 
               } 
               else{ 
                   temp=new File(oldPath+File.separator+files[i]); 
               } 
               if(temp.isFile()){ 
                   FileInputStream input = new FileInputStream(temp); 
                   FileOutputStream output = new FileOutputStream(newPath + "/" + 
                           (temp.getName()).toString()); 
                   byte[] b = new byte[1024 * 5]; 
                   int len; 
                   while ( (len = input.read(b)) != -1) { 
                       output.write(b, 0, len); 
                   } 
                   output.flush(); 
                   output.close(); 
                   input.close(); 
               } 
               if(temp.isDirectory()){//如果是子文件夹 
                   copyFolder(oldPath+"/"+files[i],newPath+"/"+files[i]); 
               } 
           } 
       } 
       catch (Exception e) { 
           e.printStackTrace(); 
       } 
   }
   
   //删除指定文件夹下所有文件
   //param path 文件夹完整绝对路径
    public static boolean delAllFile(String path) {
    	boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
          return flag;
        }
        if (!file.isDirectory()) {
          return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
           if (path.endsWith(File.separator)) {
              temp = new File(path + tempList[i]);
           } else {
               temp = new File(path + File.separator + tempList[i]);
           }
           if (temp.isFile()) {
              temp.delete();
           }
           if (temp.isDirectory()) {
              delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
              delFolder(path + "/" + tempList[i]);//再删除空文件夹
              flag = true;
           }
        }
        return flag;
    }
    
    //删除文件夹
    //param folderPath 文件夹完整绝对路径
   public static void delFolder(String folderPath) {
	   try {
		   delAllFile(folderPath); //删除完里面所有内容
		   String filePath = folderPath;
		   filePath = filePath.toString();
		   File myFilePath = new File(filePath);
		   myFilePath.delete(); //删除空文件夹
	   } catch (Exception e) {
		   e.printStackTrace(); 
	   }
  }
   
   /**
    * 删除指定类型的文件
    * @param path
    * @param type
    * @return
    */
   public static boolean delAllFile(String path, String type) {
   	boolean flag = false;
       File file = new File(path);
       if (!file.exists()) {
         return flag;
       }
       if (!file.isDirectory()) {
         return flag;
       }
       File[] tempList = file.listFiles();
       File temp = null;
       if(tempList == null)
    	   return true;
       
       for (int i = 0; i < tempList.length; i++) {
         
          temp = tempList[i];
          
          if (temp.isFile() && temp.getAbsolutePath().endsWith(type)) {
             temp.delete();
          }
          
          if (temp.isDirectory()) {
             delAllFile(path + "/" + tempList[i], type);//先删除文件夹里面的文件
             flag = true;
          }
       }
       return flag;
   }
   
}
