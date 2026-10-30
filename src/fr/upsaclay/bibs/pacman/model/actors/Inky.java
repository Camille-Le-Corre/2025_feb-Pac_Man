package fr.upsaclay.bibs.pacman.model.actors;

import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

public class Inky extends AbstractGhost {

    public Inky(Board board, int x, int y) {
        super(GhostType.INKY, GhostState.SCATTER, GhostPenState.IN, board, x, y);
        this.setDirection(Direction.RIGHT);
    }


    public TilePosition getScatterTarget(){
        return new TilePosition(34, 27); // Coin en Scatter
    }

    @Override
    public TilePosition getChaseTarget() {
        int dx = 0, dy = 0;

        switch (getBoard().getPacMan().getDirection()) {
            case UP:
                dx = -2;
                dy = -2; // 2 cases au-dessus + 2 cases à gauche (bug volontaire)
                break;
            case DOWN:
                dy = 2;
                break;
            case LEFT:
                dx = -2;
                break;
            case RIGHT:
                dx = 2;
                break;
        }

        int interLine = getBoard().getPacMan().getCurrentTile().getLine() + dy;
        int interCol = getBoard().getPacMan().getCurrentTile().getCol() + dx;
        TilePosition intermediate = new TilePosition(interLine, interCol);

        TilePosition blinkyTile = getBoard().getGhost(GhostType.BLINKY).getCurrentTile();

        int vectLine = intermediate.getLine() - blinkyTile.getLine();
        int vectCol = intermediate.getCol() - blinkyTile.getCol();

        int targetLine = intermediate.getLine() + vectLine;
        int targetCol = intermediate.getCol() + vectCol;

        return new TilePosition(targetLine, targetCol);
    }


}
