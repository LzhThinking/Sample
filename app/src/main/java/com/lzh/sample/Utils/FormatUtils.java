package com.lzh.sample.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.lzh.sample.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cdy on 2018/1/23.
 */
public class FormatUtils {

    public static final int CURRENT = 0;
    public static final int DAY = 1;
    public static final int MONTH = 2;

    public static final String STR_DAY = "day";
    public static final String STR_MONTH = "month";
    public static final int INPUT_PWD_NUM_MIN = 6;//遗留问题。密码可能会有6位
    public static final int PWD_NUM_MIN = 8;
    public static final int PWD_NUM_MAX = 16;
    /**
     * 中国电话号码
     */
    private static Pattern mChiNumPattern = Pattern.compile("^1(3|4|5|6|7|8|9)[0-9]\\d{8}$");
    /**
     * 邮箱
     */
    private static Pattern mEmailPattern = Pattern.compile("^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$");
    /**
     * 纯数字（暂时匹配外国号码）
     */
    private static Pattern mNumPattern = Pattern.compile("[0-9]+");
    /**
     * 是否是数值，包括小数
     */
    private static Pattern mNumValuePattern = Pattern.compile("^[+-]?\\d+(\\.\\d+)?$");
    /**
     * 密码
     */
    private static Pattern mPwdFormatPattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)(?![_\\W]+$)[a-zA-Z0-9_\\W]{" + PWD_NUM_MIN + "," + PWD_NUM_MAX + "}$");
    /**
     * 昵称。格式要求：开头字母或汉字；不能包含除"_-"之外的特殊字符 <废弃>
     */
    private static Pattern mNickNameFormatPattern = Pattern.compile("^[\\-\\w\\u4e00-\\u9fa5]+$");
    private static Pattern mNickNameStartFormatPattern = Pattern.compile("^[A-Za-z\\u4e00-\\u9fa5][\\s\\S]*");
    /**
     * 家或房间、设备、service。格式要求：中文字符、英文字符、数字、空格、中间杠符,且末尾只能是字母、数字和中文 <废弃>
     */
    private static Pattern mPosDevServiceNameFormatPattern = Pattern.compile("^[0-9A-Za-z\\u4e00-\\u9fa5\\- ]+$");
    private static Pattern mPosDevServiceNameEndFormatPattern = Pattern.compile("^[\\s\\S]*[0-9A-Za-z\\u4e00-\\u9fa5]$");


    /**
     * 全局匹配,中日韩英都支持
     */
    private static Pattern mNamePattern = Pattern.compile("^[0-9A-Za-z\\u2E80-\\u2FDF\\u3040-\\u318F\\u31A0-\\u31BF\\u31F0-\\u31FF\\u3400-\\u4DB5\\u4E00-\\u9FA5\\uA960-\\uA97F\\uAC00-\\uD7FF- ]+$");
    private static Pattern mNameEndPattern = Pattern.compile("^[\\s\\S]*[^- ]$");

    private static long OND_DAY_TIME = 86400000L;

    private static Pattern chinesePattern = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Matcher m = chinesePattern.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    public static boolean isChiMobileNO(String mobiles) {
        if (mobiles == null) {
            return false;
        }
        Matcher m = mChiNumPattern.matcher(mobiles);
        return m.matches();
    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher m = mEmailPattern.matcher(email);
        return m.matches();
    }

    public static boolean isInteger(String str) {
        //也会判断""为false
        if (str == null || str.isEmpty()) {
            return false;
        }
        Matcher isNum = mNumPattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        Pattern pattern = Pattern.compile("[1-9][0-9]*");
        if (pattern.matcher(str).matches() && Integer.valueOf(str) > Integer.MIN_VALUE && Integer.valueOf(str) < Integer.MAX_VALUE) {
            return true;
        }
        return false;
    }

    public static boolean isElevenDigitNumber(String str) {
        //也会判断""为false
        if (str == null || str.length() != 11) {
            return false;
        }
        Matcher isNum = mNumPattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否为数字,包括判断正负数小数
     *
     * @param str
     * @return
     */
    public static boolean isNumericPro(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isNumericalValue(String str) {
        //也会判断""为false
        if (str == null || str.isEmpty()) {
            return false;
        }
        Matcher isNum = mNumValuePattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isPasswordFormatValid(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Matcher isNum = mPwdFormatPattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isNickNameFormatValid(String str) {
        //昵称格式不做要求
        if(str == null){
            return false;
        }
        Matcher isValid = mNamePattern.matcher(str);
        Matcher isEndValid = mNameEndPattern.matcher(str);
        if (!isValid.matches() || !isEndValid.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isNickNameStartFormatValid(String str) {
        if (str == null) {
            return false;
        }
        //昵称格式不做要求
//        Matcher isNum = mNickNameStartFormatPattern.matcher(str);
//        if (!isNum.matches()) {
//            return false;
//        }
        return true;
    }

    public static boolean containsEmoji(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char codePoint = str.charAt(i);
            if ((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                    || (codePoint == 0xD)
                    || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                    || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                    || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF))) {
                continue;
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isPosDevServiceFormatValid(String str) {
        if (str == null) {
            return false;
        }
//        Matcher isNum = mPosDevServiceNameFormatPattern.matcher(str);
//        Matcher isEndValid = mPosDevServiceNameEndFormatPattern.matcher(str);
        Matcher isValid = mNamePattern.matcher(str);
        Matcher isEndValid = mNameEndPattern.matcher(str);
        if (!isValid.matches() || !isEndValid.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[1-9][0-9]*");
        return pattern.matcher(str).matches();
    }

    public static Calendar tempCalendar;
    public static final String YYYY_MM_DD_HH_MM = "yyyy/MM/dd HH:mm";
    public static final String HH_MM = "HH:mm";
    public static final String hh_MM = "hh:mm";
    public static final String MM_SS = "mm:ss";
    public static final String MM_DD_HH_MM = "MM/dd HH:mm";
    public static final String MD_DD_HH_MM_BAR = "MM-dd HH:mm";
    public static final String YYYY_MM_DD_MIDDLE_LINE = "yyyy-MM-dd";
    public static final String MM_DD = "MM-dd";
    public static final String MM1DD = "MM/dd";
    public static final String YY1MM1DD = "yyyy/MM/dd";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    /**
     * 转换时间戳为特有格式的String
     * <p>
     * 格式：
     * 若今天： 时分
     * 若昨天：昨天 + 时分
     * 若前几天： 日期 + 时分
     * 若非今年： 年份 + 时分
     *
     * @param time 毫秒时间
     * @return
     */
    public static String timeFormat(Context context, long time) {
        if (time == 0) {
            return "";
        }
        Calendar nowCalendar = Calendar.getInstance();
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }

        int day = nowCalendar.get(Calendar.DAY_OF_YEAR);
        int year = nowCalendar.get(Calendar.YEAR);

        tempCalendar.setTimeInMillis(time);
        int tDay = tempCalendar.get(Calendar.DAY_OF_YEAR);
        int tYear = tempCalendar.get(Calendar.YEAR);
        String timeStr;

        if (tYear != year) {//非今年
            simpleDateFormat.applyPattern(YYYY_MM_DD_HH_MM);
            timeStr = simpleDateFormat.format(tempCalendar.getTime());
        } else if (tDay == day) {//今天
            simpleDateFormat.applyPattern(HH_MM);
            timeStr = simpleDateFormat.format(tempCalendar.getTime());
        } else if (day - tDay == 1) {//昨天
            timeStr = String.format("%s %02d:%02d", context.getResources().getString(R.string.yesterday), tempCalendar.get(Calendar.HOUR_OF_DAY), tempCalendar.get(Calendar.MINUTE));
        } else {//今年几天前
            simpleDateFormat.applyPattern(MM_DD_HH_MM);
            timeStr = simpleDateFormat.format(tempCalendar.getTime());
        }
        return String.format("%s ", timeStr);
    }


    public static String timeFormatInSecond(Context context, long time) {
        return timeFormat(context, time);
    }

    public static String timeFormathhMMHalfDay(long time) {
        if (time == 0) {
            return "";
        }
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);
        simpleDateFormat.applyPattern(hh_MM);
        return simpleDateFormat.format(tempCalendar.getTime());
    }

    public static String timeFormatHHMM(long time) {
        if (time == 0) {
            return "";
        }
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);
        simpleDateFormat.applyPattern(HH_MM);
        return simpleDateFormat.format(tempCalendar.getTime());
    }

    public static String forenoonOrAfternoon(Context context, long time) {
        if (time == 0 || context == null) {
            return "";
        }
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);
        Date date = tempCalendar.getTime();
        if (date.getHours() < 12) {
            return context.getResources().getString(R.string.forenoon);
        } else {
            return context.getResources().getString(R.string.afternoon);
        }
    }

    public static String timeFormatMMSS(long time) {
        if (time < 0) {
            return "";
        }
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);
        simpleDateFormat.applyPattern(MM_SS);
        return simpleDateFormat.format(tempCalendar.getTime());
    }

    public static String timeFormatMMDD(long time) {
        simpleDateFormat.applyPattern(MM_DD);
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);

        return simpleDateFormat.format(tempCalendar.getTime());
    }

    public static String timeFormatMM1DDOrDate(Context context, long time) {
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(System.currentTimeMillis());
        if (context != null) {
            tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
            tempCalendar.set(Calendar.MINUTE, 0);
            tempCalendar.set(Calendar.SECOND, 0);
            tempCalendar.set(Calendar.MILLISECOND, 0);
            long timeOffset = tempCalendar.getTimeInMillis() - time;
            if (timeOffset <= 0) {
                return context.getString(R.string.today);
            } else if (timeOffset <= OND_DAY_TIME && timeOffset > 0) {
                return context.getString(R.string.yesterday);
            }
        }
        int year = tempCalendar.get(Calendar.YEAR);
        tempCalendar.setTimeInMillis(time);
        int tyear = tempCalendar.get(Calendar.YEAR);
        if (tyear != year) {
            simpleDateFormat.applyPattern(YY1MM1DD);
        } else {
            simpleDateFormat.applyPattern(MM1DD);
        }
        return simpleDateFormat.format(tempCalendar.getTime());
    }


    public static String timeFormatWithMiddleLine(long time, int type) {
        simpleDateFormat.applyPattern(YYYY_MM_DD_MIDDLE_LINE);
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);
        if (type == DAY) {
            int date = tempCalendar.get(Calendar.DATE);
            tempCalendar.set(Calendar.DATE, date - 1);
        } else if (type == MONTH) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        }

        return simpleDateFormat.format(tempCalendar.getTime());
    }

    public static String timeFormatWithFullDetail(long time) {
        simpleDateFormat.applyPattern(YYYY_MM_DD_HH_MM);
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);

        return simpleDateFormat.format(tempCalendar.getTime());
    }

    public static String timeFormatWithMMDDHHmm(long time) {
        simpleDateFormat.applyPattern(MM_DD_HH_MM);
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);

        return simpleDateFormat.format(tempCalendar.getTime());
    }

    /**
     * 根据传入的字符串格式格式化time
     * @param time 时间
     * @param format 时间格式
     * @return
     */
    public static String timeFormatWidthFormat(long time, String format) {
        simpleDateFormat.applyPattern(MM_DD_HH_MM);
        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        tempCalendar.setTimeInMillis(time);

        return simpleDateFormat.format(tempCalendar.getTime());
    }

    /**
     * 格式化小时内的时间为string
     *
     * @param context
     * @param time
     * @return
     */
    public static String formatHourTime2String(Context context, long time) {
        Resources res = context.getResources();
        StringBuilder result = new StringBuilder();
        int minutes = (int) (time / 60);
        boolean isZh = isZh();
        if (minutes > 0) {
            result.append(minutes);
            //非中文加空格，如2min0sec -> 2 min 0 sec
            if (!isZh) {
                result.append(" ");
            }
            result.append(res.getString(R.string.minutes));
            if (!isZh) {
                result.append(" ");
            }
        }
        int seconds = (int) (time % 60);

        result.append(seconds);
        if (!isZh) {
            result.append(" ");
        }
        result.append(res.getString(R.string.seconds));
        return result.toString();
    }


    public static final int[] weekends = new int[]{R.string.saturday, R.string.sunday, R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday};

    /**
     * get  test string from index
     *
     * @param context
     * @param weekIndex 0:Sunday  - 6:Saturday
     * @return
     */
    public static String getWeekStrFromIndex(Context context, int weekIndex) {
        return context.getString(weekends[weekIndex % 7]);
    }

    public static boolean isZh() {
        String language = Locale.getDefault().getLanguage();
        if (language.endsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 手机当前的时区
     *
     * @return
     */
    public static String getCurrentTimezoneOffset() {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        String offset = (offsetInMillis >= 0 ? "+" : "-") + Math.abs(offsetInMillis / 3600000);
        return offset;
    }

    /**
     * GMT+8:00 => +8
     *
     * @param gmt
     * @return
     */
    public static String parseGMTToCloudTimeZone(String gmt) {
        if (gmt == null) {
            return "+8";
        }
        try {
            String split[] = null;
            boolean isPositive = true;
            if (gmt.contains("–")) {
                split = gmt.split("–");
                isPositive = false;
            } else if (gmt.contains("-")) {
                split = gmt.split("-");
                isPositive = false;
            } else if (gmt.contains("+")) {
                split = gmt.split("\\+");
            } else {
                return "+8";
            }
            String[] times = split[split.length - 1].split(":");
            StringBuilder stringBuilder = new StringBuilder(isPositive ? "+" : "-");
            stringBuilder.append(Integer.parseInt(times[0]));
            int minutes = Integer.parseInt(times[1]);
            if (minutes > 0) {
                stringBuilder.append(".");
                stringBuilder.append(minutes * 10 / 60);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "+8";
        }
    }

    /**
     * 统一显示成GMT(+/-)00:80格式
     *
     * @param gmt
     * @return
     */
    public static String parseCloudTimeZoneToDetail(String gmt) {
        try {
            int minute = (int) (Float.parseFloat(gmt) * 60);
            StringBuilder builder = new StringBuilder("GMT");
            DecimalFormat format = new DecimalFormat("00");
            builder.append(minute >= 0 ? "+" : "-");
            minute = Math.abs(minute);
            builder.append(format.format(minute / 60));
            builder.append(":");
            builder.append(format.format(minute % 60));
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return gmt;
        }
    }

    /**
     * +8 => (GMT+8:00) 2019.02.25 09:41
     *
     * @param gmt
     * @return
     */
    public static String parseCloudTimeZoneToWithoutTimeDetail(String gmt) {
        try {
            int minute = (int) (Float.parseFloat(gmt) * 60);
            StringBuilder builder = new StringBuilder("GMT");
            DecimalFormat format = new DecimalFormat("00");
            builder.append(minute > 0 ? "+" : "-");
            minute = Math.abs(minute);
            builder.append(format.format(minute / 60));
            builder.append(":");
            builder.append(format.format(minute % 60));
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT" + gmt));
            return String.format("%s", builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return gmt;
        }
    }
}
