package com.homehunt.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homehunt.Adapters.AdapterRecyclerSuggestions;
import com.homehunt.Adapters.AdapterRoomSuggestion;
import com.homehunt.model.Room;
import com.homehunt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchView extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String REQUEST = "requestcode";
    public final static int REQUEST_DISTRICT = 99;
    // Save state of 4 fragment instead reload
    private HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();
    ImageView ivBack;
    RecyclerView recyclerSearchRoom;
    EditText edTSearch;
    ProgressBar progessBarLoad;
    LinearLayout lnLtResultReturnSearchView;
    TextView txtNumberRoom;
    NestedScrollView nestedScrollSearchView;
    ProgressBar progressBarLoadMoreSearchView;
    FrameLayout fragmentContainer;
    String district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        initControl();
        getDistrict();
    }
    @Override
    protected void onStart() {
        super.onStart();
        callSearchRoomController();
        getDistrict();
    }
    private void getDistrict(){
        Intent intent = getIntent();
        district = intent.getStringExtra(AdapterRecyclerSuggestions.INTENT_DISTRICT);
        //Set text cho district
        edTSearch.setText(district);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ListRoom");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    try {
                        ArrayList<Room> listRoom = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Room room = dataSnapshot.getValue(Room.class);
                            if(room.getDistrict().equals(district)){
                                listRoom.add(room);
                            }
                        }
                        AdapterRoomSuggestion adapterRoomSuggestion = new AdapterRoomSuggestion(SearchView.this, listRoom);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchView.this);
                        recyclerSearchRoom.setLayoutManager(linearLayoutManager);
                        recyclerSearchRoom.setAdapter(adapterRoomSuggestion);
                        progessBarLoad.setVisibility(View.GONE);
                        lnLtResultReturnSearchView.setVisibility(View.VISIBLE);
                        txtNumberRoom.setText(listRoom.size() + "");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initControl(){
        edTSearch = findViewById(R.id.edT_search);
        edTSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchView.this, LocationSearch.class);
                intent.putExtra(REQUEST,REQUEST_DISTRICT);
                startActivityForResult(intent,REQUEST_DISTRICT);
            }
        });
        txtNumberRoom = findViewById(R.id.txt_number_room);
        progessBarLoad = findViewById(R.id.progress_bar_search_view);
        // Edit color for progressbar
        progessBarLoad.getIndeterminateDrawable().setColorFilter(Color.parseColor("#F54500"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        // Hide on the first load
        progessBarLoad.setVisibility(View.GONE);
        lnLtResultReturnSearchView = (LinearLayout) findViewById(R.id.lnLt_resultReturn_search_view);
        nestedScrollSearchView = (NestedScrollView) findViewById(R.id.nested_scroll_search_view);
        progressBarLoadMoreSearchView = (ProgressBar) findViewById(R.id.progress_bar_load_more_search_view);
        progressBarLoadMoreSearchView.getIndeterminateDrawable().setColorFilter(Color.parseColor("#F54500"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        fragmentContainer = findViewById(R.id.fragment_container);
        recyclerSearchRoom = findViewById(R.id.recycler_search_room);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onClick(View view) {
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    }

    private void callSearchRoomController(){
        // Display progress bar
        progessBarLoad.setVisibility(View.VISIBLE);
        // Hide quantity result
        lnLtResultReturnSearchView.setVisibility(View.GONE);
        // Hide progress bar load more
        progressBarLoadMoreSearchView.setVisibility(View.GONE);
    }
}
