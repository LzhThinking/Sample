package com.lzh.sample.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lzh.sample.R;
import com.lzh.sample.entity.IndicatroItemBean;

import org.jetbrains.annotations.NotNull;

import me.drakeet.multitype.ItemViewBinder;

public class IndicatorItemViewBinder extends ItemViewBinder<IndicatroItemBean, IndicatorItemViewBinder.ViewHolder> {

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull LayoutInflater layoutInflater, @NotNull ViewGroup viewGroup) {
        View view  = layoutInflater.inflate(R.layout.layout_indicator_item, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, IndicatroItemBean indicatroItemBean) {
        viewHolder.tvTitle.setText(indicatroItemBean.title);
        viewHolder.tvContent.setText(indicatroItemBean.content);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.text_title);
            tvContent = itemView.findViewById(R.id.text_content);
        }
    }
}
