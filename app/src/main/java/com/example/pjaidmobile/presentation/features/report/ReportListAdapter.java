package com.example.pjaidmobile.presentation.features.report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pjaidmobile.R;
import com.example.pjaidmobile.data.model.ReportItem;

public class ReportListAdapter extends ListAdapter<ReportItem, ReportListAdapter.ReportViewHolder> {

    protected ReportListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportItem item = getItem(position);
        holder.description.setText(item.getTitle());
        holder.date.setText(item.getDate());
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView description, date;

        ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.textViewDescription);
            date = itemView.findViewById(R.id.textViewDate);
        }
    }

    private static final DiffUtil.ItemCallback<ReportItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ReportItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ReportItem oldItem, @NonNull ReportItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReportItem oldItem, @NonNull ReportItem newItem) {
            return oldItem.equals(newItem);
        }
    };
}