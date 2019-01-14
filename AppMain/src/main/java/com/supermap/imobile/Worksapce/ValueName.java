package com.supermap.imobile.Worksapce;

import com.supermap.data.DeviceIDType;
import com.supermap.data.EngineType;
import com.supermap.data.Module;
import com.supermap.data.WorkspaceVersion;

//import com.supermap.data.LicenseManager;

/**
 * Created by Jun on 2017/8/8.
 */

public class ValueName {

    public static final String[] mWorksapceTypeArr = {"sxw", "smw", "sxwu", "smwu"};
    public static final String[] mEngineTypeArr = {"OpenGLCache", "Rest(rest,sci)", "ImagePlugins(bmp,jpg,png,sci,sit,tif,tiff)", "UDB",
            "BaiDu", "BingMaps", "GoogleMaps", "OGC(wmts)", "OGC(wms)", "OGC(wcs)", "OGC(wfs)", "OpenStreetMaps", "SuperMapCloud"};

    public static final String[] mWorksapceVerArr = {"SuperMap UGC 7.0", "SuperMap UGC 6.0"};

    public static String getEngineTypeName(EngineType engineType) {
        String type = "";
        if (engineType == null)
            return type;
        if (engineType == EngineType.OpenGLCache) {
            type = "OpenGLCache";
        } else if (engineType == EngineType.Rest) {
            type = "Rest";
        } else if (engineType == EngineType.IMAGEPLUGINS) {
            type = "ImagePlugins";
        } else if (engineType == EngineType.UDB) {
            type = "UDB";
        } else if (engineType == EngineType.BaiDu) {
            type = "BaiDu";
        } else if (engineType == EngineType.BingMaps) {
            type = "BingMaps";
        } else if (engineType == EngineType.GoogleMaps) {
            type = "GoogleMaps";
        } else if (engineType == EngineType.OGC) {
            type = "OGC";
        } else if (engineType == EngineType.OpenStreetMaps) {
            type = "OpenStreetMaps";
        } else if (engineType == EngineType.SuperMapCloud) {
            type = "SuperMapCloud";
        } else {
            type = "Unknown";
        }

        return type;
    }

    public static EngineType getEngineTypeByName(String name) {
        EngineType engineType = null;
        if (name == null)
            return null;

        if (name.equals("OpenGLCache")) {
            engineType = EngineType.OpenGLCache;
        } else if (name.equals("Rest")) {
            engineType = EngineType.Rest;
        } else if (name.equals("ImagePlugins")) {
            engineType = EngineType.IMAGEPLUGINS;
        } else if (name.equals("UDB")) {
            engineType = EngineType.UDB;
        } else if (name.equals("BaiDu")) {
            engineType = EngineType.BaiDu;
        } else if (name.equals("BingMaps")) {
            engineType = EngineType.BingMaps;
        } else if (name.equals("GoogleMaps")) {
            engineType = EngineType.GoogleMaps;
        } else if (name.equals("OGC")) {
            engineType = EngineType.OGC;
        } else if (name.equals("OpenStreetMaps")) {
            engineType = EngineType.OpenStreetMaps;
        } else if (name.equals("SuperMapCloud")) {
            engineType = EngineType.SuperMapCloud;
        }
        return engineType;
    }

    public static String getWorkspaceVersionName(WorkspaceVersion version) {
        String ver = null;
        if (version == WorkspaceVersion.UGC70) {
            ver = mWorksapceVerArr[0];
        } else if (version == WorkspaceVersion.UGC60) {
            ver = mWorksapceVerArr[1];
        }

        return ver;
    }

    public static WorkspaceVersion getWorkspaceVersionByName(String name) {
        WorkspaceVersion version = null;
        if (name != null) {
            if (name.equals(mWorksapceVerArr[0])) {
                version = WorkspaceVersion.UGC70;
            } else if (name.equals(mWorksapceVerArr[1])) {
                version = WorkspaceVersion.UGC60;
            }
        }
        return version;
    }


    public static String[] mModules = {
            "Core_Dev", "Core_Runtime",
            "Indoor_Navigation_Dev", "Indoor_Navigation_Runtime",
            "Industry_Navigation_Dev", "Industry_Navigation_Runtime",
            "Navigation_Dev", "Navigation_Runtime",
            "Plot3D_Dev", "Plot3D_Runtime",
            "Plot_Dev", "Plot_Runtime",
            "Realspace_Analyst_Dev", "Realspace_Analyst_Runtime",
            "Realspace_Dev", "Realspace_Runtime",
            "Realspace_Effect_Dev", "Realspace_Effect_Runtime",

    };

    // 8C
    public static String getModuleName(Module module) {
        String moduleStr = null;
        if (module == Module.Core_Dev) {
            moduleStr = "Core_Dev";
        } else if (module == Module.Core_Runtime) {
            moduleStr = "Core_Runtime";
        } else if (module == Module.Indoor_Navigation_Dev) {
            moduleStr = "Indoor_Navigation_Dev";
        } else if (module == Module.Indoor_Navigation_Runtime) {
            moduleStr = "Indoor_Navigation_Runtime";
        } else if (module == Module.Industry_Navigation_Dev) {
            moduleStr = "Industry_Navigation_Dev";
        } else if (module == Module.Industry_Navigation_Runtime) {
            moduleStr = "Industry_Navigation_Runtime";
        } else if (module == Module.Navigation_Dev) {
            moduleStr = "Navigation_Dev";
        } else if (module == Module.Navigation_Runtime) {
            moduleStr = "Navigation_Runtime";
        } else if (module == Module.Plot3D_Dev) {
            moduleStr = "Plot3D_Dev";
        } else if (module == Module.Plot3D_Runtime) {
            moduleStr = "Plot3D_Runtime";
        } else if (module == Module.Plot_Dev) {
            moduleStr = "Plot_Dev";
        } else if (module == Module.Plot_Runtime) {
            moduleStr = "Plot_Runtime";
        } else if (module == Module.Realspace_Analyst_Dev) {
            moduleStr = "Realspace_Analyst_Dev";
        } else if (module == Module.Realspace_Analyst_Runtime) {
            moduleStr = "Realspace_Analyst_Runtime";
        } else if (module == Module.Realspace_Dev) {
            moduleStr = "Realspace_Dev";
        } else if (module == Module.Realspace_Runtime) {
            moduleStr = "Realspace_Runtime";
        } else if (module == Module.Realspace_Effect_Dev) {
            moduleStr = "Realspace_Effect_Dev";
        } else if (module == Module.Realspace_Effect_Runtime) {
            moduleStr = "Realspace_Effect_Runtime";
        }
        return moduleStr;
    }

    public static Module getModuleByName(String name){
        Module module = null;
        if("Core_Dev".equals(name)){
            module = Module.Core_Dev;
        } else if("Core_Runtime".equals(name)){
            module = Module.Core_Runtime;
        } else if("Indoor_Navigation_Dev".equals(name)){
            module = Module.Indoor_Navigation_Dev;
        } else if("Indoor_Navigation_Runtime".equals(name)){
            module = Module.Indoor_Navigation_Runtime;
        } else if("Industry_Navigation_Dev".equals(name)){
            module = Module.Industry_Navigation_Dev;
        } else if("Industry_Navigation_Runtime".equals(name)){
            module = Module.Industry_Navigation_Runtime;
        } else if("Navigation_Dev".equals(name)){
            module = Module.Navigation_Dev;
        } else if("Navigation_Runtime".equals(name)){
            module = Module.Navigation_Runtime;
        } else if("Plot3D_Dev".equals(name)){
            module = Module.Plot3D_Dev;
        } else if("Plot3D_Runtime".equals(name)){
            module = Module.Plot3D_Runtime;
        } else if("Plot_Dev".equals(name)){
            module = Module.Plot_Dev;
        } else if("Plot_Runtime".equals(name)){
            module = Module.Plot_Runtime;
        } else if("Realspace_Analyst_Dev".equals(name)){
            module = Module.Realspace_Analyst_Dev;
        } else if("Realspace_Analyst_Runtime".equals(name)){
            module = Module.Realspace_Analyst_Runtime;
        } else if("Realspace_Dev".equals(name)){
            module = Module.Realspace_Dev;
        } else if("Realspace_Runtime".equals(name)){
            module = Module.Realspace_Runtime;
        } else if("Realspace_Effect_Dev".equals(name)){
            module = Module.Realspace_Effect_Dev;
        } else if("Realspace_Effect_Runtime".equals(name)){
            module = Module.Realspace_Effect_Runtime;
        }
        return  module;
    }


    public static String getDeviceIDTypeName(DeviceIDType deviceIDType){
        String type = "";
        if(deviceIDType == DeviceIDType.MAC){
            type = "MAC";
        } else if(deviceIDType == DeviceIDType.UUID){
            type = "UUID";
        } else if(deviceIDType == DeviceIDType.IMEI){
            type = "IMEI";
        } else if(deviceIDType == DeviceIDType.TMIMEI){
            type = "TMIMEI";
        }

        return type;
    }

    public static DeviceIDType getDeviceIDType(String name){

        DeviceIDType type = null;
        if ("MAC".equals(name)){
            type = DeviceIDType.MAC;
        } else if ("UUID".equals(name)){
            type = DeviceIDType.UUID;
        } else if ("IMEI".equals(name)){
            type = DeviceIDType.IMEI;
        } else if ("TMIMEI".equals(name)){
            type = DeviceIDType.TMIMEI;
        }
        return type;
    }

}
