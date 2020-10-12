package com.hango.hangoactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class DrinkListAdapter extends BaseAdapter {

    // 음료 정보 Item View Type
    private static final int ITEM_VIEW_TYPE_DRINK_INFO = 0;
    // 음료 추가 Item View Type
    private static final int ITEM_VIEW_TYPE_ADD_DRINK = 1;
    private static final int ITEM_VIEW_TYPE_MAX = 2;

    // 자판기 최대 칸 수
    private int fullSize;
    // 자판기 SerialNumber
    private String serialNumber;

    ArrayList<DrinkItem> drinkItems = new ArrayList<DrinkItem>();


    // 음료정보 Item 생성자
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

    // 음료추가 Item 생성자
    public void addDrinkItem(String serialNumber){
        this.serialNumber = serialNumber;
        DrinkItem item =new DrinkItem();
        item.setType(ITEM_VIEW_TYPE_ADD_DRINK);
        drinkItems.add(item);
    }

    // 자판기 최대 칸 수 Setter&Getter
    public void setFullSize(int fullSize){
        this.fullSize = fullSize;
    }

    public int getFullSize(){
        return fullSize;
    }

    // 자판기 SerialNumber Setter&Getter
    public void setSerialNumber(String serialNumber){
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber(){
        return serialNumber;
    }

    // 음료정보 초기화
    public void itemClear(){
        drinkItems.clear();
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

    // 음료개수 반환
    @Override
    public int getCount() {

        return drinkItems.size();
    }

    // position 위치의 음료정보 리턴
    @Override
    public Object getItem(int position) {

        return drinkItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    // DrinkMainActivity의 음료정보 GridView Item 출력
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            holder = new ViewHolder();

            // viewType 변수에 따른 Item 생성
            switch (viewType) {
                //음료 정보 Item View Type
                case ITEM_VIEW_TYPE_DRINK_INFO:
                    convertView = inflater.inflate(R.layout.drink_item,
                            parent, false);

                    holder.tv_drink_item_drink_name = (TextView) convertView.findViewById(R.id.tv_drink_item_drink_name) ;
                    holder.tv_drink_item_position = (TextView) convertView.findViewById(R.id.tv_drink_item_position) ;
                    holder.tv_drink_item_price = (TextView) convertView.findViewById(R.id.tv_drink_item_price);
                    holder.iv_drink_item_amount_circle = (ImageView)convertView.findViewById(R.id.iv_drink_item_amount_circle);

                    convertView.setTag(holder);

                    break;

                //음료 추가 Item View Type
                case ITEM_VIEW_TYPE_ADD_DRINK:
                    convertView = inflater.inflate(R.layout.add_drink_item,
                            parent, false);
                    ImageView btn_add_drink = (ImageView) convertView.findViewById(R.id.btn_add_drink);

                    //음료 추가 Item Click Listener
                    btn_add_drink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if(getFullSize()<=position){
                                Toast.makeText(context, "자판기 최대 칸수 이상의 음료는 만들 수 없습니다", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent = new Intent(context, AddDrinkActivity.class);
                                intent.putExtra("position", position + 1);
                                intent.putExtra("serialNumber", serialNumber);
                                v.getContext().startActivity(intent);
                            }
                        }
                    });

                    break;
            }
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(viewType == ITEM_VIEW_TYPE_DRINK_INFO){
            holder.tv_drink_item_drink_name.setText(drinkItems.get(position).getDrinkName());
            holder.tv_drink_item_position.setText(drinkItems.get(position).getDrinkPosition());
            holder.tv_drink_item_price.setText(drinkItems.get(position).getDrinkPrice()+"원");


            if(((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 >=70) {
                GradientDrawable GradientDrawable = (GradientDrawable) holder.iv_drink_item_amount_circle.getBackground();
                GradientDrawable.setColor(Color.BLUE);
            }
            else if((((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 >=40) &&
                    (((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 <70)){
                GradientDrawable GradientDrawable = (GradientDrawable)holder.iv_drink_item_amount_circle.getBackground();
                GradientDrawable.setColor(Color.GREEN);
            }
            else if(((float)drinkItems.get(position).getCount()/(float)drinkItems.get(position).getMaxCount())*100 <40){
                GradientDrawable GradientDrawable = (GradientDrawable)holder.iv_drink_item_amount_circle.getBackground();
                GradientDrawable.setColor(Color.RED);
            }

        }

        return convertView;
    }

    // 음료정보 Item 중복출현 방지를 위한 ViewHolder
    public class ViewHolder{
        TextView tv_drink_item_position;
        TextView tv_drink_item_drink_name;
        TextView tv_drink_item_price;
        ImageView iv_drink_item_amount_circle;
    }

}
