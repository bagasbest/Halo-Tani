package com.halotani.halotani.ui.home.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.databinding.ActivityArticleBinding;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;
    private ArticleAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        initRecylerView();
        initViewModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // cek apakah role == user / role == admin
        checkRole();

        // KLIK TOMBOL TAMBAH ARTIEL KHUSUS ADMIN
        binding.fabAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ArticleActivity.this, ArticleAddActivity.class));
            }
        });

        // KEMBALI KE HOMEPAGE
        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initRecylerView() {
        binding.rvArticle.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleAdapter();
        binding.rvArticle.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan daftar artikel di halaman artikel terkait pertanian
        ArticleViewModel viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setArticleList();
        viewModel.getArticleList().observe(this, article -> {
            if (article.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(article);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    private void checkRole() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (("" + documentSnapshot.get("role")).equals("admin")) {
                                binding.fabAddArticle.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}