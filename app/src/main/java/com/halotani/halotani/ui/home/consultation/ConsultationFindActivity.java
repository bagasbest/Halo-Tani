package com.halotani.halotani.ui.home.consultation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityConsultationFindBinding;

public class ConsultationFindActivity extends AppCompatActivity {

    private ActivityConsultationFindBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}