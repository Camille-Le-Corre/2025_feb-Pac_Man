package fr.upsaclay.bibs.pacman.view;

import fr.upsaclay.bibs.pacman.control.Controller;
import fr.upsaclay.bibs.pacman.control.GameAction;
import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.actors.GhostType;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.Tile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SwingPacManView extends JFrame implements PacManView {

    public static final int PIXELS_PER_TILE = 24;
    public static final int width=PIXELS_PER_TILE*28;  // a changer après
    public static final int height=PIXELS_PER_TILE*36;     // a changer après
    private static final int borderSize = 434;

    private static final Color[] colors = new Color[]{Color.BLACK, Color.BLUE, Color.YELLOW, Color.GRAY, Color.WHITE};

    private Board board;
    private Controller controller;

    // Initialisation
    DrawPanel drawPanel;
    JPanel controlPanel;
    Timer timer;

    JPanel initialPanel;
    JPanel playPanel;
    JPanel pausePanel;
    JPanel levelOverPanel;
    JPanel lifeOverPanel;
    JPanel gameOverPanel;
    JPanel borderPanel;

    private JLabel lifeOverLabel;



    public SwingPacManView(String name, int widthField, int heightField) {

        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Create the drawPanel
        drawPanel = new DrawPanel(width, height);

        // Create the control panel
        controlPanel = new JPanel();

        // Create the border panel
        borderPanel = new JPanel();

        // Create the loop timer
        timer = new Timer(17, null);
    }

    /**
     * Sets the current board
     * the view should never perform any action on the board
     * the board is set only so that the view has the information to show the game
     * it should NOT be modified by the view
     * @param board a PacMan board
     */
    public void setBoard(Board board) {
        this.board = board;
        drawPanel.setBoard(board);
        drawPanel.setMaze(board.getMaze());
    }


    /**
     * Sets the controller
     * this is used to send action to the controller when the user interacts
     * with the view
     * @param controller, the controller of the application
     */
    public void setController(Controller controller) {
        this.controller = controller;
        addKeyListener(controller.getKeyListener());
        setFocusable(true);
    }


    /**
     * View initialization
     */
    @Override
    public void initialize() throws IOException, URISyntaxException{

        // Import textures from file
        loadTilesFromFile();
        //loadGhostsFromFile();
        //loadBonusFromFile();

        // Draw panel
        drawPanel.initialize();
        add( drawPanel, BorderLayout.CENTER );

        // Border panel
        add(borderPanel, BorderLayout.WEST);
        borderPanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        borderPanel.setBackground(colors[0]);

        // Control panel
        add( controlPanel, BorderLayout.EAST);
        controlPanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        controlPanel.setBackground(colors[0]);

        // Initial panel : before starting the game
        initialPanel = new JPanel();
        initialPanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        initialPanel.setBackground(colors[0]);
        // Start button
        JButton initialStartButton = new JButton("Start");
        initialStartButton.addActionListener(new ButtonListener(controller, GameAction.START));
        initialPanel.add(initialStartButton);
        controlPanel.add(initialPanel);

        // Play panel : when the game is on
        playPanel = new JPanel();
        playPanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        playPanel.setBackground(colors[0]);
        // Pause button
        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ButtonListener(controller, GameAction.PAUSE));
        playPanel.add(pauseButton);
        controlPanel.add(playPanel);

        // Pause panel
        pausePanel = new JPanel();
        pausePanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        pausePanel.setBackground(colors[0]);
        // Resume button
        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ButtonListener(controller, GameAction.RESUME));
        pausePanel.add(resumeButton);
        controlPanel.add(pausePanel);

        // Level over panel
        levelOverPanel = new JPanel();
        levelOverPanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        levelOverPanel.setBackground(colors[0]);
        // Display message
        JLabel levelOverLabel = new JLabel("Level Complete!");
        levelOverPanel.add(levelOverLabel);
        // Next level button
        JButton nextLevelButton = new JButton("Next level");
        nextLevelButton.addActionListener(new ButtonListener(controller, GameAction.NEXT_LEVEL));
        levelOverPanel.add(nextLevelButton);
        controlPanel.add(levelOverPanel);

        // Life over panel
        lifeOverPanel = new JPanel();
        lifeOverPanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        lifeOverPanel.setBackground(colors[0]);
        // Display message
        lifeOverLabel = new JLabel();
        lifeOverPanel.add(lifeOverLabel);
        // Next life button
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ButtonListener(controller, GameAction.NEW_LIFE));
        lifeOverPanel.add(continueButton);
        controlPanel.add(lifeOverPanel);

        // Game over panel
        gameOverPanel = new JPanel();
        gameOverPanel.setPreferredSize(new Dimension(borderSize, drawPanel.getPreferredSize().height));
        gameOverPanel.setBackground(colors[0]);
        // Display message
        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverPanel.add(gameOverLabel);
        // New game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ButtonListener(controller, GameAction.NEW_GAME));
        gameOverPanel.add(newGameButton);
        controlPanel.add(gameOverPanel);

        // Timer initialization
        timer.addActionListener(new ButtonListener(controller, GameAction.NEXT_FRAME));

        //setSize(width+100, height);
        pack();
        setVisible(true);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private void drawInitView(){

        drawPanel.setVisible(true);
        initialPanel.setVisible(true);
        playPanel.setVisible(false);
        pausePanel.setVisible(false);
        levelOverPanel.setVisible(false);
        lifeOverPanel.setVisible(false);
        gameOverPanel.setVisible(false);
    }

    private void drawGameOnView(){

        drawPanel.setVisible(true);
        initialPanel.setVisible(false);
        playPanel.setVisible(true);
        pausePanel.setVisible(false);
        levelOverPanel.setVisible(false);
        lifeOverPanel.setVisible(false);
        gameOverPanel.setVisible(false);

        timer.start();	// lancer le timer
    }

    private void drawPauseView(){

        timer.stop();	// arreter le timer

        drawPanel.setVisible(true);
        initialPanel.setVisible(false);
        playPanel.setVisible(false);
        pausePanel.setVisible(true);
        levelOverPanel.setVisible(false);
        lifeOverPanel.setVisible(false);
        gameOverPanel.setVisible(false);
    }

    private void drawLevelOverView(){

        drawPanel.setVisible(true);
        initialPanel.setVisible(false);
        playPanel.setVisible(false);
        pausePanel.setVisible(false);
        levelOverPanel.setVisible(true);
        lifeOverPanel.setVisible(false);
        gameOverPanel.setVisible(false);

        timer.stop();

    }

    private void drawLifeOverView(){

        drawPanel.setVisible(true);
        initialPanel.setVisible(false);
        playPanel.setVisible(false);
        pausePanel.setVisible(false);
        levelOverPanel.setVisible(false);
        lifeOverPanel.setVisible(true);
        gameOverPanel.setVisible(false);

        int numberOfLives = board.getNumberOfLives() + 1;
        if (numberOfLives == 2){
            lifeOverLabel.setText("Pac-Man got caught! Tou have " + numberOfLives + " lives left");
        } else {
            lifeOverLabel.setText("Pac-Man got caught! Tou have " + numberOfLives + " life left");
        }

        timer.stop();

    }

    private void drawGameOverView(){

        drawPanel.setVisible(true);
        initialPanel.setVisible(false);
        playPanel.setVisible(false);
        pausePanel.setVisible(false);
        levelOverPanel.setVisible(false);
        lifeOverPanel.setVisible(false);
        gameOverPanel.setVisible(true);

        timer.stop();
    }


    /**
     * Sets the current layout
     * layouts correspond to game phases
     * when a new layout is set, the view should take all necessary actions
     * for updating the user interface
     * @param layout, the game layout to show
     */
    public void setLayout(PacManLayout layout) {

        switch (layout) {
            case INIT: drawInitView(); break;
            case GAME_ON: drawGameOnView();break;
            case PAUSE: drawPauseView();break;
            case LEVEL_OVER: drawLevelOverView();break;
            case LIFE_OVER: drawLifeOverView();break;
            case GAME_OVER: drawGameOverView();break;
        }
    }


    /**
     * Update the view (for example, because something has changed on the board)
     */
    public void update() {
        repaint();
    }

    private void loadTilesFromFile() throws IOException, URISyntaxException {

        String[] fileList = {""};

        // List all the files with walls
        URL url = getClass().getClassLoader().getResource("tiles");
        if (url != null) {
            File directory = new File(url.toURI());
            fileList = directory.list();

        } else {
            System.out.println("Repertory not found");
        }

        java.util.List<Color[][]> colorMatrix = new ArrayList<>();
        java.util.List<Tile> correspondanceList = new ArrayList<>();


        // Pour chaque fichier : transformer en matrice et ajouter à la liste de correspondance
        for (int k=0; k<fileList.length; k++){
            colorMatrix.add(new DrawFromFile8(fileList[k], colors).getColorPixelTable24());
            correspondanceList.add(Tile.valueOf(fileList[k].substring(0, 2)));

        }

        drawPanel.setColorMatrixTiles(colorMatrix);
        drawPanel.setCorrespondanceTiles(correspondanceList);
    }

    private void loadBonusFromFile() throws IOException, URISyntaxException {

        String[] fileList = {""};

        // List all the files with ghosts
        URL url = getClass().getClassLoader().getResource("bonus/bonus24");
        if (url != null) {
            File directory = new File(url.toURI());
            fileList = directory.list();

        } else {
            System.out.println("Repertory not found");
        }

        java.util.List<Color[][]> colorMatrix = new ArrayList<>();
        java.util.List<Tile> correspondanceList = new ArrayList<>();


        // Pour chaque fichier : transformer en matrice et ajouter à la liste de correspondance
        for (int k=0; k<fileList.length; k++){
            colorMatrix.add(new DrawFromFile16(fileList[k], colors).getColorPixelTable24());
            correspondanceList.add(Tile.valueOf(fileList[k].substring(0, 2)));

        }

        drawPanel.setColorMatrixBonus(colorMatrix);
        drawPanel.setCorrespondanceBonus(correspondanceList);
    }

}
