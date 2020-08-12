package com.example.loginactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_main);

        //mainActivity에서 받아온 자판기 정보로 수정예정
        String vendingName = null;
        String vendingDescription = null;
        int vendingFullSize = 0;
        final String vendingSerialNumber = null;
        printVendingInfo(vendingName,vendingDescription,vendingFullSize,vendingSerialNumber);

        //음료정보 파싱싱
       RequestQueue queue = Volley.newRequestQueue((this));
        final String url = "http://192.168.0.31:80/mobile/drink/read";
        StringRequest drinkRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    JSONArray jsonArray = object.getJSONArray("drinks");
                    GridView drinkGridView = findViewById(R.id.drink_gridView);
                    DrinkListAdapter drinkAdater = new DrinkListAdapter();
                    if(success){
                        //음료 정보 json 파싱
                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject drink = jsonArray.getJSONObject(i);
                            //음료 수많큼 gridview에 drink_item 생성
                            drinkAdater.addDrinkItem(drink.getInt("position"),drink.getString("name"),drink.getString("price"));
                        }

                        //add_drink_item 생성(음료 추가버튼)
                        drinkAdater.addDrinkItem();
                        //gridview 목록 출력
                        drinkGridView.setAdapter(drinkAdater);


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
                params.put("serialNumber", vendingSerialNumber);
                return params;
            }
        };

        queue.add(drinkRequest);


    }

    public void printVendingInfo(String name,String description, int fullsize,String serialNumber){
        TextView vendingName = (TextView) findViewById(R.id.drinkPageVendingName);
        TextView vendingDescription = (TextView) findViewById(R.id.drinkPageVendingDescription);
        TextView vendingFullsize = (TextView) findViewById(R.id.drinkPageVendingFullsize);
        TextView vendingSerialNumber =(TextView) findViewById(R.id.drinkPageVendingSerialNumber);

        vendingName.setText(name);
        vendingDescription.setText(description);
        vendingFullsize.setText(fullsize);
        vendingSerialNumber.setText(serialNumber);
    }

}
