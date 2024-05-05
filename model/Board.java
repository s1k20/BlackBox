package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static model.BoardConstants.*;

/**
 * Represents the game board for managing game elements such as atoms, rays, and markers.
 */
public class Board {

    private final Object[][] board = new Object[HEIGHT][WIDTH];
    private final HashMap<Integer, RayInputMap> inputMapping = new HashMap<>();
    private final HashMap<RayOutputPoint, Integer> numberOut = new HashMap<>();
    private final ArrayList<Ray> sentRays = new ArrayList<>();
    private final ArrayList<Atom> placedAtoms = new ArrayList<>();
    private final ArrayList<RayMarker> rayMarkers = new ArrayList<>();
    private final HashSet<Integer> rayMarkerNumbers = new HashSet<>();
    private Ray currentRay;
    private int numAtomsPlaced;
    private int numRaysSent;

    /**
     * Constructs a new Board and initializes it along with the input mappings.
     */
    public Board(){
        initBoard();
        setInputMapping();
        numAtomsPlaced = 0;
        numRaysSent = 0;
        currentRay = null;
    }

    /**
     * Returns contents of board at a given coordinate
     *
     * @param x X-coordinate (column index)
     * @param y Y-coordinate (row index)
     * @return an object of which is in specified board position eg. atom, circle of influence etc.
     */
    public Object getBoardPosition(int x, int y){
        return this.board[y][x];
    }

    /**
     * Returns board 2d object array which is the main game board
     *
     * @return a 2d object array
     */
    public Object[][] getBoard(){
        return this.board;
    }

    /**
     * Gets the number of atoms which are on the board
     *
     * @return integer number
     */
    public int getNumAtomsPlaced() {
        return this.numAtomsPlaced;
    }

    /**
     * Gets an arraylist of rays which have been sent into the board
     *
     * @return an arraylist of type ray
     */
    public ArrayList<Ray> getSentRays(){
        return this.sentRays;
    }

    /**
     * Gets an arraylist of atoms which have been placed onto the board
     *
     * @return an arraylist of type atom
     */
    public ArrayList<Atom> getPlacedAtoms() {
        return this.placedAtoms;
    }

    /**
     * Gets an arraylist of ray markers which have been placed
     * as a result of sending rays
     *
     * @return an arraylist of type RayMarker
     */
    public ArrayList<RayMarker> getRayMarkers() {
        return this.rayMarkers;
    }

    /**
     * Returns a hashset of all numbers on the board which have a RayMarker placed on them
     *
     * @return a hashset of integers
     */
    public HashSet<Integer> getRayMarkerNumbers() {
        return this.rayMarkerNumbers;
    }

    /**
     * Returns the most recently sent ray into the board
     *
     * @return Ray object
     */
    public Ray getCurrentRay() {
        return this.currentRay;
    }

    /**
     * Returns a hashmap which maps output points to the numbers which surround the board
     *
     * @return hashmap mapping RayOutputPoint to an integer
     */
    public HashMap<RayOutputPoint, Integer> getNumberOut() {
        return this.numberOut;
    }

    /**
     * Returns the number of rays sent into the board by the experimenter
     *
     * @return an integer of the number of rays
     */
    public int getNumRaysSent() {
        return this.numRaysSent;
    }

    /**
     * Initializes the board with empty markers and null hexes based on their positions.
     */
    private void initBoard() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (checkRayMarker(i, j)) {
                    board[i][j] = new EmptyMarker();
                } else if (i + j <= 4 || i + j > 15) {
                    board[i][j] = new NullHex();
                }
            }
        }
    }

    /**
     * Checks if position on the board is a position in which a ray marker could be placed
     *
     * @param i Row index
     * @param j Column index
     * @return true if position should be reserved for ray marker
     */
    private boolean checkRayMarker(int i, int j){
        return i + j == 5 || i + j == 15 || (i == 0 && j >= 5) ||
                (j == 10 && i < 6) || (i == 10 && j <= 5) || (j == 0 && i >= 5);
    }


    /**
     * Traverses around the edge of the board and correctly maps numbers 1 - 54 with
     * what each input x coordinate, input y coordinate and initial orientation should be
     */
    public void setInputMapping(){
        int NUM_RAY_INPUTS = 54;

        // coordinates in the board data structure starting from the top, most left input = 1
        int x = 5;
        int y = 0;

        for(int i = 1; i <= NUM_RAY_INPUTS; i++){
            RayInputMap r = new RayInputMap();

            if(i <= 18){
                r.x = x;
                r.y = y;
                //1 - 9
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
                //10 - 18
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
            //19 - 27
            else if(i < 28){
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
                r.x = x;
                r.y = y;
                //28 - 36
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
                //37 - 45
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
            //46 - 54
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

    /**
     * Places an atom into board object and calls method to place its circle of influence around it
     * and updates all necessary class variables/datastructures
     *
     * @param x X-coordinate of the atom
     * @param y Y-coordinate of the atom
     */
    public void placeAtom(int x, int y){
        if (!checkInvalidInput(y, x)) {
            Atom newAtom = new Atom(x, y);
            placedAtoms.add(newAtom);
            board[y][x] = newAtom;
            placeCircleOfInfluence(newAtom);
            numAtomsPlaced++;
        } else {
            throw new IllegalArgumentException("Invalid atom position");
        }
    }

    /**
     * Iterates over an atoms circle of influences and determines whether to place it or not.
     * If possible to place then checks contents of position it is trying to place it into
     *
     * @param a Atom object whose circle of influence is being places
     */
    private void placeCircleOfInfluence(Atom a){
        for(CircleOfInfluence c : a.getCircleOfInfluence()){
            if(canPlaceCircleOfInfluence(c.getXCo_ord(), c.getYCo_ord())) {
                if(isCircleOfInfluence(c)){
                    //create new intersecting circ. object to place previous and new circle of influence
                    IntersectingCircleOfInfluence s = new IntersectingCircleOfInfluence(c.getXCo_ord(), c.getYCo_ord());

                    //get circle of influence in current board position
                    CircleOfInfluence c1 = (CircleOfInfluence) board[c.getYCo_ord()][c.getXCo_ord()];
                    //add both parts to new object
                    s.addPart(c);
                    s.addPart(c1);

                    //place new object in place of intersecting circle of influences
                    board[c.getYCo_ord()][c.getXCo_ord()] = s;
                }
                else if(isIntersectingCircleOfInfluence(c)){
                    IntersectingCircleOfInfluence s = (IntersectingCircleOfInfluence) board[c.getYCo_ord()][c.getXCo_ord()];
                    s.addPart(c);
                }
                else{
                    board[c.getYCo_ord()][c.getXCo_ord()] = c;
                }
            }
        }
    }

    /**
     * Checks board coordinates to validate placing a circle of influence in a given position
     *
     * @param x X-coordinate of the position being checked
     * @param y Y-coordinate of the position being checked
     * @return true if the position is valid to place a circle of influence into false otherwise
     */
    private boolean canPlaceCircleOfInfluence(int x, int y) {
        return !(board[y][x] instanceof Atom) && !(board[y][x] instanceof EmptyMarker);
    }

    /**
     * Checks board coordinate if a circle of influence is already in the position
     *
     * @param c The circle of influence which is getting placed
     * @return true if a circle of influence is in the position false otherwise
     */
    private boolean isCircleOfInfluence(CircleOfInfluence c) {
        return board[c.getYCo_ord()][c.getXCo_ord()] instanceof CircleOfInfluence i && c.getOrientation() != i.getOrientation();
    }

    /**
     * Checks board coordinate for if there is an intersecting circle of influence in that position
     *
     * @param c Circle of influence which is getting placed
     * @return true if intersecting circle of influence is in the position
     */
    private boolean isIntersectingCircleOfInfluence(CircleOfInfluence c) {
        return board[c.getYCo_ord()][c.getXCo_ord()] instanceof IntersectingCircleOfInfluence s &&
                !s.getCircleOfInfluences().contains(c);
    }


    /**
     * Removes atom from the board by setting position to null and calls method to also remove circle of influence
     * and updates all necessary class variables/datastructures
     *
     * @param x X-coordinate of atom
     * @param y Y-coordinate of atom
     */
    public void removeAtom(int x, int y) {
        if (!checkInvalidInput(y, x)) {
            Atom atom = (Atom) board[y][x];
            board[y][x] = null;
            placedAtoms.remove(atom);
            removeCircleOfInfluence(atom);
            replaceAtom();
            numAtomsPlaced--;
        } else {
            throw new IllegalArgumentException("Invalid atom position");
        }
    }

    /**
     * Removes an atoms circle of influence from the board
     * Must check if board position holds an intersecting circle of influence
     * if so, remove the singular circle of influence from the intersecting circle of influence array list
     *
     * @param atom Atoms whose circle of influence is getting removed
     */
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

    /**
     * Replaces all atoms circle of influence correctly following a removed atom
     */
    private void replaceAtom() {
        for (Atom a : placedAtoms) {
            placeCircleOfInfluence(a);
        }
    }

    /**
     * Checks if given x and y coordinates are not included in the board
     *
     * @param i x (Column) coordinate/index
     * @param j y (Row) coordinate/index
     * @return true if position is invalid false otherwise
     */
    protected boolean checkInvalidInput(int i, int j){
        return i + j <= 5 || i + j >= 15 || i <= 0 || j <= 0 || i >= 10 || j >= 10;
    }

    /**
     * Creates a ray to be sent based on the input number and lets the ray traverse the board
     * through a loop which will continue until the ray has either been absorbed or exited the board
     *
     * @param input integer which will be turned into a valid ray position and orientation through input mapping
     */
    public void sendRay(int input){
        Ray ray = setupRay(input);
        RayMarker startMarker = createRayMarker(ray.getCurrXCo_ord(), ray.getCurrYCo_ord(), input);

        do {
            ray.moveRay();

            if(placeRay(ray) || isAtom(ray.getCurrXCo_ord(), ray.getCurrYCo_ord())){
                handleAbsorption(ray, startMarker);
                return;
            }

        } while (!checkRayMarker(ray.getCurrXCo_ord(), ray.getCurrYCo_ord()));

        finishRay(ray, startMarker);
    }


    /**
     * Creates ray object based on x, y and orientation variable given by ray input mapping
     *
     * @param input number in which ray will originate, passed to input mapping to get correct x, y and orientation
     * @return Ray which is going to traverse board
     */
    private Ray setupRay(int input) {
        Ray r = new Ray(input);
        numRaysSent++;
        sentRays.add(r);
        currentRay = r;

        RayInputMap rMap = inputMapping.get(input);
        r.setCurrXCo_ord(rMap.x);
        r.setCurrYCo_ord(rMap.y);
        r.setOrientation(rMap.orientation);
        return r;
    }

    /**
     * Creates a ray marker which will be placed onto the board
     *
     * @param x X-coordinate for ray marker
     * @param y Y-coordinate for ray marker
     * @param input corresponding input number for the ray marker
     * @return RayMarker which has been placed onto the board
     */
    private RayMarker createRayMarker(int x, int y, int input) {
        RayMarker rayMarker = new RayMarker(x, y, input);
        rayMarkers.add(rayMarker);
        rayMarkerNumbers.add(rayMarker.getNumber());
        board[y][x] = rayMarker;
        return rayMarker;
    }

    /**
     * Method which makes use of overloading createRayMarker() to optionally
     * also allow for the colour to be set when creating the RayMarker
     * if information is known about a rays deflection type
     *
     * @param x X-coordinate for ray marker
     * @param y Y-coordinate for ray marker
     * @param input corresponding input number for the ray marker
     * @param deflectionType the deflection type of ray which is turned to a corresponding colour
     */
    private void createRayMarker(int x, int y, int input, int deflectionType) {
        RayMarker rayMarker = createRayMarker(x, y, input);
        rayMarker.setColour(deflectionType);
    }

    /**
     * Method checks the hexagon ray is currently in and will determine if it should be
     * reflected, deflected, absorbed or no change
     *
     * @param r Ray which is traversing the board
     * @return true if ray has been absorbed false otherwise
     */
    public boolean placeRay(Ray r){
        int x = r.getCurrXCo_ord();
        int y = r.getCurrYCo_ord();
        int orientation = r.getOrientation();

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

    /**
     * Method updates rays status variables and changes its starting RayMarker to indicate absorption
     *
     * @param r Ray which has been absorbed into an atom
     * @param startMarker Rays starting RayMarker created when it entered the board
     */
    private void handleAbsorption(Ray r, RayMarker startMarker) {
        r.setDeflectionType(-1);
        r.setOutput(-1);
        startMarker.setColour(r.getDeflectionType());
    }

    /**
     * Completes a rays traversal by setting output variable and also
     * creating its corresponding output RayMarker
     *
     * @param ray Ray which has finished traversing the board
     * @param startMarker Starting RayMarker from the rays input to the board
     */
    private void finishRay(Ray ray, RayMarker startMarker) {
        int exit = checkDeflectionState(ray, startMarker);
        createRayMarker(ray.getCurrXCo_ord(), ray.getCurrYCo_ord(), exit, ray.getDeflectionType());
    }

    /**
     * Checks rays location and calls method to compare to the starting RayMarker
     * and determines if the ray has been reflection i.e. returned to the same location as start RayMarker
     * will then set output marker accordingly
     *
     * @param ray Ray which is traversing the board
     * @param startMarker Starting RayMarker placed when the ray entered the board
     * @return exit in which the ray left the board to create endRayMarker
     */
    private int checkDeflectionState(Ray ray, RayMarker startMarker) {
        RayInputMap exitMap = inputMapping.get(startMarker.getNumber());
        int exit = findExit(ray);
        ray.setOutput(exit);
        ray.isReflection(startMarker.getXCo_ord(), startMarker.getYCo_ord(), exitMap.orientation);
        startMarker.setColour(ray.getDeflectionType());
        return exit;
    }

    /**
     * Traverses all possible input/output numbers and returns corresponding
     * number based on x,y coordinates and orientation
     *
     * @param ray Ray which we want to find its corresponding output numbers
     * @return corresponding output number for the rays x,y and orientation variables
     */
    private int findExit(Ray ray){
        RayInputMap r = makeInputMap(ray);

        for(int i = 1; i <= 54; i++){
            RayInputMap m = inputMapping.get(i);
            if(r.orientation == m.orientation && r.x == m.x && r.y == m.y){
                return i;
            }
        }
        return -1;
    }

    /**
     * Given a ray which holds x, y and orientation variables,
     * method creates the corresponding input map for that rays variables
     *
     * @param r Ray whose instance variables are used to create RayInputMap
     * @return RayInputMap created from rays instance variables
     */
    private RayInputMap makeInputMap(Ray r) {
        RayInputMap map = new RayInputMap();
        map.x = r.getCurrXCo_ord();
        map.y = r.getCurrYCo_ord();
        map.orientation = r.getOrientation() >= 180 ? r.getOrientation() - 180 : r.getOrientation() + 180;
        return map;
    }

    /**
     * Method will check if a given location on the board is an atom or not
     *
     * @param x X-coordinate (column index)
     * @param y Y-coordinate (row index)
     * @return true if location is an atom
     */
    private boolean isAtom(int x, int y) {
        return board[y][x] instanceof Atom;
    }

    /**
     * Class which only holds x, y and orientation for
     * mapping purposes
     */
    public static class RayInputMap {
        int x;
        int y;
        int orientation;
    }

    /**
     * Uses RayTrail class to show all rays which passed
     * through a given position on the board
     */
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
            if (!containsOrientation(orientation)) this.rayTrails.add(new RayTrail(orientation));
        }

        private boolean containsOrientation(int orientation) {
            for (RayTrail r : rayTrails) {
                if (r.orientation == orientation) return true;
            }
            return false;
        }
    }

    /**
     * Represents an object which is placed onto the board to indicate a rays path
     */
    public static class RayTrail{
        private final int orientation;
        private RayTrail(int orientation){
            this.orientation = orientation;
        }

        public int getOrientation() {
            return this.orientation;
        }
    }

    /**
     * Represents invalid coordinates on board
     */
    public static class NullHex{

    }

    /**
     * Represents a position which can include a ray marker
     */
    public static class EmptyMarker{

    }
}
