package com.hango.hangoactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LicenseListAdapter  extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> licenseArray;

    private ViewHolder mViewHolder;

    public LicenseListAdapter(Context mContext, ArrayList<String> licenseArray) {
        this.mContext = mContext;
        this.licenseArray = licenseArray;
    }

    @Override
    public int getCount() {
        return licenseArray.size();
    }

    @Override
    public Object getItem(int position) {
        return licenseArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHoldr 패턴
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.license_item, parent, false);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        // View에 Data 세팅
        mViewHolder.tv_license_name.setText(licenseArray.get(position));
        return convertView;
    }



    public class ViewHolder {
        private TextView tv_license_name;
        public ViewHolder(View convertView) {

            tv_license_name = (TextView) convertView.findViewById(R.id.tv_license_name);

        }

    }

}
