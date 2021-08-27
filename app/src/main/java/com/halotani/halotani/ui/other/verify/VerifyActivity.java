package com.halotani.halotani.ui.other.verify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityVerifyBinding;
import com.halotani.halotani.ui.home.article.ArticleAdapter;
import com.halotani.halotani.ui.home.article.ArticleViewModel;

public class VerifyActivity extends AppCompatActivity {

    private ActivityVerifyBinding binding;
    private VerifyAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        binding.verifyRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VerifyAdapter();
        binding.verifyRv.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan calon penyuluh pertanian
        VerifyViewModel viewModel = new ViewModelProvider(this).get(VerifyViewModel.class);


        binding.progressBar.setVisibility(View.VISIBLE);
        viewModel.setExpertList();
        viewModel.getExpertList().observe(this, expert -> {
            if (expert.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(expert);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}