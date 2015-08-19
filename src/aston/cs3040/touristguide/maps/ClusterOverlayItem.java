package aston.cs3040.touristguide.maps;

import java.util.List;

import com.google.android.maps.OverlayItem;

public class ClusterOverlayItem extends OverlayItem
{	
	private List<PlaceOverlayItem> places;
	
	public ClusterOverlayItem(List<PlaceOverlayItem> itemsInCluster)
	{
		super(
			itemsInCluster.get(0).getPlace().getGeometry().getLocation().getGeoPoint(), 
			itemsInCluster.get(0).getPlace().getName(), 
			itemsInCluster.get(0).getPlace().toString()
		);
		this.places = itemsInCluster;
	}
	
	/**
	 * @return the places
	 */
	public List<PlaceOverlayItem> getPlace() 
	{
		return places;
	}	
}