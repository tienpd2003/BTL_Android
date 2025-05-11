package com.homehunt.views;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class DetailRoom extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // TODO: Viết code xử lý khi tạo activity ở đây
    }

    private void initControl(){
        // TODO: Khởi tạo các control (TextView, ImageView, RecyclerView, ...)
    }

    private void setInfoRoom(){
        // TODO: Thiết lập thông tin chi tiết của phòng dựa vào đối tượng Room
    }

    @Override
    public boolean onSupportNavigateUp() {
        // TODO: Xử lý khi nhấn nút quay lại trên toolbar
        return true;
    }

    private void showImageDialog() {
        // TODO: Hiển thị dialog hình ảnh phòng với ViewPager
    }

    @Override
    public void onClick(View view){
        // TODO: Xử lý các sự kiện click
    }
}

