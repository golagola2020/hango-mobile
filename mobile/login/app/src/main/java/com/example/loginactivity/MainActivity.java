package com.example.loginactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;



import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        TextView idText = (TextView)findViewById(R.id.NameText);


        ImageButton btn_user_info = (ImageButton)findViewById(R.id.btn_user_info);

        // 로그인 화면에서 유저 이름 받아오기

        Intent intent = getIntent();
        final String UserId = intent.getStringExtra("userId"); //intent로 받아온 userID

        Log.d("TAG","받아온 id 값: " + UserId);

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

        //자판기 정보
        final ArrayList<VendingData> VData = new ArrayList<>();
        final JSONArray vendingArray = new JSONArray();

        //ListView, Adapter 생성 및 연결
        final ListView vendingListView = (ListView) findViewById(R.id.MainListView);
        final VendingListAdapter vendingAdapter = new VendingListAdapter(MainActivity.this);

        vendingAdapter.setUserId(UserId);

        //자판기 데이터 파싱하기
        RequestQueue queue = Volley.newRequestQueue((this));

        final String url = "http://192.168.0.31:80/mobile/vending/read";


        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    JSONArray jsonArray = object.getJSONArray("vendings");
                    String userName = object.getString("userName");
                    if(success){

                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject vending = jsonArray.getJSONObject(i);
                            Log.d("TAG","출력 : " +vending);
                            vendingAdapter.addItem(vending.getString("name"),vending.getString("description"),vending.getString("serialNumber"),vending.getInt("fullSize"));
                        }
                        //자판기 보유수 출력
                        printVendingCount(vendingAdapter.getCount());

                        //유저 이름 출력
                        printUserName(userName);
                        //listview 목록 출력
                        vendingListView.setAdapter(vendingAdapter);
                        //vendingAdapter.notifyDataSetChanged();
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
                params.put("userId", UserId);
                return params;
            }
        };

        queue.add(strReq);


        //각 자판기 list클릭시 이벤트
        vendingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VendingData vdata = (VendingData) vendingAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(),DrinkMainActivity.class);
                intent.putExtra("name",vdata.getVendingName());
                intent.putExtra("description",vdata.getVendingDescription());
                String fullsize = Integer.toString(vdata.getVendingFullsize());
                intent.putExtra("fullSize",fullsize);
                intent.putExtra("serialNumber",vdata.getVendingSerialNumber());
                Log.d("TAG","listview 클릭 : "+ vdata.getVendingName() + " : "+ vdata.getVendingDescription() + " : "+ vdata.getVendingFullsize() + " : "+ vdata.getVendingSerialNumber());
                startActivity(intent);
            }

        });


    }
    public void test(){

    }
    //자판기 수 출력
    private void printVendingCount(int vendingCount){
        TextView vendingCountText = (TextView) findViewById(R.id.VendingCount);

        String _vendingCountText = "보유중인 자판기 수 는 " + vendingCount + " 대입니다.";
        SpannableStringBuilder s_vendingCountText = new SpannableStringBuilder(_vendingCountText);
        s_vendingCountText.setSpan(new ForegroundColorSpan(Color.parseColor("#d3d3d3")), 0, _vendingCountText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vendingCountText.setText(_vendingCountText);
    }

    //계정 이름 출력
    private void printUserName(String UserId){
        TextView idText = (TextView) findViewById(R.id.NameText);
        String _UserId = UserId + "님";
        SpannableStringBuilder s_User_Id = new SpannableStringBuilder(_UserId);
        s_User_Id.setSpan(new ForegroundColorSpan(Color.parseColor("#ff7f00")), 0, UserId.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s_User_Id.setSpan(new RelativeSizeSpan(3.0f), 0, UserId.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        idText.setText(s_User_Id);


    }


    public void VendingDeleteRequest(final ArrayList<VendingData> VData, final String SerialNumber){
        final ListView vendingListView = (ListView) findViewById(R.id.MainListView);
        RequestQueue queue = Volley.newRequestQueue((this));
        final String url = "http://192.168.0.31:80/mobile/vending/delete";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    if(success){
                        VendingListAdapter vendingAdapter = new VendingListAdapter(MainActivity.this);
                        vendingListView.setAdapter(vendingAdapter);
                        vendingAdapter.notifyDataSetChanged();
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
                params.put("SerialNumber", SerialNumber);
                return params;
            }
        };

        queue.add(strReq);
    }

}
