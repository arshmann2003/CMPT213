package ca.cmpt213.as4.model;

import ca.cmpt213.as4.ShapeModel;
import ca.cmpt213.as4.UI.DrawableShape;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;

public class DrawableShapeModel implements ShapeModel {

    public List<Shape> shapes = new ArrayList<>();

    // Load objects from file
    @Override
    public void populateFromJSON(File jsonFile) {
        if(jsonFile.exists()){
            addJsonShapes(jsonFile);
        }
    }

    // Redact all our objects (UI updates after calling this)
    @Override
    public void redact() {
<<<<<<< HEAD:assignment4/CanvasApplication/src/ca/cmpt213/as4/model/DrawableShapeModel.java
        for (Shape shape : shapes) {
            shape.redact();
        }
=======
//        color = Color.LIGHT_GRAY;
        for(int i=0; i<shapes.size(); i++){
            shapes.get(i).redact();
        }
//        int length = text.length();
//        text = "";
//        for (int i = 0; i < length; i++) {
//            text += "X";
//        }
>>>>>>> d3db47bb3c7bb7780d51b26a59c408a29d2b466f:assignment4/As4-ShapesProvidedCode/src/ca/cmpt213/as4/model/DrawableShapeModel.java

    }
    @Override
    public Iterator<Shape> iterator() {
        return shapes.iterator();
    }

    private void addJsonShapes(File jsonFile) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(jsonFile.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObj = (JsonObject) jsonParser.parse(fileReader);
        JsonArray arr = jsonObj.get("shapes").getAsJsonArray();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type type = new TypeToken<ArrayList<Shape>>() {}.getType();

        shapes = gson.<ArrayList<Shape>>fromJson(arr, type);
    }

}
