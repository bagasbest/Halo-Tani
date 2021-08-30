package com.halotani.halotani.ui.home.consultation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.HomeActivity;
import com.halotani.halotani.LoginActivity;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityConsultationDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConsultationDetailActivity extends AppCompatActivity {

    public static final String EXTRA_EXPERT = "expert";
    private ActivityConsultationDetailBinding binding;
    private ConsultationFindModel model;
    private boolean isFavorite = false;
    private int like = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TAMPILKAN DETAIL INFORMASI DOKTER YANG INGIN DIVERIFIKASI
        model = getIntent().getParcelableExtra(EXTRA_EXPERT);

        like = Integer.parseInt(model.getLike());

        binding.name.setText(model.getName());
        binding.sertifikatKeahlian.setText(model.getKeahlian());
        binding.like.setText(model.getLike() + " Orang telah merekomendasikan " + model.getName());
        binding.description.setText(model.getDescription());
        Glide.with(this)
                .load(model.getDp())
                .into(binding.dp);

        // KLIK KONSULTASI
        binding.paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickConsultation();
            }
        });


        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            // CEK APAKAH SAYA MENYUKAI DOKTER INI SAAT INI
            checkIfLikedDoctor(model.getUid());
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            if(model.getUid().equals(uid)){
                // SEMBUNYIKAN TOMBOL KONSULTASI & REKOMENDASI
                binding.paymentBtn.setVisibility(View.INVISIBLE);
                binding.likeBtn.setVisibility(View.INVISIBLE);
            }

        }

        // KLIK SUKAI
        binding.likeBtn.setOnClickListener(view -> {
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                saveData(model.getUid());
            } else {
                binding.notLogin.setVisibility(View.VISIBLE);
                binding.detailConsult.setVisibility(View.GONE);
            }
        });

        // LOGIN BUTTON
        binding.loginBtn.setOnClickListener(view -> {
           startActivity(new Intent(this, LoginActivity.class));
        });

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private void clickConsultation() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            // HANYA USER BIASA YANG DAPAT MELAKSANAKAN KONSULTASI, DOKTER TIDAK BISA KONSULTASI DENGAN DOKTER
            FirebaseFirestore
                    .getInstance()
                    .collection("expert")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.exists()) {
                            showAlertDialog();
                        } else {
                            new AlertDialog.Builder(this)
                                    .setTitle("Konfirmasi Konsultasi")
                                    .setMessage("Apakah anada yakin ingin konsultasi dengan ahli ini ?")
                                    .setPositiveButton("YA", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                        createNewConcultation();
                                    })
                                    .setNegativeButton("TIDAK", null)
                                    .show();
                        }
                    });



        } else {
            binding.notLogin.setVisibility(View.VISIBLE);
            binding.detailConsult.setVisibility(View.GONE);
        }
    }

    private void createNewConcultation() {
        String timeInMillis = String.valueOf(System.currentTimeMillis());
        String customerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(customerUid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> consultation = new HashMap<>();
                        consultation.put("uid", timeInMillis);
                        consultation.put("doctorUid", model.getUid());
                        consultation.put("doctorName", model.getName());
                        consultation.put("customerUid", customerUid);
                        consultation.put("customerName", ""+documentSnapshot.get("name"));
                        consultation.put("doctorDp", model.getDp());
                        consultation.put("status", "Sedang Konsultasi");
                        consultation.put("keahlian", model.getKeahlian());
                        consultation.put("customerDp", ""+documentSnapshot.get("dp"));
                        consultation.put("onlineCustomer", false);
                        consultation.put("onlineDoctor", false);


                        FirebaseFirestore
                                .getInstance()
                                .collection("consultation")
                                .document(timeInMillis)
                                .set(consultation)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        createNewHistory(mProgressDialog, customerUid, timeInMillis, ""+documentSnapshot.get("name"));
                                    } else {
                                        Log.e("Error Transaction", task.toString());
                                        Toast.makeText(ConsultationDetailActivity.this, "Gagal memulai konsultasi", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void createNewHistory(ProgressDialog mProgressDialog, String uid, String timeInMillis, String name) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMMM yyyy, hh:mm:ss");
        String format = getDate.format(new Date());

        Map<String, Object> history = new HashMap<>();
        history.put("uid", timeInMillis);
        history.put("doctorUid", model.getUid());
        history.put("doctorName", model.getName());
        history.put("customerUid", uid);
        history.put("customerName", name);
        history.put("date", format);
        history.put("message", "Anda memulai konsultasi mengenai " + model.getKeahlian());


        FirebaseFirestore
                .getInstance()
                .collection("history")
                .document(timeInMillis)
                .set(history)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mProgressDialog.dismiss();
                        showSuccessDialog();
                    } else {
                        Log.e("Error Transaction", task.toString());
                        Toast.makeText(ConsultationDetailActivity.this, "Gagal memulai konsultasi", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Membuat Janji Konsultasi")
                .setMessage("Silahkan kembali ke Halaman Utama dan cek Navigasi Pesan untuk melakukan konsultasi dengan ahli pilihanmu")
                .setIcon(R.drawable.chat_with_expert)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(ConsultationDetailActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .show();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Pemberitahuan")
                .setMessage("Anda berstatus sebagai Dokter saat ini.\n\nHanya pengguna yang bukan berstatus dokter yang dapat melakukan konsultasi, silahkan mendaftar akun baru")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    private void saveData(String doctorUid) {
        checkIfLikedDoctor(doctorUid);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        if(isFavorite) {
            editor.putBoolean("status_"+uid+doctorUid, false);
            editor.apply();
            binding.likeBtn.setText("Saya merekomendasikan ahli ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.background));

            like -= 1;
        } else {
            editor.putBoolean("status_"+uid+doctorUid, true);
            editor.apply();
            binding.likeBtn.setText("Saya tidak merekomendasikan ahli ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));

            like += 1;
        }
        likedOrNot(String.valueOf(like));
        binding.like.setText(like + " Orang telah merekomendasikan" + model.getName());
    }

    @SuppressLint("SetTextI18n")
    private void checkIfLikedDoctor(String doctorUid) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences pref = this.getPreferences(Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("status_" + uid + doctorUid, false);

        Log.e("TAG", "status_" + uid + doctorUid);

        if(value) {
            isFavorite = true;
            binding.likeBtn.setText("Saya tidak merekomendasikan ahli ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey));
        } else {
            isFavorite = false;
            binding.likeBtn.setText("Saya merekomendasikan ahli ini");
            binding.likeBtn.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.background));
        }
    }

    private void likedOrNot(String like) {
        FirebaseFirestore
                .getInstance()
                .collection("expert")
                .document(model.getUid())
                .update("like", like);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}