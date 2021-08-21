package com.halotani.halotani;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.halotani.halotani.databinding.ActivityHomeBinding;
import com.halotani.halotani.ui.history.HistoryFragment;
import com.halotani.halotani.ui.home.HomeFragment;
import com.halotani.halotani.ui.message.MessageFragment;
import com.halotani.halotani.ui.other.OtherFragment;
import com.halotani.halotani.ui.profile.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.halotani.halotani.databinding.ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // untuk mengganti halaman contoh: Navigasi Beranda -> Navigasi Riwayat -> Navigasi Profil -> Navigasi Pesan -> Navigasi Lainnya
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = new HomeFragment();
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_history).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_message).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_other).setEnabled(true);
                    selectedFragment = new HomeFragment();
                    break;
                }

                case R.id.navigation_history: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_history).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_message).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_other).setEnabled(true);
                    selectedFragment = new HistoryFragment();
                    break;
                }
                case R.id.navigation_profile: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_history).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_message).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_other).setEnabled(true);
                    selectedFragment = new ProfileFragment();
                    break;
                }

                case R.id.navigation_message: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_history).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_message).setEnabled(false);
                    navView.getMenu().findItem(R.id.navigation_other).setEnabled(true);
                    selectedFragment = new MessageFragment();
                    break;
                }
                case R.id.navigation_other: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_history).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_profile).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_message).setEnabled(true);
                    navView.getMenu().findItem(R.id.navigation_other).setEnabled(false);
                    selectedFragment = new OtherFragment();
                    break;
                }
                default: {
                    navView.getMenu().findItem(R.id.navigation_home).setEnabled(false);
                }
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectedFragment);
            transaction.commit();

            return true;
        });
    }

}