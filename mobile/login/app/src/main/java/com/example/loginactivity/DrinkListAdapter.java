package com.example.loginactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DrinkListAdapter extends BaseAdapter {
    ArrayList<DrinkItem> drinkItems = new ArrayList<DrinkItem>();
    Context context;

    public void addDrinkItem(DrinkItem drinkItem){
        drinkItems.add(drinkItem);
    }

    @Override
    public int getCount() {
        return drinkItems.size();
    }

    @Override
    public Object getItem(int position) {
        return drinkItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        context = parent.getContext();
        DrinkItem drinkItem = drinkItems.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drink_item,parent,false);
        }

        TextView positionText = convertView.findViewById(R.id.positionText);
        TextView nameText = convertView.findViewById(R.id.nameText);
        TextView priceText = convertView.findViewById(R.id.priceText);

        positionText.setTextColor(drinkItem.getDrinkPosition());
        nameText.setText(drinkItem.getDrinkName());
        priceText.setText(drinkItem.getDrinkPrice());

        return convertView;
    }
}
