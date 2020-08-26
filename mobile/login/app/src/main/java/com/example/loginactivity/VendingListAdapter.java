package com.example.loginactivity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VendingListAdapter extends BaseAdapter implements Filterable {

    String userId;

    LayoutInflater inflater = null;
    public ArrayList<VendingData> VData = new ArrayList<>();
    private ArrayList<VendingData> filteredVData = VData;
    private Context context;

    Filter VDataFilter;

    public VendingListAdapter(Context context){
        this.context = context;
    }

    public void addItem(String VendingName,String VendingDescripsion,String VendingSerialNumber,int VendingFullsize){
        VendingData vdata = new VendingData();
        vdata.setVendingName(VendingName);
        vdata.setVendingDescription(VendingDescripsion);
        vdata.setVendingSerialNumber(VendingSerialNumber);
        vdata.setVendingFullsize(VendingFullsize);
        VData.add(vdata);
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    public void itemClear(){
        VData.clear();
    }

    @Override
    public int getCount() {
        return filteredVData.size();
    }

    @Override
    public Object getItem(int position) {

        return filteredVData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final VendingData vdata = VData.get(position);

        ViewHolder holder;
        if(convertView == null) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            holder = new ViewHolder();

            holder.VendingNameText = (TextView) convertView.findViewById(R.id.vending_list_name);
            holder.VendingDescriptionText = (TextView) convertView.findViewById(R.id.vending_list_description);

            holder.VendingUpdateImage = (ImageView) convertView.findViewById(R.id.Btn_vending_update);
            holder.VendingDeletImage = (ImageView) convertView.findViewById(R.id.Btn_vending_delete);
            convertView.setTag(holder);

        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.VendingNameText.setText((position+1) + ". " + VData.get(position).getVendingName());
        holder.VendingDescriptionText.setText(VData.get(position).getVendingDescription());
        holder.VendingUpdateImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UpdateVendingActivity.class);
                intent.putExtra("VendingSerialNumber",vdata.getVendingSerialNumber());
                v.getContext().startActivity(intent);
            }
        });
        holder.VendingDeletImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", position + " : delete");
                RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
                final String url = "http://192.168.0.31:80/mobile/vending/delete";
                StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            boolean success = object.getBoolean("success");
                            if(success){
                                Log.d("TAG","성공");
                            }
                            else{
                                Log.d("TAG","실패");
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
                        params.put("serialNumber", vdata.getVendingSerialNumber());
                        Log.d("TAG","삭제 요청 시리얼" + vdata.getVendingSerialNumber() );
                        return params;
                    }
                };

                queue.add(strReq);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(VDataFilter == null){
            VDataFilter = new ListFilter();
        }
        return VDataFilter;
    }

    public class ViewHolder{
            TextView VendingNameText;
            TextView VendingDescriptionText;
            ImageView VendingUpdateImage;
            ImageView VendingDeletImage;
    }

    private class ListFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = VData ;
                results.count = VData.size() ;
            } else {
                ArrayList<VendingData> itemList = new ArrayList<VendingData>() ;

                for (VendingData item : VData) {
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
            filteredVData = (ArrayList<VendingData>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }

}
