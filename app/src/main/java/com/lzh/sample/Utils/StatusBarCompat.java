package com.lzh.sample.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lzh.sample.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.View.SYSTEM_UI_FLAG_VISIBLE;

public class StatusBarCompat {
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    /**
     * 将acitivity中的activity中的状态栏设置为一个纯色
     *
     * @param activity 需要设置的activity
     * @param color    设置的颜色（一般是titlebar的颜色）
     */
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上，不设置透明状态栏，设置会有半透明阴影
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置statusBar的背景色
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusBarView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            //让我们的activity_main。xml中的布局适应屏幕
            setRootView(activity);
        }
    }

    /**
     * 当顶部是图片时，是图片显示到状态栏上
     *
     * @param activity
     */
    public static void setImage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上，不设置透明状态栏，设置会有半透明阴影
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //是activity_main。xml中的图片可以沉浸到状态栏上
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置状态栏颜色透明。
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            //。。。。
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 设置根布局参数，让跟布局参数适应透明状态栏
     */
    private static void setRootView(Activity activity) {
        //获取到activity_main.xml文件
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);

        //如果不是设置参数，会使内容显示到状态栏上
        rootView.setFitsSystemWindows(true);
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        int height = context.getResources().getDimensionPixelOffset(resourceId);
        int notchHeight = NotchUtils.getNotchHeightPure(context);

        return Math.max(height, notchHeight);
    }


    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    public static void setStatusBarStyle(Activity act, boolean isTransparent, int color) {
        if (isTransparent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                act.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                act.getWindow().setStatusBarColor(color);
            } else {
                act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                act.getWindow().setStatusBarColor(act.getResources().getColor(R.color.def_statusbar));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                act.findViewById(android.R.id.content).setSystemUiVisibility(
                        SYSTEM_UI_FLAG_VISIBLE);
                setColor(act, color);
            } else {
                act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                act.getWindow().setStatusBarColor(act.getResources().getColor(R.color.def_statusbar));
            }
        }
    }


    public static void setStatusBarIconStyle(Activity act, boolean isLight, boolean isShowNavigation) {
        //1.setMIUISetStatusBarLightMode(act, isLight)小米5C（7.1.2）无效？？？官方demo都是一直白色的。 而且是V9版本>V6。。。
        //再吐槽一句原来跳转页面icon亮暗色闪烁显示的bug是系统的锅。具体看手机自带设置的跳转效果。
        //2.小米4（MIUI8，6.0.1）用Android默认方法设置是无效的，但是setMIUISetStatusBarLightMode 效果正常。。。
        int flag;
        if (!setMIUISetStatusBarLightMode(act, isLight)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLight) {
                    flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                flag = isShowNavigation ? flag : (flag | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                act.getWindow().getDecorView().setSystemUiVisibility(flag);
            } else {
                setFlymeSetStatusBarLightMode(act, isLight, isShowNavigation);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLight) {
                    flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                flag = isShowNavigation ? flag : (flag | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                act.getWindow().getDecorView().setSystemUiVisibility(flag);
            } else {
                setFlymeSetStatusBarLightMode(act, isLight, isShowNavigation);
            }
        }

    }

    public static void setStatusBarIconStyle(Activity act, boolean isLight) {
        //1.setMIUISetStatusBarLightMode(act, isLight)小米5C（7.1.2）无效？？？官方demo都是一直白色的。 而且是V9版本>V6。。。
        //再吐槽一句原来跳转页面icon亮暗色闪烁显示的bug是系统的锅。具体看手机自带设置的跳转效果。
        //2.小米4（MIUI8，6.0.1）用Android默认方法设置是无效的，但是setMIUISetStatusBarLightMode 效果正常。。。
        if (!setMIUISetStatusBarLightMode(act, isLight)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLight) {
                    act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            } else {
                setFlymeSetStatusBarLightMode(act, isLight);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isLight) {
                    act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    act.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
            } else {
                setFlymeSetStatusBarLightMode(act, isLight);
            }
        }

    }

    /**
     * 设置魅族手机状态栏图标颜色风格 >>> note3（7.0）效果很奇怪，activity跳转时是正常的，activity动态设置则无效
     * 可以用来判断是否为Flyme用户
     *
     * @param activity 需要设置的activity
     * @param isLight  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setFlymeSetStatusBarLightMode(Activity activity, boolean isLight) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isLight) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static boolean setFlymeSetStatusBarLightMode(Activity activity, boolean isLight, boolean isShowNavigation) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            flag = isShowNavigation ? flag : (flag | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            activity.getWindow().getDecorView().setSystemUiVisibility(flag);
        }
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isLight) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置小米手机状态栏字体图标颜色模式，需要MIUIV6以上
     *
     * @param activity 需要设置的activity
     * @param isLight  是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setMIUISetStatusBarLightMode(Activity activity, boolean isLight) {
        boolean result = false;
        Window window = activity.getWindow();
        if (activity != null) {
            Class clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), isLight ? darkModeFlag : 0, darkModeFlag);
                if (isLight) {//状态栏透明且黑色字体
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {//清除黑色字体
                    int flag = activity.getWindow().getDecorView().getSystemUiVisibility()
                            & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    window.getDecorView().setSystemUiVisibility(flag);
                }
                setTranslucentStatus(activity, true);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }
}
