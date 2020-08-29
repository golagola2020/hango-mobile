package com.hango.hangoactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    // 참조변수 선언
    private EditText et_user_id, et_user_passwd;
    private Button btn_login, btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // xml의 id 불러오기
        et_user_id = findViewById(R.id.et_user_name);
        et_user_passwd = findViewById(R.id.et_user_passwd);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        // 로그인 버튼 클릭시 실행
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 입력된 값 가져오기
                final String userId = et_user_id.getText().toString();
                String userPasswd = et_user_passwd.getText().toString();

                // 리스너 생성
                Response.Listener<String> reponseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);           // 서버의 응답을 json 파싱하여 변수에 저장
                            boolean success = jsonObject.getBoolean("success");  // success를 key로 갖는 value를 저장

                            // 회원 정보 등록에 성공시 실행
                            if (success) {
                                Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userId", userId);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                // Volley를 이용하여 서버로 로그인 요청 => 이때 리스너가 실행됨.
                LoginRequest loginRequest = new LoginRequest(userId, userPasswd, reponseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
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
}