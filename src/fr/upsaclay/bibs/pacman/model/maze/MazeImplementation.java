package fr.upsaclay.bibs.pacman.model.maze;

import fr.upsaclay.bibs.pacman.model.Direction;

import java.io.FileNotFoundException;

public class MazeImplementation implements Maze {


    private static final int DEFAULT_NB_WIDTH_TILES = 28;
    private static final int DEFAULT_NB_HEIGHT_TILES = 36;

    private static int nbWidthTiles;
    private static int nbHeightTiles;
    private static Tile[][] mazeTable;



    public MazeImplementation(int nbWidthTiles, int nbHeightTiles){
        MazeImplementation.nbWidthTiles = nbWidthTiles;
        MazeImplementation.nbHeightTiles = nbHeightTiles;

        /* Table initialisation */
        MazeImplementation.mazeTable = new Tile[nbHeightTiles][nbWidthTiles];

        for (int i = 0; i < nbHeightTiles; i++) {
            for (int j = 0; j < nbWidthTiles; j++) {
                mazeTable[i][j] = Tile.EE;      // each tile is initialized as empty
            }
        }
    }

    public MazeImplementation(){
        this(DEFAULT_NB_WIDTH_TILES,DEFAULT_NB_HEIGHT_TILES);
    }


    public int getWidth(){
        /* Return the maze width (the number of tiles horizontally) */

        return nbWidthTiles;
    };

    /**
     * Return the width in number of pixels
     * @return the number of horizontal pixels
     */
    public int getPixelWidth(){

        return getWidth()*Maze.TILE_WIDTH;
    };

    /**
     * Return the maze height
     * i.e. the number of tiles vertically
     * @return the number of vertical pixels
     */
    public int getHeight(){
        /* */

        return nbHeightTiles;
    };

    /**
     * Return the maze height in number of pixels
     * @return the pixel height
     */
    public int getPixelHeight(){
        /* */

        return getHeight()*Maze.TILE_HEIGHT;
    };

    /**
     * Get the tile at given position
     * @param line, the line number
     * @param col, the col number
     * @return the corresponding tile
     */
    public Tile getTile(int line, int col){
        return mazeTable[line][col];
    };

    /**
     * Get the tile at a given position
     * @param pos, a tile position
     * @return the corresponding tile
     */
    public Tile getTile(TilePosition pos){

        return mazeTable[pos.getLine()][pos.getCol()];
    };

    /**
     * Return the next tile from a position in
     * a given direction
     * @param line, the line number
     * @param col, the col number
     * @param dir, the direction
     * @return the corresponding tile
     */
    public TilePosition getNeighbourTilePosition(int line, int col, Direction dir){

        /* Special cases : circularity  when get out of the grid */
        if (col+dir.getDx() < 0){      // left boundary
            return new TilePosition(line, getWidth()-1);    // same line, last column
        } else if (col+dir.getDx() >= getWidth()) {     // right boundary
            // == works also
            return new TilePosition(line, 0);   // same line, first column
        } else if (line+dir.getDy() < 0) {      // top boundary
            return new TilePosition(getHeight()-1, col);    // same column, last line
        } else if (line+dir.getDy() >= getHeight()) {       // bottom boundary
            return new TilePosition(0, col);    // same column, first line
        }

        /* general case */
        return new TilePosition(line+dir.getDy(), col+dir.getDx());
    };

    /**
     * Return the next tile from a position in
     * a given direction
     * @param pos, a tile position
     * @param dir, a direction
     * @return the corresponding tile
     */
    public TilePosition getNeighbourTilePosition(TilePosition pos, Direction dir){
        return getNeighbourTilePosition(pos.getLine(), pos.getCol(), dir);
    };

    /**
     * Return the next tile from a position in
     * a given direction
     * @param line, the line number
     * @param col, the col number
     * @param dir, the direction
     * @return the corresponding tile
     */
    public Tile getNeighbourTile(int line, int col, Direction dir){
        TilePosition newTilePosition = getNeighbourTilePosition(line, col, dir);
        return mazeTable[newTilePosition.getLine()][newTilePosition.getCol()];
    };

    /**
     * Return the next tile from a position in
     * a given direction
     * @param pos, a tile position
     * @param dir, a direction
     * @return the corresponding tile
     */
    public Tile getNeighbourTile(TilePosition pos, Direction dir){

        TilePosition newTilePosition = getNeighbourTilePosition(pos.getLine(), pos.getCol(), dir);
        return mazeTable[newTilePosition.getLine()][newTilePosition.getCol()];
    };

    /**
     * Put a given tile at the position
     * @param line, the line number
     * @param col, the col number
     * @param tile, the tile
     */
    public void setTile(int line, int col, Tile tile){

        mazeTable[line][col] = tile;
    };

    /**
     * Put a given tile at the position
     * @param pos, the tile position
     * @param tile, the tile
     */
    public void setTile(TilePosition pos, Tile tile){

        mazeTable[pos.getLine()][pos.getCol()] = tile;

    };

    /**
     * Return the tile position of a given pixel
     * @param x, the x coordinate of the pixel
     * @param y, the y coordinate of the pixel
     * @return a tile position
     */
    public TilePosition getTilePosition(int x, int y){

        return new TilePosition(y/8, x/8);  // division entière par 8 (taille d'une tuile en pixels)
    };


    // Step 2
    // The methods below won't be used / tested before step 2

    /**
     * Return the current number of dots on the maze (big and small)
     * @return the number of dots
     */
    public int getNumberOfDots(){

        int dots = 0;

        for (int i = 0; i < nbHeightTiles; i++) {
            for (int j = 0; j < nbWidthTiles; j++) {

                Tile tile = mazeTable[i][j];    // for each tile

                if (tile == Tile.SD || tile == Tile.ND || tile == Tile.BD){     // if this tile is a dot

                    dots += 1;      // increase the number of dots
                }
            }
        }

        return dots;
    };

}
