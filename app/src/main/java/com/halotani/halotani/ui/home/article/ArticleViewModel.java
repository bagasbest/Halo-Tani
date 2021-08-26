package com.halotani.halotani.ui.home.article;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ArticleViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ArticleModel>> listArticle = new MutableLiveData<>();
    final ArrayList<ArticleModel> articleModelArrayList = new ArrayList<>();

    private static final String TAG = ArticleViewModel.class.getSimpleName();

    public void setArticleList() {
        articleModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("article")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                ArticleModel model = new ArticleModel();

                                model.setTitle("" + document.get("title"));
                                model.setDescription("" + document.get("description"));
                                model.setDp("" + document.get("dp"));
                                model.setFavorite(document.getBoolean("isFavorite"));
                                model.setDateAdded("" + document.get("dateAdded"));
                                model.setDateUpdated("" + document.get("dateUpdated"));
                                model.setUid("" + document.get("uid"));


                                articleModelArrayList.add(model);
                            }
                            listArticle.postValue(articleModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setFavoriteArticle(boolean isFavorite) {
        articleModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("article")
                    .whereEqualTo("isFavorite", isFavorite)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                ArticleModel model = new ArticleModel();

                                model.setTitle("" + document.get("title"));
                                model.setDescription("" + document.get("description"));
                                model.setDp("" + document.get("dp"));
                                model.setFavorite(document.getBoolean("isFavorite"));
                                model.setDateAdded("" + document.get("dateAdded"));
                                model.setDateUpdated("" + document.get("dateUpdated"));
                                model.setUid("" + document.get("uid"));


                                articleModelArrayList.add(model);
                            }
                            listArticle.postValue(articleModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<ArticleModel>> getArticleList() {
        return listArticle;
    }


}
