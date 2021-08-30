package com.halotani.halotani.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.databinding.FragmentHomeBinding;
import com.halotani.halotani.ui.home.article.ArticleActivity;
import com.halotani.halotani.ui.home.article.ArticleAdapter;
import com.halotani.halotani.ui.home.article.ArticleViewModel;
import com.halotani.halotani.ui.home.consultation.ConsultationActivity;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArticleAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
        initViewModel();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // cek apakah user sudah login atau belum
        checkLoginUser();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ArticleActivity.class));
            }
        });

        binding.view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConsultationActivity.class));
            }
        });
    }

    private void checkLoginUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            binding.name.setText("" + documentSnapshot.get("name"));
                            binding.location.setText("" + documentSnapshot.get("location"));
                        }
                    });
        }
    }

    private void initRecyclerView() {
        adapter = new ArticleAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvArticle.setLayoutManager(layoutManager);
        binding.rvArticle.setHasFixedSize(true);
        binding.rvArticle.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan daftar artikel pilihan
        ArticleViewModel viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setFavoriteArticle(true);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}