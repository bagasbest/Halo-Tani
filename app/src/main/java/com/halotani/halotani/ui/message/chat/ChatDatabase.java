package com.halotani.halotani.ui.message.chat;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChatDatabase {

    public static void sendChat(String message, String format, String uid, String myUid) {


        Map<String, Object> logChat   = new HashMap<>();
        logChat.put("message", message);
        logChat.put("time", format);
        logChat.put("uid", myUid);
        logChat.put("isText", false);

        // UPDATE LOG CHAT TERAKHIR (SISI PENGIRIM)
        FirebaseFirestore
                .getInstance()
                .collection("chat")
                .document(myUid)
                .collection("listUser")
                .document(myUid+uid)
                .collection("logChat")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(logChat)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("SENDER MSG", "success");
                    }else {
                        Log.d("SENDER MSG", task.toString());
                    }
                });

        // UPDATE LOG CHAT TERAKHIR (SISI PENERIMA)
        FirebaseFirestore
                .getInstance()
                .collection("chat")
                .document(uid)
                .collection("listUser")
                .document(uid+myUid)
                .collection("logChat")
                .document(String.valueOf(System.currentTimeMillis()))
                .set(logChat)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("RECEIVER MSG", "success");
                    }else {
                        Log.d("RECEIVER MSG", task.toString());
                    }
                });
    }
}
