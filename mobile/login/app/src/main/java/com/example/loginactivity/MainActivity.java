package com.example.loginactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;



import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView vendingListView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView idText = (TextView)findViewById(R.id.NameText);
        Intent intent = getIntent();
        final String UserId = intent.getStringExtra("userId"); //intent로 받아온 userID
        printUserName(UserId);

        //자판기 정보
        final ArrayList<VendingData> VData = new ArrayList<>();
        final JSONArray vendingArray = new JSONArray();

        //자판기 정보 ListView 출력 기능

        //ListView, Adapter 생성 및 연결
        vendingListView = (ListView) findViewById(R.id.MainListView);

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
                    if(success){

                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject vending = jsonArray.getJSONObject(i);
                            VendingData vdata = new VendingData();
                            vdata.VendingName = vending.getString("name");
                            vdata.VendingDescription = vending.getString("description");
                            vdata.vendingSerialNumber = vending.getString("serialNumber");

                            VData.add(vdata);
                        }
                        //자판기 보유수 출력
                        printVendingCount(VData);

                        //listview 목록 출력
                        VendingListAdapter vendingAdapter = new VendingListAdapter(VData);
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
                params.put("userId", UserId);
                return params;
            }
        };

        queue.add(strReq);


    }
    private void printVendingCount(ArrayList<VendingData> VData){
        TextView vendingCountText = (TextView) findViewById(R.id.VendingCount);
        final VendingListAdapter vendingListAdapter = new VendingListAdapter(VData);
        int _vendingCount = vendingListAdapter.getCount();
        String _vendingCountText = "보유중인 자판기 수 는 " + _vendingCount + " 대입니다.";
        SpannableStringBuilder s_vendingCountText = new SpannableStringBuilder(_vendingCountText);
        s_vendingCountText.setSpan(new ForegroundColorSpan(Color.parseColor("#d3d3d3")), 0, _vendingCountText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        vendingCountText.setText(_vendingCountText);
    }
    private void printUserName(String UserId){
        TextView idText = (TextView) findViewById(R.id.NameText);
        String _UserId = UserId + "님";
        SpannableStringBuilder s_User_Id = new SpannableStringBuilder(_UserId);
        s_User_Id.setSpan(new ForegroundColorSpan(Color.parseColor("#ff7f00")), 0, UserId.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s_User_Id.setSpan(new RelativeSizeSpan(3.0f), 0, UserId.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        idText.setText(s_User_Id);

        //자판기 정보 ListView 출력 기능
        final ArrayList<VendingData> VData = new ArrayList<>();

        //자판기 데이터 파싱하기
        RequestQueue queue = Volley.newRequestQueue((this));
        String url = "http://ec2-3-34-207-199.ap-northeast-2.compute.amazonaws.com/mobile/vending";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("vending");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject vending = jsonArray.getJSONObject(i);
                                VendingData vdata = new VendingData();
                                vdata.Vending_name = vending.getString("vending_name");
                                vdata.Vending_discription = vending.getString("vending_discription");
                                VData.add(vdata);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

                });
        queue.add(request);


        //ListView, Adapter 생성 및 연결
        vendingListView = (ListView)findViewById(R.id.MainListView);
        VendingListAdapter vendingAdapter = new VendingListAdapter(VData);
        vendingListView.setAdapter(vendingAdapter);

    }
    public void intentVendingUpdate(String SerialNumber){
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra("SerialNumber", SerialNumber);
        startActivity(intent);
    }

    public void VendingDeleteRequest(final ArrayList<VendingData> VData, final String SerialNumber){
        RequestQueue queue = Volley.newRequestQueue((this));
        final String url = "http://192.168.0.31:80/mobile/vending/delete";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    if(success){
                        VendingListAdapter vendingAdapter = new VendingListAdapter(VData);
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
