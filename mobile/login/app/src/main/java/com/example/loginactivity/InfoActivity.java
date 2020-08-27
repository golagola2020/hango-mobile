package com.example.loginactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // xml 객체 파싱
        TextView tv_user_name = (TextView)findViewById(R.id.tv_user_name);
        Button btn_info_change = (Button)findViewById(R.id.btn_info_change);
        Button btn_logout = (Button)findViewById(R.id.btn_logout);
        Button btn_notification = (Button)findViewById(R.id.btn_notification);
        Button btn_withdrawal = (Button)findViewById(R.id.btn_withdrawal);
        Button btn_question = (Button)findViewById(R.id.btn_question);
        Button btn_etc = (Button)findViewById(R.id.btn_etc);

        // 메인 화면에서 유저 이름 받아오기
        Intent intent = getIntent();
        final String userId = intent.getStringExtra("userId"); //intent로 받아온 userID
        tv_user_name.setText(userId + "님");

        // 유저 정보 변경 클릭 이벤트
        btn_info_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 화면으로 이동
                Intent intent = new Intent(InfoActivity.this, InfoUpdateActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        // 로그아웃 클릭 이벤트
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업 메시지 객체 생성
                AlertDialog.Builder ad = new AlertDialog.Builder(InfoActivity.this);
                ad.setTitle("로그아웃하시겠습니까?");

                // 탈퇴 버튼 클릭시 실행
                ad.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); // 팝업 메시지 닫기

                        // 로그인 화면으로 이동
                        Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                        startActivity(intent);

                        Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                // 취소 버튼 클릭시 실행
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); // 팝업 메시지 닫기
                    }
                });

                // 팝업 메시지 띄우기
                ad.show();
            }
        });

        // 회원 탈퇴 버튼 클릭 이벤트
        btn_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 팝업 메시지 객체 생성
                AlertDialog.Builder ad = new AlertDialog.Builder(InfoActivity.this);
                ad.setTitle("탈퇴하시겠습니까?");
                ad.setMessage("탈퇴 후, 동일 정보로 로그인하실 수 없습니다.");

                // 탈퇴 버튼 클릭시 실행
                ad.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        // Volly를 사용하여 요청 큐 인스턴스 생성
                        RequestQueue queue = Volley.newRequestQueue((InfoActivity.this));
                        // 요청 URL 생성
                        final String url = "http://192.168.0.31:80/mobile/user/delete";

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
                                        // 정상적으로 처리됐다면 실행
                                        dialogInterface.dismiss(); // 팝업 메시지 닫기

                                        // 로그인 화면으로 이동
                                        Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                        Toast.makeText(getApplicationContext(), "회원 정보가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        // 데이터 처리 실패 메시지를 응답 받았다면 실행
                                        dialogInterface.dismiss(); // 팝업 메시지 닫기
                                        Toast.makeText(getApplicationContext(), "서버에서 데이터 처리에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e){
                                    // 서버 응답 데이터의 JSON 파싱 중 에러 발생시 실행
                                    e.printStackTrace();
                                    dialogInterface.dismiss(); // 팝업 메시지 닫기
                                    Toast.makeText(getApplicationContext(), "JSON 파싱 중 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            // 비정상 응답이 올경우 => 서버가 닫혀있거나, 해당 경로가 존재하지 않을 때 발생
                            public void onErrorResponse(VolleyError error) {
                                dialogInterface.dismiss(); // 팝업 메시지 닫기
                                Toast.makeText(getApplicationContext(), "회원 탈퇴를 요청하실 수 없습니다.", Toast.LENGTH_SHORT).show();
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
                    }
                });

                // 취소 버튼 클릭시 실행
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); // 팝업 메시지 닫기
                    }
                });

                // 팝업 메시지 띄우기
                ad.show();
            }
        });
    }
}