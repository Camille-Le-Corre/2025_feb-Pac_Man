package fr.upsaclay.bibs.pacman.model.actors;

import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.TilePosition;

public class AbstractBonus extends AbstractActor implements Bonus {

    BonusType bonusType;
    boolean isActive = false;

    public AbstractBonus(BonusType type, Board board, int x, int y) {
        super(ActorType.BONUS, board, x, y);
        bonusType = type;
    }

    @Override
    public BonusType getBonusType() {
        return bonusType;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active){
        isActive = active;
    }

    @Override
    public void start() {

    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public void setDirection(Direction direction) {
        return;
    }

    @Override
    public void setIntention(Direction direction) {
        return;
    }

    @Override
    public Direction getIntention() {
        return null;
    }

    @Override
    public boolean isBlocked() {
        return false;
    }

    @Override
    public void nextMove() {
        return;
    }

    @Override
    public void nextFrame() {
        return;
    }

}
