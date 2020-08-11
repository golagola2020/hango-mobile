package com.example.loginactivity;

public class DrinkItem {
    int drinkPosition;
    String drinkName;
    String drinkPrice;

    public DrinkItem(int drinkPosition,String drinkName,String drinkPrice){
        this.drinkPosition = drinkPosition;
        this.drinkName = drinkName;
        this.drinkPrice = drinkPrice;
    }

    public int getDrinkPosition() {
        return drinkPosition;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public String getDrinkPrice() {
        return drinkPrice;
    }

    public void setDrinkPosition(int drinkPosition) {
        this.drinkPosition = drinkPosition;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public void setDrinkPrice(String drinkPrice) {
        this.drinkPrice = drinkPrice;
    }
}
