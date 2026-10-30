package fr.upsaclay.bibs.pacman.model.actors;

import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;

public class PacMan extends AbstractActor{

    private Direction direction;
    private Direction intention = null;     // by default, no intention
    private boolean isBlocked = false;

    public PacMan(Board board, Direction dir){
        super(ActorType.PACMAN, board);
        direction = dir;
    }

    /**
     * Return the current direction
     * the actor is moving
     * @return a direction
     */
    public Direction getDirection(){
        return direction;
    };

    /**
     * Sets the actor direction
     * @param direction a direction
     */
    public void setDirection(Direction direction){
        this.direction = direction;
    };


    /**
     * Start the actor at the beginning of the game
     * Perform all necessary action to start the actor at the beginning of the game
     * (this often require to initialize some internal paramaters of the actor)
     */
    public void start(){

    };

    /**
     * Set an intended direction
     * for the actor to change direction
     * if possible
     * @param direction, a direction
     */
    public void setIntention(Direction direction){
        intention = direction;


    };

    /**
     * Return the current intention
     * of the actor for its next direction
     * @return a direction
     */
    public Direction getIntention(){
        return intention;
    };

    /**
     * Return if the actor is blocked by a wall
     * An actor is blocked if they have tried to move and
     * could not because of the wall
     * @return true if the actor is blocked
     */
    public boolean isBlocked(){
        return isBlocked;
    };

    /**
     * Perform the next move on the actor
     */
    public void nextMove(){
        /*
            Si PacMan se trouve avant le milieu d'une tuile, il continue d'avancer dans sa direction
            si PacMan est au milieu de la tuile, il vérifie s'il a une "intention" et met à jour sa direction
            si PacMan a dépassé le milieu de la tuile, il vérifie qu'il peut continuer d'avancer dans sa direction. Si ce n'est pas le cas, il arrête d'avancer, il est bloqué
        */

        // si direction = RIGHT
        // avant milieu = x< col*8 + 3

        // si direction = LEFT
        // avant milieu = x> col*8 + 3

        // si direction = TOP
        // avant milieu = y> line*8 + 3

        // si direction = BOTTOM
        // avant milieu = y< line*8 + 3


        // dans ce cas

        // s'il ne peut pas bouger
        isBlocked = true;


    };

}
