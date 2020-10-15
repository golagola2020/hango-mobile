package com.hango.hangoactivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


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

public class DrinkMainActivity extends AppCompatActivity {

    // 음료정보 GridView Item 출력을 위한 Adapter 생성
    private DrinkListAdapter drinkAdapter = new DrinkListAdapter();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_main);

        //뒤로가기 기능을 위한 ImageView
        ImageView iv_arrow_back_drink_main = (ImageView)findViewById(R.id.iv_arrow_back_drink_main);

        //뒤로가기 기능 구현
        iv_arrow_back_drink_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //gridview Adapter 생성 및 연결
        final GridView drinkGridView = (GridView)findViewById(R.id.drink_gridView);

        //음료 정보 가이드 라인 팝업창
        ImageView iv_drink_main_guide = (ImageView) findViewById(R.id.iv_drink_main_guide);
        iv_drink_main_guide.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(DrinkMainActivity.this,GuideDrinkMainActivity.class);
                startActivity(intent);
            }
        });

        // 음료의 남은 개수를 최대로 채우기 위한 refresh Button Click Listener
        ImageView iv_drink_refresh = (ImageView)findViewById(R.id.iv_drink_refresh);
        iv_drink_refresh.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(DrinkMainActivity.this,DrinkRefreshAcceptActivity.class);
                intent.putExtra("serialNumber",drinkAdapter.getSerialNumber());
                startActivity(intent);
            }
        });

        final android.support.v4.widget.SwipeRefreshLayout swiperefresh_drink_main = (android.support.v4.widget.SwipeRefreshLayout)findViewById(R.id.swiperefresh_drink_main);

        swiperefresh_drink_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                drinkDataParser(drinkAdapter,drinkGridView);
                swiperefresh_drink_main.setRefreshing(false);
            }

        });

        //mainActivity에서 받아온 자판기 정보
        Intent intent = getIntent();
        String vendingName =intent.getStringExtra("name"); // 자판기 이름
        String vendingNameTitle = "이름 : " + vendingName;

        String vendingDescription =intent.getStringExtra("description");   // 자판기 설명
        String vendingDescriptionTitle = "설명 : " + vendingDescription;

        int vendingFullSize = intent.getIntExtra("fullSize",1);
        drinkAdapter.setFullSize(vendingFullSize);     // 자판기 최대 칸 수
        String vendingFullSizeTitle = "칸 수 : " + vendingFullSize;

        final String vendingSerialNumber = intent.getStringExtra("serialNumber");
        drinkAdapter.setSerialNumber(vendingSerialNumber);      // 자판기 serialNumber
        final String vendingSerialNumberTitle = "등록번호 : " + vendingSerialNumber;

        // 선택된 자판기 정보 출력
        printVendingInfo(vendingNameTitle,vendingDescriptionTitle,vendingFullSizeTitle,vendingSerialNumberTitle);


        // 음료정보 각 Item 클릭 리스너
        drinkGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(DrinkMainActivity.this,UpdateDrinkActivity.class);
                intent1.putExtra("position",position);
                intent1.putExtra("serialNumber",vendingSerialNumber);
                startActivity(intent1);
            }
        });

    }

    // 음료정보 파싱 method, Adapter 와 GridView를 인자로 받는다
    public void drinkDataParser(final DrinkListAdapter drinkAdater, final GridView drinkGridView){

        drinkAdater.itemClear();

        // RequestQueue 생성
        RequestQueue queue = Volley.newRequestQueue((this));

        // 데이터를 송수신 할 서버 URL
        Network network = new Network();
        final String URL = network.getURL() +"/mobile/drink/read";  //URL + 음료정보 파싱 API Key

        // Request 생성
        StringRequest drinkRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);

                    // 응답 데이터가 존재 할 경우 true, 존재하지 않을 경우 false 값을 반환하는 key(success)값
                    boolean success = object.getBoolean("success");

                    // 응답 데이터가 존재할 경우 각 음료정보를 GridView Item으로 출력 후 음료 추가 Item 생성
                    if(success){
                        JSONArray jsonArray = object.getJSONArray("drinks");
                        //음료 정보 json 파싱
                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject drink = jsonArray.getJSONObject(i);
                            //음료 수많큼 gridview에 drink_item 생성
                            drinkAdater.addDrinkItem(drink.getString("position"),drink.getString("name"),drink.getString("price"),drink.getInt("maxCount"),drink.getInt("count"));

                        }


                        // add_drink_item 생성(음료 추가버튼)
                        drinkAdater.addDrinkItem(drinkAdater.getSerialNumber());

                        // GridView와 Adapter 연결
                        drinkGridView.setAdapter(drinkAdater);

                    }

                    //응답 데이터가 존재하지 않을 경우 음료 추가Item 만을 생성
                    else{
                        drinkAdater.addDrinkItem(drinkAdater.getSerialNumber());
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
                // 자판기에 해당하는 음료정보를 받기위해 서버가 요청하는 자판기 SerialNumber 전송
                params.put("serialNumber", drinkAdater.getSerialNumber());
                return params;
            }
        };

        // RequestQueue 실행
        queue.add(drinkRequest);
    }

    // 자판기 정보 출력 method, 자판기의 이름, 설명, 최대 칸 수 ,serialNumber를 인자로 받는다
    public void printVendingInfo(String name,String description, String fullsize,String serialNumber){
        TextView tv_drink_page_vending_name = (TextView) findViewById(R.id.tv_drink_page_vending_name);
        TextView tv_drink_page_vending_description = (TextView) findViewById(R.id.tv_drink_page_vending_description);
        TextView tv_drink_page_vending_full_size = (TextView) findViewById(R.id.tv_drink_page_vending_full_size);
        TextView tv_drink_page_vending_serial_number =(TextView) findViewById(R.id.tv_drink_page_vending_serial_number);

        tv_drink_page_vending_name.setText(name);
        tv_drink_page_vending_description.setText(description);
        tv_drink_page_vending_full_size.setText(fullsize);
        tv_drink_page_vending_serial_number.setText(serialNumber);
    }

    // 수정과 같은 작업 후 onResume이 호출 될때 GridView 갱신
    @Override
    protected  void onResume(){
        super.onResume();
        GridView drinkGridView = (GridView)findViewById(R.id.drink_gridView);
        drinkDataParser(drinkAdapter,drinkGridView);
    }
}
