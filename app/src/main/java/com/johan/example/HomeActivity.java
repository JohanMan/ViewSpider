package com.johan.example;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.johan.view.spider.annotation.EnableViewSpider;

@EnableViewSpider
public class HomeActivity extends AppCompatActivity {

    private HomeViewSpider homeViewSpider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeViewSpider = new HomeViewSpider();
        homeViewSpider.find(this);
        homeViewSpider.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeViewSpider.childViewSpider.inflate();
                homeViewSpider.childViewSpider.nameView.setText("哈哈哈哈哈哈哈");
            }
        });
    }

}
