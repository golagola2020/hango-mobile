package com.example.loginactivity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VendingListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<VendingData> VData = null;
    private int nListCnt = 0;

    public VendingListAdapter(ArrayList<VendingData> _VData){
        VData = _VData;
        nListCnt = VData.size();
    }

    @Override
    public int getCount() {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            final Context context = parent.getContext();
            if(inflater == null){
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent,false);
        }
        TextView TextVendingName = (TextView) convertView.findViewById(R.id.vending_list_name);
        TextView TextVendingDiscription = (TextView) convertView.findViewById(R.id.vending_list_discription);

        TextVendingName.setText(VData.get(position).Vending_name);
        TextVendingDiscription.setText(VData.get(position).Vending_discription);

        return convertView;
    }
}
