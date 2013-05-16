/**
 * A collection of comparators that sort Application objects.
 */
package edu.wpi.cs.peds.hmn.appstatviewer.sorting;

import java.util.Comparator;

import edu.wpi.cs.peds.hmn.stats.apps.Application;

/**
 * Contains a bunch of comparators for sorting applications, based on name,
 * cost, and benefit.
 * 
 * @author Austin Noto-Moniz austinnoto@wpi.edu
 *
 */
public class SortingComparators
{
	/**
	 * Sort by app name, from A to Z
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByNameAtoZ implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return app1.getName().compareTo(app2.getName());
		}
	}
	
	/**
	 * Sort by app name, from Z to A
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByNameZtoA implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return -app1.getName().compareTo(app2.getName());
		}
	}
	
	/**
	 * Sort by app's user rating, from high to low
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByUserRatingDec implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return -app1.userRating.compareTo(app2.userRating);
		}
	}
	
	/**
	 * Sort by app's user rating, from low to high
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByUserRatingAsc implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return app1.userRating.compareTo(app2.userRating);
		}
	}
	
	/**
	 * Sort by app's rating from the DB, from high to low
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByDBRatingDec implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return -app1.dbRating.compareTo(app2.dbRating);
		}
	}
	
	/**
	 * Sort by app's rating from the DB, from low to high
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByDBRatingAsc implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return app1.dbRating.compareTo(app2.dbRating);
		}
	}
	
	/**
	 * Sort by app cost, from highest cost to lowest cost.
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByCostDec implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return -app1.cost.compareTo(app2.cost);
		}
	}
	
	/**
	 * Sort by app cost, from lowest cost to highest cost.
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByCostAsc implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return app1.cost.compareTo(app2.cost);
		}
	}
	
	/**
	 * Sort by app benefit, from highest benefit to lowest benefit.
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByBenefitDec implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return -app1.benefit.compareTo(app2.benefit);
		}
	}
	
	/**
	 * Sort by app benefit, from lowest benefit to highest benefit.
	 * @author Austin Noto-Moniz, austinnoto@wpi.edu
	 */
	public static class SortByBenefitAsc implements Comparator<Application>
	{
		@Override
		public int compare(Application app1, Application app2)
		{
			return app1.benefit.compareTo(app2.benefit);
		}
	}
}
