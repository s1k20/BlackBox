package model;

public class Player {

    private final String playerName;
    private boolean isSetter;
    private int score;

    public Player(String playerName, boolean isSetter){
        this.playerName = playerName;
        this.isSetter = isSetter;
        this.score = 0;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public boolean isSetter(){
        return isSetter;
    }

    public void setSetter(){
        this.isSetter = true;
    }

    public void setExperimenter(){
        this.isSetter = false;
    }

    public void updateScore(int n){
        this.score += n;
    }

}
