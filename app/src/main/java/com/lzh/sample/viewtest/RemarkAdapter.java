package com.lzh.sample.viewtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lzh.sample.R;

import java.util.List;

public class RemarkAdapter extends RecyclerView.Adapter<RemarkAdapter.ViewHolder> {

    List<String> mNames;
    public RemarkAdapter(List<String> names) {
        this.mNames = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_name_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(mNames.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return mNames == null ? 0 : mNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
