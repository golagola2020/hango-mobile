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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class VendingListAdapter extends BaseAdapter {


    LayoutInflater inflater = null;
    private ArrayList<VendingData> VData = new ArrayList<>();
    private Context context;


    public VendingListAdapter(Context context){
        this.context = context;
    }

    public void addItem(String VendingName,String VendingDescripcion,String VendingSerialNumber,int VendingFullsize){
        VendingData vdata = new VendingData();
        vdata.setVendingName(VendingName);
        vdata.setVendingDescription(VendingDescripcion);
        vdata.setVendingSerialNumber(VendingSerialNumber);
        vdata.setVendingFullsize(VendingFullsize);
        VData.add(vdata);
    }



    @Override
    public int getCount() {
        return VData.size();
    }

    @Override
    public Object getItem(int position) {

        return VData.get(position);
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
            /*TextView TextVendingName = (TextView) convertView.findViewById(R.id.vending_list_name);
            TextView TextVendingDiscription = (TextView) convertView.findViewById(R.id.vending_list_description);

            TextVendingName.setText((position + 1) + ". " + vdata.getVendingName());
            TextVendingDiscription.setText(vdata.getVendingDescription());
            ImageView btnVendingUpdate = (ImageView) convertView.findViewById(R.id.Btn_vending_update);
            ImageView btnVendingDelete = (ImageView) convertView.findViewById(R.id.Btn_vending_delete);

            //자판기 listview 수정버튼 기능
            btnVendingUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,UpdateVendingActivity.class);
                    intent.putExtra("VendingSerialNumber",vdata.getVendingSerialNumber());
                    v.getContext().startActivity(intent);
                }
            });

            //자판기 listview 삭제버튼기능
            btnVendingDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("TAG", position + " : delete");
                    MainActivity main = new MainActivity();
                    main.VendingDeleteRequest(VData, vdata.getVendingSerialNumber());
                }
            });*/
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
                MainActivity main = new MainActivity();
                main.VendingDeleteRequest(VData, vdata.getVendingSerialNumber());
            }
        });

        return convertView;
    }
    public class ViewHolder{
            TextView VendingNameText;
            TextView VendingDescriptionText;
            ImageView VendingUpdateImage;
            ImageView VendingDeletImage;
    }



}
