package fr.upsaclay.bibs.pacman.control;

import fr.upsaclay.bibs.pacman.GameType;
import fr.upsaclay.bibs.pacman.PacManException;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.board.BoardState;
import fr.upsaclay.bibs.pacman.view.PacManLayout;
import fr.upsaclay.bibs.pacman.view.PacManView;
import fr.upsaclay.bibs.pacman.view.SwingPacManView;

import java.io.IOException;
import java.net.URISyntaxException;

public class VisualController extends SimpleController {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 40;

    private PacManView view;


    public VisualController() throws PacManException {
        super();
        //try {
                // call parent class to create the board
            //board = getBoard();
            view = new SwingPacManView("PacMan Game", WIDTH, HEIGHT);
            view.setBoard(getBoard());
        //} catch (PacManException e) {
        //    throw new PacManException();
        //}

    }

    @Override
    public void initialize() throws PacManException, IOException, URISyntaxException {

        super.initialize();
        view.setController(this);
        //view.initialize();
        view.setLayout(PacManLayout.INIT);
        //view.setLoopDelay(INITIAL_DELAY);	// initialiser le délai du timer
    }

    @Override
    public void setGameType(GameType gameType) throws PacManException, IOException, URISyntaxException {
        super.setGameType(gameType);
        view.setBoard(getBoard());
        view.setController(this);
        view.initialize();
    }


    @Override
    public void receiveAction(GameAction action) throws PacManException {

        //System.out.println("Action reçue : " + action + " / layout avant changement : " + getLayout());

        super.receiveAction(action);    // call to parent function to manage action
        switch (action) {

            case NEXT_FRAME: view.update(); break;

            case START: view.setLayout(PacManLayout.GAME_ON); break;
            case PAUSE: view.setLayout(PacManLayout.PAUSE); break;
            case RESUME: view.setLayout(PacManLayout.GAME_ON); break;

            case LEVEL_OVER: view.setLayout(PacManLayout.LEVEL_OVER); break;
            case LIFE_OVER: view.setLayout(PacManLayout.LIFE_OVER); break;

            case NEXT_LEVEL: view.setLayout(PacManLayout.GAME_ON); break;
            case NEW_LIFE: view.setLayout(PacManLayout.GAME_ON);break;


            case NEW_GAME: view.setLayout(PacManLayout.INIT); break;
        }

        if (getBoard().getBoardState() == BoardState.LEVEL_OVER){
            view.setLayout(PacManLayout.LEVEL_OVER);
        } else if (getBoard().getBoardState() == BoardState.LIFE_OVER) {
            view.setLayout(PacManLayout.LIFE_OVER);
        } else if (getBoard().getBoardState() == BoardState.GAME_OVER){
            view.setLayout(PacManLayout.GAME_OVER);
        }

        view.update();
    }
}
