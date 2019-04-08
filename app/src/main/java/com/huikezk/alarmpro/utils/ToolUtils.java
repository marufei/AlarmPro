package com.huikezk.alarmpro.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


import com.huikezk.alarmpro.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MaRufei
 * on 16/6/23.
 * Email: www.867814102@qq.com
 * Phone：13213580912
 * Purpose: 工具类
 */

public class ToolUtils {
    private static final String TAG = "ToolUtils";

    public static Context context() {
        return MyApplication.getInstance();
    }

    /**
     * Json 转成 Map<>
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, String> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            String value;
            Map<String, String> valueMap = new HashMap<String, String>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key).toString();
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Map 转 Json
     *
     * @param paramMap
     * @return
     */
    public static JSONObject map2Json(Map<String, String> paramMap) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (paramMap != null && !paramMap.isEmpty()) {
                Set<Map.Entry<String, String>> paramSet = paramMap.entrySet();
                for (Map.Entry<String, String> entry : paramSet) {
                    String key = entry.getKey();
//                    if (key.equals("sign")) {
//                        continue;
//                    }
                    String value = entry.getValue();
                    jsonObject.put(key, value);
                }
            }
        } catch (JSONException e) {
//            LogUtil.e(TAG, e.getMessage());
        }

        return jsonObject;
    }

//    /**
//     * Map转Json格式的String
//     *
//     * @param map 要转的Map
//     * @return 转出String
//     */
//    public static String Map2JsonString(Map map) {
//        return com.alibaba.fastjson.JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
//    }

    /**
     * map转String
     *
     * @param paramMap
     * @return
     */
    public static String map2String(Map<String, String> paramMap, String secretKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(secretKey);
        if (paramMap != null && !paramMap.isEmpty()) {
            Set<Map.Entry<String, String>> paramSet = paramMap.entrySet();
            for (Map.Entry<String, String> entry : paramSet) {
                String key = entry.getKey();

                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
//                if (value != null && !"".equals(value)) {
                sb.append(key);
//                    sb.append(join);
                sb.append(value);
//                }
            }
        }
//        LogUtil.e(TAG, "签名字符串拼装结果为:" + sb.toString());
        return sb.toString();
    }

//    //检查网络是否正常 [功能描述].
//    public static boolean checkNetwork() {
//        boolean flag1 = false;
//        ConnectivityManager cwjManager = (ConnectivityManager) context().getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (cwjManager.getActiveNetworkInfo() != null) {
//            flag1 = cwjManager.getActiveNetworkInfo().isAvailable();
//        }
//        return flag1;
//    }

    /**
     * 判断手机号
     *
     * @param mobiles 手机号
     * @return 是为true
     */
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^1[34578]\\d(?!(\\d)\\\\1{7})\\d{8}?");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean isCardID(String cardId) {
        if (TextUtils.isEmpty(cardId)) {
            return true;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 验证是否包含 字母和数字  并验证8-16位
     *
     * @param str 传入的值
     * @return 符合为true
     */
    public static boolean isNormal(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (str.trim().length() >= 8 && str.trim().length() <= 16) {
            String regexNum = ".*[0-9]+.*";
            Pattern pattern = Pattern.compile(regexNum);
            Matcher isNum = pattern.matcher(str);
            String regexEn = ".*[a-zA-Z]+.*";
            Pattern patternEn = Pattern.compile(regexEn);
            Matcher isEnglish = patternEn.matcher(str);
            if (isNum.matches() && isEnglish.matches()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }


    /**
     * 验证身份证号
     *
     * @param str
     * @return
     */
    public static boolean IsIDcard(String str) {
        String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        return match(regex, str);
    }

    /**
     * 数据正则对比
     *
     * @param regex 正则
     * @param str   数据
     * @return
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @param dp 要转换的 dp
     * @return 转出 px
     */
    public static int dp2px(float dp) {
        // 拿到屏幕密度
        float density = context().getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);// 四舍五入
        return px;
    }


    /**
     * @param px 要转换的px
     * @return 转出 dp
     */
    public static float px2dp(int px) {
        float density = context().getResources().getDisplayMetrics().density;
        float dp = px * density;
        return dp;

    }

    /**
     * 版本名
     *
     * @return
     */
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }


    /**
     * 版本号
     *
     * @return
     */
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo pi = null;

        try {
            PackageManager pm = context().getPackageManager();
            pi = pm.getPackageInfo(context().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtil.e(TAG, e.getMessage());
        }

        return pi;
    }

    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param date       String 想要格式化的日期
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public static String StringPattern(String date, String newPattern) {
        if (date == null || newPattern == null) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);        // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
//            LogUtil.e(TAG, e.getMessage());
        }
        return sdf2.format(d);
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight() {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context().getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtil.e(TAG, e.getMessage());
        }
        return statusHeight;
    }


    /**
     * 判断 程序是否存在
     *
     * @param packageName
     * @return
     */

    public static boolean checkApplication(String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
//
//    /**
//     * DES 加密
//     *
//     * @param map
//     * @return
//     */
//    public static String DES(Map<String, String> map) {
//        try {
//            return URLEncoder.encode(DESedeTool.encryptDES(Map2JsonString(map), MDApp.HTTPKEY).replaceAll(" ", ""), "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//    /**
//     * 复制到系统粘贴板
//     */
//    public static void copyText(Context context,String string){
//        if(!TextUtils.isEmpty(string)) {
//            //获取剪贴板管理器：
//            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            // 创建普通字符型ClipData
//            ClipData mClipData = ClipData.newPlainText("Label", string);
//            // 将ClipData内容放到系统剪贴板里。
//            cm.setPrimaryClip(mClipData);
//            TabToast.makeText("已复制");
//        }else {
//            TabToast.makeText("复制内容为空");
//        }
//    }

    /**
     * 判断某个APP是否存在
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 高德地图坐标转百度地图坐标
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 百度地图坐标转高德地图坐标
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }


}
