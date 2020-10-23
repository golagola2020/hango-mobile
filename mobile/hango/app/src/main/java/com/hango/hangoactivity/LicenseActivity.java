package com.hango.hangoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;


public class LicenseActivity extends AppCompatActivity {
    private ArrayList<String> licenseArray;
    private ImageView iv_arrow_back_license;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        iv_arrow_back_license = (ImageView)findViewById(R.id.iv_arrow_back_license);
        //뒤로가기 버튼구현
        iv_arrow_back_license.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ListView licenseListView = (ListView)findViewById(R.id.lv_license);
        licenseArray = new ArrayList<>();
        licenseArray.add("Android SDK");
        licenseArray.add("Volley");
        licenseArray.add("EazeGraph");
        licenseArray.add("NineOldAndroids");
        LicenseListAdapter licenseListAdapter= new LicenseListAdapter(getApplicationContext(), licenseArray);
        licenseListView.setAdapter(licenseListAdapter);


        licenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(LicenseActivity.this,PrintLicenseActivity.class);
                intent1.putExtra("position",position);
                startActivity(intent1);
            }

        });
    }
}
