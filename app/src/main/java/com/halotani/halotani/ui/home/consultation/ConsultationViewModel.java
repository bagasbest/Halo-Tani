package com.halotani.halotani.ui.home.consultation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ConsultationViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<ConsultationFindModel>> listExpert = new MutableLiveData<>();
    final ArrayList<ConsultationFindModel> expertArrayList = new ArrayList<>();

    private static final String TAG = ConsultationViewModel.class.getSimpleName();

    public void setExpertBySpecialist(String specialist) {
        expertArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("expert")
                    .whereEqualTo("keahlian", specialist)
                    .whereEqualTo("role", "expert")
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

    public void setAllExpert() {
        expertArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("expert")
                    .whereEqualTo("role", "expert")
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
