package fr.upsaclay.bibs.pacman.model.board;

import fr.upsaclay.bibs.pacman.GameType;
import fr.upsaclay.bibs.pacman.PacManException;
import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.actors.*;
import fr.upsaclay.bibs.pacman.model.maze.Maze;
import fr.upsaclay.bibs.pacman.model.maze.Tile;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractBoard implements Board{

    private final GameType gameType;
    private Maze maze;

    private PacMan pacMan;
    private Ghost blinky;
    private Ghost pinky;
    private Ghost inky;
    private Ghost clyde;
    private Bonus bonus;

    public List<Actor> actors;
    private final List<GhostType> disabledGhosts = new ArrayList<>();

    private int pacmanX;
    private int pacmanY;

    private int score;
    private int level;
    private int numberOfLives;

    private int initialNumberOfDots;

    private boolean wonExtraLife = false;
    private int extraLifeScore = 10000;

    private BoardState boardState = BoardState.INITIAL;

    private int[] scatterChasePattern;

    private int frameCounter = 0;
    private int scatterChaseIndex = 0;
    private boolean isScatter = true; // Commence par Scatter

    private boolean stateTimeEnabled = true;

    private static Random random = new Random(42);
    private int remainingFrightenedFrames = 0;
    private int frightenedFlashFrames;
    private boolean isFrightened = false;
    private int ghostsEatenDuringFright = 0;

    private int bonusFrames = 600;
    private int remainingBonusFrames = 0;
    private boolean flagBonus = false;  // true when the bonus is collected


    public AbstractBoard(GameType type){

        gameType = type;
    }


    /**
     * Return the type of game of the board
     * Depending on the type, the maze or other initializations might be different
     * @return the game type
     */
    public GameType getGameType(){
        /* Return the board's gameType */
        return gameType;
    };

    private void setPacmanStartPosition() {
        //Initialiser les positions du PacMan en fonction du type de board
        switch (gameType) {
            case CLASSIC:
                pacmanX = 112;
                pacmanY = 211;
                break;
            case TEST:
                pacmanX = 35;
                pacmanY = 75;
                break;
            case CLASSIC_TEST:
                pacmanX = 112;
                pacmanY = 211;
                break;
        }
    }

    private void setGhostStartPosition(GhostType ghostType){

        switch (ghostType){
            case BLINKY:
                getGhost(ghostType).setPosition(112, 115);
                break;

            case PINKY:
                getGhost(ghostType).setPosition(112, 139);
                break;

            case INKY:
                getGhost(ghostType).setPosition(96, 139);
                break;

            case CLYDE:
                getGhost(ghostType).setPosition(128, 139);
                break;
        }
    }

    /**
     * Initialization of the board
     * (loads the maze, create and place the actors, etc.)
     * @throws PacManException in case something went wrong
     */
    public void initialize() throws PacManException{

        score = 0;
        level = 1;
        numberOfLives = 2;

        /*
        System.out.println("initialize : ");
        System.out.println("level " + level);
        System.out.println("vies restantes : " + numberOfLives);*/

        try {

            // Initialize the maze with the right file
            if (getGameType() == GameType.CLASSIC) {
                maze = Maze.loadFromFile("resources/maze.txt");
            } else if (getGameType() == GameType.TEST) {
                maze = Maze.loadFromFile("resources/test.txt");
            } else {
                maze = Maze.loadFromFile("resources/mazeTest.txt");
            }

            initialNumberOfDots = maze.getNumberOfDots();

            // Change boardState to INITIAL to STARTED
            setBoardState(BoardState.STARTED);

            actors = new ArrayList<>();

            // Place PacMan
            setPacmanStartPosition();
            pacMan = new PacMan(Direction.LEFT, this, pacmanX, pacmanY);
            pacMan.setSpeed(getLevelPacManSpeed());
            actors.add(pacMan);

            // Place ghosts -> à trouver mieux ensuite
            if (gameType != GameType.TEST){

                double ghostSpeed = getLevelGhostSpeed();

                if (!disabledGhosts.contains(GhostType.BLINKY)){
                    blinky = new Blinky(this, 112, 115);
                    blinky.setSpeed(ghostSpeed);
                    actors.add(blinky);
                }
                if (!disabledGhosts.contains(GhostType.PINKY)){
                    pinky = new Pinky(this, 112, 139);
                    pinky.setSpeed(ghostSpeed);
                    actors.add(pinky);
                }

                if (!disabledGhosts.contains(GhostType.INKY)){
                    inky = new Inky(this, 96, 139);
                    inky.setSpeed(ghostSpeed);
                    actors.add(inky);
                }
                if (!disabledGhosts.contains(GhostType.CLYDE)){
                    clyde = new Clyde(this, 128, 139);
                    clyde.setSpeed(ghostSpeed);
                    actors.add(clyde);
                }
            }

            scatterChasePattern = getScatterChasePatternForLevel(level);

            for (GhostType type : GhostType.values()) {
                Ghost ghost = getGhost(type);
                if (ghost.getGhostPenState() == GhostPenState.IN) {
                    ghost.setGhostPenState(GhostPenState.GET_OUT); // On déclenche leur sortie
                }
            }

        } catch (PacManException e) {
            throw new PacManException();
        }

        setBonusOnBoard();

    }

    private int[] getScatterChasePatternForLevel(int level) {
        if (level == 1) {
            return new int[]{420, 1200, 420, 1200, 300, 1200, 300, Integer.MAX_VALUE};
        } else if (level <= 4) {
            return new int[]{420, 1200, 420, 1200, 300, 61980, 1, Integer.MAX_VALUE};
        } else {
            return new int[]{300, 1200, 300, 1200, 300, 62220, 1, Integer.MAX_VALUE};
        }
    }

    private int getFrightenedFramesNumber(int level){

        int sec = 0;

        if (level == 1){
            sec = 6;
        } else if (level == 2 | level == 6 | level == 10){
            sec = 5;
        } else if (level == 3){
            sec = 4;
        } else if (level == 4 | level == 14){
            sec = 3;
        } else if (level == 5 | level == 7 | level == 8 | level == 11){
            sec = 2;
        } else if (level == 9 | level == 12 | level == 13 | level == 15 | level == 16 | level == 18){
            sec = 1;
        }

        return sec*60;
    }

    private int getFightenedEndFramesNumber(int level){

        int nbFlash = 0;

        if (level == 9 | level == 12 | level == 13 | level == 15 | level == 16 | level == 18){
            nbFlash = 3;
        } else if (level < 9 | level == 10 | level == 11 | level == 14){
            nbFlash = 5;
        }

        return nbFlash * 10; // 1 flash == 2 frames
    }

    /** Return the maze
     * @return the maze
     */
    public Maze getMaze(){
        return maze;
    };

    /**
     * Return PacMan
     * @return the PacMan actor
     */
    public Actor getPacMan(){
        return pacMan;
    };

    /**
     * Perform all necessary actions for the next game frame
     * This might require to move the actors,
     * perform some checks, etc.
     */
    public void nextFrame() {

        if (isFrightened && remainingFrightenedFrames > 0) {
            remainingFrightenedFrames -= 1;

            if (remainingFrightenedFrames < getFrightenedFramesNumber(level) - 1) {
                for (Ghost ghost : getGhosts()){
                    ghost.setIntention(getRandomDirection(ghost));
                }
            }
        }

        if (!isFrightened && remainingFrightenedFrames != 0) {    // 1st frightened frame
            isFrightened = true;
            for (Ghost ghost : getGhosts()){
                ghost.changeGhostState(GhostState.FRIGHTENED);
                ghost.setSpeed(getFrightGhostSpeed());
            }
        } else if (remainingFrightenedFrames != 0 && remainingFrightenedFrames == frightenedFlashFrames) { // 1st frightened_end frame
            for (Ghost ghost : getGhosts()) {
                ghost.changeGhostState(GhostState.FRIGHTENED_END);
            }
        } else if (isFrightened && remainingFrightenedFrames == 0) {  // last frightened_end frame
            isFrightened = false;
            for (Ghost ghost : getGhosts()){
                ghost.setSpeed(getLevelGhostSpeed());
                ghost.changeGhostState(GhostState.SCATTER);
                pacMan.setSpeed(getLevelPacManSpeed());
                ghostsEatenDuringFright = 0;
            }


        } else if (stateTimeEnabled && !isFrightened) {
            frameCounter++;

            if (scatterChasePattern != null && frameCounter >= scatterChasePattern[scatterChaseIndex]) {
                //changement de mode pour tous les fantomes
                for (Actor actor : actors) {
                    if (actor.getType() == ActorType.GHOST) {
                        Ghost ghost = (Ghost) actor;
                        if (ghost.getGhostState() == GhostState.SCATTER) {
                            ghost.changeGhostState(GhostState.CHASE);
                        } else {
                            ghost.changeGhostState(GhostState.SCATTER);
                        }
                    }
                }
                scatterChaseIndex++;
                frameCounter = 0;
            }
        }

        //System.out.println(remainingBonusFrames);

        if (remainingBonusFrames == 0){
            bonus.setActive(false);
        } else {
            bonus.setActive(true);
            remainingBonusFrames-= 1;
        }


        // Move actors
        for (Actor actor : actors) {
            checkSlowTile(actor);
            actor.nextMove();
        }

        // Check if ghosts are on the same tile as Pac-Man
        for (Ghost ghost : getGhosts()){

            if (ghost.getCurrentTile().equals(pacMan.getCurrentTile())){

                GhostState ghostState = ghost.getGhostState();

                // Pac-Man eats a ghost
                if (ghostState == GhostState.FRIGHTENED || ghostState == GhostState.FRIGHTENED_END){
                    ghost.changeGhostState(GhostState.DEAD);
                    ghost.setSpeed(1.26);
                    ghostsEatenDuringFright += 1;
                    setScore(getScore()+(int)Math.pow(2, ghostsEatenDuringFright)*100);

                }
                // A ghost eats Pac-Man
                else if (ghostState != GhostState.DEAD) {
                    if (getNumberOfLives() > 0){
                        setNumberOfLives(getNumberOfLives() - 1);
                        setBoardState(BoardState.LIFE_OVER);
                    } else {
                        setBoardState(BoardState.GAME_OVER);
                    }
                }
            }

            if (pacMan.getCurrentTile().equals(bonus.getCurrentTile()) && bonus.isActive()){
                bonus.setActive(false);
                setScore(getScore() + bonus.getBonusType().getValue());
                remainingBonusFrames = 0;
            }
        }

        // Eating a dot
        eatDot();
    }


    // Step 2
    // The methods below won't be used / tested before step 2

    /**
     * Return the current score of the game
     * @return the score
     */
    public int getScore(){
        return score;
    }

    public void setScore(int newScore){
        score = newScore;
        if (!wonExtraLife && score >= getExtraLifeScore()){
            setNumberOfLives(getNumberOfLives()+1);
            wonExtraLife = true;
        }
    }


    public void eatDot(){
        /* Return true is PacMan is on a dot tile */

        flagBonus = false;

        TilePosition pacManTilePosition = pacMan.getCurrentTile();
        int col = pacManTilePosition.getCol();

        if (col >= 0 && col < 28){
            Tile currentPacManTile = maze.getTile(pacManTilePosition.getLine(), pacManTilePosition.getCol());

            if (currentPacManTile.hasDot()){    // if PacMan is on a dot tile

                // Update the score (+10 for a small dot ; +50 for a big dot)
                // And set stopTime (1 frame for a small dot ; 3 frames for a big one)
                if (currentPacManTile.hasBigDot()){     // for a big dot
                    setScore(getScore()+50);
                    pacMan.setStopTime(3);
                    pacMan.setSpeed(getFrightPacManSpeed());
                    remainingFrightenedFrames = getFrightenedFramesNumber(level);
                    frightenedFlashFrames = getFightenedEndFramesNumber(level);

                } else {    // for a small dot
                    setScore(getScore()+10);
                    pacMan.setStopTime(1);
                }

                // Update the maze (clear the dot)
                maze.setTile(pacManTilePosition, maze.getTile(pacManTilePosition).clearDot());

                if (maze.getNumberOfDots() == 0){
                    setBoardState(BoardState.LEVEL_OVER);
                } else if (initialNumberOfDots - maze.getNumberOfDots() == 70 && !flagBonus){     // if Pac-Man ate 70 dots
                    remainingBonusFrames = bonusFrames;
                    bonus.setActive(true);
                    flagBonus = true;
                } else if (initialNumberOfDots - maze.getNumberOfDots() == 170 && !flagBonus){
                    remainingBonusFrames = bonusFrames;
                    bonus.setActive(true);
                    flagBonus = true;
                }
            }
        }
    }

    public int getRemainingFrightenedFrames(){
        return remainingFrightenedFrames;
    }

    /**
     * Return the current state of the board
     * used to detect end of level / end of game
     * @return the board state
     */
    public BoardState getBoardState(){
        return boardState;
    }

    public void setBoardState(BoardState boardState){
        // Level is over when pacman ate all the dots -> verified in eatDot() method

        this.boardState = boardState;
    }

    /**
     * Return the ghost of a given type
     * If the board does not contain such a ghost, it returns null
     * @param ghostType, the type of ghost
     * @return a ghost or null
     */
    public Ghost getGhost(GhostType ghostType){

        for (Ghost ghost : getGhosts()) {
            if (ghost.getGhostType() == ghostType) {
                return ghost;
            }
        }
        return null;
    }

    /**
     * Return the list of ghosts present on the board
     * @return a list of ghost (might be empty)
     */
    public List<Ghost> getGhosts() {
        List<Ghost> ghosts = new ArrayList<>();
        for (Actor actor : actors) {
            if (actor instanceof Ghost) {
                ghosts.add((Ghost) actor);
            }
        }
        return ghosts;
    }


    public List<Actor> getActors(){
        return actors;
    }


    // Step 3
    // The methods below won't be used / tested before step 3

    /**
     * Return the current level of the game
     * @return a positive integer
     */
    public int getLevel(){
        return level;
    };

    /**
     * Perfom all necessary actions to initiliaze a new level
     * (might need to load the maze, to place the actors, etc)
     * @param level, a positive integer
     * @throws PacManException if anything goes wrong
     */
    public void initializeNewLevel(int level) throws PacManException{

        //System.out.println("new level : ");
        //System.out.println("level " + level);
        //System.out.println("vies restantes : " + numberOfLives);

        resetRandom();

        this.level = level;

        try {

            // Initialize the maze with the right file
            if (getGameType() == GameType.CLASSIC) {
                maze = Maze.loadFromFile("resources/maze.txt");
            } else if (getGameType() == GameType.TEST) {
                maze = Maze.loadFromFile("resources/test.txt");
            } else {
                maze = Maze.loadFromFile("resources/mazeTest.txt");
            }

            // Change boardState to INITIAL to STARTED
            setBoardState(BoardState.STARTED);

            actors = new ArrayList<>();

            // Place PacMan
            setPacmanStartPosition();
            pacMan = new PacMan(Direction.LEFT, this, pacmanX, pacmanY);
            pacMan.setSpeed(getLevelPacManSpeed());
            actors.add(pacMan);

            // Place ghosts
            if (gameType != GameType.TEST){

                double ghostSpeed = getLevelGhostSpeed();

                if (!disabledGhosts.contains(GhostType.BLINKY)){
                    blinky = new Blinky(this, 112, 115);
                    blinky.setSpeed(ghostSpeed);
                    actors.add(blinky);
                }
                if (!disabledGhosts.contains(GhostType.PINKY)){
                    pinky = new Pinky(this, 112, 139);
                    pinky.setSpeed(ghostSpeed);
                    actors.add(pinky);
                }

                if (!disabledGhosts.contains(GhostType.INKY)){
                    inky = new Inky(this, 96, 139);
                    inky.setSpeed(ghostSpeed);
                    actors.add(inky);
                }
                if (!disabledGhosts.contains(GhostType.CLYDE)){
                    clyde = new Clyde(this, 128, 139);
                    clyde.setSpeed(ghostSpeed);
                    actors.add(clyde);
                }
            }

        } catch (PacManException e) {
            throw new PacManException();
        }

        setBonusOnBoard();
    }

    /**
     * Sets the number of extra lives that pacman has
     * @param nbLives, a non negative integer
     */
    public void setNumberOfLives(int nbLives){
        numberOfLives = nbLives;
    }

    /**
     * Return the current number of extra lives
     * @return a non negative integer
     */
    public int getNumberOfLives(){
        return numberOfLives;
    }

    /**
     * Perform all necessary actions to initialize the game after a life has been lost
     * (reduce the nb of lives, replace the actors, re-initialize certain values)
     */
    public void initializeNewLife(){

        //System.out.println("new life : ");
        //System.out.println("level " + level);
        //System.out.println("vies restantes : " + numberOfLives);

        resetRandom();

        // Change boardState to INITIAL to STARTED
        setBoardState(BoardState.STARTED);

        actors = new ArrayList<>();

        // Place PacMan
        setPacmanStartPosition();
        pacMan = new PacMan(Direction.LEFT, this, pacmanX, pacmanY);
        pacMan.setSpeed(getLevelPacManSpeed());
        actors.add(pacMan);

        // Place ghosts
        if (gameType != GameType.TEST){

            double ghostSpeed = getLevelGhostSpeed();

            if (!disabledGhosts.contains(GhostType.BLINKY)){
                blinky = new Blinky(this, 112, 115);
                blinky.setSpeed(ghostSpeed);
                actors.add(blinky);
            }
            if (!disabledGhosts.contains(GhostType.PINKY)){
                pinky = new Pinky(this, 112, 139);
                pinky.setSpeed(ghostSpeed);
                actors.add(pinky);
            }

            if (!disabledGhosts.contains(GhostType.INKY)){
                inky = new Inky(this, 96, 139);
                inky.setSpeed(ghostSpeed);
                actors.add(inky);
            }
            if (!disabledGhosts.contains(GhostType.CLYDE)){
                clyde = new Clyde(this, 128, 139);
                clyde.setSpeed(ghostSpeed);
                actors.add(clyde);
            }
        }

    }

    /**
     * Return whether the board contain a certain type of ghost
     * @param ghostType the type of ghost
     * @return true if this ghost is on the board
     */
    public boolean hasGhost(GhostType ghostType){
        return getGhost(ghostType) != null;
    };

    /**
     * Disables a certain ghost before game initialization
     * @param ghostType the type of ghost to disable
     */
    public void disableGhost(GhostType ghostType){

        if (!disabledGhosts.contains(ghostType)) {
            disabledGhosts.add(ghostType);
        }

    }

    /**
     * Disable the use of state time before game initialization
     */
    public void disableStateTime(){
        stateTimeEnabled = false;
    };

    public static void resetRandom() {
        random = new Random(42);
    }

    /**
     * Return a pseudo-random direction
     * The random generator is initialized once at the beginning of the game
     * @return a pseudo-random direction
     */
    public Direction getRandomDirection(Ghost ghost){

        List<Direction> directions = List.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
        Direction randomDir = directions.get(random.nextInt(directions.size()));
        TilePosition ghostTile = ghost.getCurrentTile();
        boolean directionFound = false;
        int i = 0;

        while (!directionFound){
            Direction testedDirection = directions.get((directions.indexOf(randomDir) + i) % directions.size());
            TilePosition neighbour = getMaze().getNeighbourTilePosition(ghostTile.getLine(), ghostTile.getCol(), testedDirection);

            // if the neighbour tile exists and is not a wall
            if (neighbour != null && !getMaze().getTile(neighbour.getLine(), neighbour.getCol()).isWall()) {
                // if it is not the reverse direction (forbidden)
                if (testedDirection != ghost.getDirection().reverse()) {
                    directionFound = true;
                    return testedDirection;
                }
            }
            i++;
        }

        return null;
    }

    /**
     * Re turn the tile position of the pen entry
     * (this corresponds to the ghost target when dead)
     * @return a tile position
     */
    public TilePosition penEntry(){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the minimum y value a ghost can take when inside the pen
     * @return a positive integer
     */
    public int minYPen(){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the maximal y value a ghost can take when inside the pen
     * @return a positive integer
     */
    public int maxYPen(){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the x position of given ghost type inside the pen
     * @param type, a ghost type
     * @return a positive integer
     */
    public int penGhostXPosition(GhostType type){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the y position of given ghost type inside the pen
     * @param type, a ghost type
     * @return a positive integer
     */
    public int penGhostYPosition(GhostType type){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the x position used by ghost to enter / leave the ghost pen
     * @return a positive integer
     */
    public int outPenXPosition(){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the y position used by ghost to enter / leave the ghost pen
     * @return a positive integer
     */
    public int outPenYPosition(){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the counter used to count the number of successive frames without eating dots
     * This is a way to count the time passed between 2 dots are eaten
     * @return a counter
     */
    public Counter noDotCounter(){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the board "special counter" used for allowing ghosts out of the pen
     * @return a counter
     */
    public Counter specialDotCounter(){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the normal speed of pacman at the board current level
     * @return a speed as a decimal
     */
    public double getLevelPacManSpeed(){
        if (level == 1){    // 80%
            return 1;   // = 0.8 * 1.25
        } else if (level <= 4 | level >= 21){   // 90%
            return 1.13;    // = 0.9 * 1.25
        } else {    // 100%
            return 1.25;    // = 1 * 1.25
        }
    }

    /**
     * Return the speed of pacman in "fright" mode at the board current level
     * @return a speed as a decimal
     */
    public double getFrightPacManSpeed(){
        if (level == 1){    // 90%
            return 1.14;   // = 0.9 * 1.25
        } else if (level <= 4){   // 95%
            return 1.2;    // = 0.95 * 1.25
        } else {    // 100%
            return 1.26;    // = 1 * 1.25
        }    }

    /**
     * Return the normal speed of ghost at the board current level
     * @return a speed as a decimal
     */
    public double getLevelGhostSpeed(){
        if (level == 1){    // 75%
            return 0.94;    // = 0.75 * 1.25
        } else if (level <= 4){     // 85%
            return 1.07;    // = 0.85 * 1.25
        } else {    // 95%
            return 1.2;    // = 0.95 * 1.25
        }
    }

    protected void checkSlowTile(Actor actor) {

        TilePosition currentTile = actor.getCurrentTile();
        TilePosition previousTile = actor.getPreviousTile();
        double previousSpeed = actor.getPreviousSpeed();

        if (!currentTile.equals(previousTile)) {

            actor.setPreviousTile(currentTile);
            Tile current = getMaze().getTile(currentTile);

            if (current == Tile.SL) {
                if (previousSpeed == -1) {
                    actor.setPreviousSpeed(actor.getSpeed());
                }
                if (actor.getType() == ActorType.GHOST){
                    actor.setSpeed(getTunnelGhostSpeed());
                } else {
                    actor.setSpeed(getTunnelPacmanSpeed());
                }

            } else {
                if (previousSpeed != -1) {
                    actor.setSpeed(actor.getPreviousSpeed());
                }
            }
        }
    }

    public double getTunnelPacmanSpeed(){
        if (level == 1){
            return 0.4 * getLevelPacManSpeed();
        } else if (level <= 4){
            return 0.45 * getLevelPacManSpeed();
        } else {
            return 0.5 * getLevelPacManSpeed();
        }
    }

    /**
     * Return the slow speed of ghosts at current level (inside the tunnel)
     * @return a speed as a decimal
     */
    public double getTunnelGhostSpeed(){
        if (level == 1){
            return 0.5;
        } else if (level <= 4){
            return 0.57;
        } else {
            return 0.63;
        }
    }

    /**
     * return the speed of ghosts in "fright" mode at current level
     * @return a speed as a decimal
     */
    public double getFrightGhostSpeed(){
        double percentage = 0.6;

        if (level == 1){
            percentage = 0.5;
        } else if (level >= 2 & level <= 4){
            percentage = 0.55;
        }

        return percentage * getLevelGhostSpeed();
    }

    /**
     * return the speed of ghosts when dead at current level
     * @return a speed as a decimal
     */
    public double getDeadGhostSpeed(){throw new UnsupportedOperationException("Not implemented");};

    // Step 4
    // The methods below won't be used / tested before step 4

    /**
     * Sets the score value at which pacman gains a new life
     * @param score, a positive integer
     */
    public void setExtraLifeScore(int score){
        extraLifeScore = score;
    }

    /**
     * Return the score value at which pacman gains a new life
     * @return a positive integer
     */
    public int getExtraLifeScore(){
        return extraLifeScore;
    }

    /**
     * Return the current bonus n the board if existing, null otherwise
     * @return a Bonus or null
     */
    public Bonus getCurrentBonus(){
        return bonus;
    };

    /**
     * Return the bonus type associated with given level
     * @param level, a positive integer
     * @return the bonus type of this level
     */
    public BonusType getLevelBonusType(int level){

        if (level == 1){
            return BonusType.CHERRY;
        } else if (level == 2){
            return BonusType.STRAWBERRY;
        } else if (level <= 4){
            return BonusType.PEACH;
        } else if (level <= 6){
            return BonusType.APPLE;
        } else if (level <= 8){
            return BonusType.GRAPES;
        } else if (level <= 10){
            return BonusType.GALAXIAN;
        } else if (level <= 12){
            return BonusType.BELL;
        } else {
            return BonusType.KEY;
        }
    }

    /**
     * Place the bonus associated with current level on board at its intended position
     */
    public void setBonusOnBoard(){
        bonus = new AbstractBonus(getLevelBonusType(level), this, 112, 163);
        bonus.setActive(false);
    }

    public Bonus getBonus(){
        return bonus;
    }

    /**
     * Return the ghost speed at given Elroy value
     * Note : if elroyNumber is zero, this is the level ghost speed
     * @param elroyNumber, the ghost elroy value (0, 1 or 2)
     * @return the elroy ghost speed
     */
    public double getElroyGhostSpeed(int elroyNumber){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the dot value (number of remaining dots) at which Blinky turns to elroy (1 or 2)
     * @param elroyNumber, 1 or 2
     * @return a positive number
     */
    public int getElroyDotValue(int elroyNumber){throw new UnsupportedOperationException("Not implemented");};

    /**
     * Return the list of events that occured at the last frame
     * @return a list (might be empty)
     */
    public List<BoardEvent> getCurrentEvents(){throw new UnsupportedOperationException("Not implemented");};

}
