package com.tron.model;

import com.tron.model.PlayerModel.TronColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class TronMapModel {

    protected PlayerHumanModel humanPlayer;
    protected List<PlayerModel> players;
    protected List<TronColor> availableColors;

    protected Random rand = new Random();

    public static final int MAPWIDTH = 500;
    public static final int MAPHEIGHT = 500;
    public static final int VELOCITY = 3;

    protected int scorePlayer1 = 0;
    protected int scorePlayer2 = 0;

    protected boolean gameRunning = false;

    public TronMapModel(int numPlayers) {
        availableColors = new ArrayList<>();
        for (TronColor c : TronColor.values()) {
            availableColors.add(c);
        }
        
        this.players = new ArrayList<>(numPlayers);
    }

    public void setPlayers(List<PlayerModel> players) {
        this.players = players;
    }
    
    public abstract void tick(double dt);
    public abstract void reset();
    public abstract void updateScores();
    public abstract void addScore(int playerIndex, int scoreToAdd);

    public int[] getRandomStart() {
        int[] start = new int[4];
        int xnew = 50 + rand.nextInt(400);
        int ynew = 50 + rand.nextInt(400);
        int ra = rand.nextInt(2);
        int velx = 0;
        int vely = 0;

        if (ra == 0) {
            if (xnew < MAPWIDTH / 2) {
                velx = VELOCITY;
            } else {
                velx = -VELOCITY;
            }
        } else {
            if (ynew < MAPHEIGHT / 2) {
                vely = VELOCITY;
            } else {
                vely = -VELOCITY;
            }
        }
        start[0] = xnew;
        start[1] = ynew;
        start[2] = velx;
        start[3] = vely;
        return start;
    }

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public List<PlayerModel> getPlayers() {
        return players;
    }

    public PlayerHumanModel getHumanPlayer() {
        return humanPlayer;
    }
}
