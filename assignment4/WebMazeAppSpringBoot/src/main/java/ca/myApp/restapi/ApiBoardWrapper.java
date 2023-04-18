package ca.myApp.restapi;

/**
 * Hold information of each board created during runtime
 */

import ca.myApp.model.CellLocation;
import ca.myApp.model.Maze;
import ca.myApp.model.MazeGame;
import java.util.ArrayList;
import java.util.List;

public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    // Accept whatever object(s) you need to populate this object.
    public static ApiBoardWrapper makeFromGame(MazeGame game) {

        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.boardWidth = MazeGame.getMazeWidth(); // Fill this in, along with all the other fields.
        wrapper.boardHeight = MazeGame.getMazeHeight();
        wrapper.hasWalls = new boolean[wrapper.boardHeight][wrapper.boardWidth];
        wrapper.isVisible = new boolean[wrapper.boardHeight][wrapper.boardWidth];


        wrapper.catLocations = new ArrayList<>();
        for(int i = 0; i< MazeGame.getMazeHeight(); i++){
            for(int j=0; j<MazeGame.getMazeWidth(); j++){
                int x = j;
                int y = i;
                if(game.isCatAtLocation(new CellLocation(x, y))){
                    wrapper.catLocations.add(new ApiLocationWrapper(x, y));
                }
                else if(game.isCheeseAtLocation(new CellLocation(x,y))){
                    wrapper.cheeseLocation = new ApiLocationWrapper(x, y);
                }
                else if(game.isMouseAtLocation(new CellLocation(x, y))){
                    wrapper.mouseLocation = new ApiLocationWrapper(x, y);
                }
                else if (game.getCellState(new CellLocation(x, y)).isWall()) {
                    wrapper.hasWalls[y][x] = true;
                }
                else{
                    wrapper.hasWalls[y][x] = false;
                }
            }
        }

        for(int i=0; i<MazeGame.getMazeHeight(); i++){
            for(int j=0; j< MazeGame.getMazeWidth(); j++){
                if(!game.getCellState(new CellLocation(j, i)).isVisible() ){
                    wrapper.isVisible[i][j] = false;
                }
                else
                    wrapper.isVisible[i][j] = true;
            }
        }

        return wrapper;
    }
}
