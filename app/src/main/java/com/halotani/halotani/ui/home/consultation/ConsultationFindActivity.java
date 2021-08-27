package com.halotani.halotani.ui.home.consultation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityConsultationFindBinding;

public class ConsultationFindActivity extends AppCompatActivity {

    private ActivityConsultationFindBinding binding;
    private ConsultationFindAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
        initViewModel("all");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConsultationFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // PILIH SPESIALIS DOKTER UNTUK KONSULTASI
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specialist, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.specialistEt.setAdapter(adapter);
        binding.specialistEt.setOnItemClickListener((adapterView, view, i, l) -> {
            initRecyclerView();
            initViewModel(binding.specialistEt.getText().toString());
        });

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private void initRecyclerView() {
        binding.rvExpert.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConsultationFindAdapter();
        binding.rvExpert.setAdapter(adapter);
    }

    private void initViewModel(String specialist) {
        // tampilkan daftar ahli pertanian
        ConsultationViewModel viewModel = new ViewModelProvider(this).get(ConsultationViewModel.class);

        binding.progressBar.setVisibility(View.VISIBLE);
        if (specialist.equals("all")) {
            viewModel.setAllExpert();
        }  else {
            viewModel.setExpertBySpecialist(specialist);
        }

        viewModel.getExpertList().observe(this, expert -> {
            Log.e("TAG", expert.toString());
            if (expert.size() > 0) {
                binding.noData.setVisibility(View.GONE);
                adapter.setData(expert);
            } else {
                binding.noData.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}