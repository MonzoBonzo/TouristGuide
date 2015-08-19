package aston.cs3040.touristguide.maps;

import aston.cs3040.model.Place;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class PlaceOverlayItem extends OverlayItem
{
	private final GeoPoint point;
	private final Place place;
	private boolean inCluster = false;
	private boolean isVisible = true;

	public PlaceOverlayItem(Place p)
	{
		super(p.getGeoPoint(), p.getName(), p.getFormatted_address());
		this.point = p.getGeoPoint();
		this.place = p;
	}

	/**
	 * @return the point
	 */
	public GeoPoint getPoint()
	{
		return point;
	}

	/**
	 * @return the place
	 */
	public Place getPlace()
	{
		return place;
	}
	
	public void setInCluster(boolean inCluster)
	{
		this.inCluster = inCluster;
	}
	
	public boolean getInCluster()
	{
		return inCluster;
	}
	
	public void setVisible()
	{
		this.isVisible = !isVisible;
	}
	
	public boolean getVisible()
	{
		return isVisible;
	}
}
