package ca.myApp.model;

import java.util.ArrayList;
import java.util.List;

public class MazeGame {
	private static final int MAZE_SIZE_WIDTH  = 20;
	private static final int MAZE_SIZE_HEIGHT = 15;
	private static final int DEFAULT_NUM_CHEESE_TO_COLLECT = 5;

	private static final CellLocation LOCATION_TOP_LEFT     = new CellLocation(1, 1);
	private static final CellLocation LOCATION_TOP_RIGHT    = new CellLocation(MAZE_SIZE_WIDTH - 2, 1);
	private static final CellLocation LOCATION_BOTTOM_LEFT  = new CellLocation(1, MAZE_SIZE_HEIGHT - 2);
	private static final CellLocation LOCATION_BOTTOM_RIGHT = new CellLocation(MAZE_SIZE_WIDTH - 2, MAZE_SIZE_HEIGHT - 2);

	private final Maze maze = new Maze(MAZE_SIZE_WIDTH, MAZE_SIZE_HEIGHT);
	
	private CellLocation playerLocation = LOCATION_TOP_LEFT;
	private CellLocation cheeseLocation;
	private final List<Cat> cats = new ArrayList<>();
	private int numCheeseCollected;
	private int numCheeseToCollect = DEFAULT_NUM_CHEESE_TO_COLLECT;

	public MazeGame() {
		placeNewCheeseOnBoard();
		placeCatsOnBoard();
		setVisibleAroundPlayerCell();
	}

	private void placeCatsOnBoard() {
		cats.add(new Cat(this, LOCATION_TOP_RIGHT));
		cats.add(new Cat(this, LOCATION_BOTTOM_RIGHT));
		cats.add(new Cat(this, LOCATION_BOTTOM_LEFT));
	}

	public boolean hasUserWon() {
		boolean collectedEnoughCheese = numCheeseCollected >= numCheeseToCollect;
		return !hasUserLost() && collectedEnoughCheese;
	}
	public boolean hasUserLost() {
		return isCatAtLocation(playerLocation);
	}
	
	public int getNumberCheeseToCollect() {
		return numCheeseToCollect;
	}
	public int getNumberCheeseCollected() {
		return numCheeseCollected;
	}

	public boolean isValidPlayerMove(MoveDirection move) {
		CellLocation targetLocation = playerLocation.getMovedLocation(move);
		return maze.isCellOpen(targetLocation);
	}

	public boolean isCellOpen(CellLocation cell) {
		return maze.isCellOpen(cell);
	}

	public void recordPlayerMove(MoveDirection move) {
		assert isValidPlayerMove(move);
		playerLocation = playerLocation.getMovedLocation(move);
		
		setVisibleAroundPlayerCell();
		
		// Compute goal states achieved
		if (isCheeseAtLocation(playerLocation)) {
			numCheeseCollected++;
			placeNewCheeseOnBoard();
		}
		
		// Cat move called from UI component.
	}

	private void placeNewCheeseOnBoard() {
		// Find a random open location which is connected to where the player is, 
		// but which is not where the player currently is.
		do {
			cheeseLocation = maze.getRandomLocationInsideMaze();
		} while (isMouseAtLocation(cheeseLocation)
				|| maze.isCellAWall(cheeseLocation));
				
	}

	private void setVisibleAroundPlayerCell() {
		CellLocation up = playerLocation.getMovedLocation(MoveDirection.MOVE_UP);
		CellLocation down = playerLocation.getMovedLocation(MoveDirection.MOVE_DOWN);
		CellLocation right = playerLocation.getMovedLocation(MoveDirection.MOVE_RIGHT);
		CellLocation left = playerLocation.getMovedLocation(MoveDirection.MOVE_LEFT);

		// Current cell, Up, Down, Right, Left.
		maze.recordCellVisible(playerLocation);
		maze.recordCellVisible(up);
		maze.recordCellVisible(down);
		maze.recordCellVisible(right);
		maze.recordCellVisible(left);

		// 45' Angles
		maze.recordCellVisible(up.getMovedLocation(MoveDirection.MOVE_RIGHT));
		maze.recordCellVisible(up.getMovedLocation(MoveDirection.MOVE_LEFT));
		maze.recordCellVisible(down.getMovedLocation(MoveDirection.MOVE_RIGHT));
		maze.recordCellVisible(down.getMovedLocation(MoveDirection.MOVE_LEFT));
	}
	
	public CellState getCellState(CellLocation cell) {
		return maze.getCellState(cell);
	}
	public boolean isMouseAtLocation(CellLocation cell) {
		return playerLocation.equals(cell);
	}
	public boolean isCatAtLocation(CellLocation cell) {
		for (Cat cat : cats) {
			CellLocation catLocation = cat.getLocation();
			if (catLocation.equals(cell)) {
				return true;
			}
		}
		return false;
	}
	public boolean isCheeseAtLocation(CellLocation cell) {
		return cheeseLocation != null && cheeseLocation.equals(cell);
	}

	public static int getMazeWidth() {
		return MAZE_SIZE_WIDTH;
	}
	public static int getMazeHeight() {
		return MAZE_SIZE_HEIGHT;
	}
	
	public void doCatMoves() {
		for (Cat cat : cats) {
			cat.doMove();
		}
	}

	public void makeVisible(){
		for(int i=0; i<MazeGame.getMazeHeight(); i++){
			for(int j=0; j<MazeGame.getMazeWidth(); j++){
				maze.recordCellVisible(new CellLocation(j, i));
			}
		}
	}

	public void setNumberCheeseToCollect(int numCheeseToCollect) {
		this.numCheeseToCollect = numCheeseToCollect;
	}
}
