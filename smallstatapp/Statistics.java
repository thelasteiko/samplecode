import java.util.Arrays;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

/**
 * Calculates statistics from XYChart data that are numbers.
 * @author Melinda Robertson
 * @version 28 March 2015
 */
public class Statistics {
	
	/**
	 * The x components of the XYChart.
	 */
	private double[] x;
	
	/**
	 * The y components of the XYChart.
	 */
	private double[] y;
	
	/**
	 * The mean values for both x and y components.
	 */
	private double[] mean;

	/**
	 * Creates a reference for retrieving statistical information.
	 * Input data is sorted in ascending order and the mean is calculated.
	 * @param data is the data to reference.
	 */
	public Statistics(ObservableList<XYChart.Data<Number, Number>> data) {
		double[][] array = new double[2][data.size()];
		for (int i = 0; i < array[0].length; i++ ) {
			array[0][i] = (Double) data.get(i).getXValue();
			array[1][i] = (Double) data.get(i).getYValue();
		}
		
		x = Arrays.copyOf(array[0], array[0].length);
		y = Arrays.copyOf(array[1], array[1].length);
		
		Arrays.sort(x);
		Arrays.sort(y);
		
		mean = new double[2];
		
		for (int i = 0; i < x.length; i++) {
			mean[0] += x[i];
			mean[1] += y[i];
		}
		
		mean[0] = mean[0] / x.length;
		mean[1] = mean[1] / y.length;
	}
	
	/**
	 * Retrieve the x component average value.
	 * @return the x mean.
	 */
	public double getXMean() {
		return mean[0];
	}
	
	/**
	 * Retrieve the y component average value.
	 * @return the y mean.
	 */
	public double getYMean() {
		return mean[1];
	}
	
	/**
	 * Retrieve the x component middle value.
	 * @return the x median.
	 */
	public double getXMedian() {
		double median = 0;

		if (x.length == 2) {
			median = (x[0] + x[1]) / 2; 
		} else if (x.length % 2 == 0) {
			median = (x[x.length / 2] + x[(x.length / 2) + 1]) / 2;
			
		} else {
			median = x[x.length / 2];
		}
		
		return median;
	}
	
	/**
	 * Retrieve the y component middle value.
	 * @return the y median.
	 */
	public double getYMedian() {
		double median = 0;
		
		if (y.length == 2) {
			median = (y[0] + y[1]) / 2;
		} else if (y.length % 2 == 0) {
			median = (y[y.length / 2] + y[(y.length / 2) + 1]) / 2;
			
		} else {
			median = y[y.length / 2];
		}
		
		return median;
	}
	
	/**
	 * Retrieve the x component most frequent value.
	 * @return the x mode.
	 */
	public double getXMode() {	    
		double mode = 0;
		double xcurrent= x[0];
		int xcount = 0;
		int xmax = 0;
		
		for (int i = 0; i < x.length; i++) {
			if (x[i] == xcurrent) xcount++;
			else {
				xcurrent = x[i];
				xcount = 0;
			}
			
			if (xmax <= xcount) {
				xmax = xcount;
				mode = xcurrent;
			}
		}
		
		return mode;
	}
	
	/**
	 * Retrieve the y component most frequent value.
	 * @return the y mode.
	 */
	public double getYMode() {
		double mode = 0;
		double ycurrent = y[0];
		int ycount = 0;
		int ymax = 0;
		
		for (int i = 0; i < x.length; i++) {
			
			if (y[i] == ycurrent) ycount++;
			else {
				ycurrent = y[i];
				ycount = 0;
			}
			
			if (ymax <= ycount) {
				ymax = ycount;
				mode = ycurrent;
			}
		}
		
		return mode;
	}
	
	/**
	 * Retrieve the width of x component values.
	 * @return the x range.
	 */
	public double getXRange() {
		return x[x.length - 1] - x[0];
	}
	
	/**
	 * Retrieve the width of y component values.
	 * @return the y range.
	 */
	public double getYRange() {
		return y[y.length - 1] - y[0];
	}
	
	/**
	 * Retrieve the x component deviation from the mean.
	 * @return the x standard deviation.
	 */
	public double getXs() {
		double sum = 0;
		
		for (int i = 0; i < x.length; i++) {
			sum += Math.pow(x[i] - mean[0], 2);
		}
		
		sum = sum / (x.length - 1);
		return Math.sqrt(sum);
	}
	
	/**
	 * Retrieve the y component deviation from the mean.
	 * @return the y standard deviation.
	 */
	public double getYs() {
		double sum = 0;
		
		for (int i = 0; i < y.length; i++) {
			sum += Math.pow(y[i] - mean[1], 2);
		}
		
		sum = sum / (y.length - 1);
		return Math.sqrt(sum);
	}
}
