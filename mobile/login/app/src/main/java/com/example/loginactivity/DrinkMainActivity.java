package com.example.loginactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.GridView;
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

        //mainActivity에서 받아온 serialNumber로 수정예정
        final String serialNumber = null;

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

                        for(int i =0;i<jsonArray.length();i++){
                            JSONObject drink = jsonArray.getJSONObject(i);
                            drinkAdater.addDrinkItem(new DrinkItem(drink.getInt("position"),drink.getString("name"),drink.getString("price")));
                        }


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
                params.put("serialNumber", serialNumber);
                return params;
            }
        };

        queue.add(drinkRequest);


    }

}
