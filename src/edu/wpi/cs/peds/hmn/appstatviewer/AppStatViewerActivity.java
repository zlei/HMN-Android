/**
 * Constructs a screen to display the app list requested by the user.
 */
package edu.wpi.cs.peds.hmn.appstatviewer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.appcollector.AppCollectorService;
import edu.wpi.cs.peds.hmn.appcollector.AppCollectorService.MyBinder;
import edu.wpi.cs.peds.hmn.appdetailviewer.ApplicationDetailViewActivity;
import edu.wpi.cs.peds.hmn.appstatviewer.sorting.SortOrder;
import edu.wpi.cs.peds.hmn.log.HmnLog;
import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;
import edu.wpi.cs.peds.hmn.stats.costbenefit.OverallBenefitDetailActivity;

/**
 * The view of the app list shown to the user
 * 
 * @author Richard Brown, rpb111@wpi.edu
 * @author Zhenhao Lei, zlei@wpi.edu
 * @author Austin Noto-Moniz, austinnoto@wpi.edu
 */
public class AppStatViewerActivity extends ListActivity implements IObserver {
	public class AppCollectorServiceConnection implements ServiceConnection {

		public AppCollectorServiceConnection(IObservable observableService,
				IObserver componentToRegister) {
			observer = componentToRegister;
			observedService = observableService;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			observedService = ((MyBinder) service).getService();
			observedService.addObserver(observer);
			apps = GlobalAppList.getInstance().getDisplayedApps();
			((AppListAdapter) adapter).setAppList(apps);
		}

		public void onServiceDisconnected(ComponentName name) {
			observedService.deleteObserver(observer);
			observedService = null;
		}
	}

	private BaseAdapter adapter;
	private IObserver observer;
	private IObservable observedService;
	private List<Application> apps;
	private Intent appCollector;
	private AppCollectorServiceConnection appCollectorConnection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void onStart() {
		super.onStart();
		setContentView(R.layout.activity_main);

		// Log.i(HmnLog.HMN_LOG_TAG,"!!!!!!!!!!!APPS GET STARTED!!!!!");
		createSortingDropdown();
		startAppCollector();
		adapter = new AppListAdapter(this, new ArrayList<Application>());
		setListAdapter(adapter);
	}

	/**
	 * Initializes the drop down menu that allows the user to sort the list of
	 * apps.
	 */
	public void createSortingDropdown() {
		final Spinner sortBySpinner = (Spinner) findViewById(R.id.sort_by_spinner);
		final Spinner sortOrderSpinner = (Spinner) findViewById(R.id.sort_order_spinner);

		// Initializes the drop down for selecting which field to sort by
		ArrayAdapter<SortOrder> sortBySpinnerItemAdapter = new ArrayAdapter<SortOrder>(
				this, android.R.layout.simple_spinner_item, SortOrder.values());
		sortBySpinnerItemAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sortBySpinner.setAdapter(sortBySpinnerItemAdapter);
		sortBySpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						SortOrder sortBy = (SortOrder) parent
								.getItemAtPosition(pos);
						String sortOrder = (String) sortOrderSpinner
								.getSelectedItem();
						if (sortOrder.equals("Ascending"))
							sortBy.sortAsc(GlobalAppList.getInstance()
									.getDisplayedApps());
						else
							sortBy.sortDec(GlobalAppList.getInstance()
									.getDisplayedApps());
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		ArrayAdapter<String> sortOrderSpinnerItemAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, new String[] {
						"Ascending", "Descending" });
		sortOrderSpinnerItemAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sortOrderSpinner.setAdapter(sortOrderSpinnerItemAdapter);
		sortOrderSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						SortOrder sortBy = (SortOrder) sortBySpinner
								.getSelectedItem();
						String sortOrder = (String) parent
								.getItemAtPosition(pos);
						if (sortOrder.equals("Ascending"))
							sortBy.sortAsc(GlobalAppList.getInstance()
									.getDisplayedApps());
						else
							sortBy.sortDec(GlobalAppList.getInstance()
									.getDisplayedApps());
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
	}

	public void startAppCollector() {
		appCollector = new Intent(this, AppCollectorService.class);
		appCollectorConnection = new AppCollectorServiceConnection(null, this);

		bindService(appCollector, appCollectorConnection, 0);
	}

	/**
	 * Detects that an app has been clicked on, and sends the user to a page
	 * displaying the detailed stats collected about that app.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if (id <= Integer.MAX_VALUE && id >= Integer.MIN_VALUE) {
			int i = (int) id;
			Application chosenApp = apps.get(i);

			// Sends the UID for lookup
			Intent detailIntent = new Intent(this,
					ApplicationDetailViewActivity.class);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(bos);
				oos.writeInt(chosenApp.uid);
				oos.close();
				detailIntent.putExtra("uid", bos.toByteArray());
				bos.close();
				startActivity(detailIntent);
			} catch (IOException e) {
				System.out.println("ERROR: ");
				e.printStackTrace();
			}
		}
	}

	/*
	 * public boolean onCreateOptionsMenu(Menu menu) { MenuInflater inflater =
	 * getMenuInflater(); inflater.inflate(R.menu.activity_app_list, menu);
	 * return true; }
	 * 
	 * // Handles the user's menu selection.
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case R.id.refresh: refresh = true; return true;
	 * default: return super.onOptionsItemSelected(item); } }
	 */
	@Override
	protected void onPause() {
		super.onPause();
		// release listener to stop animation / table updating
		if (observedService != null) {
			observedService.deleteObserver(observer);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (observedService != null) {
			observedService.addObserver(observer);
		}
	}

	protected void onStop() {
		super.onStop();
		unbindService(appCollectorConnection);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// unbindService(appCollectorConnection);
	}

	public void update(IObservable O, Object arg) {
		Log.i(HmnLog.HMN_LOG_TAG, "Something has changed");
		adapter.notifyDataSetChanged();
	}
}
