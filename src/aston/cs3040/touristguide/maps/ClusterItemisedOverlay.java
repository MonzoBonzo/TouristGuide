package aston.cs3040.touristguide.maps;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import aston.cs3040.model.Cluster;
import aston.cs3040.touristguide.activites.map.ClusterViewActivity;
import aston.cs3040.touristguide.managers.ClusterManager;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class ClusterItemisedOverlay extends ItemizedOverlay<ClusterOverlayItem>
{
	private List<PlaceOverlayItem> items;
	private List<ClusterOverlayItem> clusters;
	private Context context;
	private List<PlaceItemizedOverlay> overlay;
	private Paint whiteText;
	private Paint circlePaint;
	
	public ClusterItemisedOverlay(Drawable defaultMarker)
	{
		super(defaultMarker);
		init(defaultMarker, null);
	}
	
	public ClusterItemisedOverlay(Drawable defaultMarker, Context context)
	{
		super(boundCenterBottom(defaultMarker));
		init(defaultMarker, context);
	}
	
	private void init(Drawable defaultMarker, Context context)
	{
		this.context = context;
		clusters = new ArrayList<ClusterOverlayItem>();
		items = new ArrayList<PlaceOverlayItem>();
		overlay = new ArrayList<PlaceItemizedOverlay>();
		
		whiteText = new Paint();
		whiteText.setTextAlign(Paint.Align.CENTER);
		whiteText.setTextSize(8);
		whiteText.setAntiAlias(true);
		whiteText.setARGB(255, 255, 255, 255);
		
		// set up a paint for the circles
		circlePaint = new Paint();
		circlePaint.setColor(0xFFFF0000);
		circlePaint.setStyle(Paint.Style.FILL);
		circlePaint.setAntiAlias(true);
	}

	/**
	 * Add an overlay to the list.
	 * 
	 * @param overlay
	 *            The overlay to be added
	 */
	public void addOverlay(ClusterOverlayItem overlay) {
		clusters.add(overlay);
		setLastFocusedIndex(-1);
		this.populate();
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
		ClusterOverlayItem item = clusters.get(index);
		Cluster cluster = new Cluster();
		
		if (item != null)
		{
			if (item.getPlace().size() > 1)
			{				
				for (PlaceOverlayItem place : item.getPlace())
				{
					cluster.addPlace(place.getPlace());
				}
				
				Intent i = new Intent(context, ClusterViewActivity.class);
				i.putExtra("cluster", cluster);
				i.putExtra("size", item.getPlace().size());
				context.startActivity(i);
				return true;
			}
			else
			{
				return super.onTap(index);
			}
		}
		else 
		{
			return super.onTap(index);
		}		
	}

	/**
	 * Remove an overlay from the list.
	 * 
	 * @param overlay
	 *            The overlay to be removed
	 */
	public void removeOverlay(int i) 
	{
		clusters.remove(i);
		setLastFocusedIndex(-1);
		this.populate();
	}
	
	public void setItems(PlaceItemizedOverlay overlay)
	{
		this.overlay.add(overlay);
		
		for (PlaceOverlayItem place : overlay.getPlaceOverlay())
		{
			this.items.add(place);
		}		
	}
	
	@Override
	protected ClusterOverlayItem createItem(int i)
	{
		if (i < clusters.size())
		{
			return clusters.get(i);
		}
		else 
		{
			return clusters.get(clusters.size() -1);
		}
	}

	@Override
	public int size()
	{
		return clusters.size();
	}
	
	/**
	 * Draw all the overlays in the list.PlaceOverlayItem
	 */
	public void draw(Canvas canvas, MapView mapview, boolean b)
	{
		if (items.size() == 0)
		{
			for (PlaceItemizedOverlay o : overlay)
			{
				for (PlaceOverlayItem place : o.getPlaceOverlay())
				{
					this.items.add(place);
				}
			}
		}

		
		if (clusters.size() > 0)
		{
			//Remove all overlay items
			for (int i=0; i < clusters.size(); i++)
			{	
				removeOverlay(i);
			}
		}		

		ClusterManager.addClusters(this,items, mapview);
		
		
		for (ClusterOverlayItem cluster : this.clusters)
		{			
			if (cluster.getPlace().size() > 1)
			{
				if (isCurrentLocationVisible(cluster, mapview))
				{
					drawCircle(
						ClusterManager.returnClusterPosition(cluster, mapview),
						(float) ClusterManager.returnClusterSize(cluster, mapview), 
						circlePaint,
						canvas
					);
					  
	                // show text to the right of the icon
	                canvas.drawText(Integer.toString(cluster.getPlace().size()),
	                	ClusterManager.returnClusterPosition(cluster, mapview).x,
	                	ClusterManager.returnClusterPosition(cluster, mapview).y,
	                	whiteText
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
		float size = 10;
		// draw a circle
		canvas.drawCircle(
			location.x, 
			location.y, 
			size, 
			paint
		);
	}
	
    private boolean isCurrentLocationVisible(ClusterOverlayItem cluster, MapView mapView)
    {
        Rect currentMapBoundsRect = new Rect();
        Point currentDevicePosition = ClusterManager.returnClusterPosition(cluster, mapView);
        
        mapView.getDrawingRect(currentMapBoundsRect);

        return currentMapBoundsRect.contains(currentDevicePosition.x, currentDevicePosition.y);
    }
}