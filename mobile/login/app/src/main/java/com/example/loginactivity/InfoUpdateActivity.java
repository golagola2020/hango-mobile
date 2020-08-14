package com.example.loginactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoUpdateActivity extends AppCompatActivity {

    EditText et_user_name, et_user_id, et_user_email, et_user_passwd, et_user_new_passwd, et_user_new_passwd_check;
    Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_update);

        // xml 객체 파싱
        et_user_name = (EditText)findViewById(R.id.et_user_name);
        et_user_id = (EditText)findViewById(R.id.et_user_id);
        et_user_email = (EditText)findViewById(R.id.et_user_email);
        et_user_passwd = (EditText)findViewById(R.id.et_user_passwd);
        et_user_new_passwd = (EditText)findViewById(R.id.et_user_new_passwd);
        et_user_new_passwd_check = (EditText)findViewById(R.id.et_user_new_passwd_check);

        btn_update = (Button)findViewById(R.id.btn_update);



    }
}