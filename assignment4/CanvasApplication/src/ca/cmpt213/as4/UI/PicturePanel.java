package ca.cmpt213.as4.UI;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.*;

/**
 * Calls on the DrawableShape objects to draw themselves to the canvas when needed.
 *
 * ** YOU DO NOT NEED TO CHANGE ANYTHING IN THIS FILE **
 *    (but you may make changes if you desire)
 */
@SuppressWarnings("serial")
public class PicturePanel extends JPanel{
	
	private static final int BORDER_WIDTH = 2;
	
	private final Canvas canvas;

	public PicturePanel(int sizeX, int sizeY) {
		canvas = new Canvas(sizeX, sizeY);

		// Set up the JPanel base class
		setBorder(BorderFactory.createLineBorder(Color.BLUE, BORDER_WIDTH));
		setLayout(new BorderLayout());

		CanvasIcon icon = new CanvasIcon(canvas);
		add(new JLabel(icon), BorderLayout.CENTER);
	}


	public void redraw(Iterator<? extends DrawableShape> itr) {
		canvas.clear();
		while (itr.hasNext()) {
			itr.next().draw(canvas);
		}
		updateUI();
	}

	public void writePictureToFile(String path) {
		File file = new File(path);
		try {
			FileWriter writer = new FileWriter(file);
			for (int y = 0; y < canvas.getSizeY(); y++) {
				for (int x= 0; x < canvas.getSizeX(); x++) {
					Color color = canvas.getCellColor(x, y);
					String colorChar = colourToString(color);
					char text = canvas.getCellText(x, y);
					writer.write(colorChar + text + ", ");
				}
				writer.write("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String colourToString(Color color) {
		if (color == Color.WHITE) return " ";		// Background is white, so display nothing.
		if (color == Color.LIGHT_GRAY) return "L";
		if (color == Color.GRAY) return "G";
		if (color == Color.DARK_GRAY) return "D";
		if (color == Color.BLACK) return "B";
		if (color == Color.RED) return "R";
		if (color == Color.PINK) return "P";
		if (color == Color.ORANGE) return "O";
		if (color == Color.YELLOW) return "Y";
		if (color == Color.GREEN) return "G";
		if (color == Color.MAGENTA) return "M";
		if (color == Color.CYAN) return "C";
		if (color == Color.BLUE) return "U";
		return "?";
	}


	/**
	 * An Icon which renders a Canvas into a graphical form.
	 * Pass the constructor the Canvas you want to render.
	 * Place the CanvasIcon in a JLabel for displaying on the screen.
	 */
	private static class CanvasIcon implements Icon {
		// Size of individual character blocks on the screen.
		private static final int BLOCK_SIZE_X = 15;
		private static final int BLOCK_SIZE_Y = 18;

		// Where in the block to start writing text
		private static final int TEXT_OFFSET_X = 2;
		private static final int TEXT_OFFSET_Y = (int) (BLOCK_SIZE_Y * .5) + 3;

		private final Canvas canvas;

		/**
		 * Create the CanvasIcon from an existing Canvas with characters and
		 * coloured blocks in it.
		 */
		public CanvasIcon(Canvas canvas) {
			this.canvas = canvas;
		}

		@Override
		public int getIconHeight() {
			return BLOCK_SIZE_Y * canvas.getSizeY();
		}

		@Override
		public int getIconWidth() {
			return BLOCK_SIZE_X * canvas.getSizeX();
		}

		@Override
		public void paintIcon(Component c, Graphics g, int xPos, int yPos) {
			// Take the basic "Graphics" context and convert to
			// more powerful Graphics2D context.
			Graphics2D g2d = (Graphics2D) g;
			g2d.setFont(new Font(g2d.getFont().getName(), Font.PLAIN, 14));

			// For each square in the canvas, render it to the graphics context.
			for (int y = 0; y < canvas.getSizeY(); y++) {
				for (int x = 0; x < canvas.getSizeX(); x++) {
					int left = x * BLOCK_SIZE_X;
					int top = y * BLOCK_SIZE_Y;

					// Draw the box (leaving a 1 pixel boarder
					Color boxColor = canvas.getCellColor(x, y);
					g2d.setColor(boxColor);
					g2d.setBackground(boxColor);
					g2d.fillRect(left, top, BLOCK_SIZE_X - 1, BLOCK_SIZE_Y - 1);

					// Draw the text
					g2d.setColor(Color.BLACK);
					g2d.drawString("" + canvas.getCellText(x, y), left
							+ TEXT_OFFSET_X, top + TEXT_OFFSET_Y);
				}
			}
		}
	}
}
