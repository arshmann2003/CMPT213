package ca.cmpt213.as4.model;

import ca.cmpt213.as4.UI.Canvas;
import ca.cmpt213.as4.UI.DrawableShape;

import java.awt.*;
import java.util.Objects;

public class Shape implements DrawableShape {
    static public int x;
    public int top;
    public int left;
    public int height;
    public int width;
    public String background;
    public String backgroundColor;
    public String line;
    public String fill;
    public String fillText;
    public String lineChar;

    public Shape(int top, int left, int height, int width, String background, String backgroundColor, String line, String fill, String fillText, String lineChar){
        this.top = top;
        this.left = left;
        this.height = height;
        this.width = width;
        this.background = background;
        this.backgroundColor = backgroundColor;
        this.line = line;
        this.fill = fill;
        this.fillText = fillText;
        this.lineChar = lineChar;
        x = 0;
    }

    public void draw(Canvas canvas) {
        Color color = getColor(backgroundColor);

        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                canvas.setCellColor(left+j, top+i, color);
                canvas.setCellText(left+j, top+i, ' ');
            }
        }
        setBorder(canvas);


    }
    private void setBorder(Canvas canvas){
        char borderSymbol = ' ';
        if(line.equals("char"))
            borderSymbol = lineChar.charAt(0);
        else if(line.equals("sequence")){

        }
        else{

        }
        if(borderSymbol!=' '){
            for(int i = 0; i<height; i++) {// Left column
                canvas.setCellText(left, top + i, borderSymbol);
                canvas.setCellText(left + width - 1, top + i, borderSymbol);
            }

            for(int j = 0; j<width; j++) {

                canvas.setCellText(left+j, height+top-1, borderSymbol);
                canvas.setCellText(left+j, top, borderSymbol);

            }
        }
    }
    private Color getColor(String color){
        if (Objects.equals(color, "light gray")) return Color.lightGray;
        if (Objects.equals(color, "gray")) return Color.gray;
        if (Objects.equals(color, "dark gray")) return Color.darkGray;
        if (Objects.equals(color, "black")) return Color.black;
        if (Objects.equals(color, "red")) return Color.red;
        if (Objects.equals(color, "pink")) return Color.pink;
        if (Objects.equals(color, "orange")) return Color.orange;
        if (Objects.equals(color, "yellow")) return Color.yellow;
        if (Objects.equals(color, "green")) return Color.green;
        if (Objects.equals(color, "magenta")) return Color.magenta;
        if (Objects.equals(color, "cyan")) return Color.cyan;
        if (Objects.equals(color, "blue")) return Color.blue;
        return  Color.white;
    }



}
