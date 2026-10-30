package fr.upsaclay.bibs.pacman.model.board;

import fr.upsaclay.bibs.pacman.GameType;

public class ClassicTestBoard extends AbstractBoard{

    public ClassicTestBoard(){
        super(GameType.CLASSIC_TEST);
    }

    /**
     * Start the actors
     * Perform all necessary actions to start actors at the beginning of the game
     */
    public void startActors(){
        //PacMan est positionné en x=112, y=211
        getPacMan().setPosition(112, 211);

        // Change boardState to INITIAL to STARTED
        setBoardState(BoardState.STARTED);
    };
}
