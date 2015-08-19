package aston.cs3040.touristguide.maps;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import aston.cs3040.model.Place;
import aston.cs3040.touristguide.activites.map.LocationInformationView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class PlaceItemizedOverlay extends ItemizedOverlay<PlaceOverlayItem> 
{
	private List<PlaceOverlayItem> items;
	private List<PlaceOverlayItem> clusterItems;
	private Context context;
	private GeoPoint center;
	private Bitmap bitmap;
	private boolean showOffScreen;
	private static final int HALF_ICON_HEIGHT = 16;
	private static final int ICON_HEIGHT = HALF_ICON_HEIGHT * 2;

	public PlaceItemizedOverlay(Drawable defaultMarker)
	{
		super(boundCenterBottom(defaultMarker));
		init(defaultMarker, null);
	}

	public PlaceItemizedOverlay(Drawable defaultMarker, Context context)
	{
		super(defaultMarker);
		init(defaultMarker, context);
	}

	private void init(Drawable defaultMarker, Context context)
	{
		this.context = context;
		items = new ArrayList<PlaceOverlayItem>();
		clusterItems = new ArrayList<PlaceOverlayItem>();
		bitmap = ((BitmapDrawable) defaultMarker).getBitmap();
		showOffScreen = true;
	}

	/**
	 * Add an overlay to the list.
	 * 
	 * @param overlay
	 *            The overlay to be added
	 */
	public void addOverlay(PlaceOverlayItem overlay)
	{
		items.add(overlay);
		clusterItems.add(overlay);
		this.populate();
	}
	
	public List<PlaceOverlayItem> getPlaceOverlay()
	{		
		return new ArrayList<PlaceOverlayItem>(clusterItems);
	}

	@Override
	protected PlaceOverlayItem createItem(int i)
	{
		return items.get(i);
	}

	@Override
	public int size()
	{
		return items.size();
	}

	/**
	 * The onTab handler for when we tap on one of the icons on the screen.
	 * 
	 * @param index
	 *            The index in the list of the icon that was tapped
	 */
	@Override
	protected boolean onTap(int index)
	{	
		PlaceOverlayItem item = items.get(index);
		
		if (item != null && !item.getInCluster() && item.getVisible())
		{
			Place place = item.getPlace();
			Intent i = new Intent(context, LocationInformationView.class);
			i.putExtra("type", "explore");
			i.putExtra("place", place);
			context.startActivity(i);
			return true;
		}
		else 
		{
			return super.onTap(index);
		}	
	}

	public void setCenter(GeoPoint center)
	{
		this.center = center;
	}

	/**
	 * Draw all the overlays in the list.
	 */
	public void draw(Canvas canvas, MapView mapview, boolean b)
	{
		//Get the map centre position
		Point ourLocationPixels = new Point();
		mapview.getProjection().toPixels(mapview.getMapCenter(), ourLocationPixels);
		
		// set up a paint for the halos
		Paint circlePaint = new Paint();
		circlePaint.setColor(0x55ff0000);
		circlePaint.setStyle(Paint.Style.STROKE);
		
		// find out the width and height of the view
		// Note that our position is in the center of the view
		int width = mapview.getWidth();
		int height = mapview.getHeight();
		
		float radius = (float) mapview.getZoomLevel() / mapview.getMaxZoomLevel();
		
		// now process each overlay in the list
		for (PlaceOverlayItem place : this.items)
		{
			Point placeLocationPixels = new Point();
			mapview.getProjection().toPixels(place.getPoint(), placeLocationPixels);
			
			// check if this item is off screen or not
			if (placeLocationPixels.x    > (ourLocationPixels.x + (width/2))
				|| placeLocationPixels.x + (ICON_HEIGHT) < (ourLocationPixels.x - (width/2))
				|| placeLocationPixels.y > (ourLocationPixels.y + (height/2))
				|| placeLocationPixels.y + (ICON_HEIGHT) < (ourLocationPixels.y - (height/2))) 
			{
				// placeLocationPixels is offscreen
				if(showOffScreen && place.getVisible())
				{
					// Use Pythagoras theorem to calculate the distance of this 
					// location from current position
					double dx = Math.abs((placeLocationPixels.x - ourLocationPixels.x));
					double dy = Math.abs((placeLocationPixels.y - ourLocationPixels.y));
					
					// The distance in screen points between
					// our current position and the location we are drawing

					double ox = dx - ((width / 2) - (width / 100) * 5);   //padding
					double oy = dy - ((height / 2) - (height / 100) * 5); //padding
					
					if (ox < 0) ox = 0;
					if (oy < 0) oy = 0;
					
					double size = Math.sqrt((ox*ox) + (oy*oy));

					// draw a circle around this item centred at its location				
					drawCircle(placeLocationPixels, (float)size, circlePaint, canvas);	
				}
			} 
			else 
			{
				//Place is not in a cluster and place type is selected
				if (!place.getInCluster() && place.getVisible())
				{
					// the location is on the screen
					// so just draw the pushpin
					canvas.drawBitmap(
						bitmap, 
						placeLocationPixels.x,
						placeLocationPixels.y, 
						null
					);					
				}
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) 
	{
		mapView.postInvalidate();
		return super.onTouchEvent(event, mapView);
	}

	/*
	 * Draw a circle centred at the given location with the given radius.
	 * Uses the paint provided and drawn on the given canvas.
	 */
	private void drawCircle(Point location, float radius, Paint paint, Canvas canvas)
	{
		// set the width of the line
		paint.setStrokeWidth((float) 3.0);
		// draw a circle radius 100 pixels
		canvas.drawCircle(location.x, location.y, radius, paint);
	}
}