import java.util.Scanner;

public class Board {

    //board is 9x9 but + 2 for allowing for ray marker positions
    public final int WIDTH = 11;
    public final int HEIGHT = 11;

    //colour index to which is used to generate colours
    private int colourIndex = 31;

    //test colours
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";


    private final Object[][] board = new Object[HEIGHT][WIDTH];

    public Board(){
        //initialise board
        initBoard();
    }

    private void initBoard() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {

                //conditions if i and j are at an index which could contain a ray marker
                if (checkRayMarker(i, j)) {
                    board[i][j] = new emptyMarker();
                }

                //conditions if i and j are at indexes which are not part of the board
                //but present in order to represent the board
                else if (i + j <= 4 || i + j > 15) {
                    board[i][j] = new nullHex();
                }
            }
        }
    }


    public void placeAtom(int x, int y){
        Atom newAtom = new Atom(x, y);

        //y and x inverted as x = j and y = i
        board[y][x] = newAtom;

        placeCircleOfInfluence(newAtom);

    }

    private void placeCircleOfInfluence(Atom a){
        for(CircleOfInfluence c : a.circleOfInfluence){

            //check to make sure atom is not getting overridden and not placing outside of main board
            if(!(board[c.getYCo_ord()][c.getXCo_ord()] instanceof Atom) && !(board[c.getYCo_ord()][c.getXCo_ord()] instanceof emptyMarker)) {

                //in the case where one part of circle of influence intersects another part;
                if(board[c.getYCo_ord()][c.getXCo_ord()] instanceof CircleOfInfluence i){
                    IntersectingCircleOfInfluence s = new IntersectingCircleOfInfluence();

                    s.addPart(i);
                    s.addPart(c);

                    board[c.getYCo_ord()][c.getXCo_ord()] = s;
                }
                //in the case where one part of a circle of influence intersects and intersection of influences
                else if(board[c.getYCo_ord()][c.getXCo_ord()] instanceof IntersectingCircleOfInfluence s){

                    s.addPart(c);
                }
                else{
                    board[c.getYCo_ord()][c.getXCo_ord()] = c;
                }
            }
        }
    }

    //TODO: change this function to not use constant values and instead will
    //TODO: - calculate each value based on our HEIGHT and WIDTH constants
    //TODO: (make it scalable)
    private static boolean checkRayMarker(int i, int j){
            return i + j == 5 || i + j == 15 || (i == 0 && j >= 5) ||
                    (j == 10 && i < 6) || (i == 10 && j <= 5) || (j == 0 && i >= 5);
    }

    //TODO develop this
    public void placeRayMarker(int x, int y){
        //validation that position is valid ray marker position
        if(!checkRayMarker(y, x)){
            throw new IllegalArgumentException("Invalid Position for Ray Marker");
        }

        RayMarker r = new RayMarker(x, y, generateColour());

        board[y][x] = r;
    }


    //TODO create enums
    private static class nullHex{
    }
    private static class emptyMarker{
    }

    //function to create a different colour for each ray marker

    private String generateColour(){
        //TODO: fix this function so it generates the colour based on
        //TODO: - what kind of reflection ray takes

        //loop colours back to the start
        if(this.colourIndex >= 37){
            this.colourIndex = 31;
        }

        return "\u001B[" + this.colourIndex++ + "m";

    }

    public void printTempBoard(){
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++){
                //space to create hexagon shape at the bottom left
                if(i > 5 && j == 0){
                    for(int k = 5; k < i; k++){
                        System.out.print(" ");
                    }
                }

                //TODO tidy this with switch statements
                if(board[i][j] instanceof nullHex){
                    System.out.print(" ");
                }
                else if(board[i][j] instanceof emptyMarker){
                    System.out.print("- ");
                }
                else if(board[i][j] instanceof Atom){
                    System.out.print(ANSI_RED + "o " + ANSI_RESET);
                }
                else if(board[i][j] instanceof IntersectingCircleOfInfluence){
                    System.out.print(ANSI_GREEN + "x " + ANSI_RESET);
                }
                else if(board[i][j] instanceof CircleOfInfluence c){
                    //TODO more switch statements
                    if(c.getOrientation() == 45){
                        System.out.print(ANSI_GREEN + "/ " + ANSI_RESET);
                    }
                    else if(c.getOrientation() == 90){
                        System.out.print(ANSI_GREEN + "| " + ANSI_RESET);
                    }
                    else{
                        System.out.print(ANSI_GREEN + "\\ " + ANSI_RESET);
                    }

                }
                else if(board[i][j] instanceof RayMarker r){

                    System.out.print(r.getColour() + "+ " + ANSI_RESET);
                }
                else{
                    System.out.print("x ");
                }
            }
            System.out.println();
        }
    }
}


//    * * * *
//   * * * * *
//  * * * * * *
// * * * * * * *
//  * * * * * *
//   * * * * *
//    * * * *
