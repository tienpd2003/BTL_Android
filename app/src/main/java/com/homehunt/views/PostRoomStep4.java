package com.homehunt.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.homehunt.R;
import com.homehunt.model.Room;
import com.homehunt.model.UserModel;

import java.util.ArrayList;

public class PostRoomStep4 extends Fragment implements View.OnClickListener{
    Button btnNextStep4PostRoom;
    EditText edtNamePushRoom, edtDescribePushRoom;

    PostRoom postRoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.post_room_step4, container, false);
        initControl(layout);

        //Lấy context của fragment
        postRoom = (PostRoom) getContext();
        return layout;
    }

    private void initControl(View view){
        edtNamePushRoom = view.findViewById(R.id.edt_name_push_room);
        edtDescribePushRoom = view.findViewById(R.id.edt_describe_push_room);

        btnNextStep4PostRoom =view.findViewById(R.id.btn_nextStep4_post_room);
        btnNextStep4PostRoom.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.btn_nextStep4_post_room){
            String title = edtNamePushRoom.getText().toString().trim();
            String descriptionRoom = edtDescribePushRoom.getText().toString().trim();

            if(title.equals("") || descriptionRoom.equals("")){
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                //  Get data from SharedPreferences
                SharedPreferences preferences = getActivity().getSharedPreferences("postRoomData", Context.MODE_PRIVATE);
                boolean checkStep1 = preferences.getBoolean("checkStep1", false);
                boolean checkStep2 = preferences.getBoolean("checkStep2", false);
                boolean checkStep3 = preferences.getBoolean("checkStep3", false);

                if (!checkStep1) {
                    Toast.makeText(getContext(), "Vui lòng nhập dữ liệu cho bước 1", Toast.LENGTH_SHORT).show();
                } else if (!checkStep2) {
                    Toast.makeText(getContext(), "Vui lòng nhập dữ liệu cho bước 2", Toast.LENGTH_SHORT).show();
                } else if (!checkStep3) {
                    Toast.makeText(getContext(), "Vui lòng nhập dữ liệu cho bước 3", Toast.LENGTH_SHORT).show();
                } else {
                    String addressRoom = preferences.getString("addressRoom", "Chưa cập nhật");
                    String typeOfRoom = preferences.getString("typeOfRoom", "Trọ");
                    int amountOfPeople = preferences.getInt("amountOfPeople", 0);
                    int rentingPriceRoom = preferences.getInt("rentingPriceRoom", 0);
                    int lengthRoom = preferences.getInt("lengthRoom", 0);
                    int widthRoom = preferences.getInt("widthRoom", 0);
                    int electricityPrice = preferences.getInt("electricityPrice", 0);
                    int waterPrice = preferences.getInt("waterPrice", 0);
                    int internetPrice = preferences.getInt("internetPrice", 0);
                    int parkingFee = preferences.getInt("parkingFee", 0);
                    String listServices = preferences.getString("listServices", "p|");
                    ArrayList<Uri> listImageUris = new ArrayList<>();

                    // Read list imageUris from SharedPreferences
                    int imageUrisSize = preferences.getInt("imageUrisSize", 0);

                    if(imageUrisSize > 0){
                        for(int i=0; i<imageUrisSize; ++i){
                            String uriString = preferences.getString("imageUri" + i, "null");
                            listImageUris.add(Uri.parse(uriString));
                        }
                    }

                    Room newPostRoom = new Room();
                    newPostRoom.setTitle(title);
                    newPostRoom.setDescription(descriptionRoom);
                    newPostRoom.setAddress(addressRoom);
                    newPostRoom.setTypeOfRoom(typeOfRoom);
                    newPostRoom.setAmountOfPeople(amountOfPeople);
                    newPostRoom.setLengthRoom(lengthRoom);
                    newPostRoom.setWidthRoom(widthRoom);
                    newPostRoom.setRentingPrice(rentingPriceRoom + "");
                    newPostRoom.setPricePostRoom(electricityPrice, waterPrice, internetPrice, parkingFee);
                    newPostRoom.setListServices(listServices);
                    newPostRoom.setConditionRoom("Còn");

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = auth.getCurrentUser();

                    if(currentUser != null){
                        String uid = currentUser.getUid();

                        FirebaseDatabase firebaseD = FirebaseDatabase.getInstance();
                        DatabaseReference databaseR = firebaseD.getReference("Users");

                        databaseR.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    UserModel userModel = snapshot.getValue(UserModel.class);
                                    newPostRoom.setRoomOwner(userModel);

                                    // add image
                                    final int sizeImageUrls = listImageUris.size() - 1;
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images");

                                    for (int i=0; i<listImageUris.size(); ++i){
                                        Uri imageUri = Uri.parse(String.valueOf(listImageUris.get(i)));
                                        StorageReference imageRef = storageReference.child(imageUri.getLastPathSegment());

                                        UploadTask uploadTask = imageRef.putFile(imageUri);
                                        int finalI = i;
                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Uri> task) {
                                                        if(task.isSuccessful()){
                                                            Uri downloadUri = task.getResult();
                                                            if(downloadUri != null){
                                                                String imgUrl = downloadUri.toString();
                                                                newPostRoom.setImageUrlNew(imgUrl);

                                                                if(finalI == sizeImageUrls){
                                                                    // Post room up to Firebase
                                                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("ListRoom");
                                                                    String key = databaseRef.push().getKey();  // Tạo ID duy nhất
                                                                    newPostRoom.setIdRoom(key);
                                                                    databaseRef.child(key).setValue(newPostRoom, new DatabaseReference.CompletionListener() {
                                                                        @Override
                                                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    if(isAdded()){
                        getActivity().finish();
                    }
                }
            }
        }
    }
}
