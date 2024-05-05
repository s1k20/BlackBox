package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents an AI player in the game, capable of setting and guessing atom positions
 * and sending rays based on a specified difficulty level.
 */
public class AiPlayer extends Player {
    private final int difficulty;  // Defines the difficulty level of the AI (1: hard, 2: medium, 3: easy)
    private Board board;           // The game board associated with this AI player

    private final ArrayList<Integer> sentRays;   // Tracks the rays sent by the AI to avoid repetition
    private final ArrayList<Point> guessedAtoms; // Tracks the atoms guessed by the AI

    private static final Random random = new Random(); // Random generator for AI operations
    private static final String[] names = {"Cian", "Lloyd", "Shlok"}; // Predefined names for AI players

    /**
     * Constructs an AI player with specified settings.
     * @param isSetter whether this AI player is initially the setter
     * @param difficulty the difficulty level of the AI (1=hard, 2=medium, 3=easy)
     * @param board the game board associated with this player
     */
    public AiPlayer(boolean isSetter, int difficulty, Board board) {
        super(getRandomName() + " (AI)", isSetter);
        this.difficulty = difficulty;
        this.board = board;

        sentRays = new ArrayList<>();
        guessedAtoms = new ArrayList<>();
    }

    /**
     * Generates a random name for the AI player from a predefined list of names.
     * @return a randomly selected name
     */
    private static String getRandomName() {
        return names[random.nextInt(names.length)];
    }

    /**
     * Simulates sending rays based on AI difficulty.
     * The number of rays sent increases with difficulty.
     * @return a list of integers representing the ray inputs
     */
    public ArrayList<Integer> ai_sendRays() {
        int multiplier = difficulty == 1 ? 2 : difficulty;

        int numRaysToSend = random.nextInt(2, 5) * multiplier;
        ArrayList<Integer> rays = new ArrayList<>();

        for (int i = 0; i < numRaysToSend; i++) {
            int ray = pickRayInput();
            rays.add(ray);
        }
        return rays;
    }

    /**
     * Helper method to pick a unique ray input that has not been used before.
     * @return a valid ray input
     */
    private int pickRayInput() {
        int input;
        do {
            input = random.nextInt(1, 55);
        } while (sentRays.contains(input));
        sentRays.add(input);
        return input;
    }

    /**
     * Directs the AI to place a specified number of atoms on the board randomly.
     * @param numAtoms the number of atoms to place
     */
    public void ai_setAtoms(int numAtoms) {
        for (int i = 0; i < numAtoms; i++) {
            Point newAtom = ai_randomAtom(true);
            board.placeAtom(newAtom.x, newAtom.y);
        }
    }

    /**
     * Generates a random point on the board that is valid and not currently occupied.
     * @param isSetting true if setting atoms, false if guessing
     * @return a valid point for placing or guessing an atom
     */
    private Point ai_randomAtom(boolean isSetting) {
        int x, y;
        do {
            x = random.nextInt(9) + 1;
            y = random.nextInt(9) + 1;
        } while (board.checkInvalidInput(x, y) || (board.getBoardPosition(x, y) instanceof Atom && isSetting) || (!isSetting && guessedAtoms.contains(new Point(x, y))));

        Point randomPosition = new Point(x, y);
        guessedAtoms.add(randomPosition);
        return randomPosition;
    }

    /**
     * Simulates the AI guessing atom positions based on its difficulty.
     * @param numAtoms the number of atoms to guess
     * @return a list of points where the AI guesses atoms are located
     */
    public ArrayList<Point> ai_guessAtoms(int numAtoms) {
        return switch (difficulty) {
            case 1 -> guessAtoms_hard(numAtoms);
            case 2 -> guessAtoms_medium(numAtoms);
            case 3 -> guessAtoms_easy(numAtoms);
            default -> throw new IllegalArgumentException("Invalid difficulty");
        };
    }

    /**
     * Updates the game board for this AI player.
     * @param board the new game board
     */
    public void setNewBoard(Board board) {
        this.board = board;
    }

    /**
     * Adds 4 correct atoms to the list of atom guesses and takes 2 guesses
     * from calculatedAtomGuess which has possibility of guessing atom correctly
     * @param numAtoms number of atoms to be placed onto the board
     * @return an arraylist of points representing atom guesses
     */
    private ArrayList<Point> guessAtoms_hard(int numAtoms) {
        ArrayList<Point> atomGuesses = new ArrayList<>();

        int i = 0;
        for (; i < 4; i++) {
            int xAtom = board.getPlacedAtoms().get(i).getXCo_ord();
            int yAtom = board.getPlacedAtoms().get(i).getYCo_ord();

            atomGuesses.add(new Point(xAtom, yAtom));
        }

        calculatedAtomGuess(atomGuesses, i, numAtoms);
        return atomGuesses;
    }
    /**
     * Adds 2 correct atoms to the list of atom guesses and takes 4 guesses
     * from calculatedAtomGuess which has possibility of guessing atom correctly
     * @param numAtoms number of atoms to be placed onto the board
     * @return an arraylist of points representing atom guesses
     */
    private ArrayList<Point> guessAtoms_medium(int numAtoms) {
        ArrayList<Point> atomGuesses = new ArrayList<>();

        int i = 0;
        for (; i < 2; i++) {
            int xAtom = board.getPlacedAtoms().get(i).getXCo_ord();
            int yAtom = board.getPlacedAtoms().get(i).getYCo_ord();

            atomGuesses.add(new Point(xAtom, yAtom));
        }

        calculatedAtomGuess(atomGuesses, i, numAtoms);

        return atomGuesses;
    }

    /**
     * Adds 1 correct atom to list of guesses and picks random positions
     * for remaining guesses
     * @param numAtoms number of atoms to be placed onto the board
     * @return an arraylist of points representing atom guesses
     */
    private ArrayList<Point> guessAtoms_easy(int numAtoms) {
        ArrayList<Point> atomGuesses = new ArrayList<>();
        int xAtom = board.getPlacedAtoms().get(0).getXCo_ord();
        int yAtom = board.getPlacedAtoms().get(0).getYCo_ord();

        atomGuesses.add(new Point(xAtom, yAtom));

        for (int i = 1; i < numAtoms; i++) {
            Point guess = ai_randomAtom(false);
            atomGuesses.add(guess);
        }
        return atomGuesses;
    }

    /**
     * Method adds calculated guesses to atomGuesses by using certain values
     * to either guess atom correctly or guess right beside it
     * @param atomGuesses atoms already guess
     * @param i number of atoms needed to fully guess 6 atoms
     * @param nAtoms number of atoms to be placed onto the board
     */
    private void calculatedAtomGuess(ArrayList<Point> atomGuesses, int i, int nAtoms) {
        for (int j = i; j < nAtoms; j++) {
            // get coordinates of correctly placed atom
            int xAtom = board.getPlacedAtoms().get(i).getXCo_ord();
            int yAtom = board.getPlacedAtoms().get(i).getYCo_ord();

            // initialise variables for creating a random point near on the correct atom
            int addX;
            int addY;
            int bound = 3; // leading to a 1 in 3 change of guessing 1 coordinate correctly
            int minus = 1;

            // enter loop to ensure of valid guess
            do {
                addX = random.nextInt(bound) - minus;
                addY = random.nextInt(bound) - minus;

                // increase bounds if first guess is invalid
                bound += 2;
                minus += 1;

            } while (board.checkInvalidInput(xAtom + addX, yAtom + addY) ||
                    atomGuesses.contains(new Point(xAtom + addX, yAtom + addY)));

            // add calculated guess to list of guesses
            atomGuesses.add(new Point(xAtom + addX, yAtom + addY));
        }
    }
}
