package com.hango.hangoactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    // 참조변수 선언
    private EditText et_user_name, et_user_id, et_user_email, et_user_passwd, et_user_passwd_check;
    private Button btn_signup;
    private EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        // xml의 id 불러오기
        et_user_name = findViewById(R.id.et_user_name);
        et_user_id = findViewById(R.id.et_user_id);
        et_user_email = findViewById(R.id.et_user_email);
        et_user_passwd = findViewById(R.id.et_user_passwd);
        et_user_passwd_check = findViewById(R.id.et_user_passwd_check);
        btn_signup = findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 입력된 값 가져오기
                String userName = et_user_name.getText().toString();
                String userId = et_user_id.getText().toString();
                String userEmail = et_user_email.getText().toString();
                String userPasswd = et_user_passwd.getText().toString();
                String userPasswdCheck = et_user_passwd_check.getText().toString();

                // 입력된 데이터 검사
                if (userName.length() == 0 || userId.length() == 0 || userEmail.length() == 0 || userPasswd.length() == 0 || userPasswdCheck.length() == 0) {
                    Toast.makeText(getApplicationContext(), "모든 정보는 필수 입력 사항입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!userPasswd.equals(userPasswdCheck)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //회원가입 요청
               signUpRequest(userName,userId,userEmail,userPasswd);
            }
        });

    }

    //회원가입 요청 method
    public void signUpRequest(String userName,String userId, String userEmail, String userPasswd){

        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);

        Network network = new Network();
        String URL = network.getURL() + "/mobile/signup";

        JSONObject user = new JSONObject();
        try {
            user.put("name",userName);
            user.put("id",userId);
            user.put("email",userEmail);
            user.put("passwd",userPasswd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject userData = new JSONObject();
        try {
            userData.put("user", user);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL,userData, new Response.Listener<JSONObject>() {

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

        queue.add(objectRequest);
    }
}