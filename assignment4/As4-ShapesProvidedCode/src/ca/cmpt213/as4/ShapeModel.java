package ca.cmpt213.as4;

import ca.cmpt213.as4.UI.DrawableShape;
import ca.cmpt213.as4.model.Shape;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Interface required by the UI to support the program's operations.
 *
 * You should create a class which implements this.
 * See the TrivialShapeModel class for an example.
 */
public interface ShapeModel {
    // Populate the model with the objects described of the jsonFile
    // (replacing any current objects)
    void populateFromJSON(File jsonFile);

    List<Shape> shapes = null;

    // Redact the display by changing all the current objects to be redacted; see assignment for details.
    void redact();

    // Get iterator to shapes
    // (We would like an iterator to objects which implement the DrawableShape interface)
    Iterator<? extends DrawableShape> iterator();
}
