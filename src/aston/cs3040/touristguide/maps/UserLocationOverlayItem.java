package aston.cs3040.touristguide.maps;

import android.content.Context;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class UserLocationOverlayItem extends MyLocationOverlay
{
	private MapController mc;
	public UserLocationOverlayItem(Context context, MapView map) 
	{
		super(context, map);
		mc = map.getController();
	}

	@Override
	public boolean onTap(GeoPoint p, MapView map)
	{
		if (this.getLastFix() != null)
		{
			mc.animateTo(getMyLocation());
			mc.setCenter(getMyLocation());			
		}
		return super.onTap(p, map);	
	}
}
