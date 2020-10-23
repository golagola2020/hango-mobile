package com.hango.hangoactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hango.environment.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VendingDeleteActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vending_delete);

        // '예' Button
        Button btn_delete_accept = (Button)findViewById(R.id.btn_delete_accept);
        // '아니오' Button
        Button btn_delete_deny = (Button)findViewById(R.id.btn_delete_deny);

        Intent intent = getIntent();
        final String serialNumber = intent.getStringExtra("serialNumber");

        // '예' Button Click Listener
        btn_delete_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVending(serialNumber);
                finish();
            }
        });

        btn_delete_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void deleteVending(final String serialNumber){
        //RequestQueue 생성
        RequestQueue queue = Volley.newRequestQueue(this);

        // 요청 URL
        Network network = new Network();
        final String url = network.getURL() + "/mobile/vending/delete";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    if (success) {
                        finish();
                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("serialNumber", serialNumber);
                return params;
            }
        };

        queue.add(strReq);
    }
}
