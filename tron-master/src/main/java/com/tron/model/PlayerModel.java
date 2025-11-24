package com.tron.model;

import java.util.ArrayList;
import java.util.List;

public abstract class PlayerModel extends GameObjectModel {
    protected TronColor color;
    protected boolean alive = true;
    protected boolean jump = false;
    protected boolean booster = false;

    protected int startVel = 0;
    protected int boostLeft = 3;
    protected long boostStartTime = 0;
    protected final long BOOST_DURATION_NANO = 300_000_000L;

    public static final int WIDTH = 5;
    public static final int HEIGHT = 5;
    public static final int VELBOOST = 5;
    public static final int JUMPHEIGHT = 16;

    protected List<PathSegment> path = new ArrayList<>();

    public PlayerModel(int x, int y, int velocityX, int velocityY, int width, int height, TronColor color) {
        super(x, y, velocityX, velocityY, width, height);
        startVel = Math.max(Math.abs(velocityX), Math.abs(velocityY));
        this.color = color;
    }

    public int getBoostsLeft() {
        return boostLeft;
    }

    @Override
    public void accelerate() {
        if (x < 0 || x > rightBound) {
            velocityX = 0;
            alive = false;
        }
        if (y < 0 || y > bottomBound) {
            velocityY = 0;
            alive = false;
        }
    }

    public void jump() {
        jump = true;
    }

    public void startBoost() {
        if (boostLeft > 0 && !booster) {
            booster = true;
            boostStartTime = System.nanoTime();
            boostLeft--;
        }
    }

    public void updateBoost(long currentTime) {
        if (booster) {
            if (currentTime - boostStartTime >= BOOST_DURATION_NANO) {
                booster = false;
            }
            if (velocityX > 0) {
                velocityX = VELBOOST;
            } else if (velocityX < 0) {
                velocityX = -VELBOOST;
            } else if (velocityY > 0) {
                velocityY = VELBOOST;
            } else if (velocityY < 0) {
                velocityY = -VELBOOST;
            }
        } else {
            if (velocityX > 0) {
                velocityX = startVel;
            } else if (velocityX < 0) {
                velocityX = -startVel;
            } else if (velocityY > 0) {
                velocityY = startVel;
            } else if (velocityY < 0) {
                velocityY = -startVel;
            }
        }
    }

    @Override
    public void move() {
        int oldX = x;
        int oldY = y;
        super.move();
        if (oldX != x || oldY != y) {
            if (alive && (velocityX != 0 || velocityY != 0)) {
                path.add(new PathSegment(oldX, oldY, x, y));
            }
        }
    }

    public boolean checkCrash(List<PlayerModel> allPlayers) {
        if (!alive) return true;
        if (path.size() > 5) {
            for (int i = 0; i < path.size() - 2; i++) {
                if (intersects(path.get(i))) {
                    alive = false;
                    return true;
                }
            }
        }
        for (PlayerModel otherPlayer : allPlayers) {
            if (otherPlayer == this) continue;
            if (intersects(otherPlayer.x, otherPlayer.y, otherPlayer.width, otherPlayer.height)) {
                alive = false;
                return true;
            }
            for (PathSegment segment : otherPlayer.getPath()) {
                if (intersects(segment)) {
                    alive = false;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean intersects(PathSegment segment) {
        int playerMinX = x - WIDTH / 2;
        int playerMaxX = x + WIDTH / 2;
        int playerMinY = y - HEIGHT / 2;
        int playerMaxY = y + HEIGHT / 2;

        int segStartX = segment.getStartX();
        int segStartY = segment.getStartY();
        int segEndX = segment.getEndX();
        int segEndY = segment.getEndY();

        if (segStartY == segEndY) {
            int lineMinX = Math.min(segStartX, segEndX);
            int lineMaxX = Math.max(segStartX, segEndX);
            int lineY = segStartY;

            return (playerMaxY >= lineY && playerMinY <= lineY && playerMaxX >= lineMinX && playerMinX <= lineMaxX);
        } else if (segStartX == segEndX) {
            int lineMinY = Math.min(segStartY, segEndY);
            int lineMaxY = Math.max(segStartY, segEndY);
            int lineX = segStartX;

            return (playerMaxX >= lineX && playerMinX <= lineX && playerMaxY >= lineMinY && playerMinY <= lineMaxY);
        }
        return false;
    }

    private boolean intersects(int otherX, int otherY, int otherWidth, int otherHeight) {
        return (x < otherX + otherWidth && x + width > otherX && y < otherY + otherHeight && y + height > otherY);
    }

    public TronColor getColor() {
        return color;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    public List<PathSegment> getPath() {
        return path;
    }

    public abstract void setDirection(Direction direction);

    public enum TronColor {
        BLUE, ORANGE, GREEN, RED, YELLOW, PINK
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
