package com.halotani.halotani.ui.history;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HistoryViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<HistoryModel>> listHistory = new MutableLiveData<>();
    final ArrayList<HistoryModel>  historyModelArrayList = new ArrayList<>();

    private static final String TAG = HistoryViewModel.class.getSimpleName();

    public void setHistoryCustomer(String customerUid) {
        historyModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("history")
                    .whereEqualTo("customerUid", customerUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                HistoryModel model = new HistoryModel();

                                model.setUid("" + document.get("uid"));
                                model.setCustomerName("" + document.get("customerName"));
                                model.setCustomerUid("" + document.get("customerUid"));
                                model.setDate("" + document.get("date"));
                                model.setDoctorName("" + document.get("doctorName"));
                                model.setDoctorUid("" + document.get("doctorUid"));
                                model.setMessage("" + document.get("message"));

                                historyModelArrayList.add(model);
                            }
                            listHistory.postValue(historyModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void setExpertUid(String expertUid) {
        historyModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("history")
                    .whereEqualTo("doctorUid", expertUid)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                HistoryModel model = new HistoryModel();

                                model.setUid("" + document.get("uid"));
                                model.setCustomerName("" + document.get("customerName"));
                                model.setCustomerUid("" + document.get("customerUid"));
                                model.setDate("" + document.get("date"));
                                model.setDoctorName("" + document.get("doctorName"));
                                model.setDoctorUid("" + document.get("doctorUid"));
                                model.setMessage("" + document.get("message"));

                                historyModelArrayList.add(model);
                            }
                            listHistory.postValue(historyModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<HistoryModel>> getHistory() {
        return listHistory;
    }
}