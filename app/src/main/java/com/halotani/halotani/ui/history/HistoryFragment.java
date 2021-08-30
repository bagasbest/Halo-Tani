package com.halotani.halotani.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.LoginActivity;
import com.halotani.halotani.databinding.FragmentHistoryBinding;

import org.jetbrains.annotations.NotNull;


public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        initRecyclerView();
        initViewModel();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }

    private void initRecyclerView() {
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter();
        binding.rvHistory.setAdapter(adapter);
    }

    private void initViewModel() {
        // tampilkan riwayat transaksi
        HistoryViewModel viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            binding.progressBar.setVisibility(View.VISIBLE);
            // cek apakah user yang login merupakan customer atau ahli
            FirebaseFirestore
                    .getInstance()
                    .collection("expert")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(("" + documentSnapshot.get("role")).equals("expert")) {
                                viewModel.setExpertUid(user.getUid());
                            } else {
                                viewModel.setHistoryCustomer(user.getUid());
                            }
                            viewModel.getHistory().observe(getViewLifecycleOwner(), historyModels -> {
                                if (historyModels.size() > 0) {
                                    binding.noData.setVisibility(View.GONE);
                                    adapter.setData(historyModels);
                                } else {
                                    binding.noData.setVisibility(View.VISIBLE);
                                }
                                binding.progressBar.setVisibility(View.GONE);
                            });
                        }
                    });
        } else {
            binding.rvHistory.setVisibility(View.GONE);
            binding.noData.setVisibility(View.GONE);
            binding.notLogin.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}