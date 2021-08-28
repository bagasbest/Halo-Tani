package com.halotani.halotani.ui.message.chat;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ChatViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ChatModel>> listChat = new MutableLiveData<>();
    final ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

    private static final String TAG = ChatViewModel.class.getSimpleName();

    public void setChatList(String uid1, String uid2) {

        chatModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("chat")
                    .document(uid1)
                    .collection("listUser")
                    .document(uid1+uid2)
                    .collection("logChat")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {

                                ChatModel model = new ChatModel();
                                model.setMessage("" + document.get("message"));
                                model.setTime("" + document.get("time"));
                                model.setUid("" + document.get("uid"));
                                model.setText(document.getBoolean("isText"));


                                chatModelArrayList.add(model);
                            }
                            listChat.postValue(chatModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<ChatModel>> getChatList() {
        return listChat;
    }

}
