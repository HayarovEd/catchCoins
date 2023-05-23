package com.edurda77.sample2;

public class Brick {
    private  boolean isVisisble;
    public  int row, column, width, height;

    public Brick(int row, int column, int width, int height) {
        isVisisble = true;
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
    }

    public  void setInvisible(){
        isVisisble = false;
    }
    public boolean getVisibility() {
        return isVisisble;
    }
}
