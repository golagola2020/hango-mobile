package com.hango.hangoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class DrinkRefreshAcceptActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_refresh_accept);

        // '예' Button
        Button btn_refresh_accept = (Button)findViewById(R.id.btn_refresh_accept);
        // '아니오' Button
        Button btn_refresh_deny = (Button)findViewById(R.id.btn_refresh_deny);

        // DrinkMainActivity 에서 받아온 자판기 serialNumber 값
        Intent intent = getIntent();
        String serialNumber = intent.getStringExtra("serialNumber");

        // 서버로 보내기위한 JSONObject 생성 , { "serialNumber" : serialNumber }
        final JSONObject object = new JSONObject();
        try {
            object.put("serialNumber", serialNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // RequestQueue 생성
        final RequestQueue queue = Volley.newRequestQueue((this));

        // 데이터를 송수신 할 서버 URL
        Network network = new Network();
        final String URL = network.getURL() + "/mobile/drink/refresh";  //URL + 음료 개수를 채우기 위한 API Key

        // '예' Button Click Listener
        btn_refresh_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL,object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // 해당 자판기가 존재하며 음료 개수를 최대로 채웠을 경우 true, 그렇지 않을 경우 false를 반환하는 key(success)
                            boolean success = response.getBoolean("success");

                            if (success) {
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                finish();
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

                //RequestQueue 실생
                queue.add(objectRequest);
            }
        });

        // '아니오' Button Click Listener
        btn_refresh_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
