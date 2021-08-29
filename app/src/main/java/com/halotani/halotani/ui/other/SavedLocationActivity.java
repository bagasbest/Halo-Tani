package com.halotani.halotani.ui.other;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.databinding.ActivitySavedLocationBinding;

public class SavedLocationActivity extends AppCompatActivity {

    private ActivitySavedLocationBinding binding;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        // AMBIL LOKASI
        getLoc();


        // SIMPAN LOKASI
        setLoc();

        // kembali ke halaman sebelumnya
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setLoc() {
        binding.saveBtn.setOnClickListener(view -> {
            String location = binding.locationEt.getText().toString().trim();

            if(location.isEmpty()) {
                binding.locationEt.setError("Lokasi tidak boleh kosong");
                return;
            }

            // SIMPAN LOKASI KE DATABASE
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(uid)
                    .update("location", location)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(SavedLocationActivity.this, "Sukses menyimpan lokasi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SavedLocationActivity.this, "Gagal menyimpan lokasi", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @SuppressLint("SetTextI18n")
    private void getLoc() {
        // SIMPAN LOKASI KE DATABASE
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    binding.locationEt.setText("" + documentSnapshot.get("location"));
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}