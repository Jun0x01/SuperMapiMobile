package com.supermap.imobile.ActivitySetting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

import java.util.ArrayList;

/**
 * Application's preference manager
 *
 * Created by Jun on 2017/4/25.
 */

public class SharedPreferenceManager{

    /**
     * Instance of SharedPreferences, it is static
     */
    static SharedPreferences mSharedPreferences;
    /**
     * SharedPreferences' Name
     */
    final String mPreferenceName = "SuperMapiMobilePreferences";
    /**
     * Log tag
     */
    private final static String tag = "SharedPreferenceManager" ;

    /**
     *  Modules
     */
    public static final String mKey_Modules = "Modules";

    final static String split = ",";

    /**
     *  User Series Number, License Code
     */
    public static final String mKey_SN = "UserSN";

    /**
     *  License path in which the license file is stored.
     */
    public static final String mKey_LicensePath = "LicensePath";

    /**
     * License file's backup path, copy the license file into this path after activating successfully.
     */
    public static final String mKey_LicenseBackupPath = "LicenseBackupPath";

    /**
     * DeviceID Type
     */
    public static final String mKey_DeviceIDType = "DeviceIDType";

    /**
     * OpenGL mode: true or false
     */
    public static final String mKey_IsOpenGL = "IsOpenGL";

    public static final String mKey_Navi_Path = "Navi_Path";
    public static final String mKey_Navi2_Path = "Navi2_Path";
    // Temporary data
    public static final String mKey_Navi2_Datasource = "Navi2_Datasource";
    public static final String mKey_Navi2_Dataset = "Navi2_Dataset";
    public static final String mKey_Navi3_Datasource = "Navi3_Datasource";

    public static final String mKey_Locale_Language = "LocaleLanguage";

    public static final String mKey_FirstRun = "FirstRun";

    /**
     * Create and initialize default preferences
     * @param context application's context
     */
    public SharedPreferenceManager(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
            initModules();
        }
    }

    /**
     * Check whether preferences is initialized or not
     * @return true if SharedPreferenceManager was instanced once
     */
    public static boolean isInited(){
        return mSharedPreferences != null;
    }
    /**
     * Registers a callback to be invoked when a change happens to a preference.
     * @param listener The callback that will run
     */
    public static void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener){
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregisters a previous callback.
     * @param listener The callback should be unregistered
     */
    public static void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener){
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void finalize() throws Throwable {
        mSharedPreferences = null;
        super.finalize();
    }

    /**
     * Retrieve a list of string from the preference with the given key. Push them into  ArrayList, if the valueList parameter is not null, return itself.
     * @param key        The name of the preference to retrieve
     * @param valueList  The list to store strings
     * @return
     */
    public static ArrayList<String> getStrings(String key, ArrayList<String> valueList){
        ArrayList<String> mValueList = null;
        if(null == valueList){
            mValueList = new ArrayList<String>();
        }else {
            mValueList = valueList;
        }
        String strValues = mSharedPreferences.getString(key, "");
        if(!strValues.isEmpty()){
            String[] strArr = strValues.split(split);
            for (int i = 0; i < strArr.length; i++) {
                if(!strArr[i].contains("-") && key.equals(mKey_SN)){
                    removeString(key, strArr[i]);
                }else {
                    mValueList.add(strArr[i]);
                }
            }
        }

        return mValueList;
    }

    /**
     * Add a String value
     * @param key
     * @param value
     */
    public static void addString(String key, String value){
        if(null == value || value.isEmpty())
            return;

        String strValues = mSharedPreferences.getString(key, "");
        if(strValues.isEmpty()){
            strValues = value;
        }else if(strValues.contains(value)){
            return;   // if existed;
        }else {
            strValues += split + value;
        }

        updateString(key, strValues);

    }

    /**
     * Remove a String value
     * @param key
     * @param value
     */
    public static void removeString(String key, String value){
        if(null == value || value.isEmpty())
            return;

        String strValues = mSharedPreferences.getString(key, "");
        if (strValues.contains(value)) {  // if existed;
            if (strValues.startsWith(value)) {
                strValues = strValues.replace(value + split, "");
            } else {
                strValues = strValues.replace(split + value, "");
            }

            updateString(key, strValues);
        }
    }
/*************************************************************/
    /**
     * Initialize valid modules of SuperMap iMobile
     */
    private static void initModules(){
        String strModules = mSharedPreferences.getString(mKey_Modules, null);

        String strModulesValid = "Core_Dev" + split + "Core_Runtime" + split +
                "Navigation_Dev" + split + "Navigation_Runtime" +  split +
                "Plot_Dev" + split + "Plot_Runtime" +  split +
                "Realspace_Dev" +  split + "Realspace_Runtime" + split +
                "Indoor_Navigation_Dev" + split + "Indoor_Navigation_Runtime" + split +
                "Industry_Navigation_Dev" + split + "Industry_Navigation_Runtime" + split +
                "Realspace_Analyst_Dev" + split + "Realspace_Analyst_Runtime" + split +
                "Realspace_Effect_Dev" + split + "Realspace_Effect_Runtime" + split +
                "Plot3D_Dev" + split + "Plot3D_Runtime";

        if (null == strModules) {
            updateString(mKey_Modules, strModulesValid);
        } else if (!strModules.equals(strModulesValid)) {
            updateString(mKey_Modules, strModulesValid);
        }
    }

/*********************************************/
    /**
     * Update the vale of the preference
     * @param key   The name of the preference to modify
     * @param value The new value of the preference
     */
    private static void updateString(String key, String value) {
        Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        boolean isTrue = editor.commit();
        if (!isTrue) {
            Log.e(tag , "SharedPreference Commit Failed ! -- " + value);
        }
    }

    /**
     * Retrieve a string value from the preferences.
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or empty string
     */
    public static String getString(String key){
        String strValue = mSharedPreferences.getString(key, "");
        return strValue;
    }

    /**
     * Set a String value in the preference.
     * @param key   The name of the preference to modify.
     * @param value The new value of the preference.
     */
    public static void setString(String key, String value){
        updateString(key, value);
    }

    /**
     * Retrieve a boolean value from the preferences.
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or true.
     */
    public static boolean getBoolean(String key){
        boolean value = mSharedPreferences.getBoolean(key, true);
        return value;
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.
     *
     */
    public static boolean getBoolean(String key, boolean defValue){
        boolean value = mSharedPreferences.getBoolean(key, defValue);
        return value;
    }

    /**
     * Set a boolean value in the preference.
     * @param key   The name of the preference to modify.
     * @param value The new value of the preference.
     */
    public static void setBoolean (String key, boolean value){
        Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        boolean isTrue = editor.commit();
        if (!isTrue) {
            Log.e(tag , "SharedPreference Commit Failed ! -- " + value);
        }
    }



}
