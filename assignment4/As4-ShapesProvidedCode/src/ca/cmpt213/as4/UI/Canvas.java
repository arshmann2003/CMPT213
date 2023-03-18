package ca.cmpt213.as4.UI;

import java.awt.Color;

/**
 * Represent a 2D surface to render text and colored squares on. 
 * Size of the canvas is set by the constructor, and then calls can be made 
 * to setCellText() and setCellColor() to add items to the canvas.
 *
 *
 * ** YOU DO NOT NEED TO CHANGE ANYTHING IN THIS FILE **
 *    (but you may make changes if you desire)
 */
public class Canvas {
	// Size of screen to draw.
	private final int sizeX;
	private final int sizeY;
	
	// Default colour and fill character for the canvas.
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static final char FILL_CHARACTER = ' ';

	// The data of the canvas in a 2D array.
	private final char[][] data;
	private final Color[][] color;

	// Create the canvas, initializing the background character to the parameter.
	public Canvas(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;

		data = new char[sizeY][sizeX];
		color = new Color[sizeY][sizeX];
		
		initializeArrays();
	}
	private void initializeArrays() {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				data[y][x] = FILL_CHARACTER;
				color[y][x] = BACKGROUND_COLOR;
			}
		}
	}

	public int getSizeX() {
		return sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}

	// Draw character ch on the canvas at (x, y).
	// x and y are 0 offsets: (0,0) is top left.
	public void setCellText(int x, int y, char ch) {
		// Set value for cells in viewable area
		if (isCellInCanvas(x, y)) {
			data[y][x] = ch;
		}
	}
	public char getCellText(int x, int y) {
		return data[y][x];
	}

	public void setCellColor(int x, int y, Color colour) {
		// Set value for cells in viewable area
		if (isCellInCanvas(x, y)) {
			color[y][x] = colour;
		}
	}

	public Color getCellColor(int x, int y) {
		// Set value for cells in viewable area
		return color[y][x];
	}
	private boolean isCellInCanvas(int x, int y) {
		boolean isOkX = (x >= 0) && (x < sizeX);
		boolean isOkY = (y >= 0) && (y < sizeY);
		return isOkX && isOkY;
	}

	public void clear() {
		initializeArrays();
	}

	public boolean isCellEmpty(int x, int y) {
		if(isCellInCanvas(x, y)){
			return data[y][x]==FILL_CHARACTER && color[y][x]==BACKGROUND_COLOR;
		}
		return false;
	}
}
