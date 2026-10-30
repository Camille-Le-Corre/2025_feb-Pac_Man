package fr.upsaclay.bibs.pacman.model.actors;

import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.board.Counter;
import fr.upsaclay.bibs.pacman.model.maze.Tile;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

public abstract class AbstractGhost extends AbstractActor implements Ghost {

    private final GhostType ghostType;
    public GhostState ghostState;
    private Direction direction;
    private Direction intention;
    private GhostPenState penState;
    private Direction penDirection;

    public AbstractGhost(GhostType type, GhostState state, GhostPenState penState, Board board, int x, int y) {
        super(ActorType.GHOST, board, x, y);
        this.ghostType = type;
        this.ghostState = state;
        this.penState = penState;
    }

    /**
     * Return the type of ghost it is
     * @return a ghost type
    */
    public GhostType getGhostType() {
        return ghostType;
    }

    /**
     * Return the current direction
     * the actor is moving
     * @return a direction
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * Sets the actor direction
     * @param direction a direction
     */
    public void setDirection(Direction direction){
        this.direction = direction;
    }

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

    /*
    public final TilePosition getTarget() {
        if (getGhostState() == GhostState.FRIGHTENED || getGhostState() == GhostState.FRIGHTENED_END) {
            return new TilePosition(0, 25); // Coin pour Scatter/Frightened
        } else if (getGhostState() == GhostState.SCATTER) {
            return getScatterTarget();
        } else { // GhostState.CHASE
            return getChaseTarget();
        }
    }
     */

    @Override
    public final TilePosition getTarget() {
        if (getGhostPenState() == GhostPenState.GET_OUT || getGhostPenState() == GhostPenState.GET_IN) {
            return new TilePosition(14, 13); // cible temporaire : sortie du Ghost Pen
        }

        if (getGhostState() == GhostState.FRIGHTENED || getGhostState() == GhostState.FRIGHTENED_END) {
            return new TilePosition(0, 25); // cible du coin en mode Frightened
        } else if (getGhostState() == GhostState.SCATTER) {
            return getScatterTarget(); // cible coin spécifique
        } else { // GhostState.CHASE
            return getChaseTarget(); // cible classique de Chase
        }
    }



    /**
     * Start the actor at the beginning of the game
     * Perform all necessary action to start the actor at the beginning of the game
     * (this often require to initialize some internal paramaters of the actor)
     */
    public void start(){
        return;
    }

    /**
     * Return if the actor is blocked by a wall
     * An actor is blocked if they have tried to move and
     * could not because of the wall
     * @return true if the actor is blocked
     */
    public boolean isBlocked(){
        return false;
    }

    private void updatePosition(Direction direction) {
        /* Update Ghost position (both real position and int position), depending on its direction */

        //int newX=0;
        double newRealX;
        double speed = getSpeed();

        switch (direction){

            case Direction.RIGHT:
                newRealX = getRealX() + speed;
                //Si on est au bord du maze mais qu'on peut aller de l'autre côté
                if (newRealX >= getBoard().getMaze().getPixelWidth()){
                    setRealPosition(0, getRealY());
                    setPosition((int)getRealX(), (int)getRealY());
                } else {
                    setRealPosition(getRealX()+speed, getRealY());
                    setPosition((int)getRealX(), (int)getRealY());
                }
                break;

            case Direction.LEFT:
                newRealX = getRealX() - speed;
                if (newRealX < 0){
                    setRealPosition(getBoard().getMaze().getPixelWidth()-speed, getRealY());
                    setPosition((int)getRealX(), (int)getRealY());
                } else {
                    setRealPosition(getRealX()-speed, getRealY());
                    setPosition((int)getRealX(), (int)getRealY());
                }
                break;

            case Direction.UP:
                setRealPosition(getRealX(), getRealY()-speed);
                setPosition((int)getRealX(), (int)getRealY());
                break;

            case Direction.DOWN:
                setRealPosition(getRealX(), getRealY()+speed);
                setPosition((int)getRealX(), (int)getRealY());
                break;
        }
    }


    public double euclideanDistance(TilePosition a, TilePosition b) {
        int dx = a.getCol() - b.getCol();
        int dy = a.getLine() - b.getLine();
        return Math.sqrt(dx * dx + dy * dy);
    }


    /**
     * Perform the next move on the actor
     */
    public void nextMove() {
        TilePosition currentTile = getCurrentTile();
        TilePosition target = getTarget();
        Direction bestDirection = null;
        double minDistance = Double.MAX_VALUE;

        // Si fantôme est dans le Ghost Pen et doit sortir
        if (penState == GhostPenState.GET_OUT && currentTile.equals(new TilePosition(14, 13))) {
            setDirection(Direction.UP);
            penState = GhostPenState.OUT;
        }

        // Si au centre de la tuile
        if (getX() == currentTile.getCol() * 8 + 3 && getY() == currentTile.getLine() * 8 + 3) {
            if (mustFollowIntention) {
                setDirection(getIntention());
                mustFollowIntention = false;
            } else if (ghostState == GhostState.FRIGHTENED || ghostState == GhostState.FRIGHTENED_END) {
                setDirection(getIntention());
            } else {
                // Calcul de la meilleure direction selon le target
                for (Direction dir : Direction.values()) {
                    if (dir == getDirection().reverse()) continue;

                    Tile neighbourTile = getBoard().getMaze().getNeighbourTile(currentTile, dir);

                    if (!neighbourTile.isWall()) {
                        TilePosition nextTile = getBoard().getMaze().getNeighbourTilePosition(currentTile, dir);

                        if ((dir == Direction.UP && neighbourTile.ghostCanGoUp()) || dir != Direction.UP) {
                            double distance = euclideanDistance(nextTile, target);
                            if (distance < minDistance) {
                                minDistance = distance;
                                bestDirection = dir;
                            }
                        }
                    }
                }

                if (bestDirection != null) {
                    setIntention(bestDirection);
                }

                setDirection(getIntention());
            }
        }

        // Fantôme avance selon sa direction
        updatePosition(getDirection());
    }



    // Step 3


    /**
     * Sets the Ghost state, which defines in particular its target and moves
     * @param state, a ghost state
     */
    public void setGhostState(GhostState state){
        ghostState = state;
    }

    /**
     * Perform all necessary actions for changing the ghost state from it current state to the new one
     * @param state, the new state of the ghost
     */
    public void changeGhostState(GhostState state){
        //System.out.println("Appel de changeGhostState avec état : " + state + " ; " + getCurrentTile());
        this.ghostState = state;
        if (state != GhostState.FRIGHTENED_END){
            reverseDirectionIntention();
        }
    }

    /**
     * Return the current ghost state
     * @return a ghost state
     */
    public GhostState getGhostState(){
        return ghostState;
    }

    /**
     * Sets the ghost pen state,
     * i.e. whether the ghost is in the pen, out or getting in / getting out
     * @param state, a ghost pen state
     */
    public void setGhostPenState(GhostPenState state){
        this.penState = state;
    }

    /**
     * Return the current ghost pen state
     * @return the ghost pe state
     */
    public GhostPenState getGhostPenState(){
        return penState;
    }

    /**
     * Sets the direction that the ghost should take when its gets out of the ghost pen
     * @param dir a direction
     */
    public void setOutOfPenDirection(Direction dir){
        this.penDirection = dir;
    }

    /**
     * Return the direction taken by the ghost when getting out of the pen
     * @return a direction
     */
    public Direction getOutOfPenDirection(){
        return penDirection;
    }

    /**
     * If out of pen : sends the intention to reverse direction
     * If in pen or getting in or out of pen : set the out of pen direction to right
     */
    @Override
    public void reverseDirectionIntention() {

        if (getGhostPenState() == GhostPenState.OUT) {
            mustFollowIntention = true;
            setIntention(getDirection().reverse());
        } else {
            setOutOfPenDirection(Direction.RIGHT);
        }
    }

    /**
     * Return whether the ghost has a "dot counter" to decide when it gets out of the ghost pen
     * @return true if the ghost uses a dot counter
     */
    public boolean hasDotCounter(){
        return true;
    }

    /**
     * Return the dot counter of the ghost if it uses one
     * @return a dot counter or null
     */
    public Counter getDotCounter(){
        return new Counter(2);
    }

    // Step 4
    // The methods below won't be used / tested before step 3

    /**
     * Sets the "Elroy" value of the ghost
     * typically, 0 (not in elroy mode), 1, or 2
     * @param elroy the elroy value
     */
    public void setElroy(int elroy){
        return;
    }

    /**
     * Return the "Elroy" value of the ghost
     * @return the Elroy value
     */
    public int getElroy(){
        return 1;
    }
}
