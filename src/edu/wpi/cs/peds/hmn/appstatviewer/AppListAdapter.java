/**
 * Allows the application itself to connect to the app collector service in
 * order to retrieve and display the app list.
 */
package edu.wpi.cs.peds.hmn.appstatviewer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.wpi.cs.peds.hmn.app.R;
import edu.wpi.cs.peds.hmn.stats.apps.Application;

/**
 * @author Richard Brown, rpb111@wpi.edu
 * 
 */
public class AppListAdapter extends BaseAdapter {

	private final LayoutInflater inflater;

	private List<Application> list;

	public AppListAdapter(Context context, List<Application> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row, parent, false);
		}
		TextView title = (TextView) convertView.findViewById(R.id.appTitle);
		TextView costText = (TextView) convertView.findViewById(R.id.appCost);
		TextView benefitText = (TextView) convertView.findViewById(R.id.appBenefit);

		Application app = list.get(position);
		String appName = app.getName();
		title.setText(appName);
		
		/*
		NetUsageList list = app.netUsage == null ? new NetUsageList() : app.netUsage;
		sub.setText(list.getShortString());
		*/
		
		int cost = app.cost;
		int benefit = app.benefit;

		costText.setText("Cost: " + cost);
		benefitText.setText("Benefit: " + benefit);
		
		return convertView;
	}

	public void setAppList(List<Application> apps) {
		this.list = apps;
		notifyDataSetChanged();
	}
}
