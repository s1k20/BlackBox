package model;

public class Player {

    private final String playerName;
    private boolean isSetter;
    private int score;
    private int numSentRays;
    private int numCorrectAtoms;

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

    public int getScore() {
        return this.score;
    }

    public void correctAtom() {
        this.numCorrectAtoms += 1;
    }

    public void removeCorrectAtom() {
        this.numCorrectAtoms -= 1;
    }

    public int getNumCorrectAtoms() {
        return this.numCorrectAtoms;
    }

    public void raySent() {
        numSentRays += 1;
    }

    public int getNumSentRays() {
        return this.numSentRays;
    }

}
