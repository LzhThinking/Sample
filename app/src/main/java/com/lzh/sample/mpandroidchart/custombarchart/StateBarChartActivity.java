package com.lzh.sample.mpandroidchart.custombarchart;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lzh.sample.R;
import com.lzh.sample.mpandroidchart.XAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class StateBarChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};
    private String[] mParties = new String[]{
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    private int[] mColors = new int[] {
            R.color.cpb_blue, R.color.cpb_blue_dark, R.color.cpb_red, R.color.cpb_green, R.color.cpb_green_dark,
            R.color.green_4AC47E, R.color.progress_text, R.color.grey_CAC9C7, R.color.progress_circle_bg, R.color.cpb_green
    };

    BarChart mChart;
    LineChart mLineChart;
    StateBarChartRender mStateBarChartRender;
    StateMarkerView mMarkerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_bar_chart);

        mChart = findViewById(R.id.chart1);
        mLineChart = findViewById(R.id.line_chart);

        initLineChart();
        initBarChart();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Matrix matrix = new Matrix();
//                mChart.getViewPortHandler().getMatrixTouch().set(matrix);
//                mChart.getViewPortHandler().getMatrixTouch().postScale(1.0f, 1.0f);
//                mChart.invalidate();
                mChart.resetZoom();
                mChart.invalidate();
            }
        });
    }

    /**
     * 初始化折线图控件属性
     */
    private void initLineChart() {
        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setBackgroundColor(Color.WHITE);
        mLineChart.setLogEnabled(true);
        mLineChart.setOnChartValueSelectedListener(this);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setHighlightPerTapEnabled(true);
        mLineChart.setDragDecelerationEnabled(true);//拖动时手指抬起后是否还继续滑动
        mLineChart.setDragDecelerationFrictionCoef(0.5f);//设置滑动摩擦系数
        mLineChart.setDrawGridBackground(false);

        mLineChart.setPinchZoom(true);//如果要x/y分开缩放，需要设置为true
        mLineChart.setDragYEnabled(false);//禁止Y轴缩放
        mLineChart.setDrawGridBackground(false);

        //设置图表四周偏移量为0.偏移量由extraOffsets和minOffset决定定，取两者中的较大值为偏移量
        mLineChart.setExtraOffsets(0, 0, 0, 50);//bottom设置的是x轴label与chart底部的距离
        mLineChart.setMinOffset(0);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setEnabled(true);//绘制x轴上的值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        //label在X轴上的偏移量
        xAxis.setXOffset(30);
        //label在y轴上的偏移量。注意：这个可以和setExtraOffsets配合使用来控制label在Y轴方向上的位置和高度。也就是x轴label和图标内容的距离
        xAxis.setYOffset(50);
        xAxis.setAvoidFirstLastClipping(true);
        //label在y轴上的偏移量。注意：这个可以和setExtraOffsets配合使用来控制label在Y轴方向上的位置和高度
//        xAxis.setYOffset(20);
        //自定义坐标轴适配器，配置在X轴，xAxis.setValueFormatter(xAxisFormatter);
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();
        xAxis.setValueFormatter(xAxisFormatter);

        //设置限制临界线
        LimitLine limitLineTop = new LimitLine(1, "浸水");
        limitLineTop.enableDashedLine(10, 10, 0);
        limitLineTop.setLineColor(getResources().getColor(R.color.color_dddddd));
        limitLineTop.setLineWidth(2f);
        limitLineTop.setTextColor(getResources().getColor(R.color.item_sub_title_text_color));
        limitLineTop.setTextSize(10);
        limitLineTop.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLineTop.setXOffset(10);
        limitLineTop.setYOffset(6);

        //设置限制临界线
        LimitLine limitLineBottom = new LimitLine(0, "正常");
        limitLineBottom.setLineColor(getResources().getColor(R.color.color_dddddd));
        limitLineBottom.setLineWidth(2f);
        limitLineBottom.setTextColor(getResources().getColor(R.color.item_sub_title_text_color));
        limitLineBottom.setTextSize(10);
        limitLineBottom.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        limitLineBottom.setXOffset(10);
        limitLineBottom.setYOffset(6);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(StateChartConstants.MIN_YAXIS_VALUE);//Y轴上最小值设置为-0.0005f,数据的最小值为0。这样设置可以避免底部直线显示不全的问题。
        leftAxis.setAxisMaximum(StateChartConstants.MAX_YAXIS_VALUE);//Y轴最上最大值是1.2，数据的最大值是1，可以通过这里设置内容区域距离顶部的位置
        leftAxis.setEnabled(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.addLimitLine(limitLineTop);
        leftAxis.addLimitLine(limitLineBottom);
        leftAxis.setDrawLimitLinesBehindData(true);
        mLineChart.getAxisRight().setEnabled(false);

        Legend legend = mLineChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormLineWidth(0);
        legend.setTextColor(getResources().getColor(R.color.transparent));
        legend.setDrawInside(true);
        legend.setEnabled(false);

        mLineChart.setPinchZoom(true);//如果要x/y分开缩放，需要设置为true
        mLineChart.setDragYEnabled(false);//禁止Y轴缩放
        mLineChart.setDragXEnabled(true);
        mLineChart.setScaleEnabled(false);
        mLineChart.setScaleXEnabled(true);
        mLineChart.setScaleYEnabled(false);

        mLineChart.setHighlighter(new StateChartHighlighter(mLineChart));
        mLineChart.setRenderer(new StateLineChartRender(mLineChart, mLineChart.getAnimator(), mLineChart.getViewPortHandler()));

        //如果点击柱形图，会弹出pop提示框.XYMarkerView为自定义弹出框
        mMarkerView = new StateMarkerView(this, xAxisFormatter);
        mMarkerView.setChartView(mLineChart);
        mLineChart.setMarker(mMarkerView);

        setLineChartData();
    }

    /**
     * 设置折线图的数据
     */
    private void setLineChartData() {
        //填充数据，在这里换成自己的数据源
        List<Entry> valsComp1 = new ArrayList<>();

        valsComp1.add(new Entry(2f, 0));
        valsComp1.add(new Entry(3f, 1));
        valsComp1.add(new Entry(4f, 0));
        valsComp1.add(new Entry(5f, 1));
        valsComp1.add(new Entry(6f, 0));
        valsComp1.add(new Entry(7f, 1));
        valsComp1.add(new Entry(8f, 0));
        valsComp1.add(new Entry(10f,1));
        valsComp1.add(new Entry(11f, 1));

        //这里，每重新new一个LineDataSet，相当于重新画一组折线
        //每一个LineDataSet相当于一组折线。比如:这里有两个LineDataSet：setComp1，setComp2。
        //则在图像上会有两条折线图，分别表示公司1 和 公司2 的情况.还可以设置更多
        LineDataSet setComp1 = new LineDataSet(valsComp1, "");
        setComp1.setDrawHorizontalHighlightIndicator(false);
        setComp1.setLabel("");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setMode(LineDataSet.Mode.STEPPED);
        setComp1.setColor(getResources().getColor(R.color.color_6C98FD));
        setComp1.setDrawCircles(false);
        setComp1.setDrawFilled(false);
        setComp1.setDrawValues(false);
        setComp1.setDrawIcons(false);
        setComp1.setLineWidth(2);
//        for (int i = 0; i < valsComp1.size(); i++) {
//            setComp1.addColor(mColors[i%mColors.length]);
//        }


        //注意：如果三条曲线的点数不一样，则X轴的标签个数等于点数最少曲线的点数
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);

        LineData lineData = new LineData(dataSets);
        mLineChart.setData(lineData);
        mLineChart.invalidate();
    }

    /**
     * 初始化柱形图控件属性
     */
    private void initBarChart() {
        mChart.setLogEnabled(true);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.getDescription().setEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDragDecelerationEnabled(true);//拖动时手指抬起后是否还继续滑动
        mChart.setDragDecelerationFrictionCoef(0.5f);//设置滑动摩擦系数

        //设置图标描述
        Description description = new Description();
        description.setText("描述");
        description.setPosition(0, 0);
        mChart.setDescription(description);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);//如果要x/y分开缩放，需要设置为true
        mChart.setDragYEnabled(false);//禁止Y轴缩放
        mChart.setDrawGridBackground(false);

        //只允许X轴缩放
        mChart.setScaleEnabled(true);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false);

        //设置自定义高亮样式
        mChart.setHighlighter(new StateChartHighlighter(mChart));

        //设置没有数据的提示
        mChart.setNoDataText("");

        //设置自定义的图标渲染器
        mStateBarChartRender = new StateBarChartRender(mChart, mChart.getAnimator(), mChart.getViewPortHandler());
        mChart.setRenderer(mStateBarChartRender);

        //设置图表四周偏移量为0.偏移量由extraOffsets和minOffset决定定，取两者中的较大值为偏移量
        mChart.setExtraOffsets(0, 0, 0, 20);
        mChart.setMinOffset(0);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(true);//绘制x轴上的值
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(false);
        //label在X轴上的偏移量
        xAxis.setXOffset(30);
        //label在y轴上的偏移量。注意：这个可以和setExtraOffsets配合使用来控制label在Y轴方向上的位置和高度
        xAxis.setYOffset(20);

        //自定义坐标轴适配器，配置在X轴，xAxis.setValueFormatter(xAxisFormatter);
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();
        //设置X轴文案转换器
        xAxis.setValueFormatter(xAxisFormatter);


        //设置限制临界线
        LimitLine limitLineTop = new LimitLine(1, "");
        limitLineTop.setLineColor(getResources().getColor(R.color.color_dddddd));
        limitLineTop.setLineWidth(0.5f);

        //设置限制临界线
        LimitLine limitLineBottom = new LimitLine(0, "");
        limitLineBottom.setLineColor(getResources().getColor(R.color.color_dddddd));
        limitLineBottom.setLineWidth(0.5f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisMinimum(StateChartConstants.MIN_ENTRY_VALUE);//Y轴上最小值设置为-0.0005f,数据的最小值为0。这样设置可以避免底部直线显示不全的问题。
        leftAxis.setAxisMaximum(StateChartConstants.MAX_ENTRY_VALUE);//Y轴最上最大值是1.2，数据的最大值是1，可以通过这里设置内容区域距离顶部的位置
        leftAxis.setEnabled(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.addLimitLine(limitLineTop);
        leftAxis.addLimitLine(limitLineBottom);
        leftAxis.setDrawLimitLinesBehindData(false);
        mChart.getAxisRight().setEnabled(false);

        //图例设置--图例就是图片描述或标题
        List<LegendEntry> entries = new ArrayList<>();
        entries.add(new LegendEntry("正常", Legend.LegendForm.CIRCLE, 8f, 0f, null, getResources().getColor(R.color.color_EBF2FF)));
        entries.add(new LegendEntry("浸水", Legend.LegendForm.CIRCLE, 8f, 0f, null, getResources().getColor(R.color.color_6C98FD)));
        entries.add(new LegendEntry("离线", Legend.LegendForm.CIRCLE, 8f, 0f, null, getResources().getColor(R.color.color_89909B)));
        Legend l  = mChart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setTextSize(14f);
        l.setTextColor(getResources().getColor(R.color.item_title_text_color));
        l.setXEntrySpace(20f); // set the space between the legend entries on the x-axis
        l.setYOffset(5f);//设置图例垂直方向的偏移量（图例与图表内容的距离）
        l.setXOffset(15f);//设置图例水平方向的偏移量（与左侧Y轴的距离）
        l.setCustom(entries);//设置多个自定义图例标题

        //如果点击柱形图，会弹出pop提示框.XYMarkerView为自定义弹出框
        mMarkerView = new StateMarkerView(this, xAxisFormatter);
        mMarkerView.setChartView(mChart);
        mChart.setMarker(mMarkerView);
        setBarChartData();
    }


    private void setBarChartData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        //在这里设置自己的数据源,BarEntry 只接收float的参数，
        //图形横纵坐标默认为float形式，如果想展示文字形式，需要自定义适配器，
//        int dataCount = 500;
//        for (int i = 0; i < dataCount; i++) {
//            yVals1.add(new BarEntry(i, dataCount - i));
//        }
        float valuse[] = new float[] {2};
        yVals1.add(new BarEntry(0, valuse));
        yVals1.add(new BarEntry(1, valuse));
        yVals1.add(new BarEntry(3, valuse));
        yVals1.add(new BarEntry(5, valuse));
        yVals1.add(new BarEntry(7, valuse));
        yVals1.add(new BarEntry(9, valuse));
//        yVals1.add(new BarEntry(7.9f, 4));
//        yVals1.add(new BarEntry(18, 4));
//        yVals1.add(new BarEntry(19, 4));


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "The year 2017");
            set1.setLabel("");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);//不绘制每个柱子的值（不是不绘制X轴上的值）
            set1.setHighLightColor(getResources().getColor(R.color.dcdcdc));//设置highlight的颜色
            for (int i = 0; i < yVals1.size(); i++) {
                set1.addColor(getResources().getColor(mColors[i % mColors.length]));
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);//可以去掉，没什么用
            //data.setBarWidth(1f);//设置主状图宽度，按百分比设置

            mChart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        log("onValueSelected entry: " + e.toString() + "\t\t highlight:" + h.toString() + ", mXPx = " + h.getXPx());
        mMarkerView.setHighlight(h);
    }

    @Override
    public void onNothingSelected() {
        log("onNothingSelected");
    }

    private void log(String msg) {
        Log.d("zhiheng", msg);
    }
}
