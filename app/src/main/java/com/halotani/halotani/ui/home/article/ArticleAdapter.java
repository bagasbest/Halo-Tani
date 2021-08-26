package com.halotani.halotani.ui.home.article;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.halotani.halotani.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {


    private final ArrayList<ArticleModel> articleList = new ArrayList<>();
    public void setData(ArrayList<ArticleModel> items) {
        articleList.clear();
        articleList.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(articleList.get(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout articleCl;
        ImageView dp;
        TextView title, description;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            articleCl = itemView.findViewById(R.id.articleCl);
            dp = itemView.findViewById(R.id.dp);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
        }

        public void bind(ArticleModel articleModel) {
            Glide.with(itemView.getContext())
                    .load(articleModel.getDp())
                    .into(dp);

            title.setText(articleModel.getTitle());
            description.setText(articleModel.getDescription());

            articleCl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), ArticleDetailActivity.class);
                    intent.putExtra(ArticleDetailActivity.EXTRA_ARTICLE, articleModel);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
