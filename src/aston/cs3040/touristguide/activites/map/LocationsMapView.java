// TODO Implement the MAMMOTH map task for the application.
package aston.cs3040.touristguide.activites.map;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import aston.cs3040.model.Place;
import aston.cs3040.model.PlaceList;
import aston.cs3040.touristguide.Config;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.GeoTagActivity;
import aston.cs3040.touristguide.activites.GuideActivity;
import aston.cs3040.touristguide.activites.HistoryActivity;
import aston.cs3040.touristguide.activites.SettingsActivity;
import aston.cs3040.touristguide.activites.TouristGuideMainMenu;
import aston.cs3040.touristguide.activites.addplaces.AddPlaceChoice;
import aston.cs3040.touristguide.maps.ClusterItemisedOverlay;
import aston.cs3040.touristguide.maps.PlaceItemizedOverlay;
import aston.cs3040.touristguide.maps.PlaceOverlayItem;
import aston.cs3040.touristguide.maps.UserLocationOverlayItem;
import aston.cs3040.touristguide.service.TouristHttpTransport;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;

public class LocationsMapView extends MapActivity implements LocationListener, OnClickListener
{	
	private MapView mapView;
	private MapController mapController;
	private GeoPoint gp;
	private LocationManager locationManager;
	private HttpRequestFactory hrf;
	private String location;
	private Map<String,PlaceItemizedOverlay> placeTypes;
	private PlaceList places;
	private PlaceItemizedOverlay itemizedOverlay;
	private Resources res;
	private HttpRequest request;
	private UserLocationOverlayItem userLoc;
	private ClusterItemisedOverlay clusterOverlay;
	private SlidingDrawer filter;
	private ImageView restaurant, food_drink, landmark, museum, shopping, entertainment;
	
	public void onClick(View v) 
	{
		PlaceItemizedOverlay items;
		
		switch(v.getId())
		{
			case R.id.filterRestaurant:
				
				items = placeTypes.get(Config.PLACE_RESTAURANTS);
			
				if(items != null && items.size() > 0)
				{				
					for (PlaceOverlayItem place : items.getPlaceOverlay())
					{
						place.setVisible();
					}
					mapView.invalidate();
					
				}
				else {
					Toast.makeText(this,"No " + Config.PLACE_RESTAURANTS + " returned." , Toast.LENGTH_SHORT).show();
				}
				filter.animateClose();
				break;
			case R.id.filterFoodDrink:
				
				items = placeTypes.get(Config.PLACE_FOOD_AND_DRINK);
				
				if(items != null && items.size() > 0)
				{	
					for (PlaceOverlayItem place : items.getPlaceOverlay())
					{
						place.setVisible();
					}
					mapView.invalidate();
					
				}
				else {
					Toast.makeText(this,"No " + Config.PLACE_FOOD_AND_DRINK + " returned." , Toast.LENGTH_SHORT).show();
				}
				filter.animateClose();
				break;
			case R.id.filterLandmark:
				
				items = placeTypes.get(Config.PLACE_LANDMARKS);
				
				if(items != null && items.size() > 0)
				{	
					for (PlaceOverlayItem place : items.getPlaceOverlay())
					{
						place.setVisible();
					}
					mapView.invalidate();
					
				}
				else {
					Toast.makeText(this,"No " + Config.PLACE_LANDMARKS + " returned." , Toast.LENGTH_SHORT).show();
				}
				filter.animateClose();
				break;
			case R.id.filterMusuem:
				
				items = placeTypes.get(Config.PLACE_MUSEUM);
				
				if(items != null && items.size() > 0)
				{	
					for (PlaceOverlayItem place : items.getPlaceOverlay())
					{
						place.setVisible();
					}
					mapView.invalidate();
					
				}
				else {
					Toast.makeText(this,"No " + Config.PLACE_MUSEUM + "'s returned." , Toast.LENGTH_SHORT).show();
				}
				filter.animateClose();
				break;
			case R.id.filterShopping:
				
				items = placeTypes.get(Config.PLACE_SHOPPING);
				
				if(items != null && items.size() > 0)
				{	
					for (PlaceOverlayItem place : items.getPlaceOverlay())
					{
						place.setVisible();
					}
					mapView.invalidate();
					
				}
				else {
					Toast.makeText(this,"No " + Config.PLACE_SHOPPING + " returned." , Toast.LENGTH_SHORT).show();
				}
				filter.animateClose();
				break;
			case R.id.filterEntertainment:
				
				items = placeTypes.get(Config.PLACE_ENTERTAINMENT);
				
				if(items != null && items.size() > 0)
				{	
					for (PlaceOverlayItem place : items.getPlaceOverlay())
					{
						place.setVisible();
					}
					mapView.invalidate();
					
				}
				else {
					Toast.makeText(this,"No " + Config.PLACE_ENTERTAINMENT + " returned." , Toast.LENGTH_SHORT).show();
				}
				filter.animateClose();
				break;
		}		
	} 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);        
        setContentView(R.layout.mapview);
        
        filter        = (SlidingDrawer) this.findViewById(R.id.slidingDrawer1);
        restaurant    = (ImageView)     this.findViewById(R.id.filterRestaurant);
        food_drink    = (ImageView)     this.findViewById(R.id.filterFoodDrink); 
        landmark      = (ImageView)     this.findViewById(R.id.filterLandmark);
        museum        = (ImageView)     this.findViewById(R.id.filterMusuem);
        shopping      = (ImageView)     this.findViewById(R.id.filterShopping);
        entertainment = (ImageView)     this.findViewById(R.id.filterEntertainment);
        
        restaurant.setOnClickListener(this);
        food_drink.setOnClickListener(this);
        landmark.setOnClickListener(this);
        museum.setOnClickListener(this);
        shopping.setOnClickListener(this);
        entertainment.setOnClickListener(this);
       
        
        placeTypes = new HashMap<String, PlaceItemizedOverlay>();
        
        hrf = TouristHttpTransport.createRequestFactory();
		res = getResources();
		
		try 
		{
			request = (HttpRequest) hrf.buildGetRequest(new GenericUrl(res.getString(R.string.places_search_url)));			
		} 
		catch (NotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
                
        mapView = (MapView) this.findViewById(R.id.map);
        mapView.setSatellite(false);
        mapView.setTraffic(false);
        //mapView.setBuiltInZoomControls(true);
        
        //gp = new GeoPoint(
        	//(int) (Config.LATTIDUDE * 1E6), 
        	//(int) (Config.LONGITUDE * 1E6)
        //);
        
        userLoc = new UserLocationOverlayItem(getApplicationContext(), mapView);
		// get a handle on the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        
		// get a handle on the location manager
       // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
        	LocationManager.NETWORK_PROVIDER,
        	0,        	
        	0,
        	this
        );
		
        clusterOverlay = new ClusterItemisedOverlay(
    			getResources().getDrawable(R.drawable.black_pushpin),
    			LocationsMapView.this
    		);
        
        
		//UserOverlay mapOverlay = new UserOverlay();
        mapView.getOverlays().add(userLoc);        
        userLoc.enableMyLocation();        

        mapController = mapView.getController();		
        int maxZoom = mapView.getMaxZoomLevel();
		final int initZoom = (int) (0.80 * (double) maxZoom);
		mapController.setZoom(initZoom);
		
        userLoc.runOnFirstFix(new Runnable() {
            public void run() {         
        		mapController.animateTo(userLoc.getMyLocation());
        		mapController.setCenter(userLoc.getMyLocation());
        		
        		location = 
        				locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude() + 
        				"," +
        				locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
        		getLandmarks();
        		getEntertainment();
        		getShopping();
        		getMuseum();
        		getRestaurants();
        		getFoodDrink();        		      		
        		
        		mapView.getOverlays().add(clusterOverlay);  
        		mapView.postInvalidate();
        		
        		Config.setLocation(
            		locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(),
            		locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude()
            	);
            }
        });
       
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Map Welcome");
		dialog.setMessage("Welcome, use the sliding drawer to adjust the types of places that you want to view.");
		dialog.show();
    }

	private void getRestaurants()
    {    	
		try 
		{
			request.getUrl().put("key", res.getString(R.string.google_places_key));
			request.getUrl().put("location", location);
			request.getUrl().put("radius", Config.RADIUS);
			request.getUrl().put("types", "restaurant");
			request.getUrl().put("sensor", false);
			request.getUrl().put("keyword", "restaurant");

			places = request.execute().parseAs(PlaceList.class);			
			renderPlaces(R.drawable.magenta_pushpin, places, Config.PLACE_RESTAURANTS);		
			
		} catch (IOException e) {
			Log.e("Tourist Guide", e.getMessage());
		}		
    }
    
    private void getFoodDrink()
    {    	
    	try 
		{
			request.getUrl().put("key", res.getString(R.string.google_places_key));
			request.getUrl().put("location", location);
			request.getUrl().put("radius", Config.RADIUS);
			//request.getUrl().put("types", "bakery|food|bar|cafe|meal_takeaway");
			request.getUrl().put("types", "bakery|bar|cafe|meal_takeaway");
			request.getUrl().put("sensor", false);

			places = request.execute().parseAs(PlaceList.class);	
			renderPlaces(R.drawable.red_pushpin, places, Config.PLACE_FOOD_AND_DRINK);	
			
		} catch (IOException e) {
			Log.e("Tourist Guide", e.getMessage());
		}		
    }
    
    private void getMuseum()
    {
		try 
		{
			request.getUrl().put("key", res.getString(R.string.google_places_key));
			request.getUrl().put("location", location);
			request.getUrl().put("radius", Config.RADIUS);
			request.getUrl().put("types", "museum|art_gallery");
			request.getUrl().put("sensor", false);

			places = request.execute().parseAs(PlaceList.class);	
			renderPlaces(R.drawable.cyan_pushpin, places, Config.PLACE_MUSEUM);	
			
		} catch (IOException e) {
			Log.e("Tourist Guide", e.getMessage());
		}		
    }
    
    private void getShopping()
    {
    	try 
		{
			request.getUrl().put("key", res.getString(R.string.google_places_key));
			request.getUrl().put("location", location);
			request.getUrl().put("radius", Config.RADIUS);
			request.getUrl().put("types", "shopping_mall|department_store");
			request.getUrl().put("sensor", false);

			places = request.execute().parseAs(PlaceList.class);
			renderPlaces(R.drawable.blue_pushpin, places, Config.PLACE_SHOPPING);	
			
		} catch (IOException e) {
			Log.e("Tourist Guide", e.getMessage());
		}		
    }
    
    private void getEntertainment()
    {
		try 
		{
			request.getUrl().put("key", res.getString(R.string.google_places_key));
			request.getUrl().put("location", location);
			request.getUrl().put("radius", Config.RADIUS);
			request.getUrl().put("types", "casino|movie_theater|bowling_alley");
			request.getUrl().put("sensor", false);

			places = request.execute().parseAs(PlaceList.class);
			renderPlaces(R.drawable.green_pushpin, places, Config.PLACE_ENTERTAINMENT);	
			
		} catch (IOException e) {
			Log.e("Tourist Guide", e.getMessage());
		}		
    }
    
    private void getLandmarks()
    {
		try 
		{
			request.getUrl().put("key", res.getString(R.string.google_places_key));
			request.getUrl().put("location", location);
			request.getUrl().put("radius",  Config.RADIUS);
			request.getUrl().put("types", "church|park|place_of_worship");
			request.getUrl().put("sensor", false);

			places = request.execute().parseAs(PlaceList.class);
			renderPlaces(R.drawable.yellow_pushpin, places, Config.PLACE_LANDMARKS);	
			
		} catch (IOException e) {
			Log.e("Tourist Guide", e.getMessage());
		}
    	
    }
    
    private void renderPlaces(int icon, PlaceList places, String type)
    {
    	if (places != null & places.getResults().size() > 0)
		{
			Log.i("Tourist Guide", "Response: " + places.getResults().size());	
    		List<Overlay> listOfOverlays = mapView.getOverlays();
			
			// this is the drawable which is plotted at the site on the map (a pushpin)
			Drawable drawable = this.getResources().getDrawable(icon);
			
			// The itemizedOverlay contains a list of overlays
			itemizedOverlay = new PlaceItemizedOverlay(drawable, this);
			// set the centre location
			itemizedOverlay.setCenter(userLoc.getMyLocation());
			
			// add all the places to the itemizedOverlay
			for (Place p : places.getResults()) 
			{
				// GeoPoint point = p.getGeoPoint();
				PlaceOverlayItem overlayItem = new PlaceOverlayItem(p);
				itemizedOverlay.addOverlay(overlayItem);
			}
			// add our itemizedOverlay to the overlays for this map view
			placeTypes.put(type, itemizedOverlay);
			listOfOverlays.add(itemizedOverlay);    
    		clusterOverlay.setItems(itemizedOverlay);
    		mapView.postInvalidate();
		}
    	else {
    		Log.i("Tourist Guide", "Response: Nothing found in area");
    		//Toast.makeText(this.getApplicationContext(), "No " + type + "'s Found.", Toast.LENGTH_SHORT).show();
    	}
    }
    
    @Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.maps_menu_group, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId())
	    {
	        case R.id.map_menu_home:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), TouristGuideMainMenu.class));	        	
	            return true;
	        case R.id.map_menu_add:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), AddPlaceChoice.class));	        	
	            return true;
	        case R.id.map_menu_guide:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GuideActivity.class));	        	
	            return true;
	        case R.id.map_menu_game:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GeoTagActivity.class));	        	
	            return true;
	        case R.id.map_menu_history:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), HistoryActivity.class));	        	
	            return true;
	        case R.id.map_menu_settings:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), SettingsActivity.class));	        	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void onLocationChanged(Location location)
	{			
		/*
		int lat = (int) (location.getLatitude()*1e6);
		int lng = (int) (location.getLongitude()*1e6);
		GeoPoint point = new GeoPoint(lat, lng);
		mapController.animateTo(point);
		mapController.setCenter(point);      		
		
		getShopping();
		*/
	}

	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub		
	}

	public void onProviderEnabled(String provider) 
	{
		// TODO Auto-generated method stub		
	}

	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub		
	}
}