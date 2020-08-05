package com.example.loginactivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Parser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView vendingListView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //사용자 이름 출력 기능
        TextView idText = (TextView)findViewById(R.id.NameText);
        Intent intent = getIntent();
        String UserId = intent.getStringExtra("userId");
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
}