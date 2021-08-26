package com.halotani.halotani.ui.home.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.halotani.halotani.R;
import com.halotani.halotani.databinding.ActivityArticleAddBinding;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ArticleAddActivity extends AppCompatActivity {


    private ActivityArticleAddBinding binding;
    private String articleDp;
    private boolean isFavorite = false;
    private static final int REQUEST_FROM_GALLERY_TO_ARTICLE_DP = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // KLIK UNGGAH ARTIKEL
        binding.uploadArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadArticle();
            }
        });

        // KLIK TAMBAH GAMBAR ARTIKEL
        binding.imageHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ArticleAddActivity.this)
                        .galleryOnly()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(REQUEST_FROM_GALLERY_TO_ARTICLE_DP);
            }
        });

        binding.imageView3.setOnClickListener(view -> onBackPressed());


        binding.artikelPilihan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isFavorite = isChecked;
            }
        });

    }

    private void uploadArticle() {
        String title = binding.titleEt.getText().toString().trim();
        String description = binding.descriptionEt.getText().toString();

        if(title.isEmpty()) {
            Toast.makeText(ArticleAddActivity.this, "Judul Artikel tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(description.isEmpty()) {
            Toast.makeText(ArticleAddActivity.this, "Deskripsi Artikel tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(articleDp == null) {
            Toast.makeText(ArticleAddActivity.this, "Gambar Artikel tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        String uid = String.valueOf(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, hh:mm:ss", Locale.getDefault());
        String date = sdf.format(new Date());

        // SIMPAN ARTIKEL KE DATABASE
        Map<String, Object> article = new HashMap<>();
        article.put("title", title);
        article.put("description", description);
        article.put("uid", uid);
        article.put("dp", articleDp);
        article.put("isFavorite", isFavorite);
        article.put("dateAdded", date);
        article.put("dateUpdated", date);

        FirebaseFirestore
                .getInstance()
                .collection("article")
                .document(uid)
                .set(article)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            showSuccessDialog();
                        }
                        else {
                            binding.progressBar.setVisibility(View.GONE);
                            showFailureDialog();
                        }
                    }
                });

    }

    private void showFailureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Gagal mengunggah artikel")
                .setMessage("Terdapat kesalahan ketika mengunggah artikel, silahkan periksa koneksi internet anda, dan coba lagi nanti")
                .setIcon(R.drawable.ic_baseline_clear_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Berhasil mengunggah artikel")
                .setMessage("Artikel akan segera terbit, anda dapat mengedit atau menghapus artikel jika terdapat kesalahan")
                .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
                .setPositiveButton("OKE", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                })
                .show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_FROM_GALLERY_TO_ARTICLE_DP) {
                uploadArticleDp(this, data.getData());
            }
        }
    }

    private void uploadArticleDp(ArticleAddActivity activity, Uri data) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        ProgressDialog mProgressDialog = new ProgressDialog(this);

        mProgressDialog.setMessage("Mohon tunggu hingga proses selesai...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        String imageFileName = "article/data_" + System.currentTimeMillis() + ".png";

        mStorageRef.child(imageFileName).putFile(data)
                .addOnSuccessListener(taskSnapshot ->
                        mStorageRef.child(imageFileName).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    mProgressDialog.dismiss();
                                    articleDp = uri.toString();
                                    binding.imageHint.setVisibility(View.GONE);
                                    Glide
                                            .with(this)
                                            .load(articleDp)
                                            .into(binding.ArticleDp);
                                })
                                .addOnFailureListener(e -> {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(activity, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    Log.d("imageDp: ", e.toString());
                                }))
                .addOnFailureListener(e -> {
                    mProgressDialog.dismiss();
                    Toast.makeText(activity, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                    Log.d("imageDp: ", e.toString());
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}