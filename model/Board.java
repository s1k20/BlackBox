package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static model.BoardConstants.*;

public class Board {

    //object array which is the board of the game
    private final Object[][] board = new Object[HEIGHT][WIDTH];

    //mapping of numbers from 1 to 54 to valid ray coordinates and orientation
    private final HashMap<Integer, RayInputMap> inputMapping = new HashMap<>();
    public final HashMap<RayOutputPoint, Integer> numberOut = new HashMap<>();

    private final ArrayList<Ray> sentRays = new ArrayList<>();
    private final ArrayList<Atom> placedAtoms = new ArrayList<>();
    private final ArrayList<RayMarker> rayMarkers = new ArrayList<>();
    public Ray currentRay;

    private int numAtomsPlaced;
    public int numRaysSent;


    public Board(){
        //initialise board
        initBoard();
        setInputMapping();
        numAtomsPlaced = 0;
        numRaysSent = 0;
        currentRay = new Ray(-1);
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

    public int getNumAtomsPlaced() {
        return this.numAtomsPlaced;
    }

    public ArrayList<Ray> getSentRays(){
        return this.sentRays;
    }

    public ArrayList<Atom> getPlacedAtoms() {
        return this.placedAtoms;
    }

    public ArrayList<RayMarker> getRayMarkers() {
        return this.rayMarkers;
    }


    public void placeAtom(int x, int y){
        if(checkInvalidInput(y, x)){
            throw new IllegalArgumentException("Invalid position for atom");
        }

        Atom newAtom = new Atom(x, y);
        placedAtoms.add(newAtom);

        //places atom into the board
        //y and x inverted as x = j and y = i
        board[y][x] = newAtom;

        //calls function which takes the new atom as argument and places its circle of
        //influence around atom which has been worked out when atom was created
        placeCircleOfInfluence(newAtom);

        numAtomsPlaced++;
    }

    public void removeAtom(int x, int y) {
        Atom atom = (Atom) board[y][x];
        board[y][x] = null;
        placedAtoms.remove(atom);

        removeCircleOfInfluence(atom);
        replaceAtom();

        numAtomsPlaced--;
    }

    public void replaceAtom() {
        for (Atom a : placedAtoms) {
            placeCircleOfInfluence(a);
        }
    }

    private void removeCircleOfInfluence(Atom atom) {
        for (CircleOfInfluence c : atom.getCircleOfInfluence()) {
            int cX = c.getXCo_ord();
            int cY = c.getYCo_ord();

            if (board[cY][cX] instanceof CircleOfInfluence) {
                board[cY][cX] = null;
            }
            else if (board[cY][cX] instanceof IntersectingCircleOfInfluence i) {
                if (i.getCircleOfInfluences().size() >= 3) {
                    i.removePart(c.getOrientation());
                }
                else {
                    i.removePart(c.getOrientation());
                    if (i.getCircleOfInfluences().isEmpty()) {
                        board[cY][cX] = null;
                    }
                    else {
                        CircleOfInfluence circleOfInfluence =  i.getCircleOfInfluence(0);
                        board[cY][cX] = circleOfInfluence;
                    }

                }
            }
        }
    }


    public void placeCircleOfInfluence(Atom a){
        //loop through all atom "a" circle of influence
        for(CircleOfInfluence c : a.getCircleOfInfluence()){

            //check to make sure atom is not getting overridden and not placing outside of main board
            if(!(board[c.getYCo_ord()][c.getXCo_ord()] instanceof Atom) && !(board[c.getYCo_ord()][c.getXCo_ord()] instanceof EmptyMarker)) {

                //in the case where one part of circle of influence intersects another part;
                if(board[c.getYCo_ord()][c.getXCo_ord()] instanceof CircleOfInfluence i && c.getOrientation() != i.getOrientation()){

                    //create new intersecting circ. object to place previous and new circle of influence
                    IntersectingCircleOfInfluence s = new IntersectingCircleOfInfluence();

                    //add both parts to new object
                    s.addPart(i);
                    s.addPart(c);

                    //place new object in place of intersecting circle of influences
                    board[c.getYCo_ord()][c.getXCo_ord()] = s;
                }

                //in the case where one part of a circle of influence intersects and intersection of influences
                else if(board[c.getYCo_ord()][c.getXCo_ord()] instanceof IntersectingCircleOfInfluence s && !s.getCircleOfInfluences().contains(c)){

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

    private static boolean checkEdgeOfBoard(int i, int j) {
        return i + j == 6 || i + j == 14 || (i == 1 && j >= 5) ||
                (j == 9 && i < 6) || (i == 9 && j <= 5) || (j == 1 && i >= 5);
    }

    //TODO develop this (probably dont need this)
    //TODO make it work for certain deflections/reflection and absorption
//    public void placeRayMarker(int x, int y){
//
//        //validation that position is valid ray marker position
//        if(!checkRayMarker(y, x)){
//            throw new IllegalArgumentException("Invalid Position for Ray Marker");
//        }
//
//        //just creates a new ray marker in a certain position and creates a random colour for it
//        RayMarker r = new RayMarker(x, y, generateColour());
//
//        //place new ray marker into board
//        board[y][x] = r;
//    }

    //classes which have no functionality other than representing a position on board
    //null hex is a position in the object array which cants be accessed in the game
    //empty ray marker is the perimeter of the board; position which will hold ray markers for rays
    public static class NullHex{
    }
    public static class EmptyMarker{
    }

    //TODO: (probably dont need this)
    //function to create a different colour for each ray marker
//    private String generateColour(){
//        //TODO: fix this function so it generates the colour based on
//        //TODO: - what kind of reflection ray takes
//
//        //loop colours back to the start
//        if(colourIndex >= 37){
//            colourIndex = 31;
//        }
//
//        return "\u001B[" + colourIndex++ + "m";
//    }

    public boolean checkInvalidInput(int i, int j){
        return i + j <= 5 || i + j >= 15 || i <= 0 || j <= 0 || i >= 10 || j >= 10;
    }

    //function to map each number between 1 - 54 (ray input positions) to a hexagon and orientation
    public void setInputMapping(){

        //TODO try not hard code this, will try later on
        int SETLENGTH = 54;

        int x = 5;
        int y = 0;

        for(int i = 1; i <= SETLENGTH; i++){
            RayInputMap r = new RayInputMap();

            if(i <= 18){
                r.x = x;
                r.y = y;
                if(i < 10){

                    if(i % 2 == 0){
                        r.orientation = 0;
                        numberOut.put(new RayOutputPoint(x, y, true), i);
                    }
                    else{
                        r.orientation = 300;
                        numberOut.put(new RayOutputPoint(x, y, false), i);
                        x--;
                        y++;
                    }

                }
                else{

                    if(i % 2 == 0){
                        r.orientation = 0;
                        numberOut.put(new RayOutputPoint(x, y, false), i);
                        y++;
                    }
                    else{
                        numberOut.put(new RayOutputPoint(x, y, true), i);
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
                    numberOut.put(new RayOutputPoint(x, y, true), i);
                }
                else{
                    r.orientation = 60;
                    numberOut.put(new RayOutputPoint(x, y, false), i);
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
                        numberOut.put(new RayOutputPoint(x, y, false), i);
                        x++;
                        y--;
                    }
                    else{
                        r.orientation = 180;
                        numberOut.put(new RayOutputPoint(x, y, true), i);
                    }

                }
                else{
                    if(i % 2 == 0){
                        r.orientation = 240;
                        numberOut.put(new RayOutputPoint(x, y, true), i);
                    }
                    else{
                        r.orientation = 180;
                        numberOut.put(new RayOutputPoint(x, y, false), i);
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
                    numberOut.put(new RayOutputPoint(x, y, false), i);
                    x--;
                }
                else{
                    r.orientation = 300;
                    numberOut.put(new RayOutputPoint(x, y, true), i);
                }
                inputMapping.put(i, r);
            }
        }
    }

    public boolean sendRay(int input){
//        System.out.println("Ray entered at " + input);
        RayInputMap rMap = inputMapping.get(input);

        Ray r = new Ray(input);
        currentRay = r;
        sentRays.add(r);

        r.setCurrXCo_ord(rMap.x);
        r.setCurrYCo_ord(rMap.y);
        r.setOrientation(rMap.orientation);

        boolean absorbed = false;

        String colour = "";
        Color guiColour = Color.WHITE;

        RayMarker start = new RayMarker(rMap.x, rMap.y, input, colour, guiColour);
        rayMarkers.add(start);
        board[rMap.y][rMap.x] = start;

        while(!(board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] instanceof EmptyMarker) && !absorbed){
            if(board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] instanceof Atom) {
                start.setColour(ANSI_GREEN);
                start.setGuiColour(Color.green);
                r.setOutput(-1);
//                System.out.println("Ray absorbed!");
                return true;
            }

            switch (r.getOrientation()) {
                case 0 -> r.move0();
                case 60 -> r.move60();
                case 120 -> r.move120();
                case 180 -> r.move180();
                case 240 -> r.move240();
                case 300 -> r.move300();
                default -> System.out.println("invalid ray movement");
            }

            //exit loop if when ray has moved it has met a pre-placed ray marker
            if(board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] instanceof RayMarker){
                break;
            }

            if(placeRay(r.getCurrXCo_ord(), r.getCurrYCo_ord(), r.getOrientation(), r)){
                start.setColour(ANSI_GREEN);
                start.setGuiColour(Color.green);
                r.setDeflectionType(-1);
                r.setOutput(-1);
                return true;
            }

        }
        if(!absorbed){
            //if ray is reflected, set colour purple
            RayInputMap rmap = inputMapping.get(start.getNumber());

//            System.out.println(r.getDeflectionType());
            if(r.getDeflectionType() == 180 || start.getXCo_ord() == r.getCurrXCo_ord() && start.getYCo_ord() == r.getCurrYCo_ord() && rmap.orientation == Math.abs(r.getOrientation() - 180)) {
                colour = "\u001B[35m";
                guiColour = new Color(191, 0, 255);
                start.setGuiColour(guiColour);
                start.setColour(colour);
                r.setDeflectionType(180);
                return true;
            }
            else if(r.getDeflectionType() == 120){
                colour = "\u001B[35m";
                guiColour = new Color(255, 234, 0);
                start.setGuiColour(guiColour);
                start.setColour(colour);
            }
            else if(r.getDeflectionType() == 60){
                colour = "\u001B[33m";
                guiColour = new Color(0, 13, 255);
                start.setGuiColour(guiColour);
                start.setColour(colour);
            }
            else if(r.getDeflectionType() == 0){
                colour = "\u001B[31m";
                guiColour = new Color(255, 0, 0);
                start.setGuiColour(guiColour);
                start.setColour(colour);
            }

            RayInputMap m = new RayInputMap();
            m.x = r.getCurrXCo_ord();
            m.y = r.getCurrYCo_ord();
            m.orientation = r.getOrientation() >= 180 ? r.getOrientation() - 180 : r.getOrientation() + 180;

            int exit = findExit(m);
            r.setOutput(exit);
//            if (r.getDeflectionType() == 180) System.out.println("Deflected!");
//            else System.out.println("Ray exited at " + exit);
            RayMarker end = new RayMarker(r.getCurrXCo_ord(), r.getCurrYCo_ord(), exit, colour, guiColour);
            rayMarkers.add(end);

            board[r.getCurrYCo_ord()][r.getCurrXCo_ord()] = end;
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
        if (board[y][x] == null || board[y][x] instanceof RayTrails) {
            if (board[y][x] instanceof RayTrails rt) rt.addRayTrail(orientation);
            else board[y][x] = new RayTrails(orientation);
            return false;
        }
        else if(board[y][x] instanceof CircleOfInfluence c){
            return r.deflectionLogic_CircleOfInfluence(c.getOrientation());
        }
        else if(board[y][x] instanceof IntersectingCircleOfInfluence i) {
            return r.deflectionLogic_IntersectingCircleOfInfluence(i);
        }
        return false;
    }



    public static class RayTrail{
        private final int orientation;
        private RayTrail(int orientation){
            this.orientation = orientation;
        }

        public int getOrientation() {
            return this.orientation;
        }
    }

    public static class RayTrails {
        private final ArrayList<RayTrail> rayTrails;

        private RayTrails(int orientation) {
            rayTrails = new ArrayList<>();
            rayTrails.add(new RayTrail(orientation));
        }

        public ArrayList<RayTrail> getRayTrails() {
            return this.rayTrails;
        }

        public void addRayTrail(int orientation) {
            this.rayTrails.add(new RayTrail(orientation));
        }
    }
}
