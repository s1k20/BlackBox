package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static model.BoardConstants.*;

public class Board {

    //colour index to which is used to generate colours
    private static int colourIndex = 31;

    //object array which is the board of the game
    private final Object[][] board = new Object[HEIGHT][WIDTH];

    //TODO make private when testing done
    public final HashMap<Integer, RayInputMap> inputMapping = new HashMap<>();

    private final ArrayList<Ray> sentRays = new ArrayList<>();


    public Board(){
        //initialise board
        initBoard();
        setInputMapping();
    }

    //function to init board and place empty ray markers in correct position and null hex in others
    private void initBoard() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {

                //conditions if i and j are at an index which could contain a ray marker
                if (checkRayMarker(i, j)) {
                    board[i][j] = new EmptyMarker();
                }

                //conditions if i and j are at indexes which are not part of the board
                //but present in order to represent the board
                else if (i + j <= 4 || i + j > 15) {
                    board[i][j] = new NullHex();
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

    public ArrayList<Ray> getSentRays(){
        return this.sentRays;
    }


    public void placeAtom(int x, int y){
        if(checkInvalidInput(y, x)){
            throw new IllegalArgumentException("Invalid position for atom");
        }

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
        for(CircleOfInfluence c : a.getCircleOfInfluence()){

            //check to make sure atom is not getting overridden and not placing outside of main board
            if(!(board[c.getYCo_ord()][c.getXCo_ord()] instanceof Atom) && !(board[c.getYCo_ord()][c.getXCo_ord()] instanceof EmptyMarker)) {

                //in the case where one part of circle of influence intersects another part;
                if(board[c.getYCo_ord()][c.getXCo_ord()] instanceof CircleOfInfluence i){

                    //create new intersecting circ. object to place previous and new circle of influence
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
    public static class NullHex{
    }
    public static class EmptyMarker{
    }

    //function to create a different colour for each ray marker
    private String generateColour(){
        //TODO: fix this function so it generates the colour based on
        //TODO: - what kind of reflection ray takes

        //loop colours back to the start
        if(colourIndex >= 37){
            colourIndex = 31;
        }

        return "\u001B[" + colourIndex++ + "m";
    }

    private static boolean checkInvalidInput(int i, int j){
        return i + j <= 5 || i + j >= 15 || i <= 0 || j <= 0 || i >= 10 || j >= 10;
    }

    //function to map each number between 1 - 54 (ray input positions) to a hexagon and orientation
    public void setInputMapping(){

        //TODO try not hard code this, will try later on
        int SET_LENGTH = 54;

        int x = 5;
        int y = 0;

        for(int i = 1; i <= SET_LENGTH; i++){
            RayInputMap r = new RayInputMap();

            if(i <= 18){
                r.x = x;
                r.y = y;
                if(i < 10){

                    if(i % 2 == 0){
                        r.orientation = 0;
                    }
                    else{
                        r.orientation = 300;
                        x--;
                        y++;
                    }

                }
                else{

                    if(i % 2 == 0){
                        r.orientation = 0;
                        y++;
                    }
                    else{
                        r.orientation = 60;
                    }


                }
                inputMapping.put(i, r);
            }
            else if(i <= 28){
                r.x = x;
                r.y = y;
                if(i % 2 == 0){
                    r.orientation = 120;
                }
                else{
                    r.orientation = 60;
                    x++;
                }
                inputMapping.put(i, r);

            }
            else if(i <= 45){
                if(i == 29){
                    x++;
                    y--;
                }
                r.x = x;
                r.y = y;
                if(i < 37){

                    if(i % 2 == 0){
                        r.orientation = 120;
                        x++;
                        y--;
                    }
                    else{
                        r.orientation = 180;
                    }

                }
                else{
                    if(i % 2 == 0){
                        r.orientation = 240;
                    }
                    else{
                        r.orientation = 180;
                        y--;
                    }


                }
                inputMapping.put(i, r);
            }
            else{
                r.x = x;
                r.y = y;
                if(i % 2 == 0){
                    r.orientation = 240;
                    x--;
                }
                else{
                    r.orientation = 300;
                }
                inputMapping.put(i, r);
            }
        }
    }



    //TODO finish function
    public boolean sendRay(int input){
        System.out.println("Ray entered at " + input);
        RayInputMap rMap = inputMapping.get(input);

        Ray r = new Ray(input);
        sentRays.add(r);

        r.setCurrXCo_ord(rMap.x);
        r.setCurrYCo_ord(rMap.y);
        r.setOrientation(rMap.orientation);

        boolean absorbed = false;

        String colour = generateColour();

        RayMarker start = new RayMarker(rMap.x, rMap.y, colour);
        board[rMap.y][rMap.x] = start;

        while(!(board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] instanceof EmptyMarker) && !absorbed){
            //TODO tidy this up, this if statement is only for an edge case if two rays are shot from the same place
            if(board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] instanceof Atom) {
                start.setColour(ANSI_GREEN);
                r.setOutput(-1);
                return true;
            }

            //for testing
         //   System.out.println(r.getOrientation());

            if(r.getOrientation() == 0){
                r.move0();
            }
            else if(r.getOrientation() == 60){
                r.move60();
            }
            else if(r.getOrientation() == 120){
                r.move120();
            }
            else if(r.getOrientation() == 180){
                r.move180();
            }
            else if(r.getOrientation() == 240){
                r.move240();
            }
            else if(r.getOrientation() == 300){
                r.move300();
            }

            //exit loop if when ray has moved it has met a pre-placed ray marker
            if(board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] instanceof RayMarker){
                break;
            }

            if(placeRay(r.getCurrXCo_ord(), r.getCurrYCo_ord(), r.getOrientation(), r)){
                start.setColour(ANSI_GREEN);
                r.setDeflectionType(-1);
                r.setOutput(-1);
                return true;
            }

        }
        if(!absorbed){
            //if ray is reflected, set colour purple
            if(start.getXCo_ord() == r.getCurrXCo_ord() && start.getYCo_ord() == r.getCurrYCo_ord()){
                colour = "\u001B[35m";
                start.setColour(colour);
                r.setDeflectionType(180);
            }
            else if(r.getDeflectionType() == 120){
                colour = "\u001B[35m";
                start.setColour(colour);
            }
            else if(r.getDeflectionType() == 60){
                colour = "\u001B[33m";
                start.setColour(colour);
            }
            else if(r.getDeflectionType() == 0){
                colour = "\u001B[31m";
                start.setColour(colour);
            }

            RayInputMap m = new RayInputMap();
            m.x = r.getCurrXCo_ord();
            m.y = r.getCurrYCo_ord();
            m.orientation = r.getOrientation() >= 180 ? r.getOrientation() - 180 : r.getOrientation() + 180;

            int exit = findExit(m);
            r.setOutput(exit);

            System.out.println("Ray exited at " + exit);
            board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] = new RayMarker(r.getCurrXCo_ord(), r.getCurrYCo_ord(), colour);
            return false;
        }

        r.setDeflectionType(-1);
        return true;
    }

    private int findExit(RayInputMap r){
        for(int i = 1; i <= 54; i++){
            RayInputMap m = inputMapping.get(i);
            if(r.orientation == m.orientation && r.x == m.x && r.y == m.y){
                return i;
            }
        }
        return -1;
    }

    //will return true if ray is absorbed
    public boolean placeRay(int x, int y, int orientation, Ray r){
        if(!(board[y][x] instanceof Atom || board[y][x] instanceof CircleOfInfluence ||
                board[y][x] instanceof IntersectingCircleOfInfluence || board[y][x] instanceof EmptyMarker)){
            board[y][x] = new RayGraphic(orientation);

            //temporary to make cool graphic
     //      printTempBoard();
    //        try {
//                Thread.sleep(750);
//            } catch (InterruptedException e) {
//                // Handle the exception
//            }

            return false;
        }
        else if(board[y][x] instanceof CircleOfInfluence c){

            //-- ray logic --
            //absorption
            if(orientation - c.getOrientation() == 90 || orientation - c.getOrientation() == -90
                    || orientation + c.getOrientation() == 360){
                //for testing
//                System.out.println(orientation + " " + c.getOrientation());

//                board[y][x] = new RayGraphic(orientation);
                return true;
            }
            else{

                //TODO tidy this up
                if(c.getOrientation() != 270 && c.getOrientation() != 240 && c.getOrientation() != 120){
                    if(r.getOrientation() == 0 && c.getOrientation() == 300){
                        r.setOrientation(360);
                    }
                    if(r.getOrientation() >= c.getOrientation()){
                        r.setOrientation(r.getOrientation() - 60);
                    }
                    else{
                        r.setOrientation(r.getOrientation() + 60);
//                        System.out.println(r.getOrientation());
                        if(r.getOrientation() == 360){
                            r.setOrientation(0);
                        }
                    }
                }
                else if(c.getOrientation() == 240){
                    if(r.getOrientation() == 60){
                        r.setOrientation(r.getOrientation() - 60);
                    }
                    else{
                        r.setOrientation(r.getOrientation() + 60);
                    }
                }
                else if(c.getOrientation() == 120){
                    if(r.getOrientation() == 180){
                        r.setOrientation(r.getOrientation() - 60);
                    }
                    else{
                        r.setOrientation(r.getOrientation() + 60);
                        if(r.getOrientation() == 360){
                            r.setOrientation(0);
                        }
                    }
                }
                else{
                    if(r.getOrientation() == 120){
                        r.setOrientation(r.getOrientation() - 60);
                    }
                    else{
                        r.setOrientation(r.getOrientation() + 60);
                    }
                }
//                System.out.println(r.getOrientation());
                if(r.getDeflectionType() != 120){
                    r.setDeflectionType(60);
                }
                return false;
            }
        }
        return false;
    }

    private static class RayGraphic{
        int orientation;
        private RayGraphic(int orientation){
            this.orientation = orientation;
        }

    }

}
