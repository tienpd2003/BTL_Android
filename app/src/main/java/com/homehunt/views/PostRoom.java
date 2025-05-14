package com.homehunt.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.homehunt.R;

import java.util.HashMap;
import java.util.Objects;

public class PostRoom extends AppCompatActivity implements View.OnClickListener {
    ImageButton btnImgLocationPushRoom, btnImgInformationPushRoom, btnImgUtilityPushRoom, btnImgConfirmPushRoom;
    TextView txtLocationPushRoom, txtInfoPushRoom, txtUtilityPushRoom, txtConfirmPushRoom;
    Toolbar toolbar;
    private ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_room_main);
        initControl();
        addControl();
    }

    private void initControl() {
        btnImgLocationPushRoom = findViewById(R.id.btnImg_location_push_room);
        btnImgInformationPushRoom = findViewById(R.id.btnImg_information_push_room);
        btnImgUtilityPushRoom = findViewById(R.id.btnImg_utility_push_room);
        btnImgConfirmPushRoom = findViewById(R.id.btnImg_confirm_push_room);

        txtLocationPushRoom = findViewById(R.id.txt_location_push_room);
        txtConfirmPushRoom = findViewById(R.id.txt_comfirm_push_room);
        txtInfoPushRoom = findViewById(R.id.txt_info_push_room);
        txtUtilityPushRoom = findViewById(R.id.txt_utility_push_room);

        btnImgLocationPushRoom.setOnClickListener(this);
        btnImgConfirmPushRoom.setOnClickListener(this);
        btnImgUtilityPushRoom.setOnClickListener(this);
        btnImgInformationPushRoom.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Bài đăng của bạn");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void addControl() {
        pager = findViewById(R.id.viewpager_post_room);
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager);      // Tạo adapter cho ViewPager
        pager.setAdapter(adapter);                             // Gán adapter vào ViewPager
    }


    // Tạo class PagerAdapter kế thừa FragmentPagerAdapter để quản lý từng bước
    public class PagerAdapter extends FragmentPagerAdapter {
        private final HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();
        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            if (fragmentHashMap.get(position) != null) {
                return Objects.requireNonNull(fragmentHashMap.get(position));
            }
            switch (position) {
                case 0:
                    fragment = new PostRoomStep1();
                    fragmentHashMap.put(0, fragment);
                    break;
                case 1:
                    fragment = new PostRoomStep2();
                    fragmentHashMap.put(1, fragment);
                    break;
                case 2:
                    fragment = new PostRoomStep3();
                    fragmentHashMap.put(2, fragment);
                    break;
                case 3:
                    fragment = new PostRoomStep4();
                    fragmentHashMap.put(3, fragment);
                    break;
            }
            return Objects.requireNonNull(fragment);
        }
        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnImg_location_push_room){
            pager.setCurrentItem(0);
        }
        else if(id == R.id.btnImg_information_push_room){
            pager.setCurrentItem(1);
        }
        else if(id == R.id.btnImg_utility_push_room){
            pager.setCurrentItem(2);
        }
        else if(id == R.id.btnImg_confirm_push_room){
            pager.setCurrentItem(3);
        }
    }

    // khi nhấn nút back trên toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences("postRoomData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Xóa toàn bộ dữ liệu đã lưu với tên "postRoomData"
        editor.apply();  // Áp dụng thay đổi

        super.onBackPressed(); // Gọi hàm gốc để thực hiện quay lại màn hình trước
    }

    public void setCurrentPage(int pageNumber){
        pager.setCurrentItem(pageNumber);
    }
}
