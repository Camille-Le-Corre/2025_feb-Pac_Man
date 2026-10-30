package fr.upsaclay.bibs.pacman.model.actors;

import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.Tile;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

public abstract class AbstractActor implements Actor{

    private final ActorType actorType;
    private final Board board;
    private int x;
    private int y;
    private double realX;
    private double realY;
    private double speed = 1;
    private int stopTime = 0;
    public TilePosition previousTile = null;
    protected double previousSpeed = -1;
    public boolean mustFollowIntention = false;


    public AbstractActor(ActorType type, Board board, int x, int y){
        actorType = type;
        this.board = board;
        this.x = x;
        this.y = y;
        this.realX = x;
        this.realY = y;
    }

    /**
     * Return the type of actor it is
     * i.e. either PacMan, a Ghost, or a bonus
     * @return an ActorType
     */
    public ActorType getType(){
        /* Return the actor's type */

        return actorType;
    };

    /**
     * Return the board on which this actor
     * is placed
     * @return the game board
     */
    public Board getBoard(){
        /* */
        return board;
    };

    /**
     * Return the x position of the actor
     * (in pixels)
     * @return the x position in pixels
     */
    public int getX(){
        return x;
    };

    /**
     * Return the y position of the actor
     * (in pixels)
     * @return the y position in pixels
     */
    public int getY(){
        return y;
    };

    /**
     * Set the actor position on the maze
     * @param x, the x position in pixels
     * @param y, the y position in pixels
     */
    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    };

    /**
     * Return the current tile position corresponding
     * to the actor position the board
     * @return a tile position
     */
    public TilePosition getCurrentTile(){
        return new TilePosition(y/8, x/8);
    }

    public TilePosition getPreviousTile(){
        return previousTile;
    }

    public void setPreviousTile(TilePosition tilePosition){
        previousTile = tilePosition;
    }

    /**
     * Perform necessary actions for the next game
     * frame (this could include a move but not always)
     */
    public void nextFrame(){

        if (stopTime > 0){
            stopTime -= 1;      // nothing happens
        } else {
            nextMove();
        }
    };


    // Step 2
    // The methods below won't be used / tested before step 2

    /**
     * Set the actor speed
     * The speed is the number of pixels moved at each frame
     * as a decimal number
     * @param speed, a deciman number
     */

    public void setSpeed(double speed){
        this.speed = speed;
    };

    /**
     * Return the speed of the actor
     * @return the speed
     */
    public double getSpeed(){
        return speed;
    };

    public double getPreviousSpeed(){ return previousSpeed;}

    public void setPreviousSpeed(double speed){previousSpeed = speed;}

    /**
     * The actor x position as a decimal number
     * @return  the x position of the actor
     */
    public double getRealX(){
        return realX;
    };

    /**
     * The actor y position as a decimal number
     * @return the y position of the actor
     */
    public double getRealY(){
        return realY;
    };

    public void setRealPosition(double realX, double realY){
        this.realX = realX;
        this.realY = realY;
    }

    /**
     * Stops the actor for a certain number of frames
     * @param nbFrames a positive integer
     */
    public void setStopTime(int nbFrames) {
        stopTime = nbFrames;
    };

    public int getStopTime(){
        return stopTime;
    }

}
