package com.hango.hangoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.hango.environment.Network;

public class AddDrinkActivity extends AppCompatActivity {


    private Button btn_regi_drink;
    private EditText et_drink_name,et_drink_price,et_drink_max_count;
    private ImageView iv_arrow_back_add_drink;

    private String serialNumber;
    private int position;

    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_drink);

        //Activity의 "추가"버튼
        btn_regi_drink = findViewById(R.id.btn_add_drink);

        //각각 Activity의 '이름', '가격', '최대개수' EditText
        et_drink_name = findViewById(R.id.et_drink_name);
        et_drink_price = findViewById(R.id.et_drink_price);
        et_drink_max_count = findViewById(R.id.et_max_count);

        //뒤로가기 버튼
        iv_arrow_back_add_drink = findViewById(R.id.iv_arrow_back_add_drink);

        iv_arrow_back_add_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //DrinkMainActivity로부터 넘어온 intent
        Intent intent = getIntent();
        serialNumber = intent.getStringExtra("serialNumber");   //서버에서 자판기를 판별하기위한 변수
        position = intent.getIntExtra("position",1);    //서버에서 자판기의 음료수를 판별하기위한 변수수

        final RequestQueue queue = Volley.newRequestQueue((this));

        btn_regi_drink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String drinkName = et_drink_name.getText().toString();
                String stringDataDrinkPrice;
                int drinkPrice;
                String stringDataDrinkMaxCount;
                int drinkMaxCount;

                try{
                    stringDataDrinkPrice = et_drink_price.getText().toString();
                    drinkPrice = Integer.parseInt(stringDataDrinkPrice);
                    stringDataDrinkMaxCount = et_drink_max_count.getText().toString();
                    drinkMaxCount = Integer.parseInt(stringDataDrinkMaxCount);

                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "음료 가격과 음료 최대 개수는 숫자만 입력 가능 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (drinkName.length() == 0 || stringDataDrinkPrice.length() == 0 || stringDataDrinkMaxCount.length() == 0) {
                    Toast.makeText(getApplicationContext(), "모든 정보는 필수 입력 사항입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }


                JSONObject drinkInfo = new JSONObject();
                try {
                    drinkInfo.put("position",position);
                    drinkInfo.put("name",drinkName);
                    drinkInfo.put("price",drinkPrice);
                    drinkInfo.put("maxCount",drinkMaxCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject object = new JSONObject();

                try {
                    object.put("serialNumber", serialNumber);
                    object.put("drink",drinkInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Network network = new Network();
                String URL = network.getURL() + "/mobile/drink/create";


                JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL,object, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            boolean success = response.getBoolean("success");
                            if (success) {

                                finish();

                            } else {
                                Toast.makeText(getApplicationContext(), "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                queue.add(objectRequest);
            }
        });
    }
}
