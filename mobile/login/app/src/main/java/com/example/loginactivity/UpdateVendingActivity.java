package com.example.loginactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateVendingActivity extends AppCompatActivity {
    private Button btn_add_vending;
    private EditText vending_name, vending_comment, vending_size, vending_SN;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_vending);

        vending_name = findViewById(R.id.Vending_name);
        vending_comment = findViewById(R.id.Vending_comment);
        vending_size = findViewById(R.id.Vending_size);
        vending_SN = findViewById(R.id.Vending_SN);
        btn_add_vending = findViewById(R.id.btn_Regi_vending);

        btn_add_vending.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String vendingName = vending_name.getText().toString();
                String vendingComment = vending_comment.getText().toString();
                String vendingSize = vending_size.getText().toString();
                String vendingSN = vending_SN.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    public void onResponse(String response){
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success){
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"등록에 실패했습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                UpdateVendingRequest createvendingrequest = new UpdateVendingRequest(vendingName,vendingComment,vendingSize,vendingSN, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UpdateVendingActivity.this);
                queue.add(createvendingrequest);

            }

        });

    }
}
