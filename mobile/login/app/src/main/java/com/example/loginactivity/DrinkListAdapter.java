package com.example.loginactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DrinkListAdapter extends BaseAdapter {

    private static final int ITEM_VIEW_TYPE_DRINK_INFO = 0;
    private static final int ITEM_VIEW_TYPE_ADD_DRINK = 1;
    private static final int ITEM_VIEW_TYPE_MAX = 2;

    private String serialNumber;

    ArrayList<DrinkItem> drinkItems = new ArrayList<DrinkItem>();

    public void addDrinkItem(String position, String name , String price, int maxCount,int count){
        DrinkItem item = new DrinkItem();
        item.setType(ITEM_VIEW_TYPE_DRINK_INFO);
        item.setDrinkPosition(position);
        item.setDrinkName(name);
        item.setDrinkPrice(price);
        item.setCount(count);
        item.setMaxCount(maxCount);
        drinkItems.add(item);
    }
    public void addDrinkItem(String serialNumber){
        this.serialNumber = serialNumber;
        DrinkItem item =new DrinkItem();
        item.setType(ITEM_VIEW_TYPE_ADD_DRINK);
        drinkItems.add(item);
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX ;
    }

    // position 위치의 아이템 타입 리턴.
    @Override
    public int getItemViewType(int position) {
        return drinkItems.get(position).getType() ;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;
        DrinkItem drinkItem = drinkItems.get(position);

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            holder = new ViewHolder();

            switch (viewType) {
                case ITEM_VIEW_TYPE_DRINK_INFO:
                    convertView = inflater.inflate(R.layout.drink_item,
                            parent, false);

                    holder.nameText = (TextView) convertView.findViewById(R.id.nameText) ;
                    holder.positionText = (TextView) convertView.findViewById(R.id.positionText) ;
                    holder.priceText = (TextView) convertView.findViewById(R.id.priceText);
                    holder.ivDrinkAmountCircle = (ImageView)convertView.findViewById(R.id.iv_drink_amount_circle);

                    convertView.setTag(holder);

                    break;
                case ITEM_VIEW_TYPE_ADD_DRINK:
                    convertView = inflater.inflate(R.layout.add_drink_item,
                            parent, false);
                    ImageView btn_add_drink = (ImageView) convertView.findViewById(R.id.btn_add_drink);

                    btn_add_drink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(context,AddDrinkActivity.class);
                            intent.putExtra("position",position+1);
                            Log.d("TAG","포지션 값 : " + position+1);
                            intent.putExtra("VendingSerialNumber",serialNumber);
                            v.getContext().startActivity(intent);
                        }
                    });

                    break;
            }
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(viewType == ITEM_VIEW_TYPE_DRINK_INFO){
            holder.nameText.setText(drinkItems.get(position).getDrinkName());
            holder.positionText.setText(drinkItems.get(position).getDrinkPosition());
            holder.priceText.setText(drinkItems.get(position).getDrinkPrice());

            Log.d("TAG","음료 남은량 : "+((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount()));

            if(((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 >=70) {
                GradientDrawable GradientDrawable = (GradientDrawable) holder.ivDrinkAmountCircle.getBackground();
                GradientDrawable.setColor(Color.BLUE);
            }
            else if((((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 >=40) &&
                    (((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 <70)){
                GradientDrawable GradientDrawable = (GradientDrawable)holder.ivDrinkAmountCircle.getBackground();
                GradientDrawable.setColor(Color.GREEN);
            }
            else if(((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 <40){
                GradientDrawable GradientDrawable = (GradientDrawable)holder.ivDrinkAmountCircle.getBackground();
                GradientDrawable.setColor(Color.RED);
            }

        }

        return convertView;
    }
    public class ViewHolder{
        TextView positionText;
        TextView nameText;
        TextView priceText;
        ImageView ivDrinkAmountCircle;
    }

}
