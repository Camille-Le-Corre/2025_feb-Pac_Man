package fr.upsaclay.bibs.pacman.model.actors;
import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

public class Blinky extends AbstractGhost {

    public Blinky(Board board, int x, int y) {
        super(GhostType.BLINKY, GhostState.SCATTER, GhostPenState.OUT, board, x, y);
        this.setDirection(Direction.LEFT);
    }


    public TilePosition getScatterTarget(){
        return new TilePosition(0, 25); // Coin en Scatter
    }

    public TilePosition getChaseTarget(){
        return getBoard().getPacMan().getCurrentTile();
    }
}
