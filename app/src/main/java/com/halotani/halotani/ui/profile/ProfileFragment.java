package com.halotani.halotani.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.LoginActivity;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.FragmentProfileBinding;
import com.halotani.halotani.utils.HomeBackground;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseUser user;

    // variabel
    private static final int REQUEST_FROM_GALLERY_TO_SELF_PHOTO = 1001;

    @Override
    public void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        populateUI();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.emailEt.setEnabled(false);
        binding.ageEt.setEnabled(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // KLIK LOGIN, JIKA BELUM LOGIN
        loginClick();

        // KLIK PERBARUI PROFIL
        updateProfile();

        // KLIK PERBARUI USER DP
        updateUserDp();
    }

    private void updateUserDp() {
        binding.updateUserDp.setOnClickListener(view -> {
            ImagePicker
                    .with(this)
                    .galleryOnly()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(REQUEST_FROM_GALLERY_TO_SELF_PHOTO);
        });
    }


    private void updateProfile() {
        binding.updateProfileBtn.setOnClickListener(view -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi Perbarui Profil")
                    .setMessage("Apakah kamu yakin ingin memperbarui profil, berdasarkan data yang telah kamu inputkan ?")
                    .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                    .setPositiveButton("YA", (dialogInterface, i) -> {
                        // SIMPAN PERUBAHAN PROFIL PENGGUNA KE DATABASE
                        saveProfileChangesToDatabase();
                    })
                    .setNegativeButton("TIDAK", null)
                    .show();
        });
    }

    private void saveProfileChangesToDatabase() {
        String name = binding.nameEt.getText().toString().trim();
        String phone = binding.phoneEt.getText().toString().trim();


        // VALIDASI KOLOM PROFIL, JANGAN SAMPAI ADA YANG KOSONG
        if (name.isEmpty()) {
            binding.nameEt.setError("Nama Lengkap tidak boleh kosong");
            return;
        } else if (phone.isEmpty()) {
            binding.phoneEt.setError("Nomor Telepon tidak boleh kosong");
            return;
        }

        Map<String, Object> updateProfile = new HashMap<>();
        updateProfile.put("name", name);
        updateProfile.put("phone", phone);

        // SIMPAN PERUBAHAN PROFIL TERBARU KE DATABASE
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .update(updateProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Berhasil memperbarui profil", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                        Log.e("Error update profil", task.toString());
                    }
                });

    }

    private void populateUI() {
        if (user != null) {

            binding.notLogin.setVisibility(View.GONE);
            binding.constraintLayout.setVisibility(View.VISIBLE);

            // TERAPKAN BACKGROUND SESUAI WAKTU
            HomeBackground.setBackgroundImage(getActivity(), binding.imageView2);


            // AMBIL DATA PENGGUNA DARI DATABASE, UNTUK DITAMPILKAN SEBAGAI PROFIL
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String name = "" + documentSnapshot.get("name").toString();
                        String email = "" + documentSnapshot.get("email").toString();
                        String phone = "" + documentSnapshot.get("phone").toString();
                        String userDp = "" + documentSnapshot.get("dp").toString();
                        String age = "" + documentSnapshot.get("age").toString();

                        //TERAPKAN PADA UI PROFIL
                        binding.nameEt.setText(name);
                        binding.emailEt.setText(email);
                        binding.phoneEt.setText(phone);
                        binding.ageEt.setText(age);

                        Glide.with(getActivity())
                                .load(userDp)
                                .error(R.drawable.ic_baseline_person_24)
                                .into(binding.userDp);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Error get profil", e.toString());
                        Toast.makeText(getActivity(), "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                    });

        } else {
            binding.notLogin.setVisibility(View.VISIBLE);
            binding.constraintLayout.setVisibility(View.GONE);

        }
    }

    private void loginClick() {
        binding.loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.progressBar.setVisibility(View.VISIBLE);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY_TO_SELF_PHOTO) {
                ProfileDatabase.uploadImageToDatabase(data.getData(), getActivity(), user.getUid());
                Glide.with(this)
                        .load(data.getData())
                        .into(binding.userDp);
                binding.progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}