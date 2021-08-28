package com.halotani.halotani.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.halotani.halotani.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final ArrayList<HistoryModel> listHistory = new ArrayList<>();
    public void setData(ArrayList<HistoryModel> items) {
        listHistory.clear();
        listHistory.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView message, date;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(HistoryModel historyModel) {



            message.setText(historyModel.getMessage());
            date.setText(historyModel.getDate());

        }
    }
}
