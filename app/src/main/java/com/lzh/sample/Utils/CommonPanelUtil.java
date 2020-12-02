package com.lzh.sample.Utils;

import android.graphics.Color;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cdy on 2018/3/13.
 */

public class CommonPanelUtil {

    //Aqara门锁属性值类型
    public final static String LOCK_USER_TYPE_ADMIN = "1";
    public final static String LOCK_USER_TYPE_ORDINARY = "2";
    public final static String ATTR_VALUE_PICK_LOCK = "16";//撬锁
    public final static int LOCK_OPEN_TYPE_FINGER = 1;
    public final static int LOCK_OPEN_TYPE_PWD = 2;
    public final static int LOCK_OPEN_TYPE_KEY_UNLOCK = 4;//钥匙或内部门把手开锁
    public final static int LOCK_OPEN_TYPE_AUTH_UNLOCK = 5;//授权开锁

    final static Pattern lockPattern = Pattern.compile("\\[(\\d+),(\\d+)\\]");

    public static String getUserType(String s) {
        if (s == null) {
            return null;
        }
        Matcher matcher = lockPattern.matcher(s);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    public static String getOpenType(String s) {
        if (s == null) {
            return null;
        }
        Matcher matcher = lockPattern.matcher(s);
        if (matcher.matches() && matcher.groupCount() > 1) {
            return matcher.group(2);
        }
        return null;
    }

    //ColorTemperature
    public static int pauseStringToColorTemperature(String tempValue, int tempMin, int tempMax) {
        float kelvins = 0f;
        int value = (int) (1000000 / kelvins);
        if (value < tempMin) {
            value = tempMin;
        } else if (value > tempMax) {
            value = tempMax;
        }
        return value;
    }

    public static float pauseStringToColorTemperatureRate(String tempValue, int tempMin, int tempMax) {
        int value = pauseStringToColorTemperature(tempValue, tempMin, tempMax);
        float rate = ((float) value - tempMin) / (tempMax - tempMin);
        return get2DecimalNum(rate);
        //return (float)(Math.round(rate * 100))/100;
    }

    public static int pauseTemperatureSendValue(float rate, int tempMin, int tempMax) {
        rate = get2DecimalNum(rate);
        int kelvins = (int) ((rate) * (tempMax - tempMin) + tempMin);
        return 1000000 / kelvins;
    }

    /**
     * 服务器只接受整数，为了防止因为精度丢失导致的误差，这里先转换成发送数据，再由发送计算出色温值
     * @param rate
     * @param tempMin
     * @param tempMax
     * @return
     */
    public static int parseRate2Temperature(float rate, int tempMin, int tempMax) {
        int sendTempValue = pauseTemperatureSendValue(rate, tempMin, tempMax);
        return pauseStringToColorTemperature(String.valueOf(sendTempValue), tempMin, tempMax);
    }

    //直接转int数值会有问题，因为RGB是无符号的
    public static int getARGB(String value) throws NumberFormatException {
        long lValue = Long.parseLong(value);
        return (int) lValue;
    }

    public static String parseARGBToString(int color, float rate) {
        color = color & 0xffffff;
//        long lColor = (long) color | ((long) (0xff * rate) << 24);
        long lColor = (long) color | ((long) (0x64 * rate) << 24);
        return lColor + "";
    }

    public static int getRGB(int color) {
        return color & 0x00ffffff;
    }

    public static String parseARGBToString(int color) {
        color = color & 0xffffff;
        int alpha = color >> 24;
        long lColor = (long) color | ((long) (alpha) << 24);
        return lColor + "";
    }

    /**
     * 将颜色值转换成对应的16进制字符串
     * @param color
     * @return
     */
    public static String toRGBHexStr(int color) {
        return Integer.toHexString(color | 0xff000000).substring(2);
    }

    /**
     * 四舍五入保留2位小数
     * @param num
     * @return
     */
    public static float get2DecimalNum(float num) {
        return Math.round(num * 100) / 100;
    }

    public static int parseColor(int color) {
        String colorStre = CommonPanelUtil.toRGBHexStr(color);
        try {
            return Color.parseColor("#" + colorStre);
        } catch (IllegalArgumentException e) {
            return color;
        }
    }


    /**
     * 计算两个颜色的色差，色差越小，颜色越接近
     * @param rgbTarget
     * @param rgbOther
     * @return
     */
    public static double calculateColorDifference(int rgbTarget, int rgbOther) {
        int r1 = ((rgbTarget >> 16) & 0xff);
        int g1 = ((rgbTarget >>  8) & 0xff);
        int b1 = ((rgbTarget      ) & 0xff);
        int r2 = ((rgbOther >> 16) & 0xff);
        int g2 = ((rgbOther >>  8) & 0xff);
        int b2 = ((rgbOther      ) & 0xff);


        long rMean = (long) ((r1 + r2) / 2);
        long r = (long) (r1 - r2);
        long g = (long) (g1 - g2);
        long b = (long) (b1 - b2);
        //double colorDiff = Math.sqrt((2 + rMean/256) * Math.pow(r, 2) + 4 * Math.pow(g, 2) + (2 + (255 -  rMean)/256) * Math.pow(b, 2));
        return Math.sqrt((((512+rMean)*r*r)>>8) + 4*g*g + (((767-rMean)*b*b)>>8));
    }
}
