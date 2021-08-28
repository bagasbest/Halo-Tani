package com.halotani.halotani.ui.message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.halotani.halotani.ui.message.consultation_status.ConsultationStatusDoneFragment;
import com.halotani.halotani.ui.message.consultation_status.ConsultationStatusProgressFragment;

import org.jetbrains.annotations.NotNull;

public class SectionPagerAdapter extends FragmentPagerAdapter {


    private static final String[] TAB_TITLES = new String[]{"Dalam Proses", "Selesai"};

    public SectionPagerAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ConsultationStatusProgressFragment();
            case 1:
                return new ConsultationStatusDoneFragment();
            default:
                throw new RuntimeException("Invalid tab position");
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

}
