package com.hango.hangoactivity;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.hango.environment.Network;

import org.eazegraph.lib.models.BarModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VendingSalesDataActivity extends AppCompatActivity {

        // 현재시간을 msec 으로 구한다.
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat nowYear = new SimpleDateFormat("yy");
        // nowDate 변수에 값을 저장한다.
        String year = nowYear.format(date);


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_vendings_salesdata);
            Intent intent = getIntent();
            final String userId = intent.getStringExtra("userId");

            salesDataParser(userId);

        }


        // 음료정보 파싱 method, Adapter 와 GridView를 인자로 받는다
        public void salesDataParser(final String userId){


            // RequestQueue 생성
            RequestQueue queue = Volley.newRequestQueue((this));

            // 데이터를 송수신 할 서버 URL
            Network network = new Network();
            final String URL = network.getURL() +"/mobile/stats/vending/read";  //URL + 음료정보 파싱 API Key

            // Request 생성
            StringRequest drinkRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject object = new JSONObject(response);

                        // 응답 데이터가 존재 할 경우 true, 존재하지 않을 경우 false 값을 반환하는 key(success)값
                        boolean success = object.getBoolean("success");
                        JSONObject vendings = object.getJSONObject("vendings");
                        JSONArray vendingName = vendings.getJSONArray("name");




                        if(success){
                            for(int i =0;i<vendings.length()-1;i++){
                                JSONObject vendingSaleData = vendings.getJSONObject(vendingName.getString(i));
                                JSONArray vendingSaleDate = vendingSaleData.getJSONArray("saleDate");
                                JSONArray vendingPrice = vendingSaleData.getJSONArray("price");

                                addBarChart(vendingSaleDate,vendingPrice,vendingName.getString(i));


                            }

                        }
                        else{

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
                    // 자판기에 해당하는 음료정보를 받기위해 서버가 요청하는 자판기 SerialNumber 전송
                    params.put("userId",userId);
                    return params;
                }
            };

            // RequestQueue 실행
            queue.add(drinkRequest);
        }

        public void addBarChart(JSONArray saleDate,JSONArray price, String vendingName) throws JSONException {

            LinearLayout vending_dynamic_bar_chart = (LinearLayout)findViewById(R.id.vending_dynamic_bar_chart);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    600, 2.0f);
            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 2.0f);

            org.eazegraph.lib.charts.BarChart barChart = new org.eazegraph.lib.charts.BarChart(VendingSalesDataActivity.this);
            barChart.clearChart();

            for(int i =0;i<saleDate.length();i++){
                String[] buf = saleDate.getString(i).split("-");
                if(Integer.parseInt(buf[0])==Integer.parseInt(year)){
                    barChart.addBar(new BarModel(buf[1] + "월", price.getInt(i), 0xFF56B7F1));
                }
            }
            barChart.setPadding(0,100,0,0);
            barChart.setBarMargin(200.0f);
            barChart.setBarWidth(100.0f);
            barChart.startAnimation();
            TextView tv_vending_name = new TextView(this);
            tv_vending_name.setGravity(LinearLayout.VERTICAL);
            tv_vending_name.setText("<"+year + "년도 " +vendingName+"자판기의 월 매출>");
            tv_vending_name.setPadding(0,0,0,100);
            vending_dynamic_bar_chart.addView(barChart,param);
            vending_dynamic_bar_chart.addView(tv_vending_name,param1);
        }

}


