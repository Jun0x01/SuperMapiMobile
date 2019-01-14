package com.supermap.imobile.ActivityMain;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;

import java.util.Locale;

/**
 * Created by Jun on 2018/6/29.
 */

public class LanguageUtil {

    public static Context changeLanguage(Context context){
        String language = SharedPreferenceManager.getString(SharedPreferenceManager.mKey_Locale_Language);
        Locale locale = null;
        if (language != null && !language.isEmpty()){
            if (0 == language.compareToIgnoreCase("English")) {
                locale = Locale.ENGLISH;
            } else if(0 == language.compareToIgnoreCase("Chinese")) {
                locale = Locale.CHINESE;
            }
        }
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        if(locale != null) {
            if(Build.VERSION.SDK_INT < 17){
                config.locale = locale;
                resources.updateConfiguration(config, resources.getDisplayMetrics());
                return  context;
            } else if (Build.VERSION.SDK_INT < 25) {
                config.setLocale(locale);
//                context.createConfigurationContext(config);
                resources.updateConfiguration(config, resources.getDisplayMetrics());
                return context;
            } else if (Build.VERSION.SDK_INT >= 25){
                config.setLocale(locale);
                return context.createConfigurationContext(config);
            }
            return context;
        }else {
            return  context;
        }
    }
}
