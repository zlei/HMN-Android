package edu.wpi.cs.peds.hmn.stats.costbenefit;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

/**
 * 
 * @author Zhenhao Lei, zlei@wpi.edu
 * 
 */

public class LineGraph {
	private GraphicalView view;
	private TimeSeries totalset = new TimeSeries("Total");
	private TimeSeries uploadset = new TimeSeries("Upload");
	private TimeSeries downloadset = new TimeSeries("Download");

	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	private XYSeriesRenderer totalrenderer = new XYSeriesRenderer();
	private XYSeriesRenderer uploadrenderer = new XYSeriesRenderer();
	private XYSeriesRenderer downloadrenderer = new XYSeriesRenderer();

	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	public LineGraph() {
		mDataset.addSeries(totalset);
		mDataset.addSeries(uploadset);
		mDataset.addSeries(downloadset);

		totalrenderer.setColor(Color.WHITE);
		totalrenderer.setPointStyle(PointStyle.DIAMOND);
		totalrenderer.setFillPoints(true);
		totalrenderer.setFillBelowLine(true);
		totalrenderer.setFillBelowLineColor(Color.argb(64, 255, 255, 255));

		uploadrenderer.setColor(Color.rgb(137, 201, 151));
		uploadrenderer.setPointStyle(PointStyle.CIRCLE);
		uploadrenderer.setFillPoints(true);
		uploadrenderer.setFillBelowLine(true);
		uploadrenderer.setFillBelowLineColor(Color.argb(64, 137, 201, 151));

		downloadrenderer.setColor(Color.rgb(0, 86, 31));
		downloadrenderer.setPointStyle(PointStyle.SQUARE);
		downloadrenderer.setFillPoints(true);
		downloadrenderer.setFillBelowLine(true);
		downloadrenderer.setFillBelowLineColor(Color.argb(64, 0, 86, 31));

		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.BLACK);
//		mRenderer.setXTitle("Time (S)");
		mRenderer.setYTitle("Network Usage (KB)");
		mRenderer.setLegendTextSize(25);
		mRenderer.setLabelsTextSize(25);
		mRenderer.setAxisTitleTextSize(25);
		mRenderer.setYLabelsAlign(Align.LEFT);
//		mRenderer.setFitLegend(true);
		// mRenderer.setLegendHeight(80);

		mRenderer.addSeriesRenderer(totalrenderer);
		mRenderer.addSeriesRenderer(uploadrenderer);
		mRenderer.addSeriesRenderer(downloadrenderer);
	}

	public GraphicalView getView(Context context) {
		view = ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}

	public void addNewTotalPoints(Point p) {
		totalset.add(p.getX(), p.getY());
	}

	public void addNewUploadPoints(Point p) {
		uploadset.add(p.getX(), p.getY());
	}

	public void addNewDownloadPoints(Point p) {
		downloadset.add(p.getX(), p.getY());
	}
}
