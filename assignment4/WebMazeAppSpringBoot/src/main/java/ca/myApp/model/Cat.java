package ca.myApp.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The position and motion control of a cat.
 */
public class Cat {
	private final MazeGame game;
	private CellLocation location;
	private MoveDirection lastMove = MoveDirection.MOVE_NONE;
	
	public Cat(MazeGame game, CellLocation location) {
		this.game = game;
		this.location = location;
	}
	
	public CellLocation getLocation() {
		return location;
	}

	public void doMove() {
		List<MoveDirection> possibleMoves = getPossibleMoves();
		pickOKMove(possibleMoves);		
		// Once moved, the game will ask where the cat is, as needed.
	}

	private List<MoveDirection> getPossibleMoves() {
		List<MoveDirection> directions = new ArrayList<>();
		directions.add(MoveDirection.MOVE_UP);
		directions.add(MoveDirection.MOVE_DOWN);
		directions.add(MoveDirection.MOVE_RIGHT);
		directions.add(MoveDirection.MOVE_LEFT);
		
		// Have the cat try not to back-track unless needed by making the 
		// backtracking move (opposite last move) be the last one to try.
		MoveDirection oppositeLastMove = lastMove.getOppositeMove();
		directions.remove(oppositeLastMove);
		Collections.shuffle(directions);
		directions.add(oppositeLastMove);
		return directions;
	}

	private void pickOKMove(List<MoveDirection> directions) {
		for (MoveDirection move : directions) {
			CellLocation targetLocation = location.getMovedLocation(move);
			if (game.isCellOpen(targetLocation)) {
				location = targetLocation;
				lastMove = move;
				return;
			}
		}
	}	
}
