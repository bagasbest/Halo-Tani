package com.halotani.halotani.ui.other.verify;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.halotani.halotani.ui.home.consultation.ConsultationFindModel;

import java.util.ArrayList;

public class VerifyViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ConsultationFindModel>> listExpert = new MutableLiveData<>();
    final ArrayList<ConsultationFindModel> expertArrayList = new ArrayList<>();

    private static final String TAG = VerifyViewModel.class.getSimpleName();

    public void setExpertList() {
        expertArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("expert")
                    .whereEqualTo("role", "waiting")
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                ConsultationFindModel model = new ConsultationFindModel();

                                model.setName("" + document.get("name"));
                                model.setDescription("" + document.get("description"));
                                model.setDp("" + document.get("dp"));
                                model.setKeahlian("" + document.get("keahlian"));
                                model.setLike("" + document.get("like"));
                                model.setExperience("" + document.get("experience"));
                                model.setUid("" + document.get("uid"));
                                model.setRole("" + document.get("role"));
                                model.setCertificate("" + document.get("certificate"));
                                model.setPhone("" + document.get("phone"));


                                expertArrayList.add(model);
                            }
                            listExpert.postValue(expertArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public LiveData<ArrayList<ConsultationFindModel>> getExpertList() {
        return listExpert;
    }
}
