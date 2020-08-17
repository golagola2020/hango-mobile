package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.GridView;
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

import java.util.HashMap;
import java.util.Map;

public class DrinkMainActivity extends AppCompatActivity {
    private String SerialNumber;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_main);

        //mainActivity에서 받아온 자판기 정보로 수정예정
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
                            drinkAdater.addDrinkItem(drink.getInt("position"),drink.getString("name"),drink.getString("price"));
                            Log.d("TAG",i+"번쨰 어댑터 값 :  " + drink.getInt("position")+" : " + drink.getString("name")+" : " + drink.getString("price"));
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
