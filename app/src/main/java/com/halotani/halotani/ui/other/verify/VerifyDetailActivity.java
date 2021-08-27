package com.halotani.halotani.ui.other.verify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityVerifyBinding;
import com.halotani.halotani.databinding.ActivityVerifyDetailBinding;
import com.halotani.halotani.ui.home.consultation.ConsultationFindModel;

import org.jetbrains.annotations.NotNull;

public class VerifyDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EXPERT = "expert";
    private ConsultationFindModel model;
    private ActivityVerifyDetailBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerifyDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_EXPERT);
        Glide.with(this)
                .load(model.getDp())
                .into(binding.dp);

        Glide.with(this)
                .load(model.getCertificate())
                .into(binding.specialist);

        binding.name.setText("Nama: " + model.getName());
        binding.sertifikatKeahlian.setText("Keahlian: " + model.getKeahlian());
        binding.pengalaman.setText("Pengalaman: " + model.getExperience() + " Tahun");
        binding.phone.setText("Telepon: " + model.getPhone());

        // kembali ke halaman sebelumnya
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // tolak pendaftaran
        binding.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmation("reject");
            }
        });


        // terima pendaftaran
        binding.verifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmation("acc");
            }
        });
    }

    private void showConfirmation(String option) {
        if(option.equals("reject")) {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi menolak pendaftaran")
                    .setMessage("Apakah anda yakin ingin menolak pendaftaran dari " + model.getName() + " ?")
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setPositiveButton("YAKIN", (dialogInterface, i) -> {
                        FirebaseFirestore
                                .getInstance()
                                .collection("expert")
                                .document(model.getUid())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            dialogInterface.dismiss();
                                            Toast.makeText(VerifyDetailActivity.this, "Berhasil menolak pendaftaran " + model.getName(), Toast.LENGTH_SHORT).show();
                                            binding.reject.setVisibility(View.GONE);
                                            binding.verifBtn.setVisibility(View.GONE);
                                        }
                                        else {
                                            dialogInterface.dismiss();
                                            Toast.makeText(VerifyDetailActivity.this, "Gagal menolak pendaftaran " + model.getName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    })
                    .setNegativeButton("TIDAK", (dialog, i) -> {
                        dialog.dismiss();
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi menerima pendaftaran")
                    .setMessage("Apakah anda yakin ingin menerima pendaftaran dari " + model.getName() + " ?")
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .setPositiveButton("YAKIN", (dialogInterface, i) -> {
                        FirebaseFirestore
                                .getInstance()
                                .collection("expert")
                                .document(model.getUid())
                                .update("role", "expert")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            dialogInterface.dismiss();
                                            Toast.makeText(VerifyDetailActivity.this, "Berhasil menerima pendaftaran " + model.getName(), Toast.LENGTH_SHORT).show();
                                            binding.reject.setVisibility(View.GONE);
                                            binding.verifBtn.setVisibility(View.GONE);
                                        }
                                        else {
                                            dialogInterface.dismiss();
                                            Toast.makeText(VerifyDetailActivity.this, "Gagal menerima pendaftaran " + model.getName(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    })
                    .setNegativeButton("TIDAK", (dialog, i) -> {
                        dialog.dismiss();
                    })
                    .show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}