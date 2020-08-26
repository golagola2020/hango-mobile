package com.example.loginactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrinkMainActivity extends AppCompatActivity {
    private String SerialNumber;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_main);

        //음료 정보 가이드 라인 팝업창
        ImageView guide = (ImageView) findViewById(R.id.iv_drink_main_guide);
        guide.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(DrinkMainActivity.this,GuideDrinkMainActivity.class);
                startActivity(intent);
            }
        });

        //mainActivity에서 받아온 자판기 정보
        Intent intent = getIntent();
        String vendingName = "이름 : "+intent.getStringExtra("name");;
        String vendingDescription = "설명 : " + intent.getStringExtra("description");;
        String vendingFullSize = "칸 수 : " + intent.getStringExtra("fullSize");
        final String _vendingSerialNumber = intent.getStringExtra("serialNumber");
        final String vendingSerialNumber = "등록번호 : " + _vendingSerialNumber;
        Log.d("TAG","listview 클릭 : "+vendingName+ " : " +vendingDescription + " : " + vendingFullSize + " : " + vendingSerialNumber);
        printVendingInfo(vendingName,vendingDescription,vendingFullSize,vendingSerialNumber);

        //gridview Adapter 생성 및 연결
        final GridView drinkGridView = (GridView)findViewById(R.id.drink_gridView);
        final DrinkListAdapter drinkAdater = new DrinkListAdapter();
        //음료정보 파싱싱
        RequestQueue queue = Volley.newRequestQueue((this));
        final String url = "http://192.168.0.31:80/mobile/drink/read";
        StringRequest drinkRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");


                    //add_drink_item 생성(음료 추가버튼)

                    Log.d("TAG","결과 : " + success);

                    if(success){
                        JSONArray jsonArray = object.getJSONArray("drinks");
                        //음료 정보 json 파싱
                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject drink = jsonArray.getJSONObject(i);
                            //음료 수많큼 gridview에 drink_item 생성
                            drinkAdater.addDrinkItem(drink.getString("position"),drink.getString("name"),drink.getString("price"),drink.getInt("maxCount"),drink.getInt("count"));
                            Log.d("TAG",i+"번쨰 어댑터 값 :  " + drink.getInt("position")+" : " + drink.getString("name")+" : " + drink.getString("price") + " : " + drink.getString("count"));
                        }

                        //add_drink_item 생성(음료 추가버튼)
                        drinkAdater.addDrinkItem(_vendingSerialNumber);
                        //gridview 목록 출력
                        drinkGridView.setAdapter(drinkAdater);


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "요청 실패", Toast.LENGTH_SHORT).show();
                        drinkAdater.addDrinkItem(_vendingSerialNumber);
                        drinkGridView.setAdapter(drinkAdater);
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
                params.put("serialNumber", _vendingSerialNumber);
                return params;
            }
        };

        queue.add(drinkRequest);


        //음료정보 각 Item 클릭 리스너
        drinkGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final DrinkItem drinkItem = (DrinkItem) drinkAdater.getItem(position);

                // 팝업 메시지 객체 생성
                AlertDialog.Builder ad = new AlertDialog.Builder(DrinkMainActivity.this);
                ad.setTitle("음료 수정");

                // 여러개의 입력 창을 다이얼로그에 띄우기 위한 레이아웃 생성
                LinearLayout layout = new LinearLayout(DrinkMainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                // 입력창 생성
                final EditText et_drink_name = new EditText(DrinkMainActivity.this);
                final EditText et_drink_price = new EditText(DrinkMainActivity.this);
                final EditText et_drink_max_count = new EditText(DrinkMainActivity.this);
                et_drink_name.setHint("음료 이름");
                et_drink_price.setHint("음료 가격");
                et_drink_max_count.setHint("음료를 최대로 채울 수 있는 개수");

                // 레이아웃에 EditText 추가
                layout.addView(et_drink_name);
                layout.addView(et_drink_price);
                layout.addView(et_drink_max_count);

                // 다이얼로그에 뷰 세트
                ad.setView(layout);

                // 수정 버튼 클릭시 실행
                ad.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {

                        // 입력창에 입력 값 불러오기
                        final String drinkName = et_drink_name.getText().toString();
                        final String drinkPrice = et_drink_price.getText().toString();
                        final String drinkMaxCount = et_drink_max_count.getText().toString();

                        // drink JSON 객체 생성
                        JSONObject drink = new JSONObject();

                        // 내부 데이터 삽입
                        try {
                            drink.put("position", drinkItem.getDrinkPosition());
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
                            object.put("serialNumber", _vendingSerialNumber);
                            object.put("drink", drink);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // 요청 데이터 출력
                        Log.d("TAG", "결과 : " + object);

                        // Volly를 사용하여 요청 큐 인스턴스 생성
                        RequestQueue queue = Volley.newRequestQueue(DrinkMainActivity.this);

                        // 요청 URL 생성
                        final String URL = "http://192.168.0.31:80/mobile/drink/update";

                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // 사용하고자 하는 데이터 파싱
                                    boolean success = response.getBoolean("success");

                                    // 서버의 데이터 처리 성공 여부 검사
                                    if (success) {
                                        dialogInterface.dismiss(); // 팝업 메시지 닫기
                                        Toast.makeText(getApplicationContext(), "음료수 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // 서버의 작업 실패 메세지 불러오기
                                        String msg = response.getString("msg");
                                        Log.e("msg", msg);
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

    public void printVendingInfo(String name,String description, String fullsize,String serialNumber){
        TextView vendingName = (TextView) findViewById(R.id.drinkPageVendingName);
        TextView vendingDescription = (TextView) findViewById(R.id.drinkPageVendingDescription);
        TextView vendingFullsize = (TextView) findViewById(R.id.drinkPageVendingFullsize);
        TextView vendingSerialNumber =(TextView) findViewById(R.id.drinkPageVendingSerialNumber);

        vendingName.setText(name);
        vendingDescription.setText(description);
        vendingFullsize.setText(fullsize);
        vendingSerialNumber.setText(serialNumber);
    }
    public void setSerialNumber(String SerialNumber){
        this.SerialNumber = SerialNumber;
    }

    public String getSerialNumber(){
        return SerialNumber;
    }

}
