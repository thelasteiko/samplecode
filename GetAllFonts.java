import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Utility to view all available fonts using a JavaFX application window. The
 * fonts can be previewed in different formats.
 * 
 * @author Melinda Robertson
 * @version 25 March 2015
 */
public class GetAllFonts extends Application {
	/**
	 * The default size of the text area.
	 */
	final private int TASIZE = 20;

	/**
	 * The main container for the scene. Holds all components.
	 */
	private HBox mainHolder;

	/**
	 * Holds the preview items and button pane.
	 */
	private VBox previewHolder;

	/**
	 * Holds the buttons.
	 */
	private TilePane btnBox;

	/**
	 * Holds the color choices.
	 */
	private VBox colorBox;

	/**
	 * The text area where the user can preview the font.
	 */
	private TextArea field;

	/**
	 * Displays the font's name in that font.
	 */
	private Label label;

	/**
	 * Main method.
	 * 
	 * @param args
	 *            are the args.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Starts the application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(new Group());

		stage.setTitle("Fonts");
		stage.setWidth(900);
		stage.setHeight(400);

		// Instantiate the containers.
		mainHolder = new HBox();
		previewHolder = new VBox();
		btnBox = new TilePane();
		colorBox = new VBox();

		// Set parameters
		previewHolder.setPadding(new Insets(10, 0, 0, 10));
		btnBox.setPrefRows(2);
		btnBox.setHgap(5);
		btnBox.setVgap(5);
		colorBox.setPadding(new Insets(10, 0, 0, 10));

		// Instantiate the preview elements.
		label = new Label();
		field = new TextArea();
		field.setEditable(true);
		field.setWrapText(true);
		field.setMinSize(400, 200);
		field.setMaxSize(400, 200);
		field.setStyle("-fx-text-fill: black; -fx-background-color: white");

		// Toggle group to change from upper to lower case and vice versa.
		final ToggleGroup caseGroup = new ToggleGroup();
		final ToggleButton btnUp = new ToggleButton("UpperCase");
		final ToggleButton btnDn = new ToggleButton("LowerCase");
		btnUp.setToggleGroup(caseGroup);
		btnDn.setToggleGroup(caseGroup);
		caseGroup.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {

					@Override
					public void changed(
							ObservableValue<? extends Toggle> observable,
							Toggle oldValue, Toggle newValue) {
						if (btnUp.isSelected()) {
							field.setText(field.getText().toUpperCase());
						} else if (btnDn.isSelected()) {
							field.setText(field.getText().toLowerCase());
						}
					}

				});

		// Toggle group to change the style of the text.
		final ToggleGroup styleGroup = new ToggleGroup();
		final ToggleButton btnBold = new ToggleButton("Bold");
		final ToggleButton btnItalic = new ToggleButton("Italic");
		final ToggleButton btnResetFont = new ToggleButton("Reset");
		btnBold.setToggleGroup(styleGroup);
		btnItalic.setToggleGroup(styleGroup);
		btnResetFont.setToggleGroup(styleGroup);
		styleGroup.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>() {

					@Override
					public void changed(
							ObservableValue<? extends Toggle> observable,
							Toggle oldValue, Toggle newValue) {
						if (btnBold.isSelected()) {
							field.setFont(Font.font(label.getText(),
									FontWeight.BOLD, TASIZE));
						} else if (btnItalic.isSelected()) {
							field.setFont(Font.font(label.getText(),
									FontPosture.ITALIC, TASIZE));
						} else if (btnResetFont.isSelected()) {
							field.setFont(Font.font(label.getText(),
									FontPosture.REGULAR, TASIZE));
							field.setFont(Font.font(label.getText(),
									FontWeight.NORMAL, TASIZE));
						}
					}

				});

		// To choose the color.
		final ColorPicker textColor = new ColorPicker(Color.BLACK);
		textColor.valueProperty().addListener(
				(observable, oldColor, newColor) -> field
						.setStyle("-fx-text-fill: " + toRgbString(newColor)
								+ ";"));

		// Add all the buttons to the button container.
		btnBox.getChildren().addAll(btnUp, btnDn, btnBold, btnItalic,
				btnResetFont, textColor);

		// Add the preview items and the button container to the preview
		// container.
		previewHolder.getChildren().addAll(label, field, btnBox);

		// Create a list to view all the fonts.
		ListView<String> box = new ListView<String>();
		ObservableList<String> data = FXCollections.observableArrayList();
		List<String> fonts = Font.getFontNames(); // Get the fonts
		for (String f : fonts) {
			data.add(f);
		}
		box.setItems(data);
		box.setMinWidth(350);
		box.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					@Override
					public void changed(ObservableValue<? extends String> obs,
							String o, String n) {
						label.setText(n);
						label.setFont(new Font(n, 38));
						field.setFont(new Font(n, TASIZE));
					}
				});

		// Add the list and the preview container to the main container.
		mainHolder.getChildren().addAll(box, previewHolder);

		// Add the main container to the scene.
		((Group) scene.getRoot()).getChildren().addAll(mainHolder);

		stage.setScene(scene);
		stage.show();
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
}
