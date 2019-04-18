package com.huikezk.alarmpro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.huikezk.alarmpro.MyApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by MaRufei
 * on 17/7/18.
 * Email: www.867814102@qq.com
 * Phone：132 1358 0912
 * Purpose: TODO 存储工具
 */

public class SaveUtils {

    public static String PREFERENCE_NAME = "huoge";
    static SharedPreferences settings = MyApplication.getInstance().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    static SharedPreferences.Editor editor = settings.edit();

    /**
     * 存储 String
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setString(String key, String value) {

        editor.putString(key, value);
        return editor.commit();
    }


    /**
     * 读取 String
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        return settings.getString(key, "");
    }

    /**
     * 存储 Int
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }


    /**
     * 读取 Int
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return settings.getInt(key, -1);

    }

    /**
     * 存储 Long
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setLong(String key, long value) {
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * 读取 Long
     *
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return settings.getLong(key, -1);
    }

    /**
     * 存储 Float
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setFloat(String key, float value) {
        editor.putFloat(key, value);
        return editor.commit();
    }


    /**
     * 读取 Float
     *
     * @param key
     * @return
     */
    public static float getFloat(String key) {
        return settings.getFloat(key, -1);
    }

    /**
     * 存储 Boolean
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 读取 Boolean
     *
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return settings.getBoolean(key, false);
    }

    /**
     * 保存Set集合
     * @param key
     * @param set
     * @return
     */
    public static boolean setSetData(String key, Set<String> set){
        editor.putStringSet(key,set);
        return editor.commit();
    }

    /**
     * 读取Set集合
     * @param key
     * @return
     */
    public static Set<String> getSetData(String key){
        return settings.getStringSet(key, new HashSet<String>());
    }

    /**
     * 删除某个键值对
     *
     * @param key
     */
    public static void removeData(String key) {
        editor.remove(key);
    }

    /**
     * 删除value是空的 键值对
     */
    public static void removeManyData() {
        Map<String, String> map = (Map<String, String>) settings.getAll();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String value = settings.getString(entry.getKey(), "");
            if (TextUtils.isEmpty(value)||"undefined".equals(value)||"null".equals(value)) {
                editor.remove(entry.getKey());
                editor.commit();
            }
        }
    }

    /**
     * 删除所有键值对
     */
    public static void removeAllData() {
        editor.clear();
        editor.commit();
    }

    /**
     * 获取所有以keyWord结尾的集合
     * @return
     */
    public static List<String> getAllEndWithKey(String keyWord) {
        List<String> list = new ArrayList<>();
        Map<String, String> map = (Map<String, String>) settings.getAll();
        for (Map.Entry<String, String> entry : map.entrySet()) {
//            Log.i("获取的key：" + entry.getKey(), "获取的value:" + settings.getString(entry.getKey(), ""));
            if (entry.getKey().endsWith(keyWord)) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    /**
     * 获取所有以keyWord结尾的集合
     * @return
     */
    public static List<String> getAllValueEndWithKey(String keyWord) {
        List<String> list = new ArrayList<>();
        Map<String, String> map = (Map<String, String>) settings.getAll();
        for (Map.Entry<String, String> entry : map.entrySet()) {
//            Log.i("获取的key：" + entry.getKey(), "获取的value:" + settings.getString(entry.getKey(), ""));
            if (entry.getKey().endsWith(keyWord)) {
                list.add(entry.getValue());
            }
        }
        return list;
    }



    /**
     * 获取所有存储的key
     * @return
     */
    public static List<String> getAllKeys(){
        List<String> list=new ArrayList<>();
        Map<String, String> map = (Map<String, String>) settings.getAll();
        for (Map.Entry<String, String> entry : map.entrySet()) {
//            MyUtils.Loge("AAA", "获取的key：" + entry.getKey() + "--获取的value:" + settings.getString(entry.getKey(), ""));
            list.add(entry.getKey());
        }
        return list;
    }
}
