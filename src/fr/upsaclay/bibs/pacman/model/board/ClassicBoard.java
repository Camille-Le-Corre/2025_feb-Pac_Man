package fr.upsaclay.bibs.pacman.model.board;

import fr.upsaclay.bibs.pacman.GameType;
import fr.upsaclay.bibs.pacman.model.actors.Actor;

public class ClassicBoard extends AbstractBoard {

    public ClassicBoard(){
        super(GameType.CLASSIC);
    }

    /**
     * Start the actors
     * Perform all necessary actions to start actors at the beginning of the game
     */
    public void startActors(){
        // Change boardState to INITIAL to STARTED
        setBoardState(BoardState.STARTED);
    };
}
