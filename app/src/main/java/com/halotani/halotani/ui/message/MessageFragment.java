package com.halotani.halotani.ui.message;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.halotani.halotani.LoginActivity;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.FragmentMessageBinding;
import com.halotani.halotani.ui.message.consultation_status.ConsultationStatusDoneFragment;
import com.halotani.halotani.ui.message.consultation_status.ConsultationStatusProgressFragment;

import org.jetbrains.annotations.NotNull;


public class MessageFragment extends Fragment {


    private FragmentMessageBinding binding;
    private FirebaseUser user;

    @Override
    public void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkIsUserLoginOrNot();
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        });
    }


    private void checkIsUserLoginOrNot() {
        if(user != null) {
            // KLIK TAB VIEW DALAM PROSES / SELESAI
            SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager());
            binding.viewPager.setAdapter(sectionPagerAdapter);
            binding.tabs.setupWithViewPager(binding.viewPager);
            binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()){
                        case 0:
                            new ConsultationStatusProgressFragment();
                        case 1:
                            new ConsultationStatusDoneFragment();
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } else {
            binding.notLogin.setVisibility(View.VISIBLE);
            binding.consultationHistory.setVisibility(View.GONE);

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}