package com.lzh.sample.mpandroidchart;

import com.github.mikephil.charting.utils.Utils;
import com.lzh.sample.R;
import com.lzh.sample.Utils.FormatUtils;

/**
 * @Author leizhiheng
 * @CreateDate 2019/9/4
 * @Description
 */
public class StateChartConstants {
    public static final float MAX_ENTRY_VALUE = 1.0f;//数据的最大值
    public static final float MIN_ENTRY_VALUE = 0.0f;//数据的最小值
    public static final float MAX_YAXIS_VALUE = 1.2f;//Y轴最大值
    public static final float MIN_YAXIS_VALUE = -0.005f;//Y轴最小值

    public static final int DEVICE_STATE_NORMAL = 0;//设备正常状态
    public static final int DEVICE_STATE_TRIGGERED = 1;//设备触发状态
    public static final int DEVICE_STATE_OFFLINE = 2;//设备离线状态
    public static final int DEVICE_STATE_ONLINE = 3;//设备在线状态

    public static final float HIGHLIGHT_LINE_WIDTH_DP  = 1f;//HighLight的线条宽度

    public static final int LINE_WIDTH_DP = 2;



    public static final long GRANULARITY_DAY_4_HOUR = 4 * 60 * 60 * 1000;//日数据标签间隔时间
    public static final long GRANULARITY_WEEK_1_DAY = 24 * 60 * 60 * 1000;//周数据标签间隔时间

    public static final long SCALE_MAX_OF_DAY = 10000/*32 - 1*/;//当显示的是日数据时X轴可放大的最大倍数：默认粒度是4小时，放大16倍之后粒度是15分钟，之后可以继续放大，但不可以让粒度变为7.5分钟

    public static final long ZERO_TIME_OF_LAST_YEAR = FormatUtils.getBeforeYearsZeroTime(0) - 8 * 30 * 24 * 60 * 60 * 1000;

    public enum DateMode {
        DAY, WEEK, MONTH, YEAR
    }
}
