package com.halotani.halotani;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.databinding.ActivityRegisterBinding;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // klik tombol registrasi
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrateUser();
            }
        });

        binding.login.setOnClickListener(view -> onBackPressed());
    }

    private void registrateUser() {
        String name = binding.nameEt.getText().toString().trim();
        String age = binding.ageEt.getText().toString().trim();
        String phone = binding.phoneEt.getText().toString().trim();
        String location = binding.locationEt.getText().toString().trim();
        String email = binding.emailEt.getText().toString().trim();
        String password = binding.passwordEt.getText().toString().trim();

        if(name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(age.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Usia tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(phone.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "No Telepon tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(location.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Lokasi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar3.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Map<String, Object> register = new HashMap<>();
                        register.put("name", name);
                        register.put("age", age);
                        register.put("phone", phone);
                        register.put("location", location);
                        register.put("email", email);
                        register.put("password", password);
                        register.put("uid", uid);
                        register.put("role", "user");
                        register.put("dp", "");

                        FirebaseFirestore
                                .getInstance()
                                .collection("users")
                                .document(uid)
                                .set(register)
                                .addOnCompleteListener(task2 -> {
                                    if(task2.isSuccessful()) {
                                        binding.progressBar3.setVisibility(View.GONE);
                                        showSuccessDialog();
                                    }
                                    else {
                                        binding.progressBar3.setVisibility(View.GONE);
                                        showFailureDialog();
                                    }
                                });
                    } else {
                        binding.progressBar3.setVisibility(View.GONE);
                        showFailureDialog();
                    }
                });



    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal melakukan registrasi")
                .setMessage("Terdapat kesalahan ketika registrasi, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil melakukan registrasi")
                .setMessage("Silahkan verifikasi email anda melalui email yang sudah kami kirimkan ke akun anda")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}