package com.edubiz.preloader.util;

import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public Map<String, Object> parsePosition(Pos position, double width, double height, double sceneWidth, double sceneHeight) {
        // Define positioning
        double fromX;
        double fromY;
        double toX;
        double toY;
        double pad = 10.0;

        // Process the notification position
        String nPos = extractPosition(position);

        //Set the positions
        switch (nPos.toLowerCase()) {
            case "center_right" -> {
                double y = (sceneHeight - height) / 2;

                fromX = sceneWidth;
                toX = (sceneWidth - width) - pad;
                fromY = y;
                toY = y;
            }
            case "center_left" -> {
                double y = (sceneHeight - height) / 2;

                fromX = -width;
                toX = 0 + pad;
                fromY = y;
                toY = y;
            }
            case "bottom_center" -> {
                double x = (sceneWidth - width) / 2;

                fromY = sceneHeight;
                toY = (sceneHeight - height) - pad;
                fromX = x;
                toX = x;
            }
            case "center" -> {
                fromX = 0.0;
                fromY = 0.0;
                toX = (sceneWidth - width) / 2;
                toY = (sceneHeight - height) / 2;
            }
            case "top_left" -> {
                fromX = -width;
                fromY = -height;
                toX = 0 + pad;
                toY = 0 + pad;
            }
            case "top_right" -> {
                fromX = sceneWidth;
                fromY = -height;
                toX = (sceneWidth - width) - pad;
                toY = 0 + pad;
            }
            case "bottom_left" -> {
                fromX = -width;
                fromY = sceneHeight;
                toX = 0 + pad;
                toY = (sceneHeight - height) - pad;
            }
            case "bottom_right" -> {
                fromX = sceneWidth;
                fromY = sceneHeight;
                toX = (sceneWidth - width) - pad;
                toY = (sceneHeight - height) - pad;
            }
            default -> {
                double x = (sceneWidth - width) / 2;

                fromY = -height;
                fromX = x;
                toY = 0 + pad;
                toX = x;
            }
        }

        Coordinates coordinates = new Coordinates(fromX,fromY,toX,toY,pad,nPos);

        return coordinates.getCoordinates();
    }

    public String extractPosition(Pos position) {
        // Process the notification position first
        String nPos = "";
        switch (position) {
            case TOP_CENTER,TOP_LEFT,TOP_RIGHT,CENTER_LEFT,CENTER_RIGHT,BOTTOM_CENTER,BOTTOM_LEFT,BOTTOM_RIGHT,CENTER ->  nPos = position.name();
        }

        return nPos;
    }


    public Map<String, Double> getGeometry(Node node, Scene scene) {
        // create Map Object
        Map<String, Double> geoObj = new HashMap<>();

        // Define bounds
        double width, height, sceneWidth, sceneHeight;

        if (node instanceof Region region) {
            region.applyCss();
            region.layout();

            width = region.prefWidth(-1);
            height = region.prefHeight(-1);
        } else {
            Bounds bounds = node.getBoundsInLocal();

            width = bounds.getWidth();
            height = bounds.getHeight();
        }

        // Get the scene bounds
        sceneWidth = scene.getWidth();
        sceneHeight = scene.getHeight();

        // Set Values
        geoObj.put("width",width);
        geoObj.put("height",height);
        geoObj.put("sceneWidth",sceneWidth);
        geoObj.put("sceneHeight",sceneHeight);

        return geoObj;
    }
}
