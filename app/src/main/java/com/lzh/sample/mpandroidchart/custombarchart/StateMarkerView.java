package com.lzh.sample.mpandroidchart.custombarchart;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.lzh.sample.R;
import com.lzh.sample.Utils.DensityUtils;
import com.lzh.sample.mpandroidchart.BaseApplication;

import java.text.DecimalFormat;

public class StateMarkerView extends MarkerView {
    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;
    private DecimalFormat format;
    //用于控制MarkerView的偏移量
    private Highlight highlight;

    public StateMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("###.000");
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        StringBuilder builder = new StringBuilder();
        builder.append("开始时间：14:20:02").append("\n");
        builder.append("结束时间：15:30:02").append("\n");
        builder.append("持续时间：1时10分0秒");
        tvContent.setText(builder.toString());
        super.refreshContent(e, highlight);
    }

    /**
     * 控制MarkView相对于HighLight的位置
     * @return
     */
    @Override
    public MPPointF getOffset() {
//        if (highlight == null) {
            return new MPPointF(-(getWidth() + (Utils.convertDpToPixel(1f)) + 1), 2);
//        } else {
//            return new MPPointF(highlight.getXPx() > getWidth() ? -(getWidth()) : 1, -getHeight());
//        }
    }

//    @Override
//    public void draw(Canvas canvas, float posX, float posY) {
//        MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);
//
//        int saveId = canvas.save();
//        // translate to the correct position and draw
//        canvas.translate(posX + offset.x, posY + offset.y);
//        draw(canvas);
//        canvas.restoreToCount(saveId);
//    }

    public void setHighlight(Highlight highlight) {
        this.highlight = highlight;
    }
}
