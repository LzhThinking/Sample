package com.lzh.sample;

import com.lzh.sample.Utils.FormatUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class JavaTest {
    public static void main(String args[]) {
        long milliSecOfDay = 24 * 60 * 60 * 1000;
        long zeroTime = FormatUtils.getCurrentWeekZeroTime();
        String zero = FormatUtils.timeFormatWidthFormat(zeroTime, FormatUtils.YYYY_MM_DD_HH_MM_SSS);
        print("week:" + zero);
        long time = (long) 1.56873602E12;
        long time2 = 1568476800327L;
        double timeD = 1567958400916D;
        float timeF = 1567958400916f;
        print(FormatUtils.timeFormatWidthFormat(time, FormatUtils.YYYY_MM_DD_HH_MM_SSS));
        print(FormatUtils.timeFormatWidthFormat(time2, FormatUtils.YYYY_MM_DD_HH_MM_SSS));
        print("-----------------------");

        print(Math.log(16) / Math.log(2) + "");

//        float times[] = new float[] {1567785600214f,
//                (float) 1.56778562E12, (float) 1.56780003E12, (float) 1.56781445E12,
//                (float) 1.56782887E12, (float) 1.56784329E12, (float) 1.5678577E12, (float) 1.56787212E12,
//                (float) 1.56788654E12,
//                (float) 1.56787212E12};
//        for (int i = 0; i < times.length; i++) {
//            print(FormatUtils.timeFormatWidthFormat((long) times[i], FormatUtils.YYYY_MM_DD_HH_MM_SSS) + "");
//        }

//        double min = 1.56778548E12;
//        double interval = 1.5677999E12;
//        print(min - interval + "");
//        print(Math.ceil(min/interval) + "");
//        m2(-0.0056f);
        m1(-10.0056f);
        print(m2(-0.0056f) + "");
        print(get2DecimalNum(2.00056f) + "");
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
