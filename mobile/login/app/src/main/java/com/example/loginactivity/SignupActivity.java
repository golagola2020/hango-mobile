package com.example.loginactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

                // 리스너 생성
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);           // 서버의 응답을 json 파싱하여 변수에 저장
                            boolean success = jsonObject.getBoolean("success");  // success를 key로 갖는 value를 저장

                            // 회원 정보 등록에 성공시 실행
                            if (success) {
                                Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "등록에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };

                // Volley를 이용하여 서버로 회원가입 요청 => 이때 리스너가 실행됨.
                SignupRequest signupRequest = new SignupRequest(userName, userId, userEmail, userPasswd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
                queue.add(signupRequest);
            }
        });

    }
}