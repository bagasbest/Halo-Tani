package com.halotani.halotani.ui.home.consultation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.halotani.halotani.LoginActivity;
import com.halotani.halotani.databinding.ActivityConsultationBinding;

public class ConsultationActivity extends AppCompatActivity {


    private ActivityConsultationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.consultWithDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserLogin();
            }
        });
    }

    private void checkUserLogin() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(ConsultationActivity.this, ConsultationFindActivity.class));
        }
        else {
            startActivity(new Intent(ConsultationActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}