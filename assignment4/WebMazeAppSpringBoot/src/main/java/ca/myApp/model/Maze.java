package ca.myApp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the maze creation, and tracking the cell state.
 * Maze created ensures a path between all four corners, and 
 * sets up some cycles (loops) inside the maze.
 */
public class Maze {
	// Removal probability rather high because only removing walls that don't create "rooms"
	private static final double WALL_REMOVE_PROBABILITY = 0.5;

	private final int width;
	private final int height;

	private CellState[][] board;
		
	public Maze(int width, int height) {
		this.width = width;
		this.height = height;
		
		do {
			makeRandomMaze();
		} while (!hasAllCornersConnected()
				|| !hasPathToAllOpenCells()
				|| hasOpen2x2Square() || hasWalled2x2Square());
	}
	private Maze(int width, int height, boolean notUsed)  {
		// For static factory method
		this.width = width;
		this.height = height;
		board = new CellState[height][width];
	}

	private void makeRandomMaze() {
		board = new CellState[height][width];
		
		fillMazeWithWalls();		
		buildMazePaths();
		clearMazeCorners();
		addLoopsToMaze();
	}
	private void fillMazeWithWalls() {
		for (int y = 0; y < height; y++){ 
			for (int x = 0; x < width; x++) {
				if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
					// Walls on edge will be visible.
					board[y][x] = new CellState(true, true);
				} else {
					board[y][x] = new CellState(true);	
				}
			}
		}
	}
	// Use Randomized Prim's algorithm, as described here:
	// http://en.wikipedia.org/wiki/Maze_generation_algorithm
	private void buildMazePaths() {
		// Maintain a list of candidate locations to change from a wall to a space.
		List<CellLocation> candidates = new ArrayList<>();

		// Start with the middle element in the maze.
		candidates.add(new CellLocation(width/2,height/2));
		
		// While there are spots to investigate, keep looping.
		while (candidates.size() > 0) {
			// Randomly pick a candidate cell to investigate.
			Collections.shuffle(candidates);
			CellLocation location = candidates.get(0);
			candidates.remove(0);
			
			// Remove the wall, if possible
			if (okToRemoveWall(location)) {
				int x = location.getX();
				int y = location.getY();

				// Remove wall
				board[y][x] = new CellState(false);
				
				// Add surrounding squares to list to explore.
				candidates.add(new CellLocation(x + 1, y));
				candidates.add(new CellLocation(x - 1, y));
				candidates.add(new CellLocation(x, y + 1));
				candidates.add(new CellLocation(x, y - 1));
			}
		}
	}
	private void clearMazeCorners() {
		// Ensure starting cells for player and cats are free of walls:
		board[1][1] = new CellState(false);
		board[1][width-2] = new CellState(false);
		board[height-2][1] = new CellState(false);
		board[height-2][width-2] = new CellState(false);
	}
	private void addLoopsToMaze() {
		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				if (board[y][x].isWall()) {
					boolean shouldRemove = Math.random() <= WALL_REMOVE_PROBABILITY;
					boolean doesRemovalMakeEmptySquare = removalMakesEmptySquare(x, y);
					if (shouldRemove && !doesRemovalMakeEmptySquare) {
						board[y][x] = new CellState(false);
					}
				}
			}
		}
	}

	private boolean removalMakesEmptySquare(int x, int y) {
		for (int dy = -1; dy <= 1; dy += 2) {
			for (int dx = -1; dx <= 1; dx +=2) {
				boolean isEmptyCorner = !board[y + dy][x + dx].isWall();
				boolean isEmptyHorizontally = !board[y + dy][x].isWall();
				boolean isEmptyVertically = !board[y][x + dx].isWall();
				if (isEmptyCorner && isEmptyHorizontally && isEmptyVertically) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean okToRemoveWall(CellLocation location) {
		int x = location.getX();
		int y = location.getY();
		boolean isWall = board[y][x].isWall();
		boolean isTop = (y == 0);
		boolean isBottom = (y == height - 1);
		boolean isLeft = (x == 0);
		boolean isRight = (x == width - 1);
		boolean isEdge = isTop || isBottom || isLeft || isRight;
		
		if (!isWall || isEdge) {
			return false;
		}
		
		final int MIN_WALL_COUNT_WHEN_NOT_CONNECTED = 2;
		boolean breaksMaze = (countWallsAroundCell(location) <= MIN_WALL_COUNT_WHEN_NOT_CONNECTED);
		return !breaksMaze;
	}

	private int countWallsAroundCell(CellLocation location) {
		int x = location.getX();
		int y = location.getY();
		int wallCount = 0;
		wallCount += board[y+1][x].isWall() ? 1 : 0;
		wallCount += board[y][x+1].isWall() ? 1 : 0;
		wallCount += board[y-1][x].isWall() ? 1 : 0;
		wallCount += board[y][x-1].isWall() ? 1 : 0;
		return wallCount;
	}
	
	private boolean hasAllCornersConnected() {
		final CellLocation LOCATION_TOP_LEFT     = new CellLocation(1, 1);
		final CellLocation LOCATION_TOP_RIGHT    = new CellLocation(width - 2, 1);
		final CellLocation LOCATION_BOTTOM_LEFT  = new CellLocation(1, height - 2);
		final CellLocation LOCATION_BOTTOM_RIGHT = new CellLocation(width - 2, height - 2);

		PathFinder pathfinder = new PathFinder(board);
		
		return pathfinder.hasPath(LOCATION_TOP_LEFT, LOCATION_TOP_RIGHT)
				&& pathfinder.hasPath(LOCATION_TOP_LEFT, LOCATION_BOTTOM_LEFT)
				&& pathfinder.hasPath(LOCATION_TOP_LEFT, LOCATION_BOTTOM_RIGHT);
	}

	public boolean hasOpen2x2Square(){
		return hasInternal2x2SquareMatchingWallState(new CellState(false));
	}
	public boolean hasWalled2x2Square(){
		return hasInternal2x2SquareMatchingWallState(new CellState(true));
	}
	private boolean hasInternal2x2SquareMatchingWallState(CellState state) {
		for (int y = 1; y < height - 2; y++) {
			for (int x = 1; x < width - 2; x++) {
				// Is this the top left of a square of walls?
				if (board[y][x].isWall() == state.isWall()
					&& board[y + 1][x].isWall() == state.isWall()
					&& board[y][x + 1].isWall() == state.isWall()
					&& board[y + 1][x + 1].isWall() == state.isWall()
				) {
					return true;
				}
			}
		}
		return false;
	}


	public CellLocation getRandomLocationInsideMaze() {
		int x = (int) (Math.random() * (width-2)) + 1;
		int y = (int) (Math.random() * (height-2)) + 1;
		return new CellLocation(x, y);
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	private boolean outOfRange(CellLocation cell) {
		int x = cell.getX();
		int y = cell.getY();
		boolean badX = (x < 0) || (x >= width);
		boolean badY = (y < 0) || (y >= height);
		return badX || badY;
	}

	
	
	public CellState getCellState(CellLocation cell) {
		int x = cell.getX();
		int y = cell.getY();
		return board[y][x];
	}
	
	public boolean isCellVisible(CellLocation cell) {
		if (outOfRange(cell)) {
			return false;
		}
		int x = cell.getX();
		int y = cell.getY();
		return board[y][x].isVisible();
	}

	public boolean isCellAWall(CellLocation cell) {
		if (outOfRange(cell)) {
			return false;
		}
		int x = cell.getX();
		int y = cell.getY();
		return board[y][x].isWall();
	}

	public boolean isCellOpen(CellLocation cell) {
		if (outOfRange(cell)) {
			return false;
		}
		int x = cell.getX();
		int y = cell.getY();
		return !board[y][x].isWall();
	}
	
	public void recordCellVisible(CellLocation pos) {
		CellState current = board[pos.getY()][pos.getX()];
		board[pos.getY()][pos.getX()] = current.makeVisible();
		
	}

	/*
		MARKING SUPPORT
	 */
	public static Maze makeFromStrings(List<String> inputStrings) {
		// Clean strings of spaces at start / end
		List<String> cleanStrings = new ArrayList<>();
		for (String s : inputStrings) {
			cleanStrings.add(s.trim());
		}

		// Create object
		int height = cleanStrings.size();
		if (height == 0) {
			throw new IllegalArgumentException("ERROR: No input strings given for the maze");
		}
		int width = cleanStrings.get(0).length();
		Maze maze = new Maze(width, height, false);

		// Populate with walls
		for (int y = 0; y < cleanStrings.size(); y++) {
			String row = cleanStrings.get(y);
			if (row.length() != width) {
				throw new IllegalArgumentException("ERROR: Input strings not all same length");
			}
			for (int x = 0; x < row.length(); x++) {
				boolean isWall = row.charAt(x) == '#';
				maze.board[y][x] = new CellState(isWall);
			}
		}

		return maze;
	}
	public boolean hasWallInCorners(){
		final CellLocation LOCATION_TOP_LEFT     = new CellLocation(1, 1);
		final CellLocation LOCATION_TOP_RIGHT    = new CellLocation(width - 2, 1);
		final CellLocation LOCATION_BOTTOM_LEFT  = new CellLocation(1, height - 2);
		final CellLocation LOCATION_BOTTOM_RIGHT = new CellLocation(width - 2, height - 2);
		return isCellAWall(LOCATION_TOP_LEFT)
				|| isCellAWall(LOCATION_TOP_RIGHT)
				|| isCellAWall(LOCATION_BOTTOM_LEFT)
				|| isCellAWall(LOCATION_BOTTOM_RIGHT);
	}

	public boolean hasPathToAllOpenCells(){
		// Path from top left (user starting point):
		final CellLocation LOCATION_TOP_LEFT = new CellLocation(1, 1);
		PathFinder pathfinder = new PathFinder(board);

		// to all possible open spaces
		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				CellLocation loc = new CellLocation(x, y);
				boolean openCell = !isCellAWall(loc);
				boolean hasNoPath = !pathfinder.hasPath(LOCATION_TOP_LEFT, loc);
				if (openCell && hasNoPath) {
					return false;
				}
			}
		}
		return true;
	}

	public void printPathFinder() {
		PathFinder pathfinder = new PathFinder(board);
		pathfinder.dumpFloodArray("Same number == same region");
	}
}
