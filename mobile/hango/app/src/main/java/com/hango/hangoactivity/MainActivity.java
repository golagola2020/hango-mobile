package com.hango.hangoactivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;


import android.view.View;
import android.widget.AdapterView;

import android.widget.EditText;

import android.widget.ImageView;
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
import com.hango.environment.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // 자판기 정보 ListView Adapter 생성
    private VendingListAdapter vendingAdapter = new VendingListAdapter(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btn_user_info = (ImageView) findViewById(R.id.btn_user_info);

        // 로그인 화면에서 유저 이름 받아오기
        Intent intent = getIntent();
        final String UserId = intent.getStringExtra("userId"); //intent로 받아온 userID

        // Adapter 에 userId 저장
        vendingAdapter.setUserId(UserId);

        // 유저 정보 화면(InfoActivity)로 이동
        btn_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 유저 정보 화면으로 userId 전달
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("userId", UserId);
                startActivity(intent);
            }
        });




        //자판기검색 editText
        final EditText vendingSearch = (EditText) findViewById(R.id.et_search_vending);


        //ListView 생성 및 연결
        final ListView vendingListView = (ListView) findViewById(R.id.MainListView);






        //listview 목록 출력
        vendingListView.setAdapter(vendingAdapter);

        //자판기 검색 기능
        vendingSearch.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String filterText = s.toString();
                if(filterText.length() > 0){
                    vendingListView.setFilterText(filterText);
                }
                else{
                    vendingListView.clearTextFilter();
                }
            }
        });

        // 자판기 정보 파싱 및 ListView 출력 method
        vendingDataParser(vendingAdapter);

        //각 자판기 Item Click Listener
        vendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VendingData vdata = (VendingData) vendingAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(),DrinkMainActivity.class);
                intent.putExtra("name",vdata.getVendingName());
                intent.putExtra("description",vdata.getVendingDescription());
                int fullSize = vdata.getVendingFullsize();
                intent.putExtra("fullSize",fullSize);
                intent.putExtra("serialNumber",vdata.getVendingSerialNumber());
                startActivity(intent);

            }

        });


    }


    // 자판기 데이터 파싱 method, Adapter를 인자로 받는다
    public void vendingDataParser(final VendingListAdapter vendingAdapter){

        // 파싱 전 Adapter Item Clear
        vendingAdapter.itemClear();

        //RequestQueue 생성
        RequestQueue queue = Volley.newRequestQueue((this));

        // 파싱 요청 URL
        Network network = new Network();
        final String url = network.getURL() + "/mobile/vending/read";


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);

                    // userId에 해당하는 자판기 정보가 존재 할 경우 true, 그렇지 않을경우 false 를 반환하는 Key("success")
                    boolean success = object.getBoolean("success");

                    // userId에 해당하는 자판기 정보 key("vendings")
                    JSONArray vendingsArray = object.getJSONArray("vendings");

                    // userId에 해당하는 userName key("userName")
                    String userName = object.getString("userName");
                    if(success){
                        // vendings key에 들어있는 자판기 정보를 순차적으로 호출
                        for(int i =0;i<vendingsArray.length();i++){
                            JSONObject vending = vendingsArray.getJSONObject(i);
                            // 각 자판기 정보(name, description, serialNumber, fullSize)를 Adapter에 추가
                            vendingAdapter.addItem(vending.getString("name"),vending.getString("description"),vending.getString("serialNumber"),vending.getInt("fullSize"));

                        }

                        //유저 이름 출력
                        printUserName(userName);

                        //자판기 보유수 출력
                        printVendingCount(vendingAdapter.getCount());

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "요청 실패", Toast.LENGTH_SHORT).show();
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
                // 해당 User의 자판기 데이터 파싱을 위해 서버로 전송 하는 userId
                params.put("userId", vendingAdapter.getUserId());
                return params;
            }
        };

        queue.add(strReq);
    }
    //자판기 수 출력 method , integer 형의 자판기 수를 의미하는 vendingCount를 인자로 받는다
    private void printVendingCount(int vendingCount){
        TextView vendingCountText = (TextView) findViewById(R.id.VendingCount);

        String _vendingCountText = "보유중인 자판기 수 는 " + vendingCount + " 대입니다.";
        SpannableStringBuilder s_vendingCountText = new SpannableStringBuilder(_vendingCountText);
        s_vendingCountText.setSpan(new ForegroundColorSpan(Color.parseColor("#d3d3d3")), 0, _vendingCountText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vendingCountText.setText(_vendingCountText);
    }

    //userName 출력 method, String 형의 userName을 인자로 받는다
    private void printUserName(String userName){
        TextView idText = (TextView) findViewById(R.id.NameText);
        String _UserId = userName + "님";
        SpannableStringBuilder s_User_Id = new SpannableStringBuilder(_UserId);
        s_User_Id.setSpan(new ForegroundColorSpan(Color.parseColor("#2db73e")), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s_User_Id.setSpan(new RelativeSizeSpan(3.0f), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        idText.setText(s_User_Id);


    }

    // 수정, 삭제등 다른 작업 후 onRestart 가 호출되며 ListView 갱신
    @Override
    protected void onRestart() {
        super.onRestart();
        vendingDataParser(vendingAdapter);
    }
}
