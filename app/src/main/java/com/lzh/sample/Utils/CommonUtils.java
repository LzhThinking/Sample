package com.lzh.sample.Utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hetian on 2018/1/15.
 */

public class CommonUtils {

    private static final String PATTERN = "lumi-([a-z]+)-.+";
    // 创建 Pattern 对象
    private static final Pattern pattern = Pattern.compile(PATTERN);

    /**
     * 传入WIFI SSID,然后解析出设备名，如果不是设备WIFI AP，那么返回null，注意判断NPE
     *
     * @param ssid
     * @return 设备名，如果不是设备那么返回null
     */
    public static String parseDeviceThroughWifiSSID(String ssid) {
        String deviceName = null;
        if (TextUtils.isEmpty(ssid) || !ssid.startsWith("lumi-")) {
            return deviceName;
        }

        Matcher m = pattern.matcher(ssid);
        if (m.find()) {
            deviceName = m.group(1);
        }

        return deviceName;
    }


    public static String getPositionTypeById(String positionId) {
        if (TextUtils.isEmpty(positionId)) {
            return "";
        } else if (positionId.startsWith("real1")) {
            return "home";
        } else if (positionId.startsWith("real2")) {
            return "room";
        } else if (positionId.startsWith("BP")) {//收藏的位置
            return "BP";
        }
        return "";
    }

    public static String getStartTimeHintStr(String startTime) {
        if (!startTime.matches("\\d+ \\d+ *")) {
            return "";
        }
        String[] values = startTime.split(" ");
        return String.format(Locale.CHINESE, "%02d:%02d", Integer.parseInt(values[1]), Integer.parseInt(values[0]));
    }

    /**
     * 从字符串中解析出 触发器标识符
     *
     * @param triggerOrActionId
     * @return
     */
    public static String getCommonTriggerOrActionId(String triggerOrActionId) {
        if (!TextUtils.isEmpty(triggerOrActionId)) {
            int index = triggerOrActionId.lastIndexOf(".");
            if (index < 0) {
                return triggerOrActionId;
            }
            return triggerOrActionId.substring(index + 1);
        }
        return triggerOrActionId;
    }


    public static final String SYS_EMUI = "sys_emui";
    public static final String SYS_MIUI = "sys_miui";
    public static final String SYS_FLYME = "sys_flyme";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    /**
     * 获取系统标识（区分具体手机系统）
     *
     * @return
     */
    public static String getSystem() {
        String SYS = "";
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
                SYS = SYS_MIUI;//小米
            } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    || prop.getProperty(KEY_EMUI_VERSION, null) != null
                    || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                SYS = SYS_EMUI;//华为
            }
//            else if (getMeizuFlymeOSFlag().toLowerCase().contains("flyme")) {
//                SYS = SYS_FLYME;//魅族
//            }
            ;
        } catch (IOException e) {
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

//    public static String getMeizuFlymeOSFlag() {
//        return getSystemProperty("ro.build.display.id", "");
//    }
//
//    private static String getSystemProperty(String key, String defaultValue) {
//        try {
//            Class<?> clz = Class.forName("android.os.SystemProperties");
//            Method get = clz.getMethod("get", String.class, String.class);
//            return (String) get.invoke(clz, key, defaultValue);
//        } catch (Exception e) {
//        }
//        return defaultValue;
//    }

    public static boolean checkMiPushProcessIsAlive(Application applicationContext) {
        ActivityManager activityManager = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        String packageName = applicationContext.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null || appProcesses.size() == 0) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            String pushServiceName = String.format("%s:pushservice", packageName);
            if (appProcess.processName.equals(pushServiceName)) {
                return true;
            }
        }
        return false;
    }

    private volatile static long clickTime = 0;
    private volatile static long TIME_DURING_CLICK = 500;
    private volatile static Integer clickPageHashCode = null;

    /**
     * 500ms内连续点击判断为双击，忽略。
     * 目前只在大部分onClick方法里面添加了这个判断
     * 使用的时候要注意不要连续调用两次，比如ItemViewBinder里面的一个onClick里面调用了另一个onClick，
     * 如果两个onClick里面都做个这个判断，那么后面onClick里面的代码永远执行不了。
     *
     * @param page 传入要统一处理double click 的页面(或者任何能表示该页的对象)
     * @return
     */
    public synchronized static boolean isDoubleClick(Object page) {
        if (page == null) {
            return false;
        }
        long tempTime = System.currentTimeMillis();
        if (clickPageHashCode == null) {
            clickPageHashCode = page.hashCode();
            clickTime = tempTime;
            return false;
        }
        boolean isDouble = false;
        if (clickPageHashCode.equals(page.hashCode())) {
            isDouble = tempTime - clickTime <= TIME_DURING_CLICK;
            //not double click so refresh click time;
            if (!isDouble) {
                clickTime = tempTime;
            } else {
//                ALog.i("lalalalalala~~~~~~~ double click 🤔");
            }
        } else {
            clickTime = tempTime;
//            ALog.i("lalalalalala~~~~~~~ not double click---> hashcode change 🤔 >>>>> " + clickPageHashCode + " -->" + page.hashCode(), page.toString());
        }
        clickPageHashCode = page.hashCode();
        return isDouble;
    }

    /**
     * Manufacturer of mobile
     *
     * @return
     */
    public static String getMobileManufacturer() {
        String manufacturer = android.os.Build.MANUFACTURER;
        return manufacturer;
    }

    /**
     * Brand of mobile
     *
     * @return
     */
    public static String getMobileBrand() {
        String brand = android.os.Build.BRAND;
        return brand;
    }

    /**
     * Is OnePlus mobile
     *
     * @return
     */
    public static boolean isOnePlus() {
        if ("OnePlus".equalsIgnoreCase(getMobileManufacturer())
                && "OnePlus".equalsIgnoreCase(getMobileBrand())) {
            return true;
        }
        return false;
    }

    public static String formatTime(int time) {
        return time < 10 ? "0" + time : time + "";
    }


    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 格式化时间为：1980-08-31 12：23：34
     *
     * @param time 毫秒时间
     * @return
     */
    public static String formatTime2YYYYMMDD(long time) {
        return simpleDateFormat.format(time);
    }

    public static String getGMTTime() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        return sdf.format(cd.getTime());
    }

    /**
     * @return
     */
    public static String getCurrentTimezoneOffset() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        String offset = (offsetInMillis >= 0 ? "+" : "-") + Math.abs(offsetInMillis / 3600000);
        return offset;
    }


    public static final int UNIT_BIT = 1;
    public static final int UNIT_KB = 2;
    public static final int UNIT_MB = 3;
    public static final int UNIT_GB = 4;
    public static final int UNIT_TB = 5;

    /**
     * @param size
     * @param unit
     * @return
     */
    public static String calculateMemorySpace(float size, int unit) {
        switch (unit) {
            case UNIT_BIT:
                if (size > 1024) {
                    size = size / 1024f;
                    return calculateMemorySpace(size, UNIT_KB);
                }
                return CommonPanelUtil.get2DecimalNum(size) + "B";
            case UNIT_KB:
                if (size > 1024) {
                    size = size / 1024f;
                    return calculateMemorySpace(size, UNIT_MB);
                }
                return CommonPanelUtil.get2DecimalNum(size) + "K";
            case UNIT_MB:
                if (size > 1024) {
                    size = size / 1024f;
                    return calculateMemorySpace(size, UNIT_GB);
                }
                return CommonPanelUtil.get2DecimalNum(size) + "M";
            case UNIT_GB:
                if (size > 1024) {
                    size = size / 1024f;
                    return calculateMemorySpace(size, UNIT_TB);
                }
                return CommonPanelUtil.get2DecimalNum(size) + "G";
            case UNIT_TB:

                return CommonPanelUtil.get2DecimalNum(size) + "T";
        }
        return size + "";
    }

    /**
     * 渲染图标颜色，不影响全局图标颜色
     *
     * @param imageView
     * @param color
     */
    public static void renderImageColor(ImageView imageView, int color) {
        Drawable drawable = imageView.getDrawable().mutate();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        imageView.setImageDrawable(drawable);
    }

    /**
     * 比较两个 hh:mm格式的时间的先后
     *
     * @param hourBefor
     * @param hourAfter
     * @return
     */
    public static int calculateMinuteDis(String hourBefor, String hourAfter) {
        String[] before = hourBefor.split(":");
        String[] after = hourAfter.split(":");
        try {
            after[0] = extractDigitFromStr(after[0]);
            return (Integer.valueOf(after[0]) * 60 + Integer.valueOf(after[1])) - (Integer.valueOf(extractDigitFromStr(before[0])) * 60 + Integer.valueOf(before[1]));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 提取字符串中的数字
     *
     * @param target
     * @return
     */
    public static String extractDigitFromStr(String target) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(target);
        return m.replaceAll("").trim();
    }

    /**
     * 显示wifi选择页面
     */
    public static void showWifiPickPage(Context context) {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        context.startActivity(wifiSettingsIntent);
    }

    public static boolean isStrAllDigits(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }

        return target.trim().matches("^[0-9]*$");
    }
}
