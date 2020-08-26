package com.example.loginactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        btn_add_vending = findViewById(R.id.btn_Regi_vending);

        final Intent intent = getIntent();
        final String VendingSerialNumber = intent.getStringExtra("VendingSerialNumber"); //intent로 받아온 userID
        final String userId = intent.getStringExtra("userId");
        final RequestQueue queue = Volley.newRequestQueue((this));

        btn_add_vending.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final String vendingName = vending_name.getText().toString();
                final String vendingDescription = vending_description.getText().toString();
                String vendingFullSize = vending_fullsize.getText().toString();
                JSONObject vending_parameters = new JSONObject();
                try {
                    vending_parameters.put("name",vendingName);
                    vending_parameters.put("description",vendingDescription);
                    vending_parameters.put("fullSize",vendingFullSize);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject object = new JSONObject();

                try {
                    object.put("serialNumber", VendingSerialNumber);
                    object.put("vending",vending_parameters);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", "결과 : " + object);
                String URL = "http://192.168.0.31:80/mobile/vending/update";
                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL,object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean success = response.getBoolean("success");
                            Log.d("TAG", "결과 : " + success);
                            if (success) {
                                Intent intent1 = new Intent(UpdateVendingActivity.this,MainActivity.class);
                                intent1.putExtra("userId",userId);
                                finish();
                                startActivity(intent1);



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

                queue.add(objectRequest);
            }
        });
    }
}
