package com.edubiz.preloader;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Index extends Application {
    @Override
    public void start(Stage stage) {
        // create  the root
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800,600);
        root.setStyle("-fx-background-color: #3b3b3b;");

        Button button = getButton(stage);

        root.getChildren().add(button);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Preloader Demo");


        // Show the stage
        stage.show();
    }

    private static Button getButton(Stage stage) {
        Button button = new Button("See MyNotifier");
        button.setCursor(Cursor.HAND);

        Preloader preloader = new Preloader(stage);
        stage.setOnCloseRequest(ev -> {
            preloader.destroy();
        });
        preloader.setCustomTextNode(()->{
            return new Label("I am something different");
        }).setCustomComponent(()->{
            return new HBox(new Label("Hello"),new Button("new button"));
        }).setCustomLoaderNode(()->{
            return new Circle(50, Paint.valueOf("red"));
        });
        button.setOnAction(e -> {
            preloader.show();

            new Thread(()->{
                try {
                    Thread.sleep(3000);
                    preloader.hide();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }).start();
        });

        return button;
    }

    public static void main(String[] args) {
        launch();
    }
}