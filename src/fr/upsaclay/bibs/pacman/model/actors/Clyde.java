package fr.upsaclay.bibs.pacman.model.actors;

import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

public class Clyde extends AbstractGhost {

    public Clyde(Board board, int x, int y) {
        super(GhostType.CLYDE, GhostState.SCATTER, GhostPenState.IN, board, x, y);
        this.setDirection(Direction.RIGHT);
    }


    public TilePosition getScatterTarget(){
        return new TilePosition(34, 0); // Coin en Scatter
    }

    public TilePosition getChaseTarget() {
        TilePosition pacmanPos = getBoard().getPacMan().getCurrentTile();
        TilePosition clydePos = this.getCurrentTile();

        double distance = euclideanDistance(pacmanPos, clydePos);

        if (distance >= 8) {
            return pacmanPos; // Chase Pac-Man
        } else {
            return getScatterTarget(); // Retourne à son coin en bas à gauche
        }
    }
}
