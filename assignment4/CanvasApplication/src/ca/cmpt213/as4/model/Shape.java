package ca.cmpt213.as4.model;

import ca.cmpt213.as4.UI.Canvas;
import ca.cmpt213.as4.UI.DrawableShape;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;


public class Shape implements DrawableShape {
    private static final int SEQUENCE_START = 1;
    private static final int SEQUENCE_END = 5;
    private static final char REDACTED_BORDER_SYMBOL = '+';
    private static final char ASCII_HORIZONTAL = '═';
    private static final char ASCII_VERTICAL = '║';
    private static final char ASCII_TOP_LEFT = '╔';
    private static final char ASCII_TOP_RIGHT = '╗';
    private static final char ASCII_BOT_LEFT = '╚';
    private static final char ASCII_BOT_RIGHT = '╝';
    private static final char ASCII_DEFAULT = '■';

    private Canvas canvas;
    private final int top;
    private final int left;
    private final int height;
    private final int width;
    private final String background;
    private final String backgroundColor;
    private final String line;
    private final String fill;
    private final String fillText;
    private final String lineChar;
    private boolean redacted;

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
        this.redacted = false;
    }

    public void draw(Canvas canvas) {
        this.canvas = canvas;

        if(redacted){
            setRedactedShape();
            setRedactedBorder();
        }
        else{
            setBackgroundColor();
            setText();
            setBorder();
        }
    }

    public void redact() {
        this.redacted = true;
    }

    private void setBackgroundColor() {

        if(background.equals("solid")){
            for(int i = 0; i<height; i++){
                for(int j = 0; j<width; j++){
                    canvas.setCellColor(left+j, top+i, getColor(backgroundColor));
                    canvas.setCellText(left+j, top+i, ' ');
                }
            }
        }
        else if(background.equals("checker")){
            Color color;
            int count = 0;
            for(int i = 0; i<height; i++){
                for(int j = 0; j<width; j++) {
                    if ((j + count) % 2 == 0) {
                        color = getColor(backgroundColor);
                    } else {
                        color = getColor("white");
                    }
                    canvas.setCellColor(left + j, top + i, color);
                    canvas.setCellText(left + j, top + i, ' ');
                }
                count++;
            }
        }
        else{
            for(int i=0; i<height; i++){
                for(int j=i; j<width; j++){
                    canvas.setCellColor(left+j, top+i, getColor(backgroundColor));
                    canvas.setCellText(left+j, top+i, ' ');
                }
            }
        }
    }

    private void setRedactedShape() {
        for(int i = 0; i<height; i++){
            for(int j = 0; j<width; j++){
                canvas.setCellColor(left+j, top+i, Color.lightGray);
                canvas.setCellText(left+j, top+i, 'X');
            }
        }
    }

    private void setText() {
        if(fill.equals("solid")){
            char firstChar = fillText.charAt(0);
            fillShapes(firstChar);
        }
        else{
            fillShapesWithText();
        }


    }

    private void fillShapesWithText() {
        ArrayList<String> words = new ArrayList<>();
        StringBuilder subString = new StringBuilder();

        for(int i=0; i<fillText.length(); i++) {
            if(fillText.charAt(i)==' ' || i==fillText.length()-1){
                subString.append(fillText.charAt(i));
                words.add(String.valueOf(subString));
                subString = new StringBuilder();
            } else {
                subString.append(fillText.charAt(i));
            }
        }
        ArrayList<String> x = new ArrayList<>();
        int i=0;
        String sentence = "";
        while(i < words.size()){
            if(sentence.length() + words.get(i).trim().length() <= width-2){
                sentence += words.get(i);
            }else{
                if(!sentence.isEmpty()){
                    x.add(sentence);
                }
                sentence = words.get(i);
            }
            i++;
        }
        if(!x.contains(sentence))
            x.add(sentence);
        for(int j=0; j<x.size(); j++){
            addSentenceToScreen(x.get(j), j+1);
        }
    }

    private void addSentenceToScreen(String sentence, int row) {
        int subRow = row;
        int i = 0;
        int startIndex = getStartingIndex(sentence);
        while(i <= width-2 && i<sentence.length() && row<=height-2) {
            canvas.setCellText(left+startIndex+i+1, top+row, sentence.charAt(i));
            i++;
        }
        subRow++;
        int executed = 0;
        // wrap text if needed
        while(subRow<=height-2 && i<sentence.length()){
            i--;
            int j = getStartingIndex(sentence.substring(i));
            while(i<sentence.length() && j<=width-2){
                canvas.setCellText(left+j+1, top+subRow, sentence.charAt(i));
                i++;
                j++;
            }
            if(j>width-2)
                subRow++;
            executed++;
        }

    }

    private int getStartingIndex(String sentence) {
        if(sentence.trim().length() == width-2)
            return 0;
        int x = (width-2-sentence.trim().length()) / 2;

        if( (width-2)%2==0 && sentence.trim().length()%2!=0)
            x++;
        else if(width-2==sentence.trim().length()+1){
            x++;
        }

        if(x < 0)
            return 0;
        else
            return x;
    }

    private void fillShapes(char firstChar) {
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                canvas.setCellText(left+j, top+i, firstChar);
            }
        }
    }

    private void setBorder(){
        if(line.equals("char")){
            setCharBorder(lineChar.charAt(0));
        }
        else if(line.equals("sequence")){
            setSequenceBorder();
        }
        else {
            setAsciiBorder();
        }
    }

    private void setCharBorder(char borderSymbol) {
        for(int i = 0; i<height; i++) {// Left column
            canvas.setCellText(left, top + i, borderSymbol);
            canvas.setCellText(left + width - 1, top + i, borderSymbol);
        }
        for(int j = 0; j<width; j++) {
            canvas.setCellText(left+j, height+top-1, borderSymbol);
            canvas.setCellText(left+j, top, borderSymbol);
        }
    }

    private void setRedactedBorder() {
        setCharBorder(REDACTED_BORDER_SYMBOL);
    }

    private void setSequenceBorder() {
        int seqCount = SEQUENCE_START;
        char x = ' ';
        for(int j = 0; j<width-1; j++){  // start sequence horizontally from top row
            x = (char)(seqCount + '0');
            canvas.setCellText(left+j, top, x);
            seqCount++;
            if(seqCount>SEQUENCE_END) seqCount = SEQUENCE_START;
        }
        for(int i = 0; i < height; i++){   // continue sequence vertically from last column
            x = (char)(seqCount + '0');
            canvas.setCellText(left+width-1, top+i, x);
            seqCount++;
            if(seqCount>SEQUENCE_END) seqCount = SEQUENCE_START;
        }
        if(height > 1){
            for(int j = 0; j < width-1; j++){
                x = (char)(seqCount + '0');
                canvas.setCellText(left+width-2-j, top+height-1, x);
                seqCount++;
                if(seqCount>SEQUENCE_END) seqCount = SEQUENCE_START;
            }
        }
        if(width > 1){
            for(int i=0; i < height-2; i++){
                x = (char)(seqCount + '0');
                canvas.setCellText(left, top+height-2-i, x);
                seqCount++;
                if(seqCount>SEQUENCE_END) seqCount = SEQUENCE_START;
            }
        }

    }

    private void setAsciiBorder() {
        if(width > 1 && height > 1){
            for(int i = 0; i<height; i++) {// Left column
                canvas.setCellText(left, top + i, ASCII_VERTICAL);
                canvas.setCellText(left + width - 1, top + i, ASCII_VERTICAL);
            }
            for(int j = 0; j<width; j++) {
                canvas.setCellText(left+j, height+top-1, ASCII_HORIZONTAL);
                canvas.setCellText(left+j, top, ASCII_HORIZONTAL);
            }
            //set corners
            canvas.setCellText(left, top, ASCII_TOP_LEFT);
            canvas.setCellText(left+width-1, top, ASCII_TOP_RIGHT);
            canvas.setCellText(left, top+height-1, ASCII_BOT_LEFT);
            canvas.setCellText(left+width-1, top+height-1, ASCII_BOT_RIGHT);
            return;
        }

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                canvas.setCellText(left+j, top+i, ASCII_DEFAULT);
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
