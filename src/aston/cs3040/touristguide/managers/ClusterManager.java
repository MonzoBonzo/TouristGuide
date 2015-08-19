package aston.cs3040.touristguide.managers;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import aston.cs3040.touristguide.maps.ClusterItemisedOverlay;
import aston.cs3040.touristguide.maps.ClusterOverlayItem;
import aston.cs3040.touristguide.maps.PlaceOverlayItem;

import com.google.android.maps.MapView;

public class ClusterManager 
{
	private static PlaceOverlayItem marker;
	private static Point placeLocationPixels1 = new Point();
	private static Point placeLocationPixels2 = new Point();
	private static double distance;
	private static double dx; 
	private static double dy; 
	

	public static void addClusters(ClusterItemisedOverlay clusterOverlay, List<PlaceOverlayItem> places, MapView mv)
	{
		 List< List<PlaceOverlayItem>> clustered = new ArrayList< List<PlaceOverlayItem>>();
		//TODO: Clear the arrays so that we can calculate most up to date clusters
		while (places.size() > 0)
		{
			marker = places.remove(0);
			List<PlaceOverlayItem> cluster = new ArrayList<PlaceOverlayItem>();
			
			for (int i = 0; i < places.size(); i++)
			{				
				if (places.get(i).getVisible())
				{
					mv.getProjection().toPixels(marker.getPoint(), placeLocationPixels1);
					mv.getProjection().toPixels(places.get(i).getPoint(), placeLocationPixels2);
					
					// Use Pythagoras theorem to calculate the distance of this 
					// location from current position
					dx = Math.abs(placeLocationPixels1.x - placeLocationPixels2.x);
					dy = Math.abs(placeLocationPixels1.y - placeLocationPixels2.y);
					
					// distance is the distance in screen points between
					// our current marker and next marker in the list
					distance = Math.sqrt((dx * dx) + (dy * dy));	
					
					//If the distance is close remove and add to cluster				
					if(distance <= 30.0)
					{
						places.get(i).setInCluster(true);
						cluster.add(places.get(i));
						places.remove(i);
						i--;
					}
					else {
						places.get(i).setInCluster(false);
					}
				}
			}

			if (cluster.size() > 0)
	        {
				marker.setInCluster(true);
				cluster.add(marker);
	        	clustered.add(cluster);
	        }		
	        else {
	        	List<PlaceOverlayItem> p = new ArrayList<PlaceOverlayItem>();
	        	marker.setInCluster(false);
	        	p.add(marker);
	        	clustered.add(p);
	        }
		}

		//Before adding overlays we need to define the list of clusters
		 for (List<PlaceOverlayItem> c : clustered)
		 {				 
			 // GeoPoint point = p.getGeoPoint();
			ClusterOverlayItem overlayItem = new ClusterOverlayItem(c);
			clusterOverlay.addOverlay(overlayItem);
		 }
	}
	
	public static Point returnClusterPosition(ClusterOverlayItem cluster, MapView mv)
	{
		Point placeLocationPixels = new Point();
		Point clusterLocation = new Point();
		
		for (int i = 0; i < cluster.getPlace().size(); i++)
		{
			mv.getProjection().toPixels(
				cluster.getPlace().get(i).getPoint(),
				placeLocationPixels
			);
			
			clusterLocation.x += placeLocationPixels.x;
			clusterLocation.y += placeLocationPixels.y;
		}
		
		clusterLocation.x /= cluster.getPlace().size();
		clusterLocation.y /= cluster.getPlace().size();
		
		return clusterLocation;
	}
	
	public static float returnClusterSize(ClusterOverlayItem cluster, MapView mv)
	{
		Point placeLocationPixels = new Point();
		
		float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;		
		        

		for (int i = 0; i < cluster.getPlace().size(); i++)
		{
			mv.getProjection().toPixels(
				cluster.getPlace().get(i).getPoint(),
				placeLocationPixels
			);
			
	        maxX = Math.max(placeLocationPixels.x, maxX);
	        minX = Math.min(placeLocationPixels.x, minX);
	        maxY = Math.max(placeLocationPixels.y, maxY);
	        minY = Math.min(placeLocationPixels.y, minY);

		}
        
        float distance = (float) findDistance(minX, minY, maxX, maxY);

		return distance;
	}
	
	private static double findDistance(float x1, float y1, float x2, float y2)
	{
	    return Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
	}
}