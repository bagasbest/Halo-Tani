package com.halotani.halotani.ui.message;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MessageViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<MessageModel>> listMessage = new MutableLiveData<>();
    final ArrayList<MessageModel> messageViewModelArrayList = new ArrayList<>();

    private static final String TAG = MessageViewModel.class.getSimpleName();

    public void setListMessage(String userUid, String option) {
        messageViewModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("consultation")
                    .whereEqualTo(option, userUid)
                    .whereEqualTo("status", "Sedang Konsultasi")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {

                                MessageModel model = new MessageModel();
                                model.setCustomerDp("" + document.get("customerDp"));
                                model.setCustomerName("" + document.get("customerName"));
                                model.setCustomerUid("" + document.get("customerUid"));
                                model.setDoctorDp("" + document.get("doctorDp"));
                                model.setDoctorName("" + document.get("doctorName"));
                                model.setDoctorUid("" + document.get("doctorUid"));
                                model.setKeahlian("" + document.get("keahlian"));
                                model.setOnline(document.getBoolean("online"));
                                model.setStatus("" + document.get("status"));
                                model.setUid("" + document.get("uid"));


                                messageViewModelArrayList.add(model);
                            }
                            listMessage.postValue(messageViewModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setFinishMessage(String userUid, String option) {
        messageViewModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("consultation")
                    .whereEqualTo(option, userUid)
                    .whereEqualTo("status", "Selesai")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {

                                MessageModel model = new MessageModel();
                                model.setCustomerDp("" + document.get("customerDp"));
                                model.setCustomerName("" + document.get("customerName"));
                                model.setCustomerUid("" + document.get("customerUid"));
                                model.setDoctorDp("" + document.get("doctorDp"));
                                model.setDoctorName("" + document.get("doctorName"));
                                model.setDoctorUid("" + document.get("doctorUid"));
                                model.setKeahlian("" + document.get("keahlian"));
                                model.setOnline(document.getBoolean("online"));
                                model.setStatus("" + document.get("status"));
                                model.setUid("" + document.get("uid"));


                                messageViewModelArrayList.add(model);
                            }
                            listMessage.postValue(messageViewModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<MessageModel>> getMessage() {
        return listMessage;
    }

}
