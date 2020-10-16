package com.hango.hangoactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.hango.environment.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InfoUpdateActivity extends AppCompatActivity {

    EditText et_user_name, et_user_id, et_user_email, et_user_passwd, et_user_new_passwd, et_user_new_passwd_check;
    Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_update);

        // xml 객체 파싱
        et_user_name = (EditText)findViewById(R.id.et_user_name);
        et_user_id = (EditText)findViewById(R.id.et_user_id);
        et_user_email = (EditText)findViewById(R.id.et_user_email);
        et_user_passwd = (EditText)findViewById(R.id.et_user_passwd);
        et_user_new_passwd = (EditText)findViewById(R.id.et_user_new_passwd);
        et_user_new_passwd_check = (EditText)findViewById(R.id.et_user_new_passwd_check);

        btn_update = (Button)findViewById(R.id.btn_update);

        // 유저 정보 화면에서 유저 이름 받아오기
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("userId"); //intent로 받아온 userID

        // Volly를 사용하여 요청 큐 인스턴스 생성
        RequestQueue queue = Volley.newRequestQueue((InfoUpdateActivity.this));
        // 요청 URL 생성
        final Network network = new Network();
        final String url = network.getURL() + "/mobile/user/read";

        // 요청 정보 구현
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            // 성공적으로 응답이 올 경우
            public void onResponse(String response) {
                try{
                    // 응답 데이터를 JSON 파싱
                    JSONObject object = new JSONObject(response);

                    // 사용하고자 하는 데이터 파싱
                    boolean success = object.getBoolean("success");

                    // 서버의 데이터 처리 성공 여부 검사
                    if(success){
                        // 유저 정보 받아오기
                        JSONObject user = object.getJSONObject("user");
                        String userName = user.getString("name");
                        String userId = user.getString("id");
                        String userEmail = user.getString("email");

                        // EditText에 기존 정보 세팅
                        et_user_name.setText(userName);
                        et_user_id.setText(userId);
                        et_user_email.setText(userEmail);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "서버에서 데이터 처리에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    // 서버 응답 데이터의 JSON 파싱 중 에러 발생시 실행
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "JSON 파싱 중 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            // 비정상 응답이 올경우 => 서버가 닫혀있거나, 해당 경로가 존재하지 않을 때 발생
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "회원 정보를 요청하실 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }){
            // 서버에게 요청할 데이터 가공
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                return params;
            }
        };
        
        // 서버에게 데이터 처리 요청
        queue.add(strReq);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText 입력값 불러오기
                final String userName = et_user_name.getText().toString();
                final String userNewId = et_user_id.getText().toString();
                final String userEmail = et_user_email.getText().toString();
                final String userNewPasswd = et_user_new_passwd.getText().toString();

                JSONObject user = new JSONObject();
                try {
                    user.put("id", userId);
                    user.put("name", userName);
                    user.put("email", userEmail);
                    user.put("newPasswd", userNewPasswd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject object = new JSONObject();

                try {
                    object.put("user", user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", "결과 : " + object);

                // Volly를 사용하여 요청 큐 인스턴스 생성
                RequestQueue queue = Volley.newRequestQueue((InfoUpdateActivity.this));

                // 요청 URL 생성
                final String URL = network.getURL() + "/mobile/user/update";

                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // 사용하고자 하는 데이터 파싱
                            boolean success = response.getBoolean("success");

                            // 서버의 데이터 처리 성공 여부 검사
                            if (success) {
                                // 로그인 화면으로 이동
                                Intent intent = new Intent(InfoUpdateActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "정보가 변경되었습니다. 다시 로그인을 시도하여 주십시오.", Toast.LENGTH_SHORT).show();
                            } else {
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
                        Toast.makeText(getApplicationContext(), "회원 탈퇴를 요청하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(objectRequest);
            }
        });

    }
}