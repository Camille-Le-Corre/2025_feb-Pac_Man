package fr.upsaclay.bibs.pacman.model.actors;

import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

public class Pinky extends AbstractGhost{

    public Pinky(Board board, int x, int y) {
        super(GhostType.PINKY, GhostState.SCATTER, GhostPenState.IN, board, x, y);
        this.setDirection(Direction.LEFT);
    }


    public TilePosition getScatterTarget(){
        return new TilePosition(0, 2);
    }

    public TilePosition getChaseTarget(){

        Direction pacmanDirection = getBoard().getPacMan().getDirection();
        TilePosition pacmanPosition = getBoard().getPacMan().getCurrentTile();

        int lineOffset=0;
        int colOffset=0;

        switch (pacmanDirection) {

            case LEFT:
                colOffset = -4;
                break;

            case RIGHT:
                colOffset = 4;
                break;

            case DOWN:
                lineOffset = 4;
                break;

            case UP:
                lineOffset = -4;
                colOffset = -4;
                break;
        }

        return new TilePosition(pacmanPosition.getLine() + lineOffset, pacmanPosition.getCol() + colOffset);
    }
}
