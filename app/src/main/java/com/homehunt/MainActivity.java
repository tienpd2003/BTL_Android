package com.homehunt;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homehunt.Adapters.AdapterRoomSuggestion;
//import com.homehunt.controller.MainActivityController;
import com.homehunt.model.Room;
import com.homehunt.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends Fragment  {
    RecyclerView recyclerGridMainRoom;
    ProgressBar progressBarMain;
    NestedScrollView nestedScrollMainView;
    ProgressBar progressBarLoadMoreGridMainRoom;
    GridView grVLocation;
    EditText edTSearch;
    View layout;
//    MainActivityController mainActivityController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
    private void initRecyclerView() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("ListRoom");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Room> roomArrayList = new ArrayList<>();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    try {
                        Room room = dataSnapshot.getValue(Room.class);
                        roomArrayList.add(0, room);
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Lỗi tải dữ liệu phòng", Toast.LENGTH_SHORT).show();
                    }
                }

                AdapterRoomSuggestion listRoomSuggestions = new AdapterRoomSuggestion(getActivity(), roomArrayList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                RecyclerView recyclerViewListRoomSuggestions = layout.findViewById(R.id.recycler_Grid_Main_Room);
                recyclerViewListRoomSuggestions.setLayoutManager(linearLayoutManager);
                recyclerViewListRoomSuggestions.setAdapter(listRoomSuggestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi kết nối cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}