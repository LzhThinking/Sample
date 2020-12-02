package com.lzh.sample.Utils;

import java.util.HashMap;

/**
 * @Author leizhiheng
 * @CreateDate 2019/12/16
 * @Description
 */
public class ColorUtils {

//    标准    X   Y                   X     Y
//    红色  A3D6  547A                A3DB  5478
//    绿色  4CCC  9999                ‭4CCC‬  ‭9998‬
//    蓝色  2666  F5C                 2667‬   F5C
//    黄色  6b58  8157                ‭6B58‬  ‭8157‬
//    紫色  5228  2778                ‭5227‬  ‭2776‬
//    某色  3983  5429
//    白色  500C  5439                ‭500D‬  ‭5439‬

    //预设情景
    public static HashMap<Long, Integer> sPreInstallProfiles = new HashMap<>();
    static {
        //RGBW预设情景
        sPreInstallProfiles.put(2193910133L, 0xFE912D);//放松
        sPreInstallProfiles.put(932594195L, 0x53C6FD);//休息
        sPreInstallProfiles.put(1810380207L, 0xFD01AE);//电影
        sPreInstallProfiles.put(1920301991L, 0xFFDB01);//起床
    }

    public static float sPreY = -1f;
    public static float sPreLightXy = -1f;

    /**
     * RGB转light_xy
     * @param rgb
     * @return
     */
    public static long convertRGB2Light_xy(int rgb) {
        return convertRGB2Light_xy(rgb, true);
    }

    /**
     *
     * @param rgb
     * @param isSaveY 是否保存Y值
     * @return
     */
    public static long convertRGB2Light_xy(int rgb, boolean isSaveY) {


        float[] XYZ = RGB2XYZ(rgb);
        float[] xyY = XYZ2xyY(XYZ[0], XYZ[1], XYZ[2]);

        sPreY = isSaveY ? xyY[2] : -1;


        print("convertRGB2Light_xy : fx = " + xyY[0] + ", fy = " + xyY[1] + ", Y = " + xyY[2]);

        long x = (int) (xyY[0] * 65535);
        long y = (int) (xyY[1] * 65535);


        print("convertRGB2Light_xy : x = " + x + ", y = " + y + ", Y = " + xyY[2]);

        long light_xy = (x << 16) | y;
        sPreLightXy = light_xy;
        savePreYxy();

        return light_xy;
    }

    public static float[] RGB2XYZ(long rgb) {
        rgb = rgb & 0xffffff;
        float fRed = ((rgb >> 16) & 0xff);
        float fGreen = ((rgb >> 8) & 0xff);
        float fBlue = ((rgb) & 0xff);

        print("RGB2XYZ  R = " + fRed + ", G = " + fGreen + ", B = " +fBlue);

        double var_R = ( fRed / 255 );
        double var_G = ( fGreen / 255 );
        double var_B = ( fBlue / 255 );

        if ( var_R > 0.04045 ) var_R = Math.pow( (( var_R + 0.055 ) / 1.055 ), 2.4);
        else                   var_R = var_R / 12.92;
        if ( var_G > 0.04045 ) var_G = Math.pow(( ( var_G + 0.055 ) / 1.055 ), 2.4);
        else                   var_G = var_G / 12.92;
        if ( var_B > 0.04045 ) var_B = Math.pow(( ( var_B + 0.055 ) / 1.055 ), 2.4);
        else                   var_B = var_B / 12.92;

        var_R = var_R * 100;
        var_G = var_G * 100;
        var_B = var_B * 100;

        double X = var_R * 0.4124f + var_G * 0.3576f + var_B * 0.1805f;
        double Y = var_R * 0.2126f + var_G * 0.7152f + var_B * 0.0722f;
        double Z = var_R * 0.0193f + var_G * 0.1192f + var_B * 0.9505f;

        print("RGB2XYZ  X = " + X + ", Y = " + Y + ", Z = " +Z);

        float[] XYZ = new float[3];
        XYZ[0] = (float) X;
        XYZ[1] = (float) Y;
        XYZ[2] = (float) Z;

        return XYZ;
    }

    private static float[] XYZ2xyY(float fX, float fY, float fZ) {

        /* x = X / (X + Y + Z) */
        float fx = (fX / (fX + fY + fZ));

        /* y = Y / (X + Y + Z) */
        float fy = (fY / (fX + fY + fZ));

        /* Y = Y */
        float fYY = fY;

        float[] xyY = new float[3];
        xyY[0] = fx;
        xyY[1] = fy;
        xyY[2] = fYY;
        return xyY;
    }


    /**
     * light_xy转RGB
     * @param light_xy
     * @returnf
     */
    public static int convertLight_xy2RGB(long light_xy, float light) {
        float x = (light_xy >> 16 & 0xffff) * 1.0f / 65535;
        float y = (light_xy & 0xffff) * 1.0f / 65535;

        if (sPreInstallProfiles.containsKey(light_xy)) {
            return sPreInstallProfiles.get(light_xy);
        } else {
            checkPreYxy();
        }

        if (light_xy == sPreLightXy && sPreY > 0) {
            float Y = sPreY;

            float[] XYZ = xyY2XYZ(x, y, Y);
            float[] RGB = XYZ2RGB(XYZ[0], XYZ[1], XYZ[2]);

            int red = (int) RGB[0];
            int green = (int) RGB[1];
            int blue = (int) RGB[2];
            //return Color.argb(255, red, green, blue) & 0x00ffffff;
            return (red << 16 | green << 8 | blue ) & 0x00ffffff;
        } else {
            //如果不是之前RGB-Yxy转化过程中得到的x,y,Y，则使用这个方式转化成RGB，这个方式有误差，但是误差不至于太离谱
            return convertLight_xy2RGBTemp(light_xy, 1f);
        }


        //return (red << 16 | green << 8 | blue ) & 0x00ffffff;

    }

    private static float[] xyY2XYZ(float x, float y, float Y) {

//        X = x * ( Y / y )
//        Y = Y
//        Z = ( 1 - x - y ) * ( Y / y )
        print("fx = " + x + ", fy = " + y + ", fY = " + Y);


        /* X = (x / y) * Y */
        float fX = (x / y) * Y;

        /* Y = Y */
        float fY = Y;

        /* Z = (z / y) * Y */
        float fZ = (1 - x - y) * (Y / y);

        float[] XYZ = new float[3];
        XYZ[0] = fX;
        XYZ[1] = fY;
        XYZ[2] = fZ;

        return XYZ;
    }

    private static float[] XYZ2RGB(float X, float Y, float Z) {

        print("XYZ2RGB:  X = " + X + ", Y = " + Y + ", Z = " + Z);

        double var_X = X / 100;
        double var_Y = Y / 100;
        double var_Z = Z / 100;

        double var_R = var_X *  3.2406 + var_Y * -1.5372 + var_Z * -0.4986;
        double var_G = var_X * -0.9689 + var_Y *  1.8758 + var_Z *  0.0415;
        double var_B = var_X *  0.0557 + var_Y * -0.2040 + var_Z *  1.0570;

        if ( var_R > 0.0031308 ) {
            var_R = 1.055 * ( Math.pow(var_R, ( 1 / 2.4 ))) - 0.055;
        } else                     {
            var_R = 12.92 * var_R;
        }
        if ( var_G > 0.0031308 ) {
            var_G = 1.055 * ( Math.pow(var_G, ( 1 / 2.4 ) )) - 0.055;
        } else {
            var_G = 12.92 * var_G;
        }
        if ( var_B > 0.0031308 ) {
            var_B = 1.055 * ( Math.pow(var_B, ( 1 / 2.4 ) )) - 0.055;
        } else {
            var_B = 12.92 * var_B;
        }

        float sR = (float) (var_R * 255);
        float sG = (float) (var_G * 255);
        float sB = (float) (var_B * 255);

        print("XYZ2RGB:  sR = " + (sR) + ", sG = " + (sG) + ", sB = " + (sB));


        float[] RGB = new float[3];
        RGB[0] = sR;
        RGB[1] = sG;
        RGB[2] = sB;
        return RGB;
    }

    /**
     * light_xy转RGB， 如果没有Y值时使用这个方法。会有误差，但是误差比较小
     * @param light_xy
     * @return
     */
    public static int convertLight_xy2RGBTemp(long light_xy, float light) {
        float x = (light_xy >> 16 & 0xffff) * 1.0f / 65535;
        float y = (light_xy & 0xffff) * 1.0f / 65535;
        float Y = 255f;

        float[] XYZ = xyY2XYZTemp(x, y, Y);
        float[] RGB = XYZ2RGBTemp(XYZ[0], XYZ[1], XYZ[2]);

//        int colorRgb = ((int)RGB[0] << 16) | ((int)RGB[1] <<8) | ((int)RGB[2]);
        int colorRgb = 0xff000000 |
                ((int) (RGB[0]   * 255.0f + 0.5f) << 16) |
                ((int) (RGB[1] * 255.0f + 0.5f) <<  8) |
                (int) (RGB[2]  * 255.0f + 0.5f);

        return colorRgb & 0x00ffffff;
    }

    private static float[] xyY2XYZTemp(float fx, float fy, float fY) {
        /* z = 1 - x - y */
        float fz = (float) (1.0 - fx - fy);

        /* X = (x / y) * Y */
        float pfX = (fx / fy) * fY;

        /* Y = Y */
        float pfY = fY;

        /* Z = (z / y) * Y */
        float pfZ = (fz / fy) * fY;

        float[] XYZ = new float[3];
        XYZ[0] = pfX;
        XYZ[1] = pfY;
        XYZ[2] = pfZ;

        return XYZ;
    }

    private static float[] XYZ2RGBTemp(float fX, float fY, float fZ) {

        float R, G, B, Max;

        fX = fX / 100f;
        fY = fY / 100f;
        fZ = fZ / 100f;

        R = (float) (fX * 3.240970 + fY * -1.537383 + fZ * -0.498611);
        G = (float) (fX * -0.969244 + fY * 1.875967 + fZ * 0.041555);
        B = (float) (fX * 0.055630 + fY * -0.203977 + fZ * 1.056971);

        Max = Math.max(Math.max(R, G), B);

        if (Max > 1.000) {
            R = R / Max;
            G = G / Max;
            B = B / Max;
        }

        if (R < 0.0) R = 0;
        if (G < 0.0) G = 0;
        if (B < 0.0) B = 0;

        float pfRed = R;
        float pfGreen = G;
        float pfBlue = B;

        float[] RGB = new float[3];
        RGB[0] = pfRed;
        RGB[1] = pfGreen;
        RGB[2] = pfBlue;
        return RGB;
    }

    private static void savePreYxy() {
//        SPUtils.put(AppContext.get(), "preY", sPreY, SPUtils.COMMON_FILE_NAME);
//        SPUtils.put(AppContext.get(), "preLight_xy", sPreLightXy, SPUtils.COMMON_FILE_NAME);
    }

    private static void checkPreYxy() {
        if (sPreY == -1 || sPreLightXy == -1) {
//            sPreY = (float) SPUtils.get(AppContext.get(), "preY", -1f, SPUtils.COMMON_FILE_NAME);
//            sPreLightXy = (float) SPUtils.get(AppContext.get(), "preLight_xy", -1f, SPUtils.COMMON_FILE_NAME);
        }
    }

    public static void print(String msg) {
        //Log.d("zhiheng", msg);
    }
}
