package distribute.restapi;

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
//    public static ApiBoardWrapper makeFromGame(MazeGame game) {
//        ApiBoardWrapper wrapper = new ApiBoardWrapper();
//        wrapper.boardWidth = 1234; // Fill this in, along with all the other fields.
//        return wrapper;
//    }
}
