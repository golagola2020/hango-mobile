package com.example.loginactivity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class VendingListAdapter extends BaseAdapter {


    LayoutInflater inflater = null;
    private ArrayList<VendingData> VData = null;
    private int nListCnt = 0;

    private ListBtnClickListener ListBtnClickListener;

    public VendingListAdapter(ArrayList<VendingData> _VData){
        VData = _VData;
        nListCnt = VData.size();
    }



    public interface ListBtnClickListener{
        void onListBtnClick(int position);
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

    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            final Context context = parent.getContext();
            if(inflater == null){
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent,false);
        }
        TextView TextVendingName = (TextView) convertView.findViewById(R.id.vending_list_name);

        TextView TextVendingDiscription = (TextView) convertView.findViewById(R.id.vending_list_description);

        TextVendingName.setText((position+1) +". "+ VData.get(position).VendingName);
        TextVendingDiscription.setText(VData.get(position).VendingDescription);

        ImageButton btnVendingUpdate = (ImageButton) convertView.findViewById(R.id.Btn_vending_update);
        ImageButton btnVendingDelete = (ImageButton) convertView.findViewById(R.id.Btn_vending_delete);
        btnVendingUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                Log.i("TAG",position+" : update");
                MainActivity main = new MainActivity();
                main.intentVendingUpdate(VData.get(position).vendingSerialNumber);
            }
        });
        btnVendingDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.i("TAG",position+" : delete");
                MainActivity main = new MainActivity();
                main.VendingDeleteRequest(VData,VData.get(position).vendingSerialNumber);
            }
        });

        return convertView;
    }




}
