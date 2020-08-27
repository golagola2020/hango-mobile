package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class DrinkRefreshAcceptActivity extends AppCompatActivity {
    private JSONObject object;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_refresh_accept);

        Button accept = (Button)findViewById(R.id.btn_refresh_accept);
        Button deny = (Button)findViewById(R.id.btn_refresh_deny);

        Intent intent = getIntent();
        String serialNumber = intent.getStringExtra("serialNumber");
        final JSONObject object = new JSONObject();

        try {
            object.put("serialNumber", serialNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG", "보낼 데이터 : "+ object);

        final RequestQueue queue = Volley.newRequestQueue((this));
        final String URL = "http://192.168.0.31:80/mobile/drink/refresh";
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL,object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean success = response.getBoolean("success");
                            Log.d("TAG", "결과 : " + success);
                            if (success) {
                                Log.d("TAG", "성공");
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

                queue.add(objectRequest);
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
