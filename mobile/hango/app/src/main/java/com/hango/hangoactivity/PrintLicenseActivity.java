package com.hango.hangoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PrintLicenseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_license);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position",-1);

        TextView tv_print_license = (TextView)findViewById(R.id.tv_print_license);
        tv_print_license.setText(readLicense(position));
    }

    private String readLicense(int position){
        String data = null;
        InputStream inputStream =null;
        switch(position){
            case 0:
                inputStream = getResources().openRawResource(R.raw.android_sdk_license);
                break;
            case 1:
                inputStream = getResources().openRawResource(R.raw.volley_license);
                break;
            case 2:
                inputStream = getResources().openRawResource(R.raw.eaze_graph_license);
                break;
            case 3:
                inputStream = getResources().openRawResource(R.raw.nine_old_androids_license);
                break;
            default:
                return " ";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try{
            i = inputStream.read();
            while(i != -1){
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            data = new String(byteArrayOutputStream.toByteArray(),"MS949");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
