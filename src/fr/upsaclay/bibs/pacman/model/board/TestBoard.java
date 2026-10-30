package fr.upsaclay.bibs.pacman.model.board;

import fr.upsaclay.bibs.pacman.GameType;

public class TestBoard extends AbstractBoard{

    public TestBoard(){
        super(GameType.TEST);
    }

    /**
     * Start the actors
     * Perform all necessary actions to start actors at the beginning of the game
     */
    public void startActors(){
        //PacMan est positionné en x=35, y=75
        getPacMan().setPosition(35, 75);

        // Change boardState to INITIAL to STARTED
        setBoardState(BoardState.STARTED);
    };

}
