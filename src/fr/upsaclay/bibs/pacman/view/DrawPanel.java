package fr.upsaclay.bibs.pacman.view;

import fr.upsaclay.bibs.pacman.GameType;
import fr.upsaclay.bibs.pacman.model.Direction;
import fr.upsaclay.bibs.pacman.model.actors.*;
import fr.upsaclay.bibs.pacman.model.board.Board;
import fr.upsaclay.bibs.pacman.model.maze.Maze;
import fr.upsaclay.bibs.pacman.model.maze.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JPanel;

public class DrawPanel extends JPanel {


    private Board board;
    private Maze maze;
    private static final int size = SwingPacManView.PIXELS_PER_TILE;


    private List<Color[][]> colorMatrixTiles;
    private List<Tile> correspondanceTiles;

    private Color[][] colorMatrixPacMan;

    private List<Color[][]> colorMatrixGhosts;
    private List<Direction> correspondanceGhosts;

    private List<Color[][]> colorMatrixBonus;
    private List<Tile> correspondanceBonus;



    public DrawPanel(int width, int heiht) {
        super();
        setPreferredSize(new Dimension(width * SwingPacManView.PIXELS_PER_TILE, heiht * SwingPacManView.PIXELS_PER_TILE));
    }

    public void initialize() {
        setBackground(Color.BLACK);
    }


    public void setColorMatrixTiles(List<Color[][]> colorMatrixTiles) {
        this.colorMatrixTiles = colorMatrixTiles;
    }

    public void setCorrespondanceTiles(List<Tile> correspondanceTiles){
        this.correspondanceTiles = correspondanceTiles;
    }

    public void setColorMatrixPacMan(Color[][] colorMatrixPacMan){
        this.colorMatrixPacMan = colorMatrixPacMan;
    }

    public void setColorMatrixGhosts(List<Color[][]> colorMatrixGhosts) {
        this.colorMatrixGhosts = colorMatrixGhosts;
    }

    public void setCorrespondanceGhosts(List<Direction> correspondanceGhosts){
        this.correspondanceGhosts = correspondanceGhosts;
    }

    public void setColorMatrixBonus(List<Color[][]> colorMatrixBonus) {
        this.colorMatrixBonus = colorMatrixBonus;
    }

    public void setCorrespondanceBonus(List<Tile> correspondanceBonus){
        this.correspondanceBonus = correspondanceBonus;
    }


    public void paintFromMatrix(Color[][] colorMatrix, Graphics g, int pixelX, int pixelY) {

        for (int i = 0; i < colorMatrix.length; i += 3) {
            for (int j = 0; j < colorMatrix[i].length; j += 3) {
                Color color = colorMatrix[i][j];
                if (!color.equals(Color.BLACK)) {
                    g.setColor(color);
                    g.fillRect(pixelX + j, pixelY + i, 3, 3);
                }
            }
        }
    }

    public void paintMaze(Graphics g) {

        if (maze != null) {

            for (int i = 0; i < maze.getHeight(); i++) {
                for (int j = 0; j < maze.getWidth(); j++) {

                    Tile tile = maze.getTile(i, j);     // for each tile
                    int k = correspondanceTiles.indexOf(tile);
                    if (k != -1) {
                        Color[][] matrix = colorMatrixTiles.get(k);
                        paintFromMatrix(matrix, g, j * size, i * size);
                    }
                }
            }
        }
    }

    public void paintPacMan(Graphics g, int x, int y) {

        x -= 8;
        y -= 8;

        Graphics2D g2d = (Graphics2D) g;

        // Yellow circle
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x, y, size, size);

        // Black triangle for its mouth
        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int mouthOffset = size / 4;

        Polygon mouth = new Polygon();

        mouth.addPoint(centerX, centerY); // centre


        // Mouth orientation depends on its direction
        switch (board.getPacMan().getDirection()) {
            case LEFT:
                mouth.addPoint(x, centerY - mouthOffset);
                mouth.addPoint(x, centerY + mouthOffset);
                break;
            case RIGHT:
                mouth.addPoint(x + size, centerY - mouthOffset);
                mouth.addPoint(x + size, centerY + mouthOffset);
                break;
            case UP:
                mouth.addPoint(centerX - mouthOffset, y);
                mouth.addPoint(centerX + mouthOffset, y);
                break;
            case DOWN:
                mouth.addPoint(centerX - mouthOffset, y + size);
                mouth.addPoint(centerX + mouthOffset, y + size);
                break;
        }

        g2d.setColor(Color.BLACK);
        g2d.fillPolygon(mouth);
    }

    private boolean isFlashing(int frame){
        /* Return True if the frame is a flash frame or a normal one */

        return frame%10 <5;
    }

    public void drawGhost(Graphics g, int x, int y, int ghostSize, GhostType type, GhostState ghostState, Direction direction) {

        Graphics2D g2d = (Graphics2D) g;
        x -= 8;
        y -= 8;

        if (ghostState != GhostState.DEAD){

            Color bodyColor = switch (type) {
                case BLINKY -> Color.RED;
                case PINKY -> new Color(255, 150, 240);
                case INKY -> Color.CYAN;
                case CLYDE -> new Color(255, 150, 0);
                default -> Color.GRAY;
            };

            if (ghostState == GhostState.FRIGHTENED || (ghostState == GhostState.FRIGHTENED_END && !isFlashing(board.getRemainingFrightenedFrames()))){
                bodyColor = Color.BLUE;
            } else if (ghostState == GhostState.FRIGHTENED_END && isFlashing(board.getRemainingFrightenedFrames())) {
                bodyColor = Color.WHITE;
            }

            int radius = size / 2;
            int width = size;
            int height = size;

            // Body
            g2d.setColor(bodyColor);

            // Head
            g2d.fillArc(x, y, width, height, 0, 180);

            // Bottom
            g2d.fillRect(x, y + radius, width, radius);

            // "triangles"
            g2d.setColor(Color.BLACK);
            int triWidth = width / 3;
            int triHeight = radius / 2;

            Polygon leftTooth = new Polygon(
                    new int[]{x, x + triWidth / 2, x + triWidth},
                    new int[]{y + height, y + height - triHeight, y + height},
                    3
            );

            Polygon middleTooth = new Polygon(
                    new int[]{x + width / 2 - triWidth / 2, x + width / 2, x + width / 2 + triWidth / 2},
                    new int[]{y + height, y + height - triHeight, y + height},
                    3
            );

            Polygon rightTooth = new Polygon(
                    new int[]{x + width - triWidth, x + width - triWidth / 2, x + width},
                    new int[]{y + height, y + height - triHeight, y + height},
                    3
            );

            g2d.fillPolygon(leftTooth);
            g2d.fillPolygon(middleTooth);
            g2d.fillPolygon(rightTooth);

        }

        // Eyes
        int eyeWidth = ghostSize / 4;
        int eyeHeight = ghostSize / 3;
        int pupilSize = ghostSize / 5;
        int eyeY = y + ghostSize / 4;
        int eyeGap = ghostSize / 6;
        int eyeCenter = x + ghostSize / 2;

        int leftEyeX = eyeCenter - eyeWidth - eyeGap / 2;
        int rightEyeX = eyeCenter + eyeGap / 2;

        if (ghostState == GhostState.FRIGHTENED_END && isFlashing(board.getRemainingFrightenedFrames())){
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.WHITE);
        }
        g2d.fillOval(leftEyeX, eyeY, eyeWidth, eyeHeight);
        g2d.fillOval(rightEyeX, eyeY, eyeWidth, eyeHeight);

        // Directional pupils
        int pupilOffsetX = 0;
        int pupilOffsetY = 0;
        switch (direction) {
            case UP -> pupilOffsetY = -pupilSize / 2;
            case DOWN -> pupilOffsetY = pupilSize / 2;
            case LEFT -> pupilOffsetX = -pupilSize / 2;
            case RIGHT -> pupilOffsetX = pupilSize / 2;
        }

        if (ghostState == GhostState.FRIGHTENED || ghostState == GhostState.FRIGHTENED_END){
            pupilOffsetX = 0;
            pupilOffsetY = 0;

            // Add its mouth
            int mouthWidth = ghostSize / 2;
            int mouthHeight = ghostSize / 12;
            int mouthX = x + (ghostSize - mouthWidth) / 2;
            int mouthY = y + ghostSize * 3 / 4;

            int segments = 6;
            int segmentWidth = mouthWidth / segments;

            int[] xPoints = new int[segments + 1];
            int[] yPoints = new int[segments + 1];

            for (int i = 0; i <= segments; i++) {
                xPoints[i] = mouthX + i * segmentWidth;
                if (i % 2 == 0) {
                    yPoints[i] = mouthY + mouthHeight;
                } else {
                    yPoints[i] = mouthY - mouthHeight;
                }
            }

            if (ghostState == GhostState.FRIGHTENED_END && isFlashing(board.getRemainingFrightenedFrames())){
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.WHITE);
            }

            g2d.setStroke(new BasicStroke(1));
            for (int i = 0; i < segments; i++) {
                g2d.drawLine(xPoints[i], yPoints[i], xPoints[i + 1], yPoints[i + 1]);
            }
        }

        if (ghostState == GhostState.FRIGHTENED_END && isFlashing(board.getRemainingFrightenedFrames())){
            g2d.setColor(Color.RED);
        } else {
            g2d.setColor(Color.BLACK);
        }
        g2d.setColor(Color.BLACK);
        g2d.fillOval(leftEyeX + eyeWidth / 2 - pupilSize / 2 + pupilOffsetX,
                eyeY + eyeHeight / 2 - pupilSize / 2 + pupilOffsetY,
                pupilSize, pupilSize);
        g2d.fillOval(rightEyeX + eyeWidth / 2 - pupilSize / 2 + pupilOffsetX,
                eyeY + eyeHeight / 2 - pupilSize / 2 + pupilOffsetY,
                pupilSize, pupilSize);
    }

    public void paintBonus(Graphics g, int x, int y){
        Graphics2D g2d = (Graphics2D) g;

        // Paramètres
        int cherryRadius = 6;
        int cherrySpacing = 4;

        // Position des cerises
        int cherry1X = x;
        int cherry1Y = y;
        int cherry2X = x + cherryRadius + cherrySpacing;
        int cherry2Y = y + 2;

        // Position du point de jonction des tiges
        int stemTopX = x + cherryRadius + cherrySpacing / 2;
        int stemTopY = y - 10;

        // Dessiner les cerises
        g2d.setColor(new Color(220, 0, 0)); // Rouge vif
        g2d.fillOval(cherry1X, cherry1Y, cherryRadius * 2, cherryRadius * 2);
        g2d.fillOval(cherry2X, cherry2Y, cherryRadius * 2, cherryRadius * 2);

        // Reflets
        g2d.setColor(Color.WHITE);
        g2d.fillOval(cherry1X + 3, cherry1Y + 3, 3, 3);
        g2d.fillOval(cherry2X + 3, cherry2Y + 3, 3, 3);

        // Tiges
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(new Color(139, 69, 19)); // Marron foncé
        g2d.drawLine(cherry1X + cherryRadius, cherry1Y, stemTopX, stemTopY);
        g2d.drawLine(cherry2X + cherryRadius, cherry2Y, stemTopX, stemTopY);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Maze
        paintMaze(g);

        // Pac-Man
        paintPacMan(g, board.getPacMan().getX()*3, board.getPacMan().getY()*3);

        // Ghosts
        if (board.getGameType() != GameType.TEST){

            for (Ghost ghost : board.getGhosts()){  // for each ghost
                GhostType ghostType = ghost.getGhostType();     // gets its type
                GhostState ghostState = board.getGhost(ghostType).getGhostState();   // gets its state
                drawGhost(g, ghost.getX()*3, ghost.getY()*3, 24, ghostType, ghostState, ghost.getDirection());
            }

        }

        // Bonus
        Bonus bonus = board.getBonus();
        if (bonus.isActive()){
            paintBonus(g, bonus.getX()*3, bonus.getY()*3);
        }

        g.setFont(new Font("Monospaced", Font.BOLD, 28));
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + board.getScore(), 10, 20);
    }


    public Board getBoard(){ return board;}

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public Maze getMaze(){
        return maze;
    }
}
