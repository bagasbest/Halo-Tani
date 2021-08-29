package com.halotani.halotani.ui.message.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.halotani.halotani.databinding.ActivityChatNoteBinding;
import com.halotani.halotani.ui.message.MessageModel;

import java.util.HashMap;
import java.util.Map;

public class ChatNoteActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "note";
    private ActivityChatNoteBinding binding;
    private MessageModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        model = getIntent().getParcelableExtra(EXTRA_NOTE);


        // KLIK KEMBALI KE HALAMAN SEBELUMNYA
        binding.backButton.setOnClickListener(view -> onBackPressed());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // JIKA PENGGUNA SAAT INI DOTER, MAKA IA DAPAT MENULISKAN CATATAN DOKTER
        if(model.getDoctorUid().equals(uid)) {
            binding.sendNoteBtn.setVisibility(View.VISIBLE);
            binding.note.setEnabled(true);

            // AMBIL CATATAN
            getNote();


            // KLIK KIRIM CATATAN & REKOMENDASI OBAT
            binding.sendNoteBtn.setOnClickListener(view -> {
                String note = binding.note.getText().toString();

                if(note.isEmpty()) {
                    binding.note.setError("Catatan tidak boleh kosong");
                    return;
                }

                // KIRIM CATATAN KE PENGGUNA (KUSTOMER)
                sendNoteToUser(note);

            });

        } else {
            // JIKA PENGGUNA MERUPAKAN KUSTOMER YANG MENJALANI KONSULTASI, TAMPILKAN CATATAN
            getNote();

        }
    }

    private void sendNoteToUser(String note) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> notes = new HashMap<>();
        notes.put("note", note);

        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(model.getUid())
                .collection("consultationNote")
                .document(model.getDoctorUid())
                .set(notes)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChatNoteActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChatNoteActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getNote() {
        FirebaseFirestore
                .getInstance()
                .collection("consultation")
                .document(model.getUid())
                .collection("consultationNote")
                .document(model.getDoctorUid())
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        String note = "" + documentSnapshot.get("note");
                        binding.note.setText(note);
                    } else {
                        binding.note.setText("");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
         binding = null;
    }
}