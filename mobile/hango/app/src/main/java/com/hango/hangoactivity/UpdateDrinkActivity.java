package com.hango.hangoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class UpdateDrinkActivity extends AppCompatActivity {

    private EditText et_drink_name,et_drink_price,et_drink_max_count;
    private Button btn_drink_update_accept,btn_drink_update_deny;
    private ImageView iv_arrow_back_update_vending;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_drink);

        Intent intent = getIntent();
        final int position = intent.getIntExtra("position",1);
        final String serialNumber = intent.getStringExtra("serialNumber");

        et_drink_name = (EditText)findViewById(R.id.et_drink_name);
        et_drink_price = (EditText)findViewById(R.id.et_drink_price);
        et_drink_max_count = (EditText)findViewById(R.id.et_drink_max_count);

        btn_drink_update_accept =(Button)findViewById(R.id.btn_drink_update_accept);
        btn_drink_update_deny =(Button)findViewById(R.id.btn_drink_update_deny);

        iv_arrow_back_update_vending = (ImageView)findViewById(R.id.iv_arrow_back_update_vending);

        //뒤로가기 기능 구현
        iv_arrow_back_update_vending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_drink_update_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String drinkName = et_drink_name.getText().toString();
                final String drinkPrice = et_drink_price.getText().toString();
                final String drinkMaxCount = et_drink_max_count.getText().toString();

                // drink JSON 객체 생성
                JSONObject drink = new JSONObject();

                // 내부 데이터 삽입
                try {
                    drink.put("position", position+1);
                    drink.put("name", drinkName);
                    drink.put("price", drinkPrice);
                    drink.put("maxCount", drinkMaxCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 서버에게 요청할 JSON 객체 생성
                JSONObject object = new JSONObject();
                // 최종 데이터 삽입
                try {
                    object.put("serialNumber", serialNumber);
                    object.put("drink", drink);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Volly를 사용하여 요청 큐 인스턴스 생성
                RequestQueue queue = Volley.newRequestQueue(UpdateDrinkActivity.this);

                // 요청 URL 생성
                Network network = new Network();
                final String URL = network.getURL() +  "/mobile/drink/update"; // 자판기 수정을 위한 API key

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 사용하고자 하는 데이터 파싱
                            boolean success = response.getBoolean("success");

                            // 서버의 데이터 처리 성공 여부 검사
                            if (success) {
                                finish();
                                Toast.makeText(getApplicationContext(), "음료수 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                finish();
                                // 서버의 작업 실패 메세지 불러오기
                                String msg = response.getString("msg");
                                Toast.makeText(getApplicationContext(), "서버에서 데이터 처리에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // 서버 응답 데이터의 JSON 파싱 중 에러 발생시 실행
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON 파싱 중 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    // 비정상 응답이 올경우 => 서버가 닫혀있거나, 해당 경로가 존재하지 않을 때 발생
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "서버와의 연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                // 서버에게 데이터 요청
                queue.add(objectRequest);
            }
        });

        btn_drink_update_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
