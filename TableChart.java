import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.InputMismatchException;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Creates a scatter chart from the data in a table. Allows the user to add
 * and remove data points and displays statistical information of the sets.
 * @author Melinda Robertson
 * @version 28 March 2015
 */
public class TableChart extends Application {
	
	/**
	 * For displaying the double values.
	 */
	private final NumberFormat DF = new DecimalFormat("#0.00");
	
	/**
	 * Holds all components.
	 */
	private HBox mainHolder;
	/**
	 * Holds the x y table and statistics(calcBox).
	 */
	private BorderPane tableBox;
	/**
	 * Holds the statistics.
	 */
	private GridPane calcBox;
	
	/**
	 * Displays the x-y pairs from the table.
	 */
	private ScatterChart<Number, Number> chart;
	/**
	 * The table for the x-y pairs.
	 */
	private TableView<XYChart.Data<Number, Number>> table;
	/**
	 * The data for the chart.
	 */
	private ObservableList<XYChart.Data<Number, Number>> data;
	
	/**
	 * The text objects that display the statistics.
	 */
	private ObservableList<Text> display;

	/**
	 * Starts the application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Scatter Chart");
		
		Group root = new Group();
		Scene scene = new Scene(root);
		mainHolder = new HBox();
		
		//Builds a border pane that holds the x-y table and statistics.
		buildTableBox();
		
		//----------CREATE THE CHART------------------
		buildChart();
		
		//---------ADD ALL THE THINGS---------------------
		mainHolder.getChildren().addAll(chart, tableBox);
		
		
		//Set the style and show the stage.
		scene.getStylesheets().add("scatter/scatter.css");
		stage.setScene(scene);
		root.getChildren().addAll(mainHolder);
		stage.show();
	}
	
	/**
	 * Creates the chart to display data points.
	 */
	private void buildChart() {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		
		xAxis.setSide(Side.BOTTOM);
		xAxis.setLabel("X");
		yAxis.setSide(Side.LEFT);
		yAxis.setLabel("Y");
		
		final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setData(data);
		
		chart = new ScatterChart<Number, Number>(xAxis, yAxis);
		chart.getData().add(series);
		chart.setLegendVisible(false);
	}
	
	/**
	 * Creates a border pane to hold the x-y table and statistics.
	 */
	@SuppressWarnings("unchecked")
	private void buildTableBox() {
		tableBox = new BorderPane();
		tableBox.getStyleClass().add("border-pane");
		
		Label label = new Label("X|Y Table");
		
		//--------------CREATE THE TABLE----------------------
		table = new TableView<XYChart.Data<Number, Number>>();
		data = FXCollections.observableArrayList();
		
		TableColumn<XYChart.Data<Number, Number>, Double> xCol =
				new TableColumn<XYChart.Data<Number, Number>, Double>("X");
		xCol.setCellValueFactory(
				new PropertyValueFactory<XYChart.Data<Number, Number>, Double>("XValue"));
		xCol.setOnEditCommit(
				new EventHandler<CellEditEvent<XYChart.Data<Number, Number>, Double>>() {

					@Override
					public void handle(CellEditEvent<XYChart.Data<Number, Number>, Double> e) {
						((Data<Number, Number>) e.getTableView().getItems().get(
								e.getTablePosition().getRow())).setXValue(
										e.getNewValue());
					}
					
				});
		TableColumn<XYChart.Data<Number, Number>, Double> yCol =
				new TableColumn<XYChart.Data<Number, Number>, Double>("Y");
		yCol.setCellValueFactory(
				new PropertyValueFactory<XYChart.Data<Number, Number>, Double>("YValue"));
		yCol.setOnEditCommit(
				new EventHandler<CellEditEvent<XYChart.Data<Number, Number>, Double>>() {

					@Override
					public void handle(CellEditEvent<XYChart.Data<Number, Number>, Double> e) {
						((Data<Number, Number>) e.getTableView().getItems().get(
								e.getTablePosition().getRow())).setYValue(
										e.getNewValue());
					}
					
				});
		table.setItems(data);
		table.getColumns().addAll(xCol, yCol);
		table.setMaxWidth(162);
		
		//-------------CONSTRUCT THE BORDERPANE--------------
		tableBox.setTop(label);
		tableBox.setBottom(buildAddPane());
		buildCalcBox();
		tableBox.setCenter(calcBox);
		tableBox.setLeft(table);		
	}
	
	/**
	 * Creates a panel to add and remove items in the x-y table and chart.
	 * @return a panel with some text fields and buttons.
	 */
	private HBox buildAddPane() {
		HBox box = new HBox();
		
		//-------------FIELDS----------------------
		final TextField xField = new TextField();
		xField.setPromptText("X");
		
		final TextField yField = new TextField();
		yField.setPromptText("Y");
		
		//---------------BUTTONS--------------------
		final Button addBtn = new Button("Add");
		addBtn.setOnAction( new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				try {
					data.add(new XYChart.Data<Number, Number>(Double.parseDouble(xField.getText()),
							Double.parseDouble(yField.getText())));
				} catch (InputMismatchException e1) {
					JOptionPane.showMessageDialog(null, "Please input a number.");
				}
						
				xField.clear();
				yField.clear();
				updateCalcBox();
				xField.requestFocus();
			}
			
		});
		addBtn.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				if (KeyCode.ENTER.equals(arg0.getCode())) {
					addBtn.fire();
				}
			}
			
		});
		
		final Button rmBtn = new Button("Remove");
		rmBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					Double x = Double.parseDouble(xField.getText());
					Double y = Double.parseDouble(yField.getText());

				
					for (int i = 0; i < data.size(); i++) {
						if (x.equals(data.get(i).getXValue()) && y.equals(data.get(i).getYValue())) {
							data.remove(i);
							break;
						}
					}
				} catch (InputMismatchException e2) {
					JOptionPane.showMessageDialog(null, "Please input a number.");
				}
				
				xField.clear();
				yField.clear();
				updateCalcBox();
				xField.requestFocus();
			}
		});
		rmBtn.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (KeyCode.ENTER.equals(e.getCode())) {
					rmBtn.fire();
				}
			}
		});
		
		//Setup the box and add children.
		box.setSpacing(5);
		box.setPadding(new Insets(10, 0, 0, 10));
		box.getChildren().addAll(xField, yField, addBtn, rmBtn);
		
		return box;
	}
	
	/**
	 * Creates the grid pane to display statistics from the x-y table.
	 */
	private void buildCalcBox() {
		//Layout things.
		calcBox = new GridPane();
		calcBox.getStyleClass().add("grid");
		calcBox.getColumnConstraints().add(new ColumnConstraints(100));
		calcBox.getColumnConstraints().add(new ColumnConstraints(100));
		calcBox.getColumnConstraints().add(new ColumnConstraints(100));
		
		//--------------LABELS-------------------
		Label lblMeanX = new Label("X Stats");
		calcBox.add(lblMeanX, 1, 0);
		
		Label lblMeanY = new Label("Y Stats");
		calcBox.add(lblMeanY, 2, 0);
		
		Label mean = new Label("Mean: ");
		calcBox.add(mean, 0, 1);
		
		Label median = new Label("Median: ");
		calcBox.add(median, 0, 2);
		
		Label mode = new Label("Mode: ");
		calcBox.add(mode, 0, 3);
		
		Label range = new Label("Range: ");
		calcBox.add(range, 0, 4);
		
		Label Sx = new Label("Sx: ");
		calcBox.add(Sx, 0, 5);
		
		//-------------DISPLAY FIELDS-----------------
		//TODO How much statistics do we need?
		display = FXCollections.observableArrayList();
		for (int i = 1; i < 10; i++) {
			Text text1 = new Text();
			calcBox.add(text1, 1, i);
			display.add(text1);
			
			Text text2 = new Text();
			calcBox.add(text2, 2, i);
			display.add(text2);
		}
		
	}
	
	/**
	 * Updates the text in the calcBox.
	 */
	private void updateCalcBox() {
		
		Statistics st = new Statistics(data);
		
		display.get(0).setText("" + DF.format(st.getXMean()));
		display.get(1).setText("" + DF.format(st.getYMean()));
		display.get(2).setText("" + DF.format(st.getXMedian()));
		display.get(3).setText("" + DF.format(st.getYMedian()));
		display.get(4).setText("" + DF.format(st.getXMode()));
		display.get(5).setText("" + DF.format(st.getYMode()));
		display.get(6).setText("" + DF.format(st.getXRange()));
		display.get(7).setText("" + DF.format(st.getYRange()));
		display.get(8).setText("" + DF.format(st.getXs()));
		display.get(9).setText("" + DF.format(st.getYs()));
	}

	/**
	 * Main Method!
	 * @param args are the args.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
