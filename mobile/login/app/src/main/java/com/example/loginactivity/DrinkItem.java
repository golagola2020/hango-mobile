package com.example.loginactivity;

public class DrinkItem {
    private String drinkPosition;
    private String drinkName;
    private String drinkPrice;
    private int maxCount;
    private int remainCount;

    private int type;

    public String getDrinkPosition() {
        return this.drinkPosition;
    }

    public String getDrinkName() {
        return this.drinkName;
    }

    public String getDrinkPrice() {
        return this.drinkPrice;
    }

    public int getMaxCount(){return this.maxCount;}

    public int getRemainCount(){return this.remainCount;}

    public int getType(){return this.type;}

    public void setDrinkPosition(String drinkPosition) {
        this.drinkPosition = drinkPosition;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public void setDrinkPrice(String drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    public void setMaxCount(int maxCount){this.maxCount =maxCount;}

    public void setRemainCount(int remainCount){this.remainCount = remainCount;}

    public void setType(int type){this.type = type;}
}
