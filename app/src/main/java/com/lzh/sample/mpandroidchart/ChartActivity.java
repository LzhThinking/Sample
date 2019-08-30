package com.lzh.sample.mpandroidchart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.lzh.sample.MainActivity;
import com.lzh.sample.R;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

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
    private Typeface mTfRegular;
    private Typeface mTfLight;
    protected BarChart mChart;
    private HorizontalBarChart hBarChart;
    private LineChart lineChart;
    private PieChart pieChart;
    private MyBarChartRenderer myBarChartRenderer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "DINCond-Regular.ttf");

        mChart = findViewById(R.id.chart1);
        hBarChart = findViewById(R.id.hBarChart);
        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.pieChart);

        initBarChart();
        initHBarChart();
        initLineChart();
        initPieChart();

//        mChart.setVisibility(View.GONE);
        hBarChart.setVisibility(View.GONE);
    }



    /**
     * 初始化柱形图控件属性
     */
    private void initBarChart() {
        mChart.setLogEnabled(true);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDragDecelerationEnabled(true);//拖动时手指抬起后是否还继续滑动
        mChart.setDragDecelerationFrictionCoef(0.5f);//设置滑动摩擦系数
//        mChart.setDrawBarShadow(true);


        Description description = new Description();
        description.setText("描述");
        description.setPosition(0, 0);
        mChart.setDescription(description);
        mChart.setNoDataText("没有数据");
        mChart.setNoDataTextColor(getResources().getColor(R.color.orange_FAD38D));
        mChart.getPaint(Chart.PAINT_DESCRIPTION).setColor(getResources().getColor(R.color.yellow_F79640));
        mChart.getPaint(Chart.PAINT_INFO).setColor(getResources().getColor(R.color.orange_FAD38D));
        myBarChartRenderer = new MyBarChartRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler());
        myBarChartRenderer.setRenderColor(getResources().getColor(R.color.color_5FA7FE));
        mChart.setRenderer(myBarChartRenderer);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        //自定义坐标轴适配器，配置在X轴，xAxis.setValueFormatter(xAxisFormatter);
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);//可以去掉，没什么用
        xAxis.setDrawAxisLine(false);
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
        leftAxis.setTypeface(mTfLight);//可以去掉，没什么用
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

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
        mChart.setMarker(mv);
        setBarChartData();
    }

    /**
     * 初始化水平柱形图图控件属性
     */
    private void initHBarChart() {
        hBarChart.setOnChartValueSelectedListener(this);
        hBarChart.setDrawBarShadow(false);
        hBarChart.setDrawValueAboveBar(true);
        hBarChart.getDescription().setEnabled(false);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        hBarChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        hBarChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        hBarChart.setDrawGridBackground(false);

        //自定义坐标轴适配器，设置在X轴
        DecimalFormatter formatter = new DecimalFormatter();
        XAxis xl = hBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(mTfLight);
        xl.setLabelRotationAngle(-45f);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(1f);
//        xl.setAxisMinimum(0);
        xl.setValueFormatter(formatter);

        //对Y轴进行设置
        YAxis yl = hBarChart.getAxisLeft();
        yl.setTypeface(mTfLight);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        hBarChart.getAxisRight().setEnabled(false);

        //图例设置
        Legend l = hBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        setHBarChartData();
        hBarChart.setFitBars(true);
        hBarChart.animateY(2500);
    }

    /**
     * 初始化折线图控件属性
     */
    private void initLineChart() {
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        lineChart.setBackgroundColor(Color.WHITE);

        //自定义适配器，适配于X轴
        IAxisValueFormatter xAxisFormatter = new XAxisValueFormatter();

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(xAxisFormatter);

        //自定义适配器，适配于Y轴
        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        lineChart.getAxisRight().setEnabled(true);

        setLineChartData();
    }

    private void initPieChart() {
        pieChart.setDrawCenterText(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setUsePercentValues(true);
        PieData pieData = new PieData();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        IPieDataSet pieDataSet = new PieDataSet(pieEntries, "第一个饼图");
        for (int i = 0; i < 10; i++) {
            pieEntries.add(new PieEntry(10));
            ((PieDataSet) pieDataSet).addColor(getResources().getColor(mColors[i%mColors.length]));
        }

        pieData.setDataSet(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        log("onValueSelected entry: " + e.toString() + "\t\t highlight:" + h.toString());
    }

    @Override
    public void onNothingSelected() {
        log("onNothingSelected");
    }

    private void setBarChartData() {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        //在这里设置自己的数据源,BarEntry 只接收float的参数，
        //图形横纵坐标默认为float形式，如果想展示文字形式，需要自定义适配器，
//        int dataCount = 500;
//        for (int i = 0; i < dataCount; i++) {
//            yVals1.add(new BarEntry(i, dataCount - i));
//        }
        yVals1.add(new BarEntry(0, 4));
        yVals1.add(new BarEntry(1, 2));
        yVals1.add(new BarEntry(2, 6));
        yVals1.add(new BarEntry(3, 1));


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
            set1.setBarBorderColor(getResources().getColor(R.color.green_4AC47E));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);//可以去掉，没什么用
            data.setBarWidth(1f);//设置主状图宽度，按百分比设置

            mChart.setData(data);
        }
    }

    /**
     * 设置水平柱形图数据的方法
     */
    private void setHBarChartData() {
        //填充数据，在这里换成自己的数据源
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(0, 4));
        yVals1.add(new BarEntry(1, 2));
        yVals1.add(new BarEntry(2, 6));
        yVals1.add(new BarEntry(3, 1));
        BarDataSet set1;

        if (hBarChart.getData() != null &&
                hBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) hBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            hBarChart.getData().notifyDataChanged();
            hBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "DataSet 1");

            set1.setDrawIcons(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);//可以去掉，没什么用
            data.setBarWidth(0.5f);

            hBarChart.setData(data);
        }
    }

    /**
     * 设置折线图的数据
     */
    private void setLineChartData() {
        //填充数据，在这里换成自己的数据源
        List<Entry> valsComp1 = new ArrayList<>();
        List<Entry> valsComp2 = new ArrayList<>();
        List<Entry> valsComp3 = new ArrayList<>();

        valsComp1.add(new Entry(1.5f, 4));
        valsComp1.add(new Entry(1.6f, 4));
        valsComp1.add(new Entry(2.1f, 4));
        valsComp1.add(new Entry(3.3f, 4));
        valsComp1.add(new Entry(4.9f, 4));
        valsComp1.add(new Entry(5.1f, 4));

        valsComp2.add(new Entry(0, 2));
        valsComp2.add(new Entry(1, 0));
        valsComp2.add(new Entry(2, 4));
        valsComp2.add(new Entry(3, 2));
        valsComp2.add(new Entry(4, 5));
        valsComp2.add(new Entry(5, 4));

        valsComp3.add(new Entry(0, 3));
        valsComp3.add(new Entry(1, 7));
        valsComp3.add(new Entry(2, 9));
        valsComp3.add(new Entry(3, 2));
        valsComp3.add(new Entry(4, 5));
        valsComp3.add(new Entry(5, 4));

        //这里，每重新new一个LineDataSet，相当于重新画一组折线
        //每一个LineDataSet相当于一组折线。比如:这里有两个LineDataSet：setComp1，setComp2。
        //则在图像上会有两条折线图，分别表示公司1 和 公司2 的情况.还可以设置更多
        LineDataSet setComp1 = new LineDataSet(valsComp1, "STEPPED");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setColor(getResources().getColor(R.color.blue_5AC7EC));
        setComp1.setDrawCircles(false);
        setComp1.setMode(LineDataSet.Mode.STEPPED);
        setComp1.setDrawFilled(true);
        for (int i = 0; i < valsComp1.size(); i++) {
            setComp1.addColor(mColors[i%mColors.length]);
        }

        LineDataSet setComp2 = new LineDataSet(valsComp2, "LINEAR");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setDrawCircles(true);
        setComp2.setColor(getResources().getColor(R.color.red_ec2700));
        setComp2.setMode(LineDataSet.Mode.LINEAR);

        LineDataSet setComp3 = new LineDataSet(valsComp2, "CUBIC_BEZIER");
        setComp3.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp3.setDrawCircles(true);
        setComp3.setColor(getResources().getColor(R.color.green_4AC47E));
        setComp3.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //注意：如果三条曲线的点数不一样，则X轴的标签个数等于点数最少曲线的点数
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setComp1);
//        dataSets.add(setComp2);
//        dataSets.add(setComp3);

        LineData lineData = new LineData(dataSets);

        lineChart.setRenderer(new StateLineChartRender(lineChart, lineChart.getAnimator(), lineChart.getViewPortHandler()));
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ChartActivity.class);
        context.startActivity(intent);
    }

    private void log(String msg) {
        Log.d("zhiheng", msg);
    }
}