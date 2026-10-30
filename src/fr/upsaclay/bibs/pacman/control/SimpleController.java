package fr.upsaclay.bibs.pacman.control;

import fr.upsaclay.bibs.pacman.GameType;
import fr.upsaclay.bibs.pacman.PacManException;
import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.board.BoardState;
import fr.upsaclay.bibs.pacman.view.PacManLayout;
import fr.upsaclay.bibs.pacman.view.PacManView;
import fr.upsaclay.bibs.pacman.view.SwingPacManView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLOutput;

public class SimpleController implements Controller {

    //public static final int WIDTH = 60;
    //public static final int HEIGHT = 40;
    //public static final int INITIAL_DELAY = 50;


    private GameType gameType;
    private Board board;

    private PacManLayout layout = PacManLayout.INIT;


    public SimpleController() throws PacManException{

        try {
            gameType = GameType.CLASSIC;    // Classic board by default
                                            // can be modified by SetGameType() if needed
            //Board
            this.board = Board.createBoard(gameType);
            this.board.initialize();

        } catch (PacManException e) {
            throw new PacManException();
        }
    }


    public KeyListener getKeyListener() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                try {
                    handleKeyPress(event);
                } catch (PacManException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void handleKeyPress(KeyEvent event) throws PacManException{
        int keyCode = event.getKeyCode();

        try {
            switch (keyCode) {
                case KeyEvent.VK_UP:
                    receiveAction(GameAction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    receiveAction(GameAction.DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    receiveAction(GameAction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    receiveAction(GameAction.RIGHT);
                    break;
                default:
                    break;
            }
        } catch (PacManException e) {
            throw new PacManException();
        }
    }

    @Override
    public void initialize() throws PacManException, IOException, URISyntaxException {

        board.setBoardState(BoardState.INITIAL);
        initializeNewGame();
    }

    @Override
    public void initializeNewGame() throws PacManException, URISyntaxException {

        //try {

        /*} catch (PacManException e) {
            throw new PacManException();
        }*/

    }

    @Override
    public void setGameType(GameType gameType) throws PacManException, IOException, URISyntaxException {

        try {
            this.gameType = gameType;

            this.board = Board.createBoard(gameType);

            this.board.initialize();

        } catch (PacManException e) {
            throw new PacManException();
        }
    }

    @Override
    public GameType getGameType() {
        return gameType;
    }

    @Override
    public void receiveAction(GameAction action) throws PacManException, ForbiddenActionException {

        switch (action) {

            case NEXT_FRAME:
                if (layout != PacManLayout.GAME_ON & layout != PacManLayout.LIFE_OVER) {
                    throw new ForbiddenActionException(action);
                }
                getBoard().nextFrame();
                break;

            case START:
                if (layout != PacManLayout.INIT) {
                    throw new ForbiddenActionException(action);
                }
                layout = PacManLayout.GAME_ON;
                //System.out.println("START");
                board.setBoardState(BoardState.STARTED);
                break;

            case PAUSE:
                if (layout != PacManLayout.GAME_ON) {
                    throw new ForbiddenActionException(action);
                }
                layout = PacManLayout.PAUSE;
                //System.out.println("PAUSE");
                break;

            case RESUME:
                if (layout != PacManLayout.PAUSE) {
                    throw new ForbiddenActionException(action);
                }
                layout = PacManLayout.GAME_ON;
                //System.out.println("RESUME");
                break;

            case UP:
                if (layout != PacManLayout.GAME_ON) {
                    throw new ForbiddenActionException(action);
                }
                getBoard().getPacMan().setIntention(Direction.UP);
                //System.out.println("UP");
                break;

            case DOWN:
                if (layout != PacManLayout.GAME_ON) {
                    throw new ForbiddenActionException(action);
                }
                getBoard().getPacMan().setIntention(Direction.DOWN);
                //System.out.println("DOWN");
                break;

            case LEFT:
                if (layout != PacManLayout.GAME_ON) {
                    throw new ForbiddenActionException(action);
                }
                getBoard().getPacMan().setIntention(Direction.LEFT);
                //System.out.println("LEFT");
                break;

            case RIGHT:
                if (layout != PacManLayout.GAME_ON) {
                    throw new ForbiddenActionException(action);
                }
                getBoard().getPacMan().setIntention(Direction.RIGHT);
                //System.out.println("RIGHT");
                break;

            case LEVEL_OVER:
                System.out.println("LEVEL_OVER ; layout = " + layout);
                if (layout != PacManLayout.GAME_ON) {
                    throw new ForbiddenActionException(action);
                }
                layout = PacManLayout.LEVEL_OVER;
                break;

            case LIFE_OVER:
                System.out.println("LIFE_OVER ; layout = " + layout);
                if (layout != PacManLayout.GAME_ON) {
                    throw new ForbiddenActionException(action);
                }
                layout = PacManLayout.LIFE_OVER;
                break;

            case NEXT_LEVEL:
                System.out.println("NEXT_LEVEL ; layout = " + layout);
                if (layout != PacManLayout.LEVEL_OVER) {
                    throw new ForbiddenActionException(action);
                }
                board.initializeNewLevel(board.getLevel() + 1);
                layout = PacManLayout.GAME_ON;
                board.setBoardState(BoardState.STARTED);
                break;

            case NEW_LIFE:
                if (layout != PacManLayout.LIFE_OVER){
                    throw new ForbiddenActionException(action);
                }
                board.initializeNewLife();
                layout = PacManLayout.GAME_ON;
                board.setBoardState(BoardState.STARTED);
                break;

            case NEW_GAME:
                if (layout != PacManLayout.GAME_OVER) {
                    throw new ForbiddenActionException(action);
                }
                board.initialize();
                layout = PacManLayout.INIT;
                board.setBoardState(BoardState.INITIAL);
                break;

        }

        if (board.getBoardState() == BoardState.LEVEL_OVER & layout != PacManLayout.INIT){
            layout = PacManLayout.LEVEL_OVER;
        } else if (board.getBoardState() == BoardState.LIFE_OVER){
            layout = PacManLayout.LIFE_OVER;
        } else if (board.getBoardState() == BoardState.GAME_OVER){
            layout = PacManLayout.GAME_OVER;
        }

    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public PacManLayout getLayout() {
        return layout;
    }
}
