package com.homehunt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.homehunt.views.DetailRoom;
import com.homehunt.model.Room;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity"; // Thêm TAG để dễ lọc log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.room_detail_view);

        // // Cập nhật padding cho giao diện để hỗ trợ các cửa sổ hệ thống
        // ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        //     Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        //     v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        //     return insets;
        // });

        // Tải thông tin phòng từ Firebase và mở DetailRoom
        openDetailRoom();
    }

    private void openDetailRoom() {
        // Lấy thông tin phòng từ Firebase (giả sử lấy phòng đầu tiên trong "ListRoom")
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ListRoom");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "Tổng số phòng: " + snapshot.getChildrenCount());
                
                for (DataSnapshot roomSnap : snapshot.getChildren()) {
                    Room room = roomSnap.getValue(Room.class);
                    String roomId = roomSnap.getKey();
                    
                    if (room != null) {
                        Log.d(TAG, "Phòng ID: " + roomId);
                        Log.d(TAG, "Tên phòng: " + room.getTitle());
                        Log.d(TAG, "Giá thuê: " + room.getRentingPrice());
                        Log.d(TAG, "Địa chỉ: " + room.getAddress());
                        Log.d(TAG, "Trạng thái: " + room.getConditionRoom());
                        Log.d(TAG, "----------------------------------------");
                    }

                    // Mở DetailRoom với ID phòng đầu tiên
                    if (roomSnap.equals(snapshot.getChildren().iterator().next())) {
                        Intent intent = new Intent(MainActivity.this, DetailRoom.class);
                        intent.putExtra("intentDetailRoom", roomId);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Lỗi tải danh sách phòng: " + error.getMessage());
                Toast.makeText(MainActivity.this, "Không tải được phòng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
