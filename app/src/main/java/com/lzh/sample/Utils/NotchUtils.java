package com.lzh.sample.Utils;

import android.content.Context;
import android.util.Log;


import java.lang.reflect.Method;

/**
 * 处理人神共厌的刘海屏😒
 * Created by cdy on 2019/4/29.
 */
public class NotchUtils {

    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海

    private static final int MI_HAS_NOTCH = 1;
    private static final int MI_NO_NOTCH = 0;

    /**
     * 暂时只适配华为、vivo、小米、oppo，其他按没有处理。单位px
     */
    public static int getNotchHeight(Context context) {
        if (hasNotchAtHuawei(context)) {
            return getNotchSizeAtHuawei(context, false)[1];
        } else if (hasNotchAtVivo(context)) {
            return DensityUtils.dp2px(context, 27);
        } else if (hasNotchAtOPPO(context)) {
            return 80;
        } else if (hasNotchAtXIAOMI(context)) {
            return getNotchSizeAtMI(context, false);
        } else {
            return StatusBarCompat.getStatusBarHeight(context);
        }
    }

    /**
     * 暂时只适配华为、vivo、小米、oppo，其他按没有处理。单位px
     * isPure 表示只获取notchHeight，不获取statusbarHeight
     */
    public static int getNotchHeightPure(Context context) {
        if (hasNotchAtHuawei(context)) {
            return getNotchSizeAtHuawei(context, true)[1];
        } else if (hasNotchAtVivo(context)) {
            return DensityUtils.dp2px(context, 27);
        } else if (hasNotchAtOPPO(context)) {
            return 80;
        } else if (hasNotchAtXIAOMI(context)) {
            return getNotchSizeAtMI(context, true);
        }
        return 0;
    }

    public static boolean hasNotch(Context context) {
        if (hasNotchAtHuawei(context)) {
            return true;
        } else if (hasNotchAtVivo(context)) {
            return true;
        } else if (hasNotchAtOPPO(context)) {
            return true;
        } else if (hasNotchAtXIAOMI(context)) {
            return true;
        } else {
            return false;
        }
    }

    private static int getNotchSizeAtMI(Context context, boolean isPure) {
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return isPure ? 0 : StatusBarCompat.getStatusBarHeight(context);
    }

    private static boolean hasNotchAtXIAOMI(Context context) {
        return getSystemPropertiesValue(context, "ro.miui.notch", MI_NO_NOTCH) == MI_HAS_NOTCH;
    }

    public static int getSystemPropertiesValue(Context context, String key, int defaultValue) {
        int ret = defaultValue;
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class systemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[2];
            paramTypes[0] = String.class;
            paramTypes[1] = int.class;
            Method get = systemProperties.getMethod("getInt", paramTypes);
            //参数
            Object[] params = new Object[2];
            params[0] = new String(key);
            params[1] = defaultValue;
            ret = (int) get.invoke(systemProperties, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static boolean hasNotchAtOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    public static boolean hasNotchAtVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
//            ALog.w("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
//            ALog.w("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
//            ALog.w("Notch", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }
    }

    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
//            ALog.w("Notch", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
//            ALog.w("Notch", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
//            ALog.w("Notch", "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }

    public static int[] getNotchSizeAtHuawei(Context context, boolean isPure) {
        int[] ret = new int[]{0, isPure ? 0 : StatusBarCompat.getStatusBarHeight(context)};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.w("Notch", "getNotchSizeAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.w("Notch", "getNotchSizeAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.w("Notch", "getNotchSizeAtHuawei Exception");
        } finally {
            return ret;
        }
    }
}
