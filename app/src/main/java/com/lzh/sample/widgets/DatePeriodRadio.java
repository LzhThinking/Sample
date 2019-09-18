package com.lzh.sample.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.utils.Utils;
import com.lzh.sample.R;
import com.lzh.sample.Utils.BitmapUtils;

import java.util.List;

import static com.github.mikephil.charting.utils.Utils.convertDpToPixel;

/**
 * @Author leizhiheng
 * @CreateDate 2019/9/10
 * @Description
 */
public class DatePeriodRadio extends LinearLayout {

    private int radioMargin;
    private int radioCount;
    private int radioWidth;
    private int radioHeight;
    private Drawable radioBackground;
    private Drawable radioSelectedDrawable;
    private CharSequence[] radioTexts;
    private int selectedColor;
    private int radioTextColor;

    private OnRadioSelectedListener listener;
    private int selectedIndex = -1;

    public DatePeriodRadio(Context context) {
        super(context);
    }

    public DatePeriodRadio(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DatePeriodRadio);
        radioCount = array.getInteger(R.styleable.DatePeriodRadio_radio_count, 0);
        radioBackground = array.getDrawable(R.styleable.DatePeriodRadio_radio_background);
        radioSelectedDrawable = array.getDrawable(R.styleable.DatePeriodRadio_radio_background);
        radioSelectedDrawable = BitmapUtils.tintDrawable(radioSelectedDrawable, ColorStateList.valueOf(selectedColor));//修改为选中后的颜色
        radioMargin = (int) array.getDimension(R.styleable.DatePeriodRadio_radio_margin, 0);
        radioTexts = array.getTextArray(R.styleable.DatePeriodRadio_radio_texts);
        radioWidth = (int) array.getDimension(R.styleable.DatePeriodRadio_radio_width, context.getResources().getDimension(R.dimen.px50));
        radioHeight = (int) array.getDimension(R.styleable.DatePeriodRadio_radio_height, context.getResources().getDimension(R.dimen.px26));
        selectedColor = array.getColor(R.styleable.DatePeriodRadio_radio_selected_color, context.getResources().getColor(R.color.white));
        radioTextColor = array.getColor(R.styleable.DatePeriodRadio_radio_text_color, context.getResources().getColor(R.color.color_666666));
        addItems(context);

        setOrientation(HORIZONTAL);
    }

    private void addItems(Context context) {
        if (radioCount == 0 || radioTexts == null || radioTexts.length == 0) {
            return;
        }
        for(int i = 0; i < radioCount; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(radioWidth, radioHeight);
            if (radioCount > 1 && i < radioCount -1) {
                params.rightMargin = radioMargin;
            }
            TextView textView = new TextView(context);
            textView.setText(radioTexts[i % radioTexts.length]);
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(radioBackground);

            textView.setOnClickListener(clickListener);
            textView.setTag(i);

            addView(textView, params);
        }
    }

    private OnClickListener clickListener = v -> {
        int index = (int) v.getTag();
        if (selectedIndex == index) {
            return;
        }

        TextView textView;
        if (selectedIndex != -1) {
            textView = (TextView) getChildAt(selectedIndex);
            textView.setBackground(radioBackground);
            textView.setText(radioTextColor);
        }

        textView = (TextView) v;
        textView.setBackground(radioSelectedDrawable);
        textView.setTextColor(getContext().getResources().getColor(R.color.white));
        selectedIndex = index;

        if (listener != null) listener.onRadioSelected(selectedIndex);
    };

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (this.selectedIndex == selectedIndex) {
            return;
        }

        if (this.selectedIndex != -1) {
            getChildAt(this.selectedIndex).setBackground(radioBackground);
        }

        getChildAt(selectedIndex).setBackground(radioSelectedDrawable);
        this.selectedIndex = selectedIndex;
    }

    public void setOnRadioSelectedListener(OnRadioSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnRadioSelectedListener {
        void onRadioSelected(int index);
    }
}
