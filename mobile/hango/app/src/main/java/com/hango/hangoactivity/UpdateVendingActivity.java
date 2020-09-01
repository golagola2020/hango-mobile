package com.hango.hangoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hango.environment.Network;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateVendingActivity extends AppCompatActivity {
    private Button btn_add_vending;
    private EditText vending_name, vending_description, vending_fullsize;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_vending);

        vending_name = findViewById(R.id.Vending_name);
        vending_description = findViewById(R.id.Vending_comment);
        vending_fullsize = findViewById(R.id.Vending_size);
        // '수정' Button
        btn_add_vending = findViewById(R.id.btn_Regi_vending);

        // MainActivity 에서 받아온 serialNumber
        final Intent intent = getIntent();
        final String serialNumber = intent.getStringExtra("serialNumber"); //intent로 받아온 userID

        // RequestQueue 생성
        final RequestQueue queue = Volley.newRequestQueue((this));

        // '수정' Button Click Listener
        btn_add_vending.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String vendingName = vending_name.getText().toString();
                final String vendingDescription = vending_description.getText().toString();
                String vendingFullSize = vending_fullsize.getText().toString();

                // 자판기 정보 JSONObject 생성
                JSONObject vending_parameters = new JSONObject();
                try {
                    vending_parameters.put("name",vendingName);
                    vending_parameters.put("description",vendingDescription);
                    vending_parameters.put("fullSize",vendingFullSize);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버에서 요청하는 데이터 형태
                JSONObject object = new JSONObject();
                try {
                    object.put("serialNumber", serialNumber);
                    object.put("vending",vending_parameters);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 요청 URL
                Network network = new Network();
                String URL = network.getURL() + "/mobile/vending/update"; //자판기 정보 수정을 위한 API Key

                // JSONObjectRequest 생성
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL,object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                //RequestQueue 실행
                queue.add(objectRequest);
            }
        });
    }
}
