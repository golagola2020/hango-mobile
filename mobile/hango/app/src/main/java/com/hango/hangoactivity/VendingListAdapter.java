package com.hango.hangoactivity;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VendingListAdapter extends BaseAdapter implements Filterable {

    String userId;

    LayoutInflater inflater = null;
    // 자판기 정보 ArrayList
    public ArrayList<VendingData> VendingData = new ArrayList<>();
    // 검색된 자판기 정보를 저장하기 위한 ArrayList
    private ArrayList<VendingData> filteredVendingData = VendingData;
    private Context context;

    Filter VendingDataFilter;

    // VendingListAdapter 생성자
    public VendingListAdapter(Context context){
        this.context = context;
    }

    // 자판기 정보 Item 생성자
    public void addItem(String VendingName,String VendingDescripsion,String VendingSerialNumber,int VendingFullsize){
        VendingData vdata = new VendingData();
        vdata.setVendingName(VendingName);
        vdata.setVendingDescription(VendingDescripsion);
        vdata.setVendingSerialNumber(VendingSerialNumber);
        vdata.setVendingFullsize(VendingFullsize);
        VendingData.add(vdata);
    }

    // UserId Setter,Getter
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    // 자판기 data 초기화 method
    public void itemClear(){
        VendingData.clear();
    }

    // 자판기의 수를 반환
    @Override
    public int getCount() {
        return filteredVendingData.size();
    }

    // position에 해당하는 자판지 정보 반환
    @Override
    public Object getItem(int position) {

        return filteredVendingData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    // MainAcitiviy 에 자판기 정보 ListView Item 출력
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        // position에 해당하는 VendingData
        final VendingData vendingData = VendingData.get(position);

        ViewHolder holder;

        if(convertView == null) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            holder = new ViewHolder();

            holder.VendingNameText = (TextView) convertView.findViewById(R.id.vending_list_name);
            holder.VendingDescriptionText = (TextView) convertView.findViewById(R.id.vending_list_description);

            // 각 Item 의 '수정' ImageView
            holder.VendingUpdateImage = (ImageView) convertView.findViewById(R.id.Btn_vending_update);
            // 각 Item 의 '삭제' ImageView
            holder.VendingDeletImage = (ImageView) convertView.findViewById(R.id.Btn_vending_delete);
            convertView.setTag(holder);

        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.VendingNameText.setText((position+1) + ". " + VendingData.get(position).getVendingName());
        holder.VendingDescriptionText.setText(VendingData.get(position).getVendingDescription());

        // '수정' ImageView Click Listener
        holder.VendingUpdateImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateVendingActivity.class);
                intent.putExtra("serialNumber",vendingData.getVendingSerialNumber());
                v.getContext().startActivity(intent);
            }
        });

        // '삭제' ImageView Click Listener
        holder.VendingDeletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RequestQueue 생성
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

                // 요청 URL
                Network network = new Network();
                final String url = network.getURL() + "/mobile/vending/delete";
                StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            boolean success = object.getBoolean("success");
                            if(success){
                            }
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
                        params.put("serialNumber", vendingData.getVendingSerialNumber());
                        return params;
                    }
                };

                queue.add(strReq);
            }
        });

        return convertView;
    }

    // 검색 기능을 위한 Filter 생성
    @Override
    public Filter getFilter() {
        if(VendingDataFilter == null){
            VendingDataFilter = new ListFilter();
        }
        return VendingDataFilter;
    }

    // 자판기 Item 중복출력 방지를 위한 ViewHolder
    public class ViewHolder{
            TextView VendingNameText;
            TextView VendingDescriptionText;
            ImageView VendingUpdateImage;
            ImageView VendingDeletImage;
    }

    // 자판기 정보 검색 class
    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = VendingData ;
                results.count = VendingData.size() ;
            } else {
                ArrayList<VendingData> itemList = new ArrayList<VendingData>() ;

                for (VendingData item : VendingData) {
                    if (item.getVendingName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getVendingDescription().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item) ;
                    }
                }

                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // update listview by filtered data list.
            filteredVendingData = (ArrayList<VendingData>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }

}
