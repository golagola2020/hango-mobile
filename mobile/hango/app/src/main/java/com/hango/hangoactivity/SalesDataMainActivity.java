package com.hango.hangoactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hango.environment.Network;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesDataMainActivity extends AppCompatActivity {
    BarChart bar_chart_main;

    // 현재시간을 msec 으로 구한다.
    long now = System.currentTimeMillis();
    // 현재시간을 date 변수에 저장한다.
    Date date = new Date(now);
    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
    SimpleDateFormat nowYear = new SimpleDateFormat("yy");
    // nowDate 변수에 값을 저장한다.
    String year = nowYear.format(date);
    SimpleDateFormat nowMonth = new SimpleDateFormat("MM");
    String month = nowMonth.format(date);
    SimpleDateFormat nowDay = new SimpleDateFormat("dd");
    String day = nowDay.format(date);

    int monthSale = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesdata_main);
        bar_chart_main = (BarChart)findViewById(R.id.bar_chart_main);
        bar_chart_main.clearChart();
        Intent intent = getIntent();
        final String UserId = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("userName");

        Button vendingsSaleData = (Button)findViewById(R.id.btn_vendings_total_sale);
        vendingsSaleData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // 유저 정보 화면으로 userId 전달
                Intent intent = new Intent(SalesDataMainActivity.this, VendingSalesDataActivity.class);
                intent.putExtra("userId", UserId);
                startActivity(intent);
            }
        });

        Button drinksSaleData = (Button)findViewById(R.id.btn_drinks_total_sale);
        drinksSaleData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                // 유저 정보 화면으로 userId 전달
                Intent intent = new Intent(SalesDataMainActivity.this, DrinkSalesDataActivity.class);
                intent.putExtra("userId", UserId);
                startActivity(intent);
            }
        });

        TextView tv_year_total_sales = (TextView)findViewById(R.id.tv_year_total_sales);
        tv_year_total_sales.setText("<"+year+"년 월별 매출>");

        salesDataParser(UserId,userName);


        bar_chart_main.startAnimation();

    }

    //userName 출력 method, String 형의 userName을 인자로 받는다
    private void printUserInfo(String userName){
        TextView nameText = (TextView) findViewById(R.id.tv_salesdata_name);
        TextView saleText = (TextView) findViewById(R.id.tv_month_total_sales);
        TextView dateText = (TextView) findViewById(R.id.tv_month_sales_date);

        String _UserId = userName + "님의 이번달 총 매출";
        SpannableStringBuilder s_User_Id = new SpannableStringBuilder(_UserId);
        s_User_Id.setSpan(new ForegroundColorSpan(Color.parseColor("#2db73e")), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s_User_Id.setSpan(new RelativeSizeSpan(1.0f), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String totalSales = monthSale + "원";
        SpannableStringBuilder s_totalSales = new SpannableStringBuilder(totalSales);
        s_totalSales.setSpan(new RelativeSizeSpan(1.5f), 0, totalSales.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        String monthDate = month + ".01~"+month+"."+day;



        nameText.setText(s_User_Id);
        saleText.setText(s_totalSales);
        dateText.setText(monthDate);

    }

    // 음료정보 파싱 method, Adapter 와 GridView를 인자로 받는다
    public void salesDataParser(final String userId, final String userName){


        // RequestQueue 생성
        RequestQueue queue = Volley.newRequestQueue((this));

        // 데이터를 송수신 할 서버 URL
        Network network = new Network();
        final String URL = network.getURL() +"/mobile/stats/read";  //URL + 음료정보 파싱 API Key

        // Request 생성
        StringRequest drinkRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);

                    // 응답 데이터가 존재 할 경우 true, 존재하지 않을 경우 false 값을 반환하는 key(success)값
                    boolean success = object.getBoolean("success");
                    JSONObject users = object.getJSONObject("users");
                    JSONArray date = users.getJSONArray("saleDate");
                    JSONArray price = users.getJSONArray("price");



                    if(success){
                        int average=0;
                        int count=0;
                        for(int i =0;i<date.length();i++){
                            String[] buf = date.getString(i).split("-");


                            if(Integer.parseInt(buf[0])==20){
                                average += price.getInt(i);
                                count++;
                                bar_chart_main.addBar(new BarModel(buf[1] + "월", price.getInt(i), 0xFF56B7F1));
                                if(Integer.parseInt(buf[1])==Integer.parseInt(month)){
                                    monthSale = price.getInt(i);
                                }
                            }
                        }
                        bar_chart_main.addBar(new BarModel("평균", average/count, 0xFFe6f4fa));
                        printUserInfo(userName);
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

}
