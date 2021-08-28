package com.halotani.halotani.ui.message;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halotani.halotani.R;
import com.halotani.halotani.ui.message.chat.ChatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final ArrayList<MessageModel> messageList = new ArrayList<>();
    public void setData(ArrayList<MessageModel> items) {
        messageList.clear();
        messageList.addAll(items);
        notifyDataSetChanged();
    }

    String currentUid;
    public MessageAdapter(String currentUid) {
        this.currentUid = currentUid;
    }

    @NonNull
    @NotNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapter.ViewHolder holder, int position) {
        holder.bind(messageList.get(position), currentUid);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private View bg;
        private ImageView dp;
        private TextView name, keahlian, status;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            dp = itemView.findViewById(R.id.dp);
            name = itemView.findViewById(R.id.name);
            keahlian = itemView.findViewById(R.id.sertifikatKeahlian);
            status = itemView.findViewById(R.id.status);
            bg = itemView.findViewById(R.id.bg);
        }

        public void bind(MessageModel model, String currentUid) {
            if(model.getCustomerUid().equals(currentUid)) {
                Glide.with(itemView.getContext())
                        .load(model.getDoctorDp())
                        .error(R.drawable.ic_baseline_person_24)
                        .into(dp);
                name.setText(model.getDoctorName());
                keahlian.setText(model.getKeahlian());
            }
            else  {
                Glide.with(itemView.getContext())
                        .load(model.getCustomerDp())
                        .error(R.drawable.ic_baseline_person_24)
                        .into(dp);
                name.setText(model.getCustomerName());
                keahlian.setVisibility(View.GONE);
            }

            status.setText(model.getStatus());

            if(model.getStatus().equals("Sedang Konsultasi")) {
                bg.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.progress));
            }
            else {
                bg.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.rounded_bg));
            }

            cv.setOnClickListener(view -> {
                if(model.getStatus().equals("Sedang Konsultasi")) {
                    Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                    intent.putExtra(ChatActivity.EXTRA_CONSULTATION, model);
                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }
}
