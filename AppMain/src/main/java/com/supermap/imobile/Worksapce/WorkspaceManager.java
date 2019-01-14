package com.supermap.imobile.Worksapce;

import android.util.Log;

import com.jun.tools.Message.ShowMessage;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.Datasources;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.data.WorkspaceVersion;

import java.io.File;

/**
 * Created by Jun on 2017/4/27.
 */

public class WorkspaceManager {

    static String tag = "WorkspaceManager";

    /**
     * Open a workspace
     * @param path
     * @param password
     * @param version
     * @return
     */
    public static boolean openWorkspace(Workspace workspace, final String path, final String password, final WorkspaceVersion version){

        boolean result = false;

        String suffix  = null;
        File file = new File(path);
        if(!file.exists()){
//            ShowMessage.showError(tag, "OpenWorkspace, the file doesn't exist. File: " + path);
            Log.e(tag,"OpenWorkspace, the file doesn't exist. File: " + path);
            return result;
        }

        if(file.isFile()){
            suffix = path.substring(path.lastIndexOf('.') + 1);
        }else{
            return result;
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

        int index1 = path.lastIndexOf('/');
        int index2 = path.lastIndexOf('.');
        String name = path.substring(index1 + 1, index2);
        workspace.setCaption(name);

        WorkspaceConnectionInfo wrkConnInfo = new WorkspaceConnectionInfo();
        wrkConnInfo.setServer(path);
        wrkConnInfo.setPassword(password);
        wrkConnInfo.setType(wrkType);
        wrkConnInfo.setName(name);
        if (version != null)
            wrkConnInfo.setVersion(version);

        result = workspace.open(wrkConnInfo);
        wrkConnInfo.dispose();
        if (!result) {
//            ShowMessage.showError(tag, "Open workspace failed. File: " + path);
            Log.e(tag, "Open workspace failed. File: " + path);
        }else {

//            ShowMessage.showInfo(tag, "Open workspace: " + name);
            Log.d(tag, "Open workspace: " + name);
        }

        return result;
    }

    /**
     * Create a workspace file
     * @param path
     * @param password
     * @param version
     * @return
     */
    public static boolean createWorkspace(Workspace workspace, final String path, String name, final String password, final WorkspaceVersion version){

        boolean result = false;
        if(workspace == null || path == null){
            return false;
        }
        String suffix  = null;
        File file = new File(path);
        if(file.exists()){
            ShowMessage.showError(tag, "Create workspace: The file has existed. Path: " + path);
//            Log.e(tag, "Create workspace: The file has existed. Path: " + path);
            return result;
        }
        // Create directories
        file.getParentFile().mkdirs();
        int index = path.lastIndexOf('.');
        if(index <=0) {
//            ShowMessage.showError(tag, "Create workspace: The path is illegal. Path: " + path);
            Log.e(tag, "Create workspace: The path is illegal. Path: " + path);
            return result;
        }

        if(name == null || name.isEmpty()){
            name = new File(path).getName();
            if(index > 0){
                name = name.substring(0, index);
            }
        }

        suffix = path.substring(index + 1);

        WorkspaceType wrkType = null;
        if(0 == suffix.compareToIgnoreCase("SMWU")) {
            wrkType = WorkspaceType.SMWU;
        } else if(0 == suffix.compareToIgnoreCase("SXWU")) {
            wrkType = WorkspaceType.SXWU;
        } else {
            wrkType = WorkspaceType.DEFAULT;
        }

        WorkspaceConnectionInfo wrkConnInfo = workspace.getConnectionInfo();
        wrkConnInfo.setServer(path);
        if (password != null)
            wrkConnInfo.setPassword(password);
        wrkConnInfo.setType(wrkType);
        wrkConnInfo.setName(name);
        workspace.setCaption(name);

        if(version != null)
        wrkConnInfo.setVersion(version);

        try {
            result = workspace.save();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!result) {
//            ShowMessage.showError(tag, "Create worksapce failed. File: " + path);
            Log.e(tag,"Create worksapce failed. File: " + path);
        }

        return result;
    }

    public static Datasource openDatasource(Workspace workspace, String path, String name, String password, EngineType engineType){
        Datasource datasource = null;
        DatasourceConnectionInfo info = null;

        if(path != null && workspace != null /*&& engineType != null*/){
            if(!new File(path).exists() && !path.startsWith("http")){
//                ShowMessage.showError(tag, "Open datasource: The file doesn't exist. File: " + path);
                Log.e(tag, "Open datasource: The file doesn't exist. File: " + path);
                return null;
            }

            if (engineType == EngineType.BingMaps) {
                name = "BingMaps";
            } else if (engineType == EngineType.BaiDu) {
                name = "BaiDuMaps";
            } else if (engineType == EngineType.GoogleMaps) {
                name = "GoogleMaps";
            } else if (engineType == EngineType.SuperMapCloud) {
                name = "GoogleMaps";
            }  else if (engineType == EngineType.OpenStreetMaps) {
                name = "OpenStreetMaps";
            } else if(path.startsWith("http")){
                String[] parts = path.split("/");
                if (engineType == EngineType.Rest) {
                    name = "Rest_" + parts[parts.length -1];
                } else  {
                    name = parts[parts.length -2] + "." + parts[parts.length -1];
                }

            } else if(name == null || name.isEmpty()){
                name = new File(path).getName();
                int index = name.lastIndexOf('.');
                if(index > 0){
                    name = name.substring(0, index);
                }
            }



            name = getValidDatasourceName(workspace, name);

            info = new DatasourceConnectionInfo();
            info.setServer(path);
            if(engineType != null)
            info.setEngineType(engineType);
            info.setAlias(name);
            if(path.startsWith("http")){
                if(path.contains("/wmts")){
                    info.setDriver("WMTS");
                }else if(path.contains("/wms")){
                    info.setDriver("WMS");
                }else if(path.contains("/wcs")){
                    info.setDriver("WCS");
                }else if(path.contains("/wfs")){
                    info.setDriver("WFS");
                }
            }
            if(engineType == EngineType.BingMaps && path.equals("https://www.microsoft.com/maps")){
                info.setPassword("AjQzacgJBGucxTgLF-rMOUVvqKyjv9BOQHWoRtGz9DW f5r9SY-97kmArtqBpxTTO");
            }
            if(engineType == EngineType.GoogleMaps && path.equals("http://www.google.cn/maps")){
                info.setPassword("AIzaSyATGDF4BqNR8_DUyLcOHVQujUEI6_9nL9o");
            }

            if(password != null)
                info.setPassword(password);

            datasource = workspace.getDatasources().open(info);
            info.dispose();
        } else {
//            ShowMessage.showError(tag, "Open Datasource, Parameters are illegal.");
            Log.e(tag, "Open Datasource, Parameters are illegal.");
        }


        return datasource ;
    }

    public static Datasource createDatasource(Workspace workspace, String path, String password, EngineType engineType, String name){
        Datasource datasource = null;
        DatasourceConnectionInfo info = null;

        if(path != null && workspace != null && engineType != null){
            if(new File(path).exists()){
                ShowMessage.showError(tag, "Create datasource, the file has existed. File: " + path);
//                Log.e(tag, "Create datasource, the file has existed. File: " + path);
                return null;
            }

            if(name == null || name.isEmpty()){
                name = new File(path).getName();
                int index = name.lastIndexOf('.');
                if(index > 0){
                    name = name.substring(0, index);
                }
            }

            name = getValidDatasourceName(workspace, name);

            info = new DatasourceConnectionInfo();
            info.setServer(path);
            info.setEngineType(engineType);
            info.setAlias(name);

            if(password != null)
                info.setPassword(password);

            datasource = workspace.getDatasources().create(info);
            info.dispose();
        } else {
//            ShowMessage.showError(tag, "Open Datasource, Parameters are illegal.");
            Log.e(tag, "Open Datasource, Parameters are illegal.");
        }

        return datasource ;
    }

    public static String getValidDatasourceName(Workspace workspace, String name){
        String newName = name;
        Datasources datasources = workspace.getDatasources();
        int count = datasources.getCount();
        Datasource datasource = null;
        for (int i=0; i<count; i++){
            datasource = datasources.get(newName);
            if(datasource != null){
                newName = name + "_" + i;
            }else {
                break;
            }
        }
        return newName;
    }

}
