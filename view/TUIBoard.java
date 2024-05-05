package view;

import model.*;

import static model.BoardConstants.*;

/**
 * Class which prints a text based version of the game board
 */
public class TUIBoard {
    private final Board board;

    public TUIBoard(Board board) {
        this.board = board;
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
                else if(board.getBoard()[i][j] instanceof Board.RayTrails rayTrails){
                    for (Board.RayTrail r : rayTrails.getRayTrails()) {
                        if(r.getOrientation() == 60 || r.getOrientation() == 240){
                            System.out.print(ANSI_PINK + "/ " + ANSI_RESET);
                        }
                        else if(r.getOrientation() == 0 || r.getOrientation() == 180){
                            System.out.print(ANSI_PINK + "- " + ANSI_RESET);
                        }
                        else{
                            System.out.print(ANSI_PINK + "\\ " + ANSI_RESET);
                        }
                        break;
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
}
