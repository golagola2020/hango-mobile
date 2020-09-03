package com.hango.hangoactivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.hango.environment.Network;

import java.util.HashMap;
import java.util.Map;

public class SignupRequest extends StringRequest {

    // 서버 URL 설정
    private static Network network = new Network();
    final static private String URL = network.getURL() + "/mobile/signup";
    private Map<String, String> map;

    public SignupRequest(String userName, String userId, String userEmail, String userPasswd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userName", userName);
        map.put("userId", userId);
        map.put("userEmail", userEmail);
        map.put("userPasswd", userPasswd);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
