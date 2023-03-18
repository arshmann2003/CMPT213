package ca.cmpt213.as4.trivial_model;

import ca.cmpt213.as4.ShapeModel;
import ca.cmpt213.as4.UI.Canvas;
import ca.cmpt213.as4.UI.DrawableShape;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Trivial ShapeModel implementation to demonstrate how it connects with provided classes.
 *
 * It must:
 * - Implement ShapeModel so that the UI can be passed a reference to the model at runtime (DI)
 *     - populateFromJSON(): load all the boxes from the JSON file
 *            Create and store DrawableShape objects.
 **
 *     - redact(): change all existing objects to be "redacted" (see assignment doc).
 *
 *     - iterator(): return an iterator to the DrawableShapes (your boxes) which you have created.
 * This class just puts some text in the middle of the screen, but it shows *how* you can do this.
 */
public class TrivialShapeModel implements ShapeModel {

    // -- Fake an object... you should make some classes! --
    // Where to put the text
    // (your objects will know this!)
    private static final int TOP = 10;
    private static final int LEFT = 5;
    // Color / text to show
    // (your objects will know this!)
    private Color color;
    private String text;


    private List<DrawableShape> shapes = new ArrayList<>();

    // Load objects from file
    @Override
    public void populateFromJSON(File jsonFile) {

        text = "Was told to load file: " + jsonFile.getName();
        color = Color.YELLOW;

        // Tell the picture panel about our new object
        shapes.add(new DrawableShape() {
            @Override
            public void draw(Canvas canvas) {
                for (int i = 0; i < text.length(); i++) {
                    canvas.setCellColor(LEFT + i, TOP, color);
                    canvas.setCellText(LEFT + i, TOP, text.charAt(i));
                }
            }
        });
    }

    // Redact all our objects (UI updates after calling this)
    @Override
    public void redact() {
        color = Color.LIGHT_GRAY;
        int length = text.length();
        text = "";
        for (int i = 0; i < length; i++) {
            text += "X";
        }
    }

    @Override
    public Iterator<DrawableShape> iterator() {
        return shapes.iterator();
    }
}
