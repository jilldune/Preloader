package com.edubiz.preloader;

import com.edubiz.preloader.classes.interfaces.LoaderCallback;
import com.edubiz.preloader.classes.interfaces.LoaderNode;
import com.edubiz.preloader.classes.interfaces.TextNode;
import com.edubiz.preloader.util.Util;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Preloader {
    private final Stage stage;
    private AnchorPane parentNode;
    private boolean useCustom = false;
    private boolean customLoaderNode = false;
    private boolean customTextNode = false;
    private String initialText = "loading...";
    private Pos DEFAULT_POSITION = Pos.CENTER;
    private LoaderCallback customComponentFunction;
    private LoaderNode loaderNodeCallback;
    private TextNode textNodeCallback;
    private final List<String> customStyleSheets = new ArrayList<>(); // User-added custom stylesheets
    private final List<String> defaultLibraryStyles = new ArrayList<>(); // Default library styles

    public Preloader(Stage stage) {
        this.stage = stage;
        init();
    }

    private void init() {
        // Parent container
        parentNode = new AnchorPane();

        Platform.runLater(()->{
            initStyles();
            parentNode.setId("preloader_edubiz_com_id");
            parentNode.getStyleClass().add("root");
            parentNode.setManaged(false);

            // Main window
            Scene scene = stage.getScene();
            Parent rootPane = scene.getRoot();

            // adding container to main window
            ((Pane) rootPane).getChildren().add(parentNode);

            // bind to scene
            parentNode.prefWidthProperty().bind(scene.widthProperty());
            parentNode.prefHeightProperty().bind(scene.heightProperty());
        });
    }

    /**
     * Initializes the default library stylesheets.
     */
    private void initStyles() {
        // Add your default library styles here
        System.out.println(getClass().getResource("/styles/preloader.css"));
        defaultLibraryStyles.add(Objects.requireNonNull(getClass().getResource("/styles/preloader.css")).toExternalForm());
        replaceStylesheets(); // Apply the default styles initially
    }

    /// Ensures that library styles remain in place and custom styles are applied last.
    /**
     * Adds a custom stylesheet provided by the user.
     *
     * @param styleSheetURL the URL of the custom stylesheet to add.
     */
    public void addStyleSheet(String styleSheetURL) {
        if (styleSheetURL != null && !styleSheetURL.trim().isEmpty()) {
            this.customStyleSheets.add(styleSheetURL); // Maintain a list of user-added stylesheets
            replaceStylesheets();
        }
    }

    /**
     * Clears and re-applies all stylesheets to the root pane.
     * Ensures library styles are applied first, followed by custom styles.
     */
    private void replaceStylesheets() {
        parentNode.getStylesheets().clear();
        parentNode.getStylesheets().addAll(defaultLibraryStyles);
        parentNode.getStylesheets().addAll(customStyleSheets);
    }

    private VBox container() {
        VBox parent = new VBox();
        parent.setAlignment(Pos.CENTER);
        parent.setFillWidth(true);
        parent.setSpacing(10.0);
        return parent;
    }

    private void create() {
        Platform.runLater(()->{
            // create content holder
            Parent container = container();

            if (useCustom) {
                container = customComponentFunction.createLoaderUI();
            } else {
                if (customLoaderNode) {
                    ((VBox) container).getChildren().add(loaderNodeCallback.createLoaderNode());
                }
                if (customTextNode) {
                    ((VBox) container).getChildren().add(textNodeCallback.createTextNode());
                }

                if (!customLoaderNode && !customTextNode) {
                    // create text node
                    Label textNode = new Label(initialText);

                    // create progress bar
                    ProgressBar mainLoader = new ProgressBar();

                    // add to the container
                    ((VBox) container).getChildren().addAll(mainLoader,textNode);
                }
            }

            parentNode.getChildren().clear();
            parentNode.getChildren().add(container);

            // get the scene from the stage passed
            Scene scene = stage.getScene();

            Util util = new Util();
            Map<String, Double> geometry =util.getGeometry(container,scene);

            // Set container bounds
            double width = geometry.get("width");
            double height = geometry.get("height");

            // Get scene bounds
            double sceneWidth = geometry.get("sceneWidth");
            double sceneHeight = geometry.get("sceneHeight");

            // Define positioning
            Map<String, Object> coordinate = util.parsePosition(DEFAULT_POSITION,width,height,sceneWidth,sceneHeight);
            double toX = (double) coordinate.get("toX");
            double toY = (double) coordinate.get("toY");

            // Initial Position
            container.setLayoutX(toX);
            container.setLayoutY(toY);

            parentNode.setVisible(true);
            parentNode.setManaged(true);
        });
    }

    public Preloader setText(String text) {
        this.initialText = text;
        return this;
    }

    public Preloader setCustomComponent(LoaderCallback callback) {
        if (callback != null) {
            this.useCustom = true;
            customComponentFunction = callback;
        }
        return this;
    }

    public Preloader setCustomTextNode(TextNode textNode) {
        if (textNode != null) {
            this.customTextNode = true;
            textNodeCallback = textNode;
        }

        return this;
    }

    public Preloader setCustomLoaderNode(LoaderNode loaderNodeCallback) {
        if (loaderNodeCallback != null) {
            this.customLoaderNode = true;
            this.loaderNodeCallback = loaderNodeCallback;
        }
        return this;
    }

    public Preloader setPosition(Pos position) {
        this.DEFAULT_POSITION = position;
        return this;
    }

    public boolean isCustomLoaderNode() {
        return customLoaderNode;
    }

    public boolean isCustomTextNode() {
        return customTextNode;
    }

    public boolean isUseCustom() {
        return useCustom;
    }

    public LoaderCallback getCustomComponentFunction() {
        return customComponentFunction;
    }

    public LoaderNode getLoaderNodeCallback() {
        return loaderNodeCallback;
    }

    public TextNode getTextNodeCallback() {
        return textNodeCallback;
    }

    public AnchorPane getParentNode() {
        return parentNode;
    }

    public String getInitialText() {
        return initialText;
    }

    public Pos getDEFAULT_POSITION() {
        return DEFAULT_POSITION;
    }

    private void reset() {
        this.useCustom = false;
        this.customLoaderNode = false;
        this.customTextNode = false;
        this.initialText = "loading...";
        this.DEFAULT_POSITION = Pos.CENTER;
    }

    private void silentDestroy() {
        Platform.runLater(()->{parentNode.getChildren().clear();});
    }

    public void destroy() {
        Scene scene = this.stage.getScene();
        Parent userRoot = scene.getRoot();

        parentNode.getChildren().clear();
        ((Pane) userRoot).getChildren().remove(parentNode);
    }

    public void show() {
        create();
        this.parentNode.setVisible(true);
    }

    public void hide() {
        this.parentNode.setVisible(false);
        silentDestroy();
    }
}
