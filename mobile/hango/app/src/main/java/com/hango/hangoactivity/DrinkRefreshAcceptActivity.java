package com.hango.hangoactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hango.environment.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DrinkRefreshAcceptActivity extends AppCompatActivity {

    private SharedPreferences soldOutData;

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
        final String vendingName = intent.getStringExtra("vendingName");
        final String userId = intent.getStringExtra("userId");

        // 서버로 보내기위한 JSONObject 생성 , { "serialNumber" : serialNumber }
        final JSONObject object = new JSONObject();
        try {
            object.put("serialNumber", serialNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // '예' Button Click Listener
        btn_refresh_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSoldOutData(vendingName,userId);
                refreshVending(object);

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
    private void refreshVending(JSONObject object){
        // 데이터를 송수신 할 서버 URL
        Network network = new Network();
        final String URL = network.getURL() + "/mobile/drink/refresh";  //URL + 음료 개수를 채우기 위한 API Key

        // RequestQueue 생성
        final RequestQueue queue = Volley.newRequestQueue((this));
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
    private void removeSoldOutData(final String vendingName, final String userId){
        // RequestQueue 생성
        RequestQueue queue = Volley.newRequestQueue(this);


        // 데이터를 송수신 할 서버 URL
        Network network = new Network();
        final String URL = network.getURL() +"/mobile/notification/";  //URL + 음료정보 파싱 API Key

        // Request 생성
        StringRequest drinkRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);

                    // 응답 데이터가 존재 할 경우 true, 존재하지 않을 경우 false 값을 반환하는 key(success)값
                    boolean success = object.getBoolean("success");

                    // 응답 데이터가 존재할 경우 각 음료정보를 GridView Item으로 출력 후 음료 추가 Item 생성
                    if(success){

                        JSONObject vending = object.getJSONObject("vending");
                        JSONArray vendingNames = vending.getJSONArray("names");
                        JSONObject soldOuts = vending.getJSONObject("soldOuts");
                        JSONArray soldOutDatas;

                        for(int i=0; i<vendingNames.length();i++) {

                            if(vendingName.equals(vendingNames.getString(i))) {
                                soldOutDatas = soldOuts.getJSONArray(vendingNames.getString(i));
                                soldOutData = getSharedPreferences("soldOutData", MODE_PRIVATE);
                                SharedPreferences.Editor editor = soldOutData.edit();
                                for (int j = 0; j < soldOutDatas.length(); j++) {
                                    editor.remove(vendingName + soldOutDatas.getString(j));
                                }
                                editor.commit();
                                break;
                            }
                        }





                    }

                    //응답 데이터가 존재하지 않을 경우 음료 추가Item 만을 생성
                    else{

                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // 자판기에 해당하는 음료정보를 받기위해 서버가 요청하는 자판기 SerialNumber 전송
                params.put("userId", userId);
                return params;
            }
        };

        // RequestQueue 실행
        queue.add(drinkRequest);
    }

}
