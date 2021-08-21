package com.halotani.halotani.ui.other;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}