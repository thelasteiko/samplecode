package myapp;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Simple Sketch application that draws lines, ovals and rectangles in various colors
 * and line widths. One day I might put all these private fields into a new class...
 * @author Melinda Robertson
 * @version 29 March 2015
 */
public class Sketch extends Application {
	
	/**
	 * The default stroke of lines.
	 */
	private static final Double DEFAULTSTROKE = 3.0;
	/**
	 * The max value for line width.
	 */
	private static final Double MAXSTROKE = 15.0;
	/**
	 * The minimum value for line width.
	 */
	private static final Double MINSTROKE = 1.0;
	/**
	 * The size of the canvas.
	 */
	private static final int SIZE = 500;
	/**
	 * Defines the line drawing mode.
	 */
	private static final int PENCIL = 1;
	/**
	 * Defines the oval drawing mode.
	 */
	private static final int CIRCLE = 2;
	/**
	 * Defines the rectangle drawing mode.
	 */
	private static final int RECTANGLE = 3;
	
	/**
	 * The current drawing mode.
	 */
	private int currentState;
	/**
	 * The beginning point of a stroke.
	 */
	private Point2D p1;
	/**
	 * The end point of a stroke.
	 */
	private Point2D p2;
	
	/**
	 * Slider that determines the stroke width.
	 */
	private Slider stroke = new Slider(MINSTROKE, MAXSTROKE, DEFAULTSTROKE);
	
	/**
	 * The drawing canvas.
	 */
	private Canvas canvas;
	/**
	 * The pane that holds the canvas.
	 * Used so the background color can be changed.
	 */
	private Pane canvasPane;
	/**
	 * The color picker for line color.
	 */
	private ColorPicker lineColor;
	/**
	 * The color picker for background color.
	 */
	private ColorPicker canvasColor;
	
	/**
	 * Holds everything in the scene.
	 */
	private HBox mainBox;
	/**
	 * Holds the controls for drawing.
	 */
	private VBox buttonBox;
	/**
	 * Holds the buttons to change the drawing mode.
	 */
	private FlowPane stateBox;

	/**
	 * Starts the application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		//Set the stage and initial values.
		stage.setTitle("Sketch Pad");
		Group root = new Group();
		Scene scene = new Scene(root);
		currentState = 1;
		
		//Builds the environment.
		buildCanvas();
		buildButtonBox();
		
		mainBox = new HBox();
		mainBox.getChildren().addAll(canvasPane, buttonBox);
		
		root.getChildren().add(mainBox);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Builds the canvas pane.
	 */
	private void buildCanvas() {
		canvasPane = new Pane();
		canvas = new Canvas(SIZE, SIZE);
		canvas.setCursor(Cursor.CROSSHAIR);
		
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		
		//--------------MOUSE EVENT HANDLERS------------------------
		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				p1 = new Point2D(e.getSceneX(), e.getSceneY());
				gc.beginPath();
				gc.moveTo(e.getX(), e.getY());
				gc.stroke();	
			}
		});
		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				p2 = new Point2D(e.getSceneX(), e.getSceneY());
				switch(currentState) {
				case PENCIL:
					gc.lineTo(e.getSceneX(), e.getSceneY());
					gc.stroke();
					break;
				case CIRCLE:
					break;
				case RECTANGLE:
					break;
				}
			}
		});
		canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				p2 = new Point2D(e.getSceneX(), e.getSceneY());
				switch(currentState) {
				case PENCIL:
					break;
				case CIRCLE:
					if (p2.x > p1.x && p2.y > p1.y) {
						gc.strokeOval(p1.x, p1.y,
								p2.x - p1.x, p2.y - p1.y);
					} else if (p2.x > p1.x && p1.y > p2.y) {
						gc.strokeOval(p1.x, p2.y,
								p2.x - p1.x, p1.y - p2.y);
					} else if (p1.x > p2.x && p1.y > p2.y) {
						gc.strokeOval(p2.x, p2.y,
								p1.x - p2.x, p1.y - p2.y);
					} else {
						gc.strokeOval(p2.x, p1.y,
								p1.x - p2.x, p2.y - p1.y);
					}
					break;
				case RECTANGLE:
					if (p2.x > p1.x && p2.y > p1.y) {
						gc.strokeRect(p1.x, p1.y,
								p2.x - p1.x, p2.y - p1.y);
					} else if (p2.x > p1.x && p1.y > p2.y) {
						gc.strokeRect(p1.x, p2.y,
								p2.x - p1.x, p1.y - p2.y);
					} else if (p1.x > p2.x && p1.y > p2.y) {
						gc.strokeRect(p2.x, p2.y,
								p1.x - p2.x, p1.y - p2.y);
					} else {
						gc.strokeRect(p2.x, p1.y,
								p1.x - p2.x, p2.y - p1.y);
					}
					break;
				}
			}
		});
		
		//Add the canvas.
		canvasPane.getChildren().add(canvas);
	}
	
	/**
	 * Builds a VBox that holds the drawing controls.
	 */
	private void buildButtonBox() {
		//Set parameters.
		buttonBox = new VBox();
		buttonBox.setMaxWidth(300);
		buttonBox.setPadding(new Insets(10, 10, 10, 10));
		buttonBox.setSpacing(10);
		buttonBox.setStyle("-fx-background-color: GAINSBORO;");
		
		//-------------CONTROL ITEMS AND EVENT HANDLERS----------------
		Label strokeLabel = new Label("Line Width");
		stroke.valueProperty().addListener((observable,
					oldValue, newValue) ->
				canvas.getGraphicsContext2D().setLineWidth((double) newValue));
		
		Label lineLabel = new Label("Line Color");
		lineColor = new ColorPicker(Color.BLACK);
		lineColor.valueProperty().addListener(
				(observable, oldColor, newColor) -> canvas.getGraphicsContext2D()
				.setStroke(newColor));
		
		Label canvasLabel = new Label("Canvas Color");
		canvasColor = new ColorPicker(Color.WHITE);
		canvasColor.valueProperty().addListener(
				(observable, oldColor, newColor) -> canvasPane
				.setStyle("-fx-background-color: " + toRgbString(newColor)
						+ ";"));
		
		Button btnClear = new Button();
		btnClear.setText("Clear");
		btnClear.setOnAction((event) ->
				canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight()));
		
		//Build a separate box for the mode selectors.
		buildStateBox();
		
		buttonBox.getChildren().addAll(stateBox, strokeLabel, stroke, lineLabel, lineColor, 
				canvasLabel, canvasColor, btnClear);
	}
	
	/**
	 * Builds a FlowPane for the mode buttons.
	 */
	private void buildStateBox() {
		stateBox = new FlowPane();
		stateBox.setHgap(2);
		stateBox.maxWidth(buttonBox.getWidth() - 100);
		
		Label stateLabel = new Label("Select Mode: ");
		
		//------------MODE BUTTONS AND HANDLERS-----------------
		Button pencil = new Button("Pencil");
		pencil.setOnAction((event) -> currentState = PENCIL);
		
		Button circle = new Button("Circle");
		circle.setOnAction((event) -> currentState = CIRCLE);
		
		Button rectangle = new Button("Rectangle");
		rectangle.setOnAction((event) -> currentState = RECTANGLE);
		
		//Adds all the buttons.
		stateBox.getChildren().addAll(stateLabel, pencil, circle, rectangle);
	}
	
	/**
	 * Changes the Color to a String representation.
	 * 
	 * @author N3WYrK
	 * @param c
	 *            is the Color.
	 * @return a String representation of the Color.
	 */
	private String toRgbString(Color c) {
		return "rgb(" + to255Int(c.getRed()) + "," + to255Int(c.getGreen())
				+ "," + to255Int(c.getBlue()) + ")";
	}

	/**
	 * Modifies a double value to an int to make a Color.
	 * 
	 * @author N3WYrK
	 * @param d
	 *            is the double.
	 * @return an int to use for making a Color.
	 */
	private int to255Int(double d) {
		return (int) (d * 255);
	}

	/**
	 * Main method.
	 * @param args are the args.
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Private point class used to define the beginning and ending points
	 * of a stroke.
	 * @author Melinda Robertson
	 * @version 29 March 2015
	 */
	private class Point2D {
		double x;
		double y;
		
		public Point2D(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
}
