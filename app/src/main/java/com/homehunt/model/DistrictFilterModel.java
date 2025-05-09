package com.homehunt.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homehunt.controller.interfaces.IDistrictFilterModel;

public class DistrictFilterModel {
    DatabaseReference nodeRoot;

    public DistrictFilterModel(){
        nodeRoot = FirebaseDatabase.getInstance().getReference().child("LocationRoom");
    }

    //Hàm trả về danh sách quận có trong firebase
    // nhận tham số filterString để lọc dữ liệu và dùng interface để gửi dữ liệu quận
    public void listDistrictLocation(String filterString, IDistrictFilterModel iDistrictFilterModel){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Firebase", "onDataChange: Data received");
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().toLowerCase().contains(filterString.toLowerCase())){
                        iDistrictFilterModel.sendDistrict(data.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error reading data", databaseError.toException());
            }
        };
        // Gắn ValueEventListener với một tham chiếu trong Firebase Realtime Database
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }
}
