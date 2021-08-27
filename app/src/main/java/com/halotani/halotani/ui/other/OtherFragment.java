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

import com.google.firebase.auth.FirebaseAuth;
import com.halotani.halotani.LoginActivity;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.FragmentOtherBinding;

import org.jetbrains.annotations.NotNull;


public class OtherFragment extends Fragment {

    private FragmentOtherBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOtherBinding.inflate(inflater, container, false);

        // cek apakah user sudah login atau belum
        checkUserLogin();

        // klik login / logout
        clickLoginOrLogout();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.becomeExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void clickLoginOrLogout() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
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
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
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