package com.homehunt.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.homehunt.Adapters.AdapterListServices;
import com.homehunt.Adapters.AdapterViewPagerImageShow;
import com.homehunt.model.Room;
import com.homehunt.model.UserModel;
import com.homehunt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailRoom extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Room room;
    private TextView typeOfRoom, title, rentingPrice, conditionRoom, amountOfPeople, acreageRoom, description, address, phoneContact;
    private ArrayList<ImageView> imageView;
    private TextView moreImg, electricityPrice, waterPrice, wifiPrice, parkingPrice;
    private RecyclerView listServicesRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_detail_view);
        initControl();

        Intent intent = getIntent();

        try {
            if(intent != null && intent.hasExtra("intentDetailRoom")) {
                String idRoom = intent.getStringExtra("intentDetailRoom");

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("ListRoom");
                databaseRef.child(idRoom).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        room = dataSnapshot.getValue(Room.class);
                        if (room != null) {
                            setInfoRoom();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } catch (Exception e){
            Toast.makeText(this, "Try catch error", Toast.LENGTH_SHORT).show();
        }
    }

    private void initControl(){
        typeOfRoom = findViewById(R.id.txt_roomType);
        title = findViewById(R.id.txt_roomName);
        rentingPrice = findViewById(R.id.txt_roomPrice);
        conditionRoom = findViewById(R.id.txt_roomStatus);
        amountOfPeople = findViewById(R.id.tv_amountOfPeople);
        acreageRoom = findViewById(R.id.txt_roomArea);
        description = findViewById(R.id.txt_roomDescription);
        address = findViewById(R.id.txt_roomAddress);
        phoneContact = findViewById(R.id.txt_room_phonenumber);
        electricityPrice = findViewById(R.id.text_view_detail_room_electricity_price);
        waterPrice = findViewById(R.id.text_view_detail_room_water_price);
        wifiPrice = findViewById(R.id.text_view_detail_room_wifi_price);
        parkingPrice = findViewById(R.id.text_view_detail_room_parking_price);

        moreImg = findViewById(R.id.txt_more_img);
        imageView = new ArrayList<>();
        imageView.add((ImageView) findViewById(R.id.img_room1));
        imageView.add((ImageView) findViewById(R.id.img_room2));
        imageView.add((ImageView) findViewById(R.id.img_room3));
        imageView.add((ImageView) findViewById(R.id.img_room4));

        listServicesRoom = findViewById(R.id.recycler_convenients_room_detail);
        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Phòng mới đăng");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Add event click to open dialog show detail imagesRoom
        for(ImageView imgView : imageView){
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImageDialog();
                }
            });
        }
    }

    private void setInfoRoom(){
        try {
            ArrayList<String> imageUrls = room.getAllImagesRoom();

            if(imageUrls.size() > 4){
                moreImg.setText("+" + (imageUrls.size() - 4));
            }else{
                moreImg.setText("");
            }

            title.setText(room.getTitle());
            typeOfRoom.setText(room.getTypeOfRoom());
            int rentingRoomPrice = Integer.parseInt(room.getRentingPrice());
            float rentingPriceFloat = (float)((float)rentingRoomPrice/1000000.0);
            rentingPrice.setText(String.format("%.2f", rentingPriceFloat) + " triệu");
            conditionRoom.setText(room.getConditionRoom());
            amountOfPeople.setText(room.getAmountOfPeople() + "");
            acreageRoom.setText(room.getLengthRoom() + "m x " + room.getWidthRoom() + "m");
            description.setText(room.getDescription());
            address.setText(room.getAddress());

            DecimalFormat formatPrice = new DecimalFormat("#.#");
            float electricityPriceFloat = room.getElectricityPrice() + 0.0f;
            float waterPriceFloat = room.getWaterPrice() + 0.0f;
            float wifiPriceFloat = room.getInternetPrice() + 0.0f;
            float parkingPriceFloat = room.getParkingFee() + 0.0f;
            if(electricityPriceFloat > 0.0){
                electricityPrice.setText(formatPrice.format(electricityPriceFloat / 1000.0) + "k");
            }else{
                electricityPrice.setText("Miễn phí");
            }
            if(waterPriceFloat > 0.0){
                waterPrice.setText(formatPrice.format(waterPriceFloat / 1000.0) + "k");
            }else{
                waterPrice.setText("Miễn phí");
            }
            if(wifiPriceFloat > 0.0){
                wifiPrice.setText(formatPrice.format(wifiPriceFloat / 1000.0) + "k");
            }else{
                wifiPrice.setText("Miễn phí");
            }
            if(parkingPriceFloat > 0.0){
                parkingPrice.setText(formatPrice.format(parkingPriceFloat / 1000.0) + "k");
            }else{
                parkingPrice.setText("Miễn phí");
            }

            UserModel use1r = room.getRoomOwner();
            phoneContact.setText(use1r.getPhoneNumber());

            for(int i = 0; i < 4; i++) {
                int indexImageView = i;
                if (indexImageView < imageUrls.size() && imageUrls.get(indexImageView) != null) {
                    Picasso.get().load(imageUrls.get(indexImageView)).into(imageView.get(indexImageView), new Callback() {
                        @Override
                        public void onSuccess() {
                            // Do nothing
                        }
                        @Override
                        public void onError(Exception e) {
                            int resId = getResources().getIdentifier("ic_room", "drawable", getPackageName());
                            imageView.get(indexImageView).setImageResource(resId);
                            e.printStackTrace();
                        }
                    });
                } else {
                    int resId = getResources().getIdentifier("ic_room", "drawable", getPackageName());
                    imageView.get(indexImageView).setImageResource(resId);
                }
            }

            // Set list services for recycler view
            AdapterListServices adapterListServices = new AdapterListServices(room.getListServices());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            listServicesRoom.setLayoutManager(gridLayoutManager);
            listServicesRoom.setAdapter(adapterListServices);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showImageDialog() {
        ArrayList<String> listImagesUrlString = room.getAllImagesRoom();
        int listImagesUrlSize = listImagesUrlString.size();
        Dialog dialogShowImageDetailRoom = new Dialog(DetailRoom.this);

        dialogShowImageDetailRoom.setContentView(R.layout.dialog_show_image_detail_room);
        ViewPager viewPagerShowImageRoom = (ViewPager) dialogShowImageDetailRoom.findViewById(R.id.viewPager_showImage_detail_room);
        Button closeDialog = (Button) dialogShowImageDetailRoom.findViewById(R.id.btn_closeShowImage_detail_room);
        TextView positionImageShow = (TextView) dialogShowImageDetailRoom.findViewById(R.id.txt_positionImage_detail_room);

        AdapterViewPagerImageShow adapterViewPagerImageShow = new AdapterViewPagerImageShow(DetailRoom.this, listImagesUrlString);
        viewPagerShowImageRoom.setAdapter(adapterViewPagerImageShow);
        positionImageShow.setText("1/" + listImagesUrlSize);

        viewPagerShowImageRoom.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int index = viewPagerShowImageRoom.getCurrentItem() + 1;
                positionImageShow.setText(index + "/" + listImagesUrlSize);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        closeDialog.setEnabled(true);
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowImageDetailRoom.cancel();
            }
        });

        dialogShowImageDetailRoom.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogShowImageDetailRoom.getWindow().setDimAmount(0.9f);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialogShowImageDetailRoom.getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);

        dialogShowImageDetailRoom.show();
    }

    @Override
    public void onClick(View view){

    }
}
