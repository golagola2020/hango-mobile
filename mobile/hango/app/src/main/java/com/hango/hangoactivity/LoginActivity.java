package com.hango.hangoactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginActivity extends AppCompatActivity {

    private MainBackPressCloseHandler loginBackPressCloseHandler;

    // 참조변수 선언
    private EditText et_user_id, et_user_passwd;
    private Button btn_login, btn_signup;

    private CheckBox checkBox;
    private boolean saveLoginData;
    private String userId,userPasswd;

    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        loadLoginData();

        //back button 두번 클릭시 종료
        loginBackPressCloseHandler = new MainBackPressCloseHandler(this);

        // xml의 id 불러오기
        et_user_id = findViewById(R.id.et_user_name);
        et_user_passwd = findViewById(R.id.et_user_passwd);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        checkBox = findViewById(R.id.cb_login_check);

        if(saveLoginData){
            LoginRequest(userId,userPasswd);
        }

        // 로그인 버튼 클릭시 실행
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 입력된 값 가져오기
                userId = et_user_id.getText().toString();
                userPasswd = et_user_passwd.getText().toString();

                LoginRequest(userId,userPasswd);
            }
        });

        // 회원가입 버튼 클릭시 실행
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override public void onBackPressed() {
        //super.onBackPressed();
        loginBackPressCloseHandler.onBackPressed();
    }

    //로그인 요청 method
    public void LoginRequest(final String userId, final String userPasswd){

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        Network network = new Network();
        String URL = network.getURL() + "/mobile/login";

        JSONObject user = new JSONObject();
        try {
            user.put("id",userId);
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

                        if(checkBox.isChecked() == true) {
                            setSaveLoginData(checkBox.isChecked(), userId, userPasswd);
                        }
                        Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);



                    } else {
                        Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
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

    public void setSaveLoginData(Boolean check,String userId,String userPasswd){
        SharedPreferences.Editor editor = appData.edit();

        editor.putBoolean("SAVE_LOGIN_DATA",check);
        editor.putString("ID",userId);
        editor.putString("PWD",userPasswd);

        editor.apply();
    }
    private void loadLoginData(){
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        userId = appData.getString("ID","");
        userPasswd = appData.getString("PWD","");
    }
}