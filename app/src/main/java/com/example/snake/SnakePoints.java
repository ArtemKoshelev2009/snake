package com.example.snake;

public class SnakePoints {

    private int positionX, PositionY;

    public SnakePoints(int positionX, int positionY) {
        this.positionX = positionX;
        PositionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return PositionY;
    }

    public void setPositionY(int positionY) {
        PositionY = positionY;
    }
}
