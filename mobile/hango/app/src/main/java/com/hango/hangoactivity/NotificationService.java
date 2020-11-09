package com.hango.hangoactivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class NotificationService extends Service {
    NotificationThread thread;
    String userId;
    private SharedPreferences soldOutData;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId = intent.getStringExtra("userId");
        myServiceHandler handler = new myServiceHandler();
        thread = new NotificationThread(handler);
        thread.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNotificationTitle();
        }
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업

    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {

            drinkDataParser(userId);
        }
    };

    // 음료정보 파싱 method, Adapter 와 GridView를 인자로 받는다
    public void drinkDataParser(final String userId){



        // RequestQueue 생성
        RequestQueue queue = Volley.newRequestQueue(this);

        // 데이터를 송수신 할 서버 URL
        Network network = new Network();
        final String URL = network.getURL() +"/mobile/notification/";  //URL + 음료정보 파싱 API Key

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
                        JSONObject vending = object.getJSONObject("vending");
                        JSONArray vendingNames = vending.getJSONArray("names");
                        JSONObject soldOuts = vending.getJSONObject("soldOuts");
                        JSONArray soldOutData;
                        for(int i=0; i<vendingNames.length();i++){
                            soldOutData = soldOuts.getJSONArray(vendingNames.getString(i));
                            for(int j=0;j<soldOutData.length();j++){
                                if(getSoldOutData(vendingNames.getString(i)+soldOutData.getString(j))){
                                    Log.d("TAG",vendingNames.getString(i)+"자판기의 "+soldOutData.getString(j)+" 품절");
                                    setNotification(vendingNames.getString(i),soldOutData.getString(j),(i+1)*(j+1));
                                    setSoldOutData(vendingNames.getString(i)+soldOutData.getString(j),false);
                                }

                            }
                        }


                    }

                    //응답 데이터가 존재하지 않을 경우 음료 추가Item 만을 생성
                    else{

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
                params.put("userId", userId);
                return params;
            }
        };

        // RequestQueue 실행
        queue.add(drinkRequest);
    }
    private void setSoldOutData(String vendingAndDrink,boolean check){
        soldOutData = getSharedPreferences("soldOutData", MODE_PRIVATE);
        SharedPreferences.Editor editor = soldOutData.edit();
        editor.putBoolean(vendingAndDrink,check);
        editor.apply();
    }
    private boolean getSoldOutData(String vendingAndDrink){
        soldOutData = getSharedPreferences("soldOutData", MODE_PRIVATE);
        return soldOutData.getBoolean(vendingAndDrink, true);
    }

    private void setNotificationTitle(){
        String channelId = "hangoNotification";
        Intent intent = new Intent(NotificationService.this, MainActivity.class);
        intent.putExtra("userId",userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int titleId = 10000;
        Notification notificationTitle = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,"hangoChannel",NotificationManager.IMPORTANCE_LOW);
                ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
                notificationTitle = new Notification.Builder(NotificationService.this,channelId)
                        .setContentTitle(URLDecoder.decode("hango", "UTF-8"))
                        .setContentText(URLDecoder.decode("음료품절 알림이 켜졌습니다.", "UTF-8"))
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        startForeground(titleId,notificationTitle);
    }

    private void setNotification(String vendingName, String drinkName, int id){

        String channelId = "hangoNotification";
        CharSequence name = "hangoPush";
        if(android.os.Build.VERSION.SDK_INT > 25) {
            //푸시를 클릭했을때 이동//
            Intent intent = new Intent(NotificationService.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userId", userId);
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //푸시를 클릭했을때 이동//
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(channelId,name , NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("drink sold out");
            mChannel.enableLights(true);
            mChannel.enableVibration(true);

            try {
                Notification notification = new Notification.Builder(NotificationService.this,channelId)
                        .setContentTitle(URLDecoder.decode(vendingName, "UTF-8"))
                        .setContentText(URLDecoder.decode(drinkName + "품절", "UTF-8"))
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build();
                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(id, notification);



            }
            catch (Exception e) {
                e.printStackTrace();
            }


        }
        else {
            Intent intent = new Intent(NotificationService.this, MainActivity.class);
            intent.putExtra("userId", userId);
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this);
            builder.setContentTitle(vendingName)
                    .setContentText(drinkName + "품절")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentIntent(pendingIntent)
                    .build();

            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(id, notification);
        }
    }
}