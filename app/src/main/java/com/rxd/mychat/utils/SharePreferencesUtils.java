package com.rxd.mychat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rxd.mychat.app.MyChatApp;

/**
 * Created by Administrator on 2017/6/6.
 */

public class SharePreferencesUtils {

    private static SharedPreferences sp;
    private static Context context = MyChatApp.getAppContext();

    private static void init(Context context){
        if (sp == null){
            sp = PreferenceManager.getDefaultSharedPreferences(MyChatApp.getAppContext());
        }
    }

    public static void setShareBooleanData(String key,boolean value){
        if (sp == null){
            init(context);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    public static Boolean getShareBooleanDataFalse(String key){
        if (sp == null){
            init(context);
        }
        return sp.getBoolean(key,false);
    }

    public static Boolean getShareBooleanDataTrue(String key){
        if (sp == null){
            init(context);
        }
        return sp.getBoolean(key,true);
    }

    public static void setShareStringData(String key,String value){
        if (sp == null){
            init(context);
        }
        sp.edit().putString(key,value).commit();
    }

    public static String getShareStringData(String key){
        if (sp == null){
            init(context);
        }
        return sp.getString(key,"");
    }

    public static void setShareLongData(String key,long value){
        if (sp == null){
            init(context);
        }
        sp.edit().putLong(key,value).commit();
    }

    public static long getShareLongData(String key){
        if (sp == null){
            init(context);
        }
        return sp.getLong(key,01);
    }

}