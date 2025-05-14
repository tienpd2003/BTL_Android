package com.homehunt.controller;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.homehunt.Adapters.AdapterRecyclerSuggestions;
import com.homehunt.controller.interfaces.IDistrictFilterModel;
import com.homehunt.model.DistrictFilterModel;
import com.homehunt.R;

import java.util.ArrayList;
import java.util.List;

public class LocationSearchController {
    Context context;
    DistrictFilterModel districtFilterModel;

    public LocationSearchController(Context context){
        this.context = context;
        this.districtFilterModel = new DistrictFilterModel();
    }

    public void loadDistrictInData(RecyclerView recyclerSuggestion, String FilterString, boolean isSearchRoomCall){

        //Khởi tạo list lưu quận
        List<String> stringListDistrict = new ArrayList<String>();

        //Khởi tạo Adapter
        AdapterRecyclerSuggestions adapterRecyclerSuggestions = new AdapterRecyclerSuggestions(context, R.layout.element_location_recycler_view, stringListDistrict, isSearchRoomCall);
        //Set adapter
        recyclerSuggestion.setAdapter(adapterRecyclerSuggestions);

        //Tạo layout cho recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerSuggestion.setLayoutManager(layoutManager);


        IDistrictFilterModel iDistrictFilterModel = new IDistrictFilterModel() {
            @Override
            public void sendDistrict(String District) {
                stringListDistrict.add(District);
                adapterRecyclerSuggestions.notifyDataSetChanged();
            }
        };
        districtFilterModel.listDistrictLocation(FilterString, iDistrictFilterModel);
    }
}
