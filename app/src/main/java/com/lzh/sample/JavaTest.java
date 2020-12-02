package com.lzh.sample;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lzh.sample.Utils.ColorUtils;
import com.lzh.sample.Utils.CommonPanelUtil;
import com.lzh.sample.Utils.FormatUtils;
import com.lzh.sample.annotation.ClassAnn;
import com.lzh.sample.entity.Entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;

@ClassAnn(typeName = "JavaTestName")
public class JavaTest {


    private static CompositeDisposable disposable = new CompositeDisposable();

    public static void main(String args[]) {
        long milliSecOfDay = 7 * 24 * 60 * 60 * 1000;
        long dayZeroTime = FormatUtils.getCurrentTime();
        //long dayEndTime = dayZeroTime + milliSecOfDay;
        long weekZeroTime = FormatUtils.getCurrentWeekZeroTime();

//        long l = FormatUtils.getLastYearZeroTime();
//        float f = FormatUtils.getLastYearZeroTime();
        long ll = milliSecOfDay;
        float f = ll;
        long dis = (long) f;


        //l = 21999759, f = 2.199976E7, disf = -1
        //l = 54274598352, f = 5.4274597E10, disf = 1488
//        print("day start:" +  "l = " + ll + ", f = " + f  + ", disf = " + FormatUtils.getCurrentDayZeroTime());
//        print("day end:" +  FormatUtils.timeFormatWidthFormat(FormatUtils.getCurrentTime(), FormatUtils.YYYY_MM_DD_HH_MM_SSS, "+8"));
//        print("day end:" +  FormatUtils.timeFormatWidthFormat(FormatUtils.getCurrentTime(), FormatUtils.YYYY_MM_DD_HH_MM_SSS, "+10"));
//        print("day end:" +  FormatUtils.timeFormatWidthFormat(FormatUtils.getCurrentTime(), FormatUtils.YYYY_MM_DD_HH_MM_SSS, "+10.5"));
//        print("day end:" +  FormatUtils.timeFormatWidthFormat(FormatUtils.getCurrentTime(), FormatUtils.YYYY_MM_DD_HH_MM_SSS, "-7"));
//        print("day end:" +  FormatUtils.timeFormatWidthFormat(FormatUtils.getCurrentTime(), FormatUtils.YYYY_MM_DD_HH_MM_SSS, "-7.5"));
//        print("day end:" +  FormatUtils.timeFormatWidthFormat(FormatUtils.getCurrentTime(), FormatUtils.YYYY_MM_DD_HH_MM_SSS, "-8"));
//
//        print("zone8 : " + FormatUtils.getDayZeroTimeByTime(1570680977520L, "+08"));
//        print("zone9 : " + FormatUtils.getDayZeroTimeByTime(1570680977520L, "-7"));

        long time = (long) 1568924196630L;
        long time2 = 1568476800327L;
        double timeD = 1567958400916D;
        float timeF = 1567958400916f;
//        print(FormatUtils.timeFormatWidthFormat(time, FormatUtils.YYYY_MM_DD_HH_MM_SSS));
//        print(FormatUtils.timeFormatWidthFormat(time2, FormatUtils.YYYY_MM_DD_HH_MM_SSS));
//        print("-----------------------");
//
//        print(Math.log(16) / Math.log(2) + "");

//        print("id: " + TimeZone.getDefault().getDisplayName());
//        print("id: " + TimeZone.getTimeZone("GMT+08").getDisplayName());
//        print("id: " + TimeZone.getTimeZone("GMT+8").getDisplayName());
//
//        print("id: " + TimeZone.getTimeZone("GMT+08").getID());
//        print("id: " + TimeZone.getTimeZone("GMT+8").getID());

//        print("Flag FLAG_ACTIVITY_NEW_TASK: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_NEW_TASK));
//        print("Flag FLAG_ACTIVITY_BROUGHT_TO_FRONT: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
//        print("Flag FILL_IN_ACTION: " + Integer.toBinaryString(Intent.FILL_IN_ACTION));
//        print("Flag FLAG_ACTIVITY_CLEAR_TASK: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_CLEAR_TASK));
//        print("Flag FLAG_ACTIVITY_FORWARD_RESULT: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_FORWARD_RESULT));
//        print("Flag FILL_IN_DATA: " + Integer.toBinaryString(Intent.FILL_IN_DATA));
//        print("Flag FILL_IN_SELECTOR: " + Integer.toBinaryString(Intent.FILL_IN_SELECTOR));
//
//        printNum("00");
//        printNum("01");
//        printNum("0001");
//        printNum("");
//        printNum("   ");
//        printNum("1.1");

        int color = 0xFF0000;
        long xy = ColorUtils.convertRGB2Light_xy(color);
        int colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("Test rbg2xy = " + xy);
//        print("Test xy2rgb = " + CommonPanelUtil.toRGBHexStr(colorAfter));

//        color = 0x00FF00;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("===================================\n");
//        color = 0x0000FF;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("===================================\n");
//
//        color = 0xFF1439;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("===================================\n");
//
//        color = 0x84e635;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("===================================\n");
//
//        color = -45881;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("color after = " + colorAfter);
//        print("===================================\n");


        print("放松: " + ColorUtils.convertRGB2Light_xy(16683309));
        print("休息: " + ColorUtils.convertRGB2Light_xy(5490429));
        print("电影: " + ColorUtils.convertRGB2Light_xy(16581038));
        print("起床: " + ColorUtils.convertRGB2Light_xy(16767745));

//        color = 0xFF0000;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("color after = " + colorAfter);
//        print("===================================\n");
//
//        color = 0x00FF00;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("color after = " + colorAfter);
//        print("===================================\n");
//
//        color = 0x0000FF;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("color after = " + colorAfter);
//        print("===================================\n");
//
//        color = 0xFFFF00;
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("黄色 color after = " + colorAfter);
//        print("===================================\n");
//
//        color = 0xff00ff;
//        print("淡红 color beflor = " + color);
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("紫色 color after = " + colorAfter);
//        print("===================================\n");
//
//        color = 0xffffff;
//        print("淡红 color beflor = " + color);
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("白色 color after = " + colorAfter);
//        print("===================================\n");
//
//        color = 0xFF201F;
//        print("淡红 color beflor = " + color);
//        xy = ColorUtils.convertRGB2Light_xy(color);
//        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
//        print("淡红 color after = " + colorAfter);
//        print("===================================\n");
//        //0xFF201F    827C  ‭54B7‬
////        parseRGB(-4694785);
//1050240632
        color = 0x289CF6;
        print("淡红 color beflor = " + color);
        xy = ColorUtils.convertRGB2Light_xy(color);
        print("xy = " + xy);
        colorAfter = ColorUtils.convertLight_xy2RGB(xy, 1f);
        print("淡红 color after = " + colorAfter);
        print("===================================\n");


        long xyNew = 2107338266L;
        print("xy->rgb : " + CommonPanelUtil.toRGBHexStr(ColorUtils.convertLight_xy2RGB(xyNew, 1f)) + ", color = " + ColorUtils.convertLight_xy2RGB(xyNew, 1f));
        print("xy->rgb : " + CommonPanelUtil.toRGBHexStr(ColorUtils.convertLight_xy2RGBTemp(xyNew, 1f)) + ", color = " + ColorUtils.convertLight_xy2RGBTemp(xyNew, 1f));

//        xy = 1428656872L;
//        print("rgb = " + ColorUtils.convertLight_xy2RGB(xy, 1f));
//        print("rgbTemp = " + ColorUtils.convertLight_xy2RGBTemp(xy, 1f));
//
//        String[] ids = new String[] {"1", "2"};
//        print(JSON.toJSONString(ids));
//        print(String.valueOf(4.499999f));
//        float d = 123456.12345678f;
//        float ff = (float) d;
//        print("ff = " + d);
//        print(Math.round(4.499999f) + "");

        float result = 2.0f;
        int resultInt = 2;
        boolean reslt = true;

        String ss = "aaabbbbccc";
        print("index : " + ss.indexOf("bb"));
        long on = 6145;
        long off = 268441601;
        print("on: " + (on & 0xFFFFFFF));
        print("off: " + ((1 & 1) << 28 | off & 0xFFFFFFF));


//        ["00010000000000"]：取消行程
//                ["00020001000000"]：极性反向
//                ["00020000000000"]：极性正向
//                ["00080000000000"]：启动手拉
//                ["00080000000100"]：禁止手拉
        //00010000000000000000000000000000000000000000
        //00001111111100000000000000000000000000000000
        long cf = 0x00020001000000L;
        print("cf: " + ((cf & 0x0000FF00000000L) >> 32));
        print("cf: " + (Integer.MAX_VALUE >> 2));

        getList1(2, 5, 1);
        getList2(2, 5, 1);
    }

    public static void getList1(int minValue, int maxValue, int step) {
        step = step == -1 ? 1 : step;
        int curValue = maxValue;
        while (curValue >= minValue) {
            print("curValue = " + curValue);
            curValue -= step;
        }
    }

    public static void getList2(int minValue, int maxValue, int step) {

        for (int i = maxValue; i >= minValue; i--) {
            print("curValue = " + i);
        }
    }

    public static String toJson(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect);
    }


    public static class User {
        public String name;

        public User(String name) {
            this.name = name;
        }
    }

    public static class Person {
        public String id = "111";

        public Drawable drawable;
        @JSONField(serialize = false)
        public List<User> users;

    }


    public static List<String> parseTextToDataKeys(String text) {
        List<String> dataKeys = new ArrayList<>();
        if (!text.contains("$")) {
            return dataKeys;
        }

        String[] parts = text.split("\\$");
        for (String part : parts) {
            if ((part != null) && part.contains("{") && part.contains("}")) {
                int indexStart = part.indexOf("{");
                int indexEnd = part.indexOf("}");
                dataKeys.add(part.substring(indexStart + 1, indexEnd));
            }
        }
        return dataKeys;
    }

    private static void parseRGB(int colorInt) {
        int alpha = (colorInt & 0xff000000) >> 24;
        int red = (colorInt & 0xff0000) >> 16;
        int green = (colorInt & 0x00ff00) >> 8;
        int blue = (colorInt & 0x0000ff);

        int color = (red << 16) | (green << 8) | blue;
        int color2 = 0xff000000 | (red << 16) | (green << 8) | blue;

        print("parseRGB: alpha = " + alpha + ", red = " + red + ", green = " + green + ", blue = " + blue + ", color = " + color + ", color2 = " + color2);
    }

    private static void printNum(String num) {
        print("num is numeric : " + isNumeric(num) + ", num: " + (isNumeric(num) ? Integer.valueOf(num) : "str_" + num));
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]+");
        return pattern.matcher(str).matches();
    }

    public static void print(String msg) {
        System.out.println(msg);
    }

    public void m4() {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
    }

    public static void m1(float num) {
        BigDecimal bg = new BigDecimal(num);
        float f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        System.out.println(f1);
    }

    public static float m2(float num) {
        DecimalFormat df = new DecimalFormat("0.00");

        return Float.valueOf(df.format(num));
    }

    public static float get2DecimalNum(float num) {
        return Math.round(num * 100) / 100f;
    }
}
