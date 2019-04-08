package com.huikezk.alarmpro.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MotionEventCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.huikezk.alarmpro.MyApplication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyUtils {
    private static String TAG = "TAG--MyUtils";
    public static Toast mToast;
    private static Pattern mPattern;
    private static Matcher mMatcher;
    public static boolean logStatus = true;

    public static void Loge(String TAG, String msg) {
        try {
            if (logStatus) {
//                Log.e(TAG + "-ttrm", msg);
                //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
                //  把4*1024的MAX字节打印长度改为2001字符数
                int max_str_length = 2001 - TAG.length();
                //大于4000时
                while (msg.length() > max_str_length) {
                    Log.e(TAG, msg.substring(0, max_str_length));
                    msg = msg.substring(max_str_length);
                }
                //剩余部分
                Log.e(TAG, msg);
            }
        } catch (Exception e) {
            Log.e(TAG + "e=", e.toString());
        }

    }

    public static void Logi(String TAG, String msg) {
        try {
            if (logStatus) {
                Log.i(TAG, msg);
            }
        } catch (Exception e) {
            Log.e(TAG + "e=", e.toString());
        }

    }

    /***
     * 单例Toast
     */
    public static void showToast(Context mContext, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        if (TextUtils.equals(msg, "连接服务器失败，请稍后再试")) {
            mToast.setText("服务器繁忙，请稍后再试");
        } else {
            mToast.setText(msg);
        }
        mToast.show();

    }

    /**
     * QQ等级计算方法
     *
     * @param level
     * @return 皇冠数量=hgs；太阳数量=tys；月亮数量=yls；星星数量=xxs；
     */
    public static int[] QQLevel(int level) {
        int[] mlevel = null;
        int hgs = 0;
        int tys = 0;
        int yls = 0;
        int xxs = 0;
        if (level < 4) {
            Log.e(TAG, level + "个星星");
        }
        if (level > 3 && level < 16) {
            yls = level / 4;
            xxs = level % 4;
            Loge(TAG, yls + "个月亮" + xxs + "星星");
        }
        if (level > 15 && level < 64) {
            tys = level / 16;
            yls = (level - tys * 16) / 4;
            xxs = level - tys * 16 - yls * 4;
            Loge(TAG, tys + "个太阳" + yls + "个月亮" + xxs + "星星");
        }
        if (level > 63) {
            hgs = level / 64;
            tys = (level - hgs * 64) / 16;
            yls = (level - hgs * 64 - tys * 16) / 4;
            xxs = level - hgs * 64 - tys * 16 - yls * 4;
            Loge(TAG, hgs + "个皇冠" + tys + "个太阳" + yls + "个月亮" + xxs + "星星");
        }

        return mlevel = new int[]{hgs, tys, yls, xxs};
    }

    /**
     * MD5加密(亲测可用)
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * string转int
     *
     * @param str
     * @return
     */
    public static int Str2Int(String str) {
//        int i = (int)Double.parseDouble(str);
        //应该先判断 字符串是不是空
        return Integer.valueOf(str).intValue();
    }

    public static int float2Int(float f) {
        int i = (int) f;
        return i;
    }

    /**
     * string转double
     *
     * @param str
     * @return
     */
    public static double Str2Double(String str) {
        double d = Double.parseDouble(str);
        return d;
    }

    /**
     * Double转float
     *
     * @return
     */
    public static float Double2Float(Double d) {
        float f = d.floatValue();
        return f;
    }

    /**
     * double保留两位数字
     *
     * @return
     */
    public static double Double2(Double d) {
        if (d != null) {
            DecimalFormat df = new DecimalFormat("0.00");
            return Str2Double(df.format(d));
        }
        return d;
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * dip 转换成 px
     *
     * @param dip
     * @param context
     * @return
     */
    public static float dip2Dimension(float dip, Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                displayMetrics);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dp) {
        // 拿到屏幕密度
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);// 四舍五入
        return px;
    }

    /**
     * px转dp
     */
    public static float px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        float dp = px * density;
        return dp;

    }

    /**
     * @param dip
     * @param context
     * @param complexUnit {@link TypedValue#COMPLEX_UNIT_DIP}
     *                    {@link TypedValue#COMPLEX_UNIT_SP}
     * @return
     */
    public static float toDimension(float dip, Context context, int complexUnit) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        return TypedValue.applyDimension(complexUnit, dip, displayMetrics);
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }

    /**
     * 获取状态栏高度
     *
     * @param v
     * @return
     */
    public static int getStatusBarHeight(View v) {
        if (v == null) {
            return 0;
        }
        Rect frame = new Rect();
        v.getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static String getActionName(MotionEvent event) {
        String action = "unknow";
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                break;
            case MotionEvent.ACTION_OUTSIDE:
                action = "ACTION_SCROLL";
                break;
            default:
                break;
        }
        return action;
    }

    /**
     * 截取2016-04-05T16:30:00   T之前的字符串
     */
    public static String InterceptStr(String str) {
        if (str != null) {
            String d = str.substring(0, str.lastIndexOf("T"));
            return d;
        }
        return str;
    }

    /**
     * 截取410105 为4101
     */
    public static String Intercept_4_Str(String str) {
        if (str != null) {
            String d = str.substring(0, 4);
            return d;
        }
        return str;
    }

    /**
     * 截取2016-04-05 16:30:00   为04-05 16：30
     */
    public static String Intercept_MD_HM(String str) {
        if (str != null) {
            String d = str.substring(5, str.length());
            String c = d.substring(0, 11);
            return c;
        }
        return str;
    }

    /**
     * 截取2016-04-05 16:30:00   为2016-04-05 16:30
     */
    public static String Intercept_YMDHM(String str) {
        if (str != null) {
            String d = str.substring(0, str.length() - 3);
            return d;
        }
        return str;
    }

    /**
     * 截取123.45   为123
     */
    public static String Intercept_Int_Point(String str) {
        if (str != null) {
            String d = str.substring(0, str.lastIndexOf("."));
            return d;
        }
        return str;
    }

    /**
     * 截取2016-04-05 16:30:00   为2016-04-05
     */
    public static String Intercept_YMD(String str) {
        if (str != null) {
            String d = str.substring(0, str.lastIndexOf(" "));
            return d;
        }
        return str;
    }

    /**
     * 截取2016-04-05 16:30:00   为16:30:00
     */
    public static String Intercept_HMS(String str) {
        if (str != null) {
            String d = str.substring(str.lastIndexOf(" "), str.length());
            return d;
        }
        return str;
    }

    /**
     * 截取com.zys.jym.lanhu.activity.LoginActivity@1f188df  为com.zys.jym.lanhu.activity.LoginActivity
     */
    public static String Intercept_cls(String str) {
        if (str != null) {
            String d = str.substring(0, str.lastIndexOf("@"));
            return d;
        }
        return str;
    }

    /**
     * 截取2016-04-05   取05
     */
    public static float InterceptStr2float(String str) {
        float b = 0;
        if (str != null) {
            String d = str.substring(str.lastIndexOf("-"), str.length());
            b = -MyUtils.Str2Int(d);
            return b;
        }
        return b;
    }

    /**
     * 替换字符"T"  2016-04-05T16:30:00为 2016-04-05 16:30:00
     */
    public static String Replace_T_Str(String s) {
        if (s != null) {
            String s1 = s.replaceFirst("T", " ");
            return s1;
        }
        return s;
    }

    /**
     * 替换字符"T"  2016-04-05T16:30:00为 2016-04-05 16:30:00
     */
    public static String Replace_hh_Str(String s) {
        try {
            if (s != null) {
                String s1 = s.replace("\\n", "\n");
                return s1;
            }
        } catch (Exception e) {
            Loge(TAG, e.toString());
            return s;
        }
        return s;
    }

    /**
     * 替换  18637752014为 186****9014
     */
    public static String Replace_phone_Str(String s) {
        if (s != null) {
            String s1 = s.substring(0, 3) + "****" + s.substring(7, s.length());
            return s1;
        }
        return s;
    }

    /**
     * 替换  215766856@qq.com为 2******@qq.com
     */
    public static String Replace_email_Str(String s) {
        if (s != null) {
            String a[] = s.split("@");
            String s1 = a[0].substring(0, 1) + "******@";
            String s2 = s1 + a[1];
            return s2;
        }
        return s;
    }

    /**
     * 替换 41215119931212515x为 ***********515x
     */
    public static String Replace_idCard_Str(String s) {
        if (s != null && s.length() > 4) {
            String s1 = "***********" + s.substring(s.length() - 4, s.length());
            return s1;
        }
        return s;
    }

    /**
     * 限制输入框 小数点后位数为两位
     */
    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

    }

    /**
     * 验证邮箱 必须带@，并且@后必须带域名，如.com
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        mPattern = Pattern.compile(str);
        mMatcher = mPattern.matcher(email);
        return mMatcher.matches();
    }

    /**
     * 验证输入身份证号
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isIDcard(String idcard) {
        Loge(TAG, "idcard.length()=" + idcard.length());
        if (idcard.length() == 15) {
            //15位检验
            String str = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
            mPattern = Pattern.compile(str);
            mMatcher = mPattern.matcher(idcard);
            return mMatcher.matches();
        }
        if (idcard.length() == 18) {
            //18位检验
            String str = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$";
            mPattern = Pattern.compile(str);
            mMatcher = mPattern.matcher(idcard);
            return mMatcher.matches();
        }
        return false;
    }

    /**
     * 验证手机号 11位手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhoneNumber(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^1[345789]\\d(?!(\\d)\\\\1{7})\\d{8}?");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证密码 6-16位数字或英文字符（不含全角字符），区分大小写，不能带空格，不能带中文字符
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        mPattern = Pattern.compile("^\\s*[^\\s\u4e00-\u9fa5]{6,16}\\s*$");
        mMatcher = mPattern.matcher(password);
        return mMatcher.matches();
    }

    /**
     * 验证推荐人账号6位数字
     *
     * @param RefereeCode
     * @return
     */
    public static boolean isRefereeCode(String RefereeCode) {
        mPattern = Pattern.compile("^[0-9]{6}$");
        mMatcher = mPattern.matcher(RefereeCode);
        return mMatcher.matches();
    }

    /**
     * 验证是否带有@符号
     *
     * @param username
     * @return
     */
    public static boolean isEmailType(String username) {
        mPattern = Pattern.compile("^.*@.*$");
        mMatcher = mPattern.matcher(username);
        return mMatcher.matches();
    }

    /**
     * 验证昵称 4-16位中英文、数字、下划线(开头结尾不能是下划线)
     *
     * @param nickname
     * @return
     */
    public static boolean isNickName(String nickname) {
        mPattern = Pattern
                .compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{2,16}+$");
        mMatcher = mPattern.matcher(nickname);
        return mMatcher.matches();
    }

    public static Bitmap drawable2bitmap(Context context, int pic) {
        Resources res = context.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, pic);
        return bmp;
    }

    public static void copyText(Context context, CharSequence textCopy) {
        if (android.os.Build.VERSION.SDK_INT > 11) {
            android.content.ClipboardManager c = (android.content.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            c.setText(textCopy);
        } else {
            android.text.ClipboardManager c = (android.text.ClipboardManager) context
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            c.setText(textCopy);
        }
    }

    /**
     * Dialog
     *
     * @param context
     * @param iconId
     * @param title
     * @param message
     * @param positiveBtnName
     * @param negativeBtnName
     * @param positiveBtnListener
     * @param negativeBtnListener
     * @return
     */
    public static Dialog createConfirmDialog(
            Context context,
            int iconId,
            String title,
            String message,
            String positiveBtnName,
            String negativeBtnName,
            android.content.DialogInterface.OnClickListener positiveBtnListener,
            android.content.DialogInterface.OnClickListener negativeBtnListener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(iconId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnName, positiveBtnListener);
        builder.setNegativeButton(negativeBtnName, negativeBtnListener);
        dialog = builder.create();
        return dialog;
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static BigDecimal mul1(double v1, double v2, int scale) {
        BigDecimal value = new BigDecimal(v1 * v2);
        value = value.setScale(scale, BigDecimal.ROUND_HALF_UP);
        System.out.println(value);
        return value;
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static BigDecimal div1(double v1, double v2, int scale) {

        BigDecimal value = new BigDecimal(v1 / v2);
        value = value.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return value;
    }

    public static String getTimestamp() {
        String timestamp = String.valueOf(new Date().getTime() / 1000);
        return timestamp;
    }

    public static String getSign() {
        String sign = "822374hehrwe7" + getTimestamp();
        sign = md5(sign);
        return sign;
    }

    /**
     * 版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }


    /**
     * 版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
            MyUtils.Loge(TAG, e.getMessage());
        }

        return pi;
    }

    /**
     * 将网络图片转换成bitmap
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static Bitmap getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        return BitmapFactory.decodeStream(is);
    }


    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
//
//    public static Bitmap GetLocalOrNetBitmap(String url) {
//        Bitmap bitmap = null;
//        InputStream in = null;
//        BufferedOutputStream out = null;
//        try {
//            in = new BufferedInputStream(new URL(url).openStream(), KeyUtils.IO_BUFFER_SIZE);
//            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//            out = new BufferedOutputStream(dataStream, KeyUtils.IO_BUFFER_SIZE);
////            copy(in, out);out
//            out.flush();
//            byte[] data = dataStream.toByteArray();
//            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//            data = null;
//            return bitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;
        MyUtils.Loge(TAG, "returnBitmap---1");
        try {
            fileUrl = new URL(url);
            MyUtils.Loge(TAG, "returnBitmap---2");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            MyUtils.Loge(TAG, "returnBitmap---3");
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            MyUtils.Loge(TAG, "returnBitmap---4");
            conn.setDoInput(true);
            MyUtils.Loge(TAG, "returnBitmap---5");
            conn.connect();
            MyUtils.Loge(TAG, "returnBitmap---6");
            if (conn != null) {
                MyUtils.Loge(TAG, "returnBitmap---7");
                InputStream is = conn.getInputStream();
                MyUtils.Loge(TAG, "returnBitmap---8");
                bitmap = BitmapFactory.decodeStream(is);
                MyUtils.Loge(TAG, "returnBitmap---9");
                is.close();
                MyUtils.Loge(TAG, "returnBitmap---10");
            }
        } catch (IOException e) {
            e.printStackTrace();
            MyUtils.Loge(TAG, "returnBitmap---11");
        }
        return bitmap;

    }

    /**
     * 将两张图片合成一张图片
     *
     * @param firstBitmap
     * @param secondBitmap
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(), firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, (float) (firstBitmap.getWidth() * 0.05), (float) (firstBitmap.getHeight() * 0.8), null);
        return bitmap;
    }


    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */

    private static int dipToPx(Context context, float dip) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 判断qq是否可用
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * view转换成bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * 判断某个APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */

    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean checkFacebookExist(Context context, String facebookPkgName) {
        return checkApkExist(context, facebookPkgName);
    }

    //根据map的value获取map的key
    public static String getKey(Map<String, String> map, String value) {
        String key = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                key = entry.getKey();
            }
        }
        return key;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t * 1000);
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * 隐藏软键盘
     * view可以当前布局中已经存在的任何一个View，如果找不到可以用getWindow().getDecorView()。
     *
     * @param context
     */
    public static void hindSoftkey(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)

        {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     * view必须是VISIBLE的EditText，如果不是VISIBLE的，需要先将其设置为VISIBLE。
     *
     * @param context
     */
    public static void showSoftkey(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    /**
     * 获取APP的sha1
     *
     * @param context
     * @return
     */
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间戳转换为 字符串
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        MyUtils.Loge(TAG, "前7天==" + dft.format(endDate));
        return dft.format(endDate);
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                int num = (int) ((Math.random() * 9 + 1) * 10000000);
                return String.valueOf(num);
            }
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                int num = (int) ((Math.random() * 9 + 1) * 10000000);
                return String.valueOf(num);
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            int num = (int) ((Math.random() * 9 + 1) * 10000000);
            return String.valueOf(num);
        }

//        String imei = null;
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
//        try {
//            imei = tm.getDeviceId();
//            if (imei == null) {// android pad
//                imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);// 平板电脑 Pad获取的imei
//            }
//            SaveUtils.setString(KeyUtils.IMEI,imei);
//        } catch (Exception e) {
//            //当targetSdkVersion >22 也就是Android 5.1以上的版本时
//            imei = SaveUtils.getString(KeyUtils.IMEI);
//            if ("".equals(imei)) {
//                UUID uuid = UUID.randomUUID();
//                SaveUtils.setString(KeyUtils.IMEI,uuid.toString());
//
//            }
//
//        }
//        return imei;


    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        String macAddress = null;
        WifiManager wifiManager =
                (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiManager ? null : wifiManager.getConnectionInfo());

        if (!wifiManager.isWifiEnabled()) {
            //必须先打开，才能获取到MAC地址
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
        }
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取安卓唯一码
     *
     * @param context
     * @return
     */
    public static String getAndoridId(Context context) {
        String androidId = null;
        if (!TextUtils.isEmpty(getPCID(context))) {
            androidId = getMyMac(context);
        } else if (!TextUtils.isEmpty(getMyMac(context))) {
            androidId = getMyMac(context);
        } else {
            int num = (int) ((Math.random() * 9 + 1) * 1000000000);
            androidId = String.valueOf(num);
        }
        return androidId;
    }

    // 获取设备mac地址
    public static String getMyMac(Context context) {
        String info = null;

        //试用于4.0至7.0系统
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
//                Log.d("第二种方法的值", res1.toString().replace(":", "-"));
                return res1.toString().replace(":", "-");
            }
        } catch (Exception ex) {
            return null;
        }

        return info;
    }

    /**
     * 获取手机设备的唯一ID。 由Android系统唯一ID(16位十六进制数值)拼接MAC地址(12位十六进制数值)组成的十六个字节的PCID
     *
     * @param context Context对象，用于获取Android系统唯一ID
     * @return 手机唯一标识PCID
     */
    public static String getPCID(Context context) {
        String PCID = null;
        if (Build.VERSION.SDK_INT < 24) {
            // 获取系统唯一ID 弊端：主流厂商有可能会一致，且2.2系统不稳定
            PCID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        }

        return PCID;
    }

}
