package com.example.loginactivity;

public class DrinkItem {
    private int drinkPosition;
    private String drinkName;
    private String drinkPrice;

    private int type;

    public int getDrinkPosition() {
        return this.drinkPosition;
    }

    public String getDrinkName() {
        return this.drinkName;
    }

    public String getDrinkPrice() {
        return this.drinkPrice;
    }

    public int getType(){return this.type;}

    public void setDrinkPosition(int drinkPosition) {
        this.drinkPosition = drinkPosition;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public void setDrinkPrice(String drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    public void setType(int type){this.type = type;}
}
