package com.supermap.imobile;

import android.util.Log;

import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;

import java.io.File;

public class SuperMapOperation {
	
	
	public static boolean openWorkspace(Workspace workspace, String path, String password){
		
		boolean result = false;
		
		if(workspace == null || path == null)
			return result;
		
		String suffix  = null;
		File file = new File(path);
		if(!file.exists()){
			Log.e("OpenWorkspace", "工作空间文件不存在");
			return result;
		}
		
		if(file.isFile()){
			suffix = path.substring(path.lastIndexOf('.') + 1);
		}else{
			return false;
		}
		WorkspaceType wrkType = null;
		if(0 == suffix.compareToIgnoreCase("SMW")) {
			wrkType = WorkspaceType.SMW;
		} else if(0 == suffix.compareToIgnoreCase("SMWU")) {
			wrkType = WorkspaceType.SMWU;
		} else if(0 == suffix.compareToIgnoreCase("SXW")) {
			wrkType = WorkspaceType.SXW;
		} else if(0 == suffix.compareToIgnoreCase("SXWU")) {
			wrkType = WorkspaceType.SXWU;
		} else {
			wrkType = WorkspaceType.DEFAULT;

		} 
		
		WorkspaceConnectionInfo wrkConnInfo = new WorkspaceConnectionInfo();
		wrkConnInfo.setServer(path);
		wrkConnInfo.setPassword(password);
		wrkConnInfo.setType(wrkType);
		
		result = workspace.open(wrkConnInfo);
		
		return result;
	}

	public static Datasource openDatasource(Workspace workspace, String path, String password, EngineType engineType, String name){
		Datasource datasource = null;
		DatasourceConnectionInfo info = null;
		
		if(path != null && workspace != null && engineType != null){
			if(!new File(path).exists() && !path.startsWith("http")){
				Log.e("OpenDatasource", "数据源文件不存在");
				return null;
			}
			info = new DatasourceConnectionInfo();
			info.setServer(path);
			info.setEngineType(engineType);
			info.setAlias(name);
			
			if(password != null)
				info.setPassword(password);
			
			datasource = workspace.getDatasources().open(info);
		}
		
		return datasource ;
	}

}
