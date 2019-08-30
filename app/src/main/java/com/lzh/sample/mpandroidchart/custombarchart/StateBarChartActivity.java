package com.lzh.sample.mpandroidchart.custombarchart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lzh.sample.R;
import com.lzh.sample.mpandroidchart.MyAxisValueFormatter;
import com.lzh.sample.mpandroidchart.MyBarChartRenderer;
import com.lzh.sample.mpandroidchart.XAxisValueFormatter;
import com.lzh.sample.mpandroidchart.XYMarkerView;

import java.util.ArrayList;

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
    StateBarChartRender mStateBarChartRender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_bar_chart);

        mChart = findViewById(R.id.chart1);

        initBarChart();
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

        //设置高亮格式
        mChart.setHighlighter(new StateChartHighlighter(mChart));

        //设置图标描述
        Description description = new Description();
        description.setText("描述");
        description.setPosition(0, 0);
        mChart.setDescription(description);

        //设置没有数据的提示
        mChart.setNoDataText("没有数据");
        mChart.setNoDataTextColor(getResources().getColor(R.color.orange_FAD38D));
        mChart.getPaint(Chart.PAINT_DESCRIPTION).setColor(getResources().getColor(R.color.yellow_F79640));
        mChart.getPaint(Chart.PAINT_INFO).setColor(getResources().getColor(R.color.orange_FAD38D));


        mStateBarChartRender = new StateBarChartRender(mChart, mChart.getAnimator(), mChart.getViewPortHandler());
        mChart.setRenderer(mStateBarChartRender);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);//如果要x/y分开缩放，需要设置为true
        mChart.setDragYEnabled(false);//禁止Y轴缩放

        mChart.setDrawGridBackground(false);

//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        //自定义坐标轴适配器，配置在X轴，xAxis.setValueFormatter(xAxisFormatter);
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);//可以去掉，没什么用
        xAxis.setDrawAxisLine(true);

        xAxis.setGranularity(0f);
        //设置X轴文案转换器
        //xAxis.setValueFormatter(xAxisFormatter);


        //自定义坐标轴适配器，配置在Y轴。leftAxis.setValueFormatter(custom);
        IAxisValueFormatter custom = new MyAxisValueFormatter();

        //设置限制临界线
        LimitLine limitLine = new LimitLine(3f, "临界点");
        limitLine.setLineColor(Color.GREEN);
        limitLine.setLineWidth(0.2f);
        limitLine.setTextColor(Color.GREEN);


        //获取到图形左边的Y轴
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.addLimitLine(limitLine);//Y轴临界线，最多可以设置6条
        //leftAxis.setTypeface(mTfLight);//可以去掉，没什么用
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(0f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);//不显示Y轴
        //获取到图形右边的Y轴，并设置为不显示
        mChart.getAxisRight().setEnabled(false);

        //图例设置--图例就是图片描述或标题
        Legend legend = mChart.getLegend();
        //设置label的位置和方向，下面三个配合使用
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setForm(Legend.LegendForm.SQUARE);//数据集label前面的图形形状
        legend.setFormSize(9f);
        legend.setTextSize(11f);
        legend.setXEntrySpace(0);

        //如果点击柱形图，会弹出pop提示框.XYMarkerView为自定义弹出框
        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart);
//        mChart.setMarker(mv);
        mChart.setMarker(null);
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
        yVals1.add(new BarEntry(1, 4));
        yVals1.add(new BarEntry(3, 4));
        yVals1.add(new BarEntry(5, 4));
        yVals1.add(new BarEntry(7, 4));
        yVals1.add(new BarEntry(9, 4));
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
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setBarBorderColor(getResources().getColor(R.color.green_4AC47E));
            for (int i = 0; i < yVals1.size(); i++) {
                set1.addColor(getResources().getColor(mColors[i % mColors.length]));
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);//可以去掉，没什么用
            data.setBarWidth(1f);//设置主状图宽度，按百分比设置

            mChart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        log("onValueSelected entry: " + e.toString() + "\t\t highlight:" + h.toString());
    }

    @Override
    public void onNothingSelected() {
        log("onNothingSelected");
    }

    private void log(String msg) {
        Log.d("zhiheng", msg);
    }
}
