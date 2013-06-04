package edu.wpi.cs.peds.hmn.stats.costbenefit;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import edu.wpi.cs.peds.hmn.app.R;

/**
 * @author Zhenhao Lei, zlei@wpi.edu
 */

public class OverallCostGraphActivity extends FragmentActivity {
	private static GraphicalView view;
	private LineGraph line = new LineGraph();
	private static Thread thread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cost_graph);

		thread = new Thread() {
			public void run() {
				for (int i = 0; i < 60; i++) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Point p1 = LiveData.getOverallTotalData(i);
					line.addNewTotalPoints(p1);
					Point p2 = LiveData.getOverallUploadData(i);
					line.addNewUploadPoints(p2);
					Point p3 = LiveData.getOverallDownloadData(i);
					line.addNewDownloadPoints(p3);
					view.repaint();
				}
			}
		};
		thread.start();
	}

	protected void onStart() {
		super.onStart();
		view = line.getView(this);
		setContentView(view);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.cost_graph, menu); return true; }
	 */

	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
