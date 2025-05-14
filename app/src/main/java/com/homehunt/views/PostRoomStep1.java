package com.homehunt.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.homehunt.R;

import java.text.Normalizer;

public class PostRoomStep1 extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner spinnerDistrictPostRoom, spinnerCityPostRoom, spinnerWardPostRoom;
    private EditText edtStreetPushRoom, edtNoPushRoom;
    private Button btnNextStep1PostRoom;
    private TextView txtChooseLocation;

    PostRoom postRoom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.post_room_step1, container, false);
        initControl(layout);
        setSpinnerData();
        spinnerCityPostRoom.setOnItemSelectedListener(this);
        spinnerDistrictPostRoom.setOnItemSelectedListener(this);
        postRoom = (PostRoom) getContext();
        return layout;
    }

    private void initControl(View view) {
        spinnerCityPostRoom = (Spinner) view.findViewById(R.id.spn_city_push_room);
        spinnerDistrictPostRoom = (Spinner) view.findViewById(R.id.spn_district_push_room);
        spinnerWardPostRoom = (Spinner) view.findViewById(R.id.spn_ward_push_room);
        edtStreetPushRoom = (EditText) view.findViewById(R.id.edt_street_push_room);
        edtNoPushRoom = (EditText) view.findViewById(R.id.edt_no_push_room);

        btnNextStep1PostRoom = (Button) view.findViewById(R.id.btn_nextStep1_post_room);
        btnNextStep1PostRoom.setOnClickListener(this);

//        txtChooseLocation = view.findViewById(R.id.txt_choose_location);
//        txtChooseLocation.setOnClickListener(this);
    }

    private void setSpinnerData() {
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCityPostRoom.setAdapter(cityAdapter);

        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.ha_noi_districts, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDistrictPostRoom.setAdapter(districtAdapter);

        ArrayAdapter<CharSequence> wardAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.ba_dinh_wards, android.R.layout.simple_spinner_item);
        wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWardPostRoom.setAdapter(wardAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_nextStep1_post_room){
            String nameStreet = edtStreetPushRoom.getText().toString().trim();
            String numberOfHouse = edtNoPushRoom.getText().toString().trim();

            if(nameStreet.equals("") || numberOfHouse.equals("")){
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }else{
                // Get data from spinners
                String selectedCity = spinnerCityPostRoom.getSelectedItem().toString();
                String selectedDistrict = spinnerDistrictPostRoom.getSelectedItem().toString();
                String selectedWard = spinnerWardPostRoom.getSelectedItem().toString();
                String addressRoom = numberOfHouse + ", " + nameStreet + ", "
                        + selectedWard + ", " + selectedDistrict + ", " + selectedCity;

                // Save data to SharePreferences
                SharedPreferences preferences = getActivity().getSharedPreferences("postRoomData" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("addressRoom", addressRoom);
                editor.putBoolean("checkStep1", true);
                editor.apply();

                // Go to next step post room
                if (isAdded() && getActivity() != null) {
                    postRoom.setCurrentPage(1);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int spinnerId = adapterView.getId();
        if(spinnerId == R.id.spn_city_push_room){
            updateDistricts(adapterView.getItemAtPosition(i).toString());
        }
        if(spinnerId == R.id.spn_district_push_room){
            updateWards(adapterView.getItemAtPosition(i).toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateDistricts(String cityName) {
        ArrayAdapter<CharSequence> districtAdapter;
        Context context = requireContext();
        String resourceName = convertCityNameToVariableName(cityName);

        int resourceId = context.getResources().getIdentifier(resourceName, "array", context.getPackageName());

        if (resourceId != 0) {
            districtAdapter = ArrayAdapter.createFromResource(requireContext(), resourceId, android.R.layout.simple_spinner_item);
            districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDistrictPostRoom.setAdapter(districtAdapter);
        }
    }

    private void updateWards(String districtName) {
        ArrayAdapter<CharSequence> wardAdapter;
        Context context = requireContext();
        String resourceName = convertDistrictNameToVariableName(districtName);

        // get ID resource by Resources.getIdentifier
        if(!resourceName.equals("null")){
            int resourceId = context.getResources().getIdentifier(resourceName, "array", context.getPackageName());

            if (resourceId != 0) {
                wardAdapter = ArrayAdapter.createFromResource(requireContext(), resourceId, android.R.layout.simple_spinner_item);
                wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerWardPostRoom.setAdapter(wardAdapter);
            }
        }
    }

    private String convertCityNameToVariableName(String cityName){
        String normalized = Normalizer.normalize(cityName.toLowerCase().trim(), Normalizer.Form.NFD); // ví dụ "à" -> "a`"
        String withoutD = normalized.replace('đ', 'd'); // TH đặc biệt
        String normalizedCityName = withoutD.replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); // bỏ dấu

        return normalizedCityName.replace(' ', '_') + "_districts";
    }

    private String convertDistrictNameToVariableName(String districtName) {
        // Convert districtName to unsigned text
        String normalized = Normalizer.normalize(districtName.toLowerCase().trim(), Normalizer.Form.NFD);
        String withoutD = normalized.replace('đ', 'd');
        String normalizedDistrictName = withoutD.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        if (normalizedDistrictName.startsWith("quan")){
            String result = normalizedDistrictName.substring(5);
            return result.replace(' ', '_') + "_wards";
        }
        if (normalizedDistrictName.startsWith("huyen")) {
            String result = normalizedDistrictName.substring(6);
            return result.replace(' ', '_') + "_suburbs";
        }
        if (normalizedDistrictName.startsWith("thi xa")) {
            String result = normalizedDistrictName.substring(7);
            return result.replace(' ', '_') + "_suburbs";
        }
        return "null";
    }
}
