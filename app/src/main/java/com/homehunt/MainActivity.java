package com.homehunt;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

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
        ref.limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot roomSnap : snapshot.getChildren()) {
                    String roomId = roomSnap.getKey();

                    // Mở DetailRoom với ID phòng lấy được từ Firebase
                    Intent intent = new Intent(MainActivity.this, DetailRoom.class);
                    intent.putExtra("intentDetailRoom", roomId);
                    startActivity(intent);
                    break; // Chỉ mở phòng đầu tiên trong danh sách
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Không tải được phòng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
