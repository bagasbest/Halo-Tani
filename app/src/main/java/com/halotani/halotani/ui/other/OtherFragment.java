package com.halotani.halotani.ui.other;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.LoginActivity;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.FragmentOtherBinding;
import com.halotani.halotani.ui.other.become_expert.BecomeExpertRegistrationActivity;
import com.halotani.halotani.ui.other.verify.VerifyActivity;

import org.jetbrains.annotations.NotNull;


public class OtherFragment extends Fragment {

    private FragmentOtherBinding binding;
    private FirebaseUser user;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOtherBinding.inflate(inflater, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        // cek apakah user sudah login atau belum
        checkUserLogin();

        // cek admin atau bukan untuk memunculkan menu verifikasi
        checkAdminOrNot();

        // klik login / logout
        clickLoginOrLogout();

        return binding.getRoot();
    }

    private void checkAdminOrNot() {
        if(user != null) {
            FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(("" + documentSnapshot.get("role")).equals("admin")) {
                                binding.verifyConsultant.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.becomeExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null) {
                    FirebaseFirestore
                            .getInstance()
                            .collection("expert")
                            .document(user.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(!documentSnapshot.exists()) {
                                        startActivity(new Intent(getActivity(), BecomeExpertRegistrationActivity.class));
                                    } else {
                                        showAlertDialog();
                                    }
                                }
                            });
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }

            }
        });

        binding.verifyConsultant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VerifyActivity.class));
            }
        });

    }

    private void showAlertDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Anda Sudah Terdaftar")
                .setMessage("Pendaftaran Menjadi Penyuluh Pertanian hanya bisa di lakukan satu kali, jika pendaftaran anda belum dikonfirmasi, harap menunggu, admin akan segera memeriksanya.")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
    }

    private void clickLoginOrLogout() {
        if(user != null) {
            binding.authBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Konfirmasi Logout")
                            .setMessage("Apakah anada yakin ingin keluar aplikasi ?")
                            .setPositiveButton("YA", (dialogInterface, i) -> {
                                // sign out dari firebase autentikasi
                                FirebaseAuth.getInstance().signOut();

                                // go to login activity
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                dialogInterface.dismiss();
                                startActivity(intent);
                                view.getContext().startActivity(intent);
                            })
                            .setNegativeButton("TIDAK", null)
                            .show();
                }
            });
        } else {
            binding.authBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // go to login activity
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkUserLogin() {
        if(user != null) {
            binding.authBtn.setText("Logout");
        } else {
            binding.authBtn.setText("Login");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}