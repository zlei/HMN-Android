/**
 * A list of options for sorting the app list, and the corresponding comparator.
 */

package edu.wpi.cs.peds.hmn.appstatviewer.sorting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.wpi.cs.peds.hmn.stats.apps.Application;
import edu.wpi.cs.peds.hmn.stats.apps.GlobalAppList;

/** 
 * A collection of sorters as a label and the appropriate Comparator. 
 * 
 * @author Austin Noto-Moniz austinnoto@wpi.edu
 *
 */
public enum SortOrder
{
	NAME("Name", new SortingComparators.SortByNameAtoZ(), new SortingComparators.SortByNameZtoA()),
	COST("Cost", new SortingComparators.SortByCostDec(), new SortingComparators.SortByCostAsc()),
	BENEFIT("Benefit", new SortingComparators.SortByBenefitDec(), new SortingComparators.SortByBenefitAsc()),
	USER_RATING("User Rating", new SortingComparators.SortByUserRatingDec() , new SortingComparators.SortByUserRatingAsc()),
	DB_RATING("DB Rating", new SortingComparators.SortByDBRatingDec(), new SortingComparators.SortByDBRatingAsc());
	
	private final String text;
	private final Comparator<Application> ascending;
	private final Comparator<Application> descending;
	
	private SortOrder(String text, Comparator<Application> decending, Comparator<Application> ascending)
	{
		this.text = text;
		this.descending = decending;
		this.ascending = ascending;
	}
	
	public String toString()
	{
		return text;
	}
	
	public void sortAsc(List<Application> appList)
	{
		Collections.sort(GlobalAppList.getInstance().getDisplayedApps(),ascending);
	}
	
	public void sortDec(List<Application> appList)
	{
		Collections.sort(GlobalAppList.getInstance().getDisplayedApps(),descending);
	}
}
