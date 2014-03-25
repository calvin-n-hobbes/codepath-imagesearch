package com.codepath.bootcamp.gridimagesearch;

import java.io.Serializable;

/**
 * A class to store advanced image search options.
 * Color, size, and type are fixed values stored as Java enumerations.
 */
public class ImageFilter implements Serializable {
    private static final long serialVersionUID = -2727646409076184446L;

    public static enum Color {
        BLACK("black"),
        BLUE("blue"),
        BROWN("brown"),
        GRAY("gray"),
        GREEN("green"),
        ORANGE("orange"),
        PINK("pink"),
        PURPLE("purple"),
        RED("red"),
        TEAL("teal"),
        WHITE("white"),
        YELLOW("yellow");

        private String requestText;

        private Color(String value) { requestText = value; }

        @Override
        public String toString() { return requestText; }
    }

    public static enum Size {
        SMALL("small"),
        MEDIUM("medium"),
        LARGE("large"),
        XLARGE("xlarge");
        
        private String requestText;

        private Size(String value) { requestText = value; }

        @Override
        public String toString() { return requestText; }
    }

    public static enum Type {
        FACE("face"),
        PHOTO("photo"),
        CLIPART("clipart"),
        LINEART("lineart");

        private String requestText;

        private Type(String value) { requestText = value; }

        @Override
        public String toString() { return requestText; }        
    }

    private Color color;
    private String site;
    private Size size;
    private Type type;

    public ImageFilter() {
        setColor(null);
        setType(null);
        setSize(null);
        setType(null);
    }

    public Color getColor() { return color; }

    public void setColor(Color c) { color = c; }

    public String getSite() { return site; }

    public void setSite(String st) { site = st; }

    public Size getSize() { return size; }

    public void setSize(Size sz) { size = sz; }

    public Type getType() { return type; }

    public void setType(Type t) { type = t; }
}
