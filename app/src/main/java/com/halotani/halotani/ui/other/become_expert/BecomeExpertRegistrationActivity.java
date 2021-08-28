package com.halotani.halotani.ui.other.become_expert;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityBecomeExpertRegistrationBinding;

import java.util.HashMap;
import java.util.Map;

public class BecomeExpertRegistrationActivity extends AppCompatActivity {

    private ActivityBecomeExpertRegistrationBinding binding;

    private static final int REQUEST_FROM_CAMERA_TO_FORMAL_PICTURE = 1001;

    private String userDp;
    private String sertifikatKeahlian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBecomeExpertRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // klik tombol kembali
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // klik tombol mendaftar
        binding.registerDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegister();
            }
        });

        // klik unggah foto formal
        binding.fotoFormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickUploadFormalPic();
            }
        });

        // PILIH SERTIFIKAT KEAHLIAN
        //tampilkan kecamatan
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialist, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.sertifikatKeahlian.setAdapter(adapter);
        binding.sertifikatKeahlian.setOnItemClickListener((adapterView, view, i, l) -> {
            sertifikatKeahlian = binding.sertifikatKeahlian.getText().toString();
        });


    }

    private void clickUploadFormalPic() {
        ImagePicker.with(BecomeExpertRegistrationActivity.this)
                .galleryOnly()
                .compress(1024)
                .start(REQUEST_FROM_CAMERA_TO_FORMAL_PICTURE);
    }

    private void clickRegister() {
        String name = binding.name.getText().toString().trim();
        String desc = binding.description.getText().toString();
        String phone = binding.phone.getText().toString().trim();
        sertifikatKeahlian = binding.sertifikatKeahlian.getText().toString();


        if (name.isEmpty()) {
            binding.name.setError("Nama Lengkap & Gelar, tidak boleh kosong");
            return;
        } else if (desc.isEmpty()) {
            binding.description.setError("Deskripsi, tidak boleh kosong");
            return;
        } else if (sertifikatKeahlian.isEmpty()) {
            binding.sertifikatKeahlian.setError("Sertifikat Keahlian, tidak boleh kosong");
            return;
        } else if (phone.isEmpty()) {
            binding.phone.setError("Nomor Telepon, tidak boleh kosong");
            return;
        } else if (userDp == null) {
            Toast.makeText(BecomeExpertRegistrationActivity.this, "Silahkan unggah foto formal", Toast.LENGTH_SHORT).show();
            return;
        }


        // SIMPAN DATA DOKTER KE DATABASE
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> expert = new HashMap<>();
        expert.put("name", name);
        expert.put("description", desc);
        expert.put("keahlian", sertifikatKeahlian);
        expert.put("phone", phone);
        expert.put("dp", userDp);
        expert.put("like", "0");
        expert.put("uid", uid);
        expert.put("role", "waiting");


        FirebaseFirestore
                .getInstance()
                .collection("expert")
                .document(uid)
                .set(expert)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        showSuccessDialog();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(BecomeExpertRegistrationActivity.this, "Gagal registrasi: " + task.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil Mendaftar")
                .setMessage("Silahkan menunggu verifikasi dari admin Halo Tani selama 7 x 24 Jam, data anda aman di database Halo Tani.\n\nAnda dapat mulai praktik langsung setelah data anda di verifikasi")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    onBackPressed();
                })
                .show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_CAMERA_TO_FORMAL_PICTURE) {
                uploadPicture(data.getData());
            }
        }
    }

    private void uploadPicture(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "expert/" + "formal" + "/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    userDp = uri.toString();
                                    Toast.makeText(BecomeExpertRegistrationActivity.this, "Berhasil mengunggah gambar", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(BecomeExpertRegistrationActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(BecomeExpertRegistrationActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}