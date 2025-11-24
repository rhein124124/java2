package com.tron.model;

public abstract class GameObjectModel {
    protected int x;
    protected int y;

    protected int width;
    protected int height;

    protected int velocityX;
    protected int velocityY;

    protected int rightBound;
    protected int bottomBound;

    public GameObjectModel(int x, int y, int velocityX, int velocityY, int width, int height) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.width = width;
        this.height = height;
    }

    public void setBounds(int width, int height) {
        rightBound = width - this.width;
        bottomBound = height - this.height;
    }

    public void setXVelocity(int velocityX) {
        if (!(velocityX > 0 && this.velocityX < 0) && !(velocityX < 0 && this.velocityX > 0)) {
            this.velocityX = velocityX;
        }
    }

    public void setYVelocity(int velocityY) {
        if (!(velocityY > 0 && this.velocityY < 0) && !(velocityY < 0 && this.velocityY > 0)) {
            this.velocityY = velocityY;
        }
    }

    public void move() {
        x += velocityX;
        y += velocityY;

        accelerate();
        clip();
    }

    public void clip() {
        if (x < 0) x = 0;
        else if (x > rightBound) x = rightBound;

        if (y < 0) y = 0;
        else if (y > bottomBound) y = bottomBound;
    }
    
    public abstract void accelerate();
    public abstract boolean isAlive(); 
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getVelocityX() { return velocityX; }
    public int getVelocityY() { return velocityY; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}
