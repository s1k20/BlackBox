package view;

import controller.Game;
import model.*;

import java.sql.SQLOutput;

import static model.BoardConstants.*;

public class GameView {

    Game game;
    Board board;

    public GameView(Game game){
        this.game = game;
        this.board = game.getBoard();
    }

    public void printStart() {
        String title = "Welcome to Black Box Plus by Cian, Lloyd and Shlok (demo version v3 - (terminal))";
        System.out.print(ANSI_BLUE); printLine('=', title.length()); System.out.print(ANSI_RESET);
        System.out.println(title);
        System.out.print(ANSI_BLUE); printLine('=', title.length());System.out.print(ANSI_RESET);

        System.out.println("Please input a number to select an option from the list below:");
        printLine('-', title.length());
        System.out.println("1. Play single player game");
        System.out.println("2. Play 2 player game");
        System.out.println("3. Rules");
        System.out.println("4. Settings");
        System.out.println("5. Quit");
    }

    public void printLine(char c, int length) {
        for (int i = 0; i < length; i++) {
            System.out.print(c);
        }
        System.out.println();
    }

    //function just prints text based board
    //"view" part of our software for now but will eventually be replaced by a gui
    public void printEntireBoard(){
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){

                //space to create hexagon shape at the bottom left
                if(i > 5 && j == 0){
                    for(int k = 5; k < i; k++){
                        System.out.print(" ");
                    }
                }
                //find out what kind of object at current co-ords to then print to display
                if(board.getBoard()[i][j] instanceof Board.NullHex){
                    System.out.print(" ");
                }
                else if(board.getBoard()[i][j] instanceof Board.EmptyMarker){
                    System.out.print("- ");
                }
                else if(board.getBoard()[i][j] instanceof Atom){
                    System.out.print(ANSI_RED + "o " + ANSI_RESET);
                }
                else if(board.getBoard()[i][j] instanceof IntersectingCircleOfInfluence){
                    System.out.print(ANSI_GREEN + "x " + ANSI_RESET);
                }
                else if(board.getBoard()[i][j] instanceof CircleOfInfluence c){

                    //find out what orientation to then print correct character to terminal
                    switch (c.getOrientation()) {
                        case 60, 240 -> System.out.print(ANSI_GREEN + "/ " + ANSI_RESET);
                        case 90, 270 -> System.out.print(ANSI_GREEN + "| " + ANSI_RESET);
                        default -> System.out.print(ANSI_GREEN + "\\ " + ANSI_RESET);
                    }

                }
                else if(board.getBoard()[i][j] instanceof Board.RayTrail rayTrail){
                    if(rayTrail.getOrientation() == 60 || rayTrail.getOrientation() == 240){
                        System.out.print(ANSI_PINK + "/ " + ANSI_RESET);
                    }
                    else if(rayTrail.getOrientation() == 0 || rayTrail.getOrientation() == 180){
                        System.out.print(ANSI_PINK + "- " + ANSI_RESET);
                    }
                    else{
                        System.out.print(ANSI_PINK + "\\ " + ANSI_RESET);
                    }
                }
                else if(board.getBoard()[i][j] instanceof RayMarker r){

                    System.out.print(r.getColour() + "+ " + ANSI_RESET);
                }
                else{
                    System.out.print("x ");
                }
            }

            //new line to create hexagon visualisation
            System.out.println();
        }
    }

    //function which will print live version of board
    //ie only contain ray marker positions, where it enters and exits the board
    public void printLiveBoard(){
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){

                //space to create hexagon shape at the bottom left
                if(i > 5 && j == 0){
                    for(int k = 5; k < i; k++){
                        System.out.print(" ");
                    }
                }

                if(board.getBoard()[i][j] instanceof Board.NullHex){
                    System.out.print(" ");
                }
                else if(board.getBoard()[i][j] instanceof Board.EmptyMarker){
                    System.out.print("- ");
                }
                else if(board.getBoard()[i][j] instanceof RayMarker r){

                    System.out.print(r.getColour() + "+ " + ANSI_RESET);
                }
                else{
                    System.out.print("x ");
                }
            }

            //new line to create hexagon visualisation
            System.out.println();
        }
    }

    public void printWinner(Player winner, int length) {
        printLine('=', length);
        if (winner != null) System.out.println(winner.getPlayerName() + " won!");
        else System.out.println("Draw!");
    }

    public void printStats(Player p1, Player p2, Player winner) {

        String scores = "Final scores - " + p1.getPlayerName() + ": " + p1.getScore() + " | " + p2.getPlayerName() + ": " + p2.getScore();
        String p1Stat = "Player 1 - " + p1.getPlayerName() + " | " + p1.getNumSentRays() + " rays sent | " + p1.getNumCorrectAtoms() + " correctly guessed atoms";
        String p2Stat = "Player 2 - " + p2.getPlayerName() + " | " + p2.getNumSentRays() + " rays sent | " + p2.getNumCorrectAtoms() + " correctly guessed atoms";

        int length = Math.max(scores.length(), p1Stat.length());
        length = Math.max(length, p2Stat.length());
        printWinner(winner, length);

        printLine('=', length);
        System.out.println(scores);
        printLine('=', length);
        System.out.println("Statistics: ");
        printLine('*', length);
        System.out.println(p1Stat);
        printLine('-', length);
        System.out.println(p2Stat);
        printLine('-', length);
    }

    public void printRound(int roundNum) {
        int length = 10;
        String round = "Round " + roundNum;
        String roundMessage = "Setter: " + game.getSetter().getPlayerName() + " | Experimenter: " + game.getExperimenter().getPlayerName();
        System.out.print(ANSI_BLUE); printLine('+', roundMessage.length() + length + length); System.out.print(ANSI_RESET);
        for (int i = 0; i < length + (roundMessage.length() / 2) - (round.length() / 2); i++) System.out.print(" ");
        System.out.println(round);
        for (int i = 0; i < length; i++) System.out.print(" ");
        System.out.print(ANSI_YELLOW); System.out.println(roundMessage); System.out.print(ANSI_RESET);
        System.out.print(ANSI_BLUE); printLine('+', roundMessage.length() + length + length); System.out.print(ANSI_RESET);
    }

}
