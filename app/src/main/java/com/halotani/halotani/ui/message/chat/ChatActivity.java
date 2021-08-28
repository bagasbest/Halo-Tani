package com.halotani.halotani.ui.message.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityChatBinding;
import com.halotani.halotani.ui.message.MessageModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_CONSULTATION = "chat";
    private ActivityChatBinding binding;
    private ChatAdapter adapter;
    private MessageModel model;

    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        model = getIntent().getParcelableExtra(EXTRA_CONSULTATION);
        String uid;
        String myUid;
        String name;
        String myName;
        String dp;
        String myDp;
        if(currentUid.equals(model.getCustomerUid())) {
            binding.textView23.setText(model.getDoctorName());

            uid = model.getDoctorUid();
            myUid = model.getCustomerUid();
            name = model.getDoctorName();
            myName = model.getCustomerName();
            dp = model.getDoctorDp();
            myDp = model.getCustomerDp();

        } else {
            binding.textView23.setText(model.getUid());

            uid = model.getCustomerUid();
            myUid = model.getDoctorUid();
            name = model.getCustomerName();
            myName = model.getDoctorName();
            dp = model.getCustomerDp();
            myDp = model.getDoctorDp();
        }


        Glide.with(this)
                .load(dp)
                .error(R.drawable.ic_baseline_person_24)
                .into(binding.dp);

        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();

        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        // KLIK BERKAS
        binding.attach.setOnClickListener(view -> {
            //showOptionDialog();
        });

        // CEK APAKAH KONSULTASI SUDAH SELESAI / BELUM
      //  checkConsultationStatus();

        sendMessage(uid, myUid, name, myName, dp, myDp);
        
    }

    private void sendMessage(String uid, String myUid, String name, String myName, String dp, String myDp) {
        binding.send.setOnClickListener(view -> {
            String message = binding.messageEt.getText().toString().trim();
            if(message.isEmpty()){
                Toast.makeText(ChatActivity.this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
            String format = getDate.format(new Date());

            ChatDatabase.sendChat(message, format, uid, myUid);
            binding.messageEt.getText().clear();

            // LOAD CHAT HISTORY
            initRecyclerView();
            initViewModel();

        });
    }


    private void initRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatRv.setLayoutManager(linearLayoutManager);

        if (currentUid.equals(model.getCustomerUid())) {
            adapter = new ChatAdapter(model.getCustomerUid());
        } else {
            adapter = new ChatAdapter(model.getDoctorUid());
        }
        binding.chatRv.setAdapter(adapter);

    }

    private void initViewModel() {

        ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        if (currentUid.equals(model.getCustomerUid())) {
            viewModel.setChatList(model.getCustomerUid(), model.getDoctorUid());
        } else {
            viewModel.setChatList(model.getDoctorUid(), model.getCustomerUid());
        }
        viewModel.getChatList().observe(this, chatList -> {
            if (chatList != null) {
                adapter.setData(chatList);
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