package model;

import static model.BoardConstants.*;

public class Board {

    //colour index to which is used to generate colours
    private static int colourIndex = 31;

    //object array which is the board of the game
    private final Object[][] board = new Object[HEIGHT][WIDTH];

    public Board(){
        //initialise board
        initBoard();
    }

    //function to init board and place empty ray markers in correct position and null hex in others
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

    //getter for certain position on board
    public Object getBoardPosition(int x, int y){
        return this.board[y][x];
    }

    public Object[][] getBoard(){
        return this.board;
    }


    public void placeAtom(int x, int y){
        Atom newAtom = new Atom(x, y);

        //places atom into the board
        //y and x inverted as x = j and y = i
        board[y][x] = newAtom;

        //calls function which takes the new atom as argument and places its circle of
        //influence around atom which has been worked out when atom was created
        placeCircleOfInfluence(newAtom);
    }

    public void placeCircleOfInfluence(Atom a){
        //loop through all atom "a" circle of influence
        for(CircleOfInfluence c : a.circleOfInfluence){

            //check to make sure atom is not getting overridden and not placing outside of main board
            if(!(board[c.getYCo_ord()][c.getXCo_ord()] instanceof Atom) && !(board[c.getYCo_ord()][c.getXCo_ord()] instanceof emptyMarker)) {

                //in the case where one part of circle of influence intersects another part;
                if(board[c.getYCo_ord()][c.getXCo_ord()] instanceof CircleOfInfluence i){

                    //create new intersectingcirc. object to place previous and new circle of influence
                    IntersectingCircleOfInfluence s = new IntersectingCircleOfInfluence();

                    //add both parts to new object
                    s.addPart(i);
                    s.addPart(c);

                    //place new object in place of intersecting circle of influences
                    board[c.getYCo_ord()][c.getXCo_ord()] = s;
                }

                //in the case where one part of a circle of influence intersects and intersection of influences
                else if(board[c.getYCo_ord()][c.getXCo_ord()] instanceof IntersectingCircleOfInfluence s){

                    //just add new part to old array list
                    s.addPart(c);
                }
                else{
                    //blank hex then just add the circle of influence
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
    //TODO make it work for certain deflections/reflection and absorption
    public void placeRayMarker(int x, int y){

        //validation that position is valid ray marker position
        if(!checkRayMarker(y, x)){
            throw new IllegalArgumentException("Invalid Position for Ray Marker");
        }

        //just creates a new ray marker in a certain position and creates a random colour for it
        RayMarker r = new RayMarker(x, y, generateColour());

        //place new ray marker into board
        board[y][x] = r;
    }

    //classes which have no functionality other than representing a position on board
    //null hex is a position in the object array which cants be accessed in the game
    //empty ray marker is the perimeter of the board; position which will hold ray markers for rays
    public static class nullHex{
    }
    public static class emptyMarker{
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

}
