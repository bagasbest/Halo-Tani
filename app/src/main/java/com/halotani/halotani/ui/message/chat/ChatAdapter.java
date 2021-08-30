package com.halotani.halotani.ui.message.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halotani.halotani.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private final ArrayList<ChatModel> chatList = new ArrayList<>();
    public void setData(ArrayList<ChatModel> items) {
        chatList.clear();
        chatList.addAll(items);
        notifyDataSetChanged();
    }

    private final String uid;
    public ChatAdapter(String uid) {
        this.uid = uid;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(chatList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed user
        if(chatList.get(position).getUid().equals(uid)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageText;
        TextView message, time;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageTv);
            time = itemView.findViewById(R.id.timeTv);
            imageText = itemView.findViewById(R.id.imageText);
        }


        public void bind(ChatModel model) {
            if(model.isText()) {
                message.setVisibility(View.GONE);
                imageText.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(model.getMessage())
                        .into(imageText);
            } else {
                imageText.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                message.setText(model.getMessage());
            }
            time.setText(model.getTime());
        }
    }
}
