package com.hango.hangoactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

public class GuideDrinkMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_drink_main);

        // Guide layout 전체
        ViewGroup layout = (ViewGroup) findViewById(R.id.guide_layout);

        // layout Click Listener
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
    }
});
    }

}
