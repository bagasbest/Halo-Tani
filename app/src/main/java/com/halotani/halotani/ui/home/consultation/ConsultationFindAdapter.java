package com.halotani.halotani.ui.home.consultation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halotani.halotani.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConsultationFindAdapter extends RecyclerView.Adapter<ConsultationFindAdapter.ViewHolder> {

    private final ArrayList<ConsultationFindModel> listExpert = new ArrayList<>();
    public void setData(ArrayList<ConsultationFindModel> items) {
        listExpert.clear();
        listExpert.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(listExpert.get(position));
    }

    @Override
    public int getItemCount() {
        return listExpert.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        CardView cv;
        ImageView dp;
        TextView name, keahlian, experience, like;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            dp = itemView.findViewById(R.id.dp);
            name = itemView.findViewById(R.id.name);
            keahlian = itemView.findViewById(R.id.sertifikatKeahlian);
            experience = itemView.findViewById(R.id.pengalaman);
            like = itemView.findViewById(R.id.like);
        }

        @SuppressLint("SetTextI18n")
        public void bind(ConsultationFindModel model) {


            Glide.with(itemView.getContext())
                    .load(model.getDp())
                    .into(dp);

            like.setText(model.getLike() + " Orang merekomendasikan dia");
            name.setText(model.getName());
            keahlian.setText(model.getKeahlian());
            experience.setText("Bekerja selama " + model.getExperience() + " Tahun");

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), ConsultationDetailActivity.class);
                    intent.putExtra(ConsultationDetailActivity.EXTRA_EXPERT, model);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}
