package com.halotani.halotani.ui.message.chat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

    private static final int REQUEST_IMAGE_FROM_CAMERA = 1001;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 1002;
    private String imageText;
    String uid;
    String myUid;
    String message;

    private String currentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        model = getIntent().getParcelableExtra(EXTRA_CONSULTATION);
        String name;
        String myName;
        String dp;
        String myDp;
        if (currentUid.equals(model.getCustomerUid())) {
            binding.textView23.setText(model.getDoctorName());

            uid = model.getDoctorUid();
            myUid = model.getCustomerUid();
            name = model.getDoctorName();
            myName = model.getCustomerName();
            dp = model.getDoctorDp();
            myDp = model.getCustomerDp();

        } else {
            binding.textView23.setText(model.getCustomerName());

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
            showOptionDialog();
        });

        // CEK APAKAH KONSULTASI SUDAH SELESAI / BELUM
        checkConsultationStatus();

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(uid, myUid);
            }
        });
    }

    private void checkConsultationStatus() {
        if (model.getStatus().equals("Selesai")) {
            binding.send.setEnabled(false);
            binding.messageEt.setEnabled(false);
        }
    }

    private void sendMessage(String uid, String myUid) {
        if (imageText != null) {
            message = imageText;
        } else {
            message = binding.messageEt.getText().toString().trim();
        }


        if (message.isEmpty()) {
            Toast.makeText(ChatActivity.this, "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat getDate = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        String format = getDate.format(new Date());

        ChatDatabase.sendChat(message, format, uid, myUid, imageText != null);
        binding.messageEt.getText().clear();
        imageText = null;

        // LOAD CHAT HISTORY
        initRecyclerView();
        initViewModel();
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

    private void showOptionDialog() {
        String[] options = {"Unggah Gambar Melalui Kamera", "Unggah Gambar Melalui Galeri", "Catatan Konsultasi", "Selesaikan Konsultasi"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilihan");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // UNGGAH GAMBAR
                dialog.dismiss();
                ImagePicker.with(ChatActivity.this)
                        .cameraOnly()
                        .compress(1024)
                        .start(REQUEST_IMAGE_FROM_CAMERA);
            } else if (which == 1) {
                // UNGGAH GAMBAR
                dialog.dismiss();
                ImagePicker.with(ChatActivity.this)
                        .galleryOnly()
                        .compress(1024)
                        .start(REQUEST_IMAGE_FROM_GALLERY);
            } else if (which == 2) {
                // CATATAN
                dialog.dismiss();
                Intent intent = new Intent(ChatActivity.this, ChatNoteActivity.class);
                intent.putExtra(ChatNoteActivity.EXTRA_NOTE, model);
                startActivity(intent);
            } else if (which == 3) {

                // SELESAIKAN KONSULTASI
                dialog.dismiss();
                if (model.getDoctorUid().equals(currentUid)) {
                    Toast.makeText(ChatActivity.this, "Hanya kustomer yang dapat menyelesaikan konsultasi", Toast.LENGTH_SHORT).show();
                } else if (model.getStatus().equals("Selesai")) {
                    Toast.makeText(ChatActivity.this, "Konsultasi sudah diselesaikan", Toast.LENGTH_SHORT).show();
                } else {
                    showConfirmDialog();
                }
            }
        });
        builder.create().show();
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Menyelesaikan Konsultasi")
                .setMessage("Apakah anda yakin ingin menyelesaikan konsultasi ?")
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setCancelable(false)
                .setPositiveButton("YA", (dialogInterface, i) -> {
                    dialogInterface.dismiss();

                    // SELESAIKAN KONSULTASI
                    FirebaseFirestore
                            .getInstance()
                            .collection("consultation")
                            .document(model.getUid())
                            .update("status", "Selesai")
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChatActivity.this, "Konsultasi Selesai", Toast.LENGTH_SHORT).show();
                                    binding.send.setEnabled(false);
                                    binding.messageEt.setEnabled(false);
                                }
                            });
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_FROM_GALLERY || requestCode == REQUEST_IMAGE_FROM_CAMERA) {
                uploadPicture(data.getData());
            }
        }
    }

    private void uploadPicture(Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "chat/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    imageText = uri.toString();
                                    sendMessage(uid, myUid);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(ChatActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(ChatActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}