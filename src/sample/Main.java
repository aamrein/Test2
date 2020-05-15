package sample;

import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    private static final Dimension2D DIMENSION = new Dimension2D(400, 400);

    private TextField brushSize = new TextField();
    private ColorPicker colorPicker = new ColorPicker();
    private CheckBox erase = new CheckBox();

    private double pressedX = 0;
    private double pressedY = 0;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, DIMENSION.getWidth(), DIMENSION.getHeight());

        root.setCenter(createCanvasStack(scene));
        root.setTop(createMenuBar());


        primaryStage.setTitle("Canvas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleRectangle(Canvas topCanvas, Canvas paintCanvas) {
        topCanvas.setOnMousePressed(mouseEvent -> {
            pressedX = mouseEvent.getX();
            pressedY = mouseEvent.getY();
        });
        topCanvas.setOnMouseDragged(mouseEvent -> {
            GraphicsContext graphicsContext2D = topCanvas.getGraphicsContext2D();
            clearCanvas(graphicsContext2D);
            graphicsContext2D.strokeRect(pressedX, pressedY, mouseEvent.getX() - pressedX, mouseEvent.getY() - pressedY);
        });
        topCanvas.setOnMouseReleased(mouseEvent -> {
            GraphicsContext topCanvasGraphicsContext2D = topCanvas.getGraphicsContext2D();
            GraphicsContext paintCanvasGraphicsContext2D = paintCanvas.getGraphicsContext2D();
            clearCanvas(topCanvasGraphicsContext2D);

            paintCanvasGraphicsContext2D.fillRect(pressedX, pressedY, mouseEvent.getX() - pressedX, mouseEvent.getY() - pressedY);
        });
    }

    private void clearCanvas(GraphicsContext graphicsContext2D) {
        graphicsContext2D.clearRect(0, 0, DIMENSION.getWidth(), DIMENSION.getHeight());
    }

    private StackPane createCanvasStack(Scene scene) {
        final Canvas topCanvas = new Canvas(DIMENSION.getWidth(), DIMENSION.getHeight());
        final Canvas paintCanvas = new Canvas(DIMENSION.getWidth(), DIMENSION.getHeight());

        setMouseEvent(topCanvas);
        handleCursor(scene, topCanvas);
        handleRectangle(topCanvas, paintCanvas);


        StackPane canvasStack = new StackPane();
        Rectangle background = new Rectangle(DIMENSION.getWidth(), DIMENSION.getHeight(), Color.WHITE);
        canvasStack.getChildren().addAll(background, paintCanvas, topCanvas);

        return canvasStack;
    }

    private void handleCursor(Scene scene, Canvas canvas) {
        canvas.setOnMouseEntered(mouseEvent -> scene.setCursor(Cursor.CROSSHAIR));
        canvas.setOnMouseExited(mouseEvent -> scene.setCursor(Cursor.DEFAULT));
    }

    private HBox createMenuBar() {
        HBox menuBar = new HBox();

        brushSize.setText("10");
        menuBar.getChildren().addAll(brushSize, colorPicker, erase);

        return menuBar;
    }

    private void setMouseEvent(Canvas canvas) {
        canvas.setOnMouseDragged(mouseEvent -> {
            double x = mouseEvent.getX();
            double y = mouseEvent.getY();

            GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
            graphicsContext2D.setFill(colorPicker.getValue());

            double brush = Double.parseDouble(brushSize.getText());
            if (erase.isSelected()) {
                graphicsContext2D.clearRect(x, y, brush, brush);
            } else {
                graphicsContext2D.fillRect(x, y, brush, brush);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
