package ca.cmpt213.as4.model;

import ca.cmpt213.as4.ShapeModel;
import ca.cmpt213.as4.UI.Canvas;
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
//        color = Color.LIGHT_GRAY;
//        int length = text.length();
//        text = "";
//        for (int i = 0; i < length; i++) {
//            text += "X";
//        }

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
