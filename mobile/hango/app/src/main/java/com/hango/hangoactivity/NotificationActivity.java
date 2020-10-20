package com.hango.hangoactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

public class NotificationActivity extends AppCompatActivity {

    private SharedPreferences appData;
    private Switch switch_notification;
    String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        switch_notification = (Switch)findViewById(R.id.switch_notification);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId"); //intent로 받아온 userID

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        switch_notification.setChecked(appData.getBoolean("SAVE_SWITCH_STATE", false));


        switch_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSwitchState(switch_notification.isChecked());
                loadService();
            }

        });


        ImageView iv_arrow_back_notification = (ImageView)findViewById(R.id.iv_arrow_back_notification);
        //뒤로가기 기능 구현
        iv_arrow_back_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveSwitchState(Boolean switchChecked){
        SharedPreferences.Editor editor = appData.edit();
        editor.putBoolean("SAVE_SWITCH_STATE",switchChecked);
        editor.apply();
    }

    private void loadService(){
        Intent serviceIntent = new Intent(NotificationActivity.this, NotificationService.class);
        if(appData.getBoolean("SAVE_SWITCH_STATE", false)){

            serviceIntent.putExtra("userId",userId);
            if(android.os.Build.VERSION.SDK_INT > 25) {
                getApplicationContext().startForegroundService(serviceIntent);
                //startService(serviceIntent);
            }
            else {
                startService(serviceIntent);
            }
        }
        else{
            stopService(serviceIntent);
            deleteSoldOutData();
        }
    }

    private void deleteSoldOutData(){
        SharedPreferences soldOutData = getSharedPreferences("soldOutData", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = soldOutData.edit();
        editor.clear();
        editor.commit();
    }
}
