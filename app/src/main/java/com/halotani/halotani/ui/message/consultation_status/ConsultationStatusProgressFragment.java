package com.halotani.halotani.ui.message.consultation_status;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.databinding.FragmentConsultationStaatusProgressBinding;
import com.halotani.halotani.ui.message.MessageAdapter;
import com.halotani.halotani.ui.message.MessageViewModel;


public class ConsultationStatusProgressFragment extends Fragment {

    private FragmentConsultationStaatusProgressBinding binding;
    private MessageAdapter adapter;
    private String userUid;

    public void onResume() {
        super.onResume();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // TAMPILKAN RIWAYAT KONSULTASI
        initRecyclerView();
        initViewModel();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConsultationStaatusProgressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.rvConsultation.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(userUid);
        adapter.notifyDataSetChanged();
        binding.rvConsultation.setAdapter(adapter);
    }

    private void initViewModel() {
        MessageViewModel viewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .whereEqualTo("customerUid", userUid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.size() > 0) {
                        Log.e("TAG", String.valueOf(queryDocumentSnapshots.size()));
                        binding.progressBar.setVisibility(View.VISIBLE);
                        viewModel.setListMessage(userUid, "customerUid");
                        viewModel.getMessage().observe(getViewLifecycleOwner(), list -> {
                            if (list.size() > 0) {
                                binding.noData.setVisibility(View.GONE);
                                adapter.setData(list);
                            } else {
                                binding.noData.setVisibility(View.VISIBLE);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                        });
                    } else {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        viewModel.setListMessage(userUid, "doctorUid");
                        viewModel.getMessage().observe(getViewLifecycleOwner(), list -> {
                            if (list.size() > 0) {
                                binding.noData.setVisibility(View.GONE);
                                adapter.setData(list);
                            } else {
                                binding.noData.setVisibility(View.VISIBLE);
                            }
                            binding.progressBar.setVisibility(View.GONE);
                        });
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}