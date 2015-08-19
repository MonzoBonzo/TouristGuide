// TODO Implement the MAMMOTH map task for the application.
package aston.cs3040.touristguide.activites.map;

import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import aston.cs3040.model.Place;
import aston.cs3040.model.PlaceDetail;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.GeoTagActivity;
import aston.cs3040.touristguide.activites.GuideActivity;
import aston.cs3040.touristguide.activites.HistoryActivity;
import aston.cs3040.touristguide.activites.SettingsActivity;
import aston.cs3040.touristguide.activites.TouristGuideMainMenu;
import aston.cs3040.touristguide.activites.addplaces.AddPlaceChoice;
import aston.cs3040.touristguide.db.PlaceDB;
import aston.cs3040.touristguide.db.PlaceSQLiteHelper;
import aston.cs3040.touristguide.service.TouristHttpTransport;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;

public class LocationInformationView extends Activity implements OnClickListener
{	
    private ImageView image;
    private TextView addressBar, categorybar, locationInfo, tel, website;
    private PlaceDetail placeDetail;
    private PlaceDB place;
    private PlaceSQLiteHelper dbPlace;
    private Bundle extras;
    private String placeType;
    private String number;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.infoview);
        setupViews();
        
		Intent i = this.getIntent();
		extras = i.getExtras();
		
		placeType = extras.getString("type");
		
		if (placeType.equalsIgnoreCase("explore"))
		{
			Place place = extras != null ? (Place) extras.getSerializable("place") : null;
			
			HttpRequestFactory hrf = TouristHttpTransport.createRequestFactory();
			HttpRequest request;
			Resources res = getResources();
			
			try {
				request = hrf.buildGetRequest(new GenericUrl(res.getString(R.string.places_details_url)));
				request.getUrl().put("key", res.getString(R.string.google_places_key));
				request.getUrl().put("reference", place.getReference());
				request.getUrl().put("sensor", "false");
				
				//Log.i("Tourist Guide", "Request resp: " + request.execute().parseAsString());
				placeDetail = request.execute().parseAs(PlaceDetail.class);
				Log.d("Tourist Guide", "Request resp: " + placeDetail.getResult().getUrl());
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}	
			
			renderViews();
			addPlaceToDB();
		}
		else if (placeType.equalsIgnoreCase("history"))
		{
			place = extras != null ? (PlaceDB) extras.getSerializable("placeDB") : null;
			renderPlaceFromHistory();
		}
    }
    
    private void setupViews()
    {
    	image        = (ImageView) this.findViewById(R.id.image);    	
    	addressBar   = (TextView)  this.findViewById(R.id.addressbar); 
    	addressBar.setSingleLine(true);
    	addressBar.setClickable(false);
    	addressBar.setTextColor(addressBar.getCurrentTextColor());
    	addressBar.setSelected(true);
    	addressBar.setEllipsize(TruncateAt.MARQUEE);
    	addressBar.setMarqueeRepeatLimit(-1);
    	
    	categorybar  = (TextView)  this.findViewById(R.id.categoryinfo);
    	locationInfo = (TextView)  this.findViewById(R.id.locationInfo); 	
    	tel          = (TextView)  this.findViewById(R.id.textTel);
    	website      = (TextView)  this.findViewById(R.id.textWebsite);
    }
    
    private void renderPlaceFromHistory()
    {
		addressBar.setText(place.getAddress());
		categorybar.setText(place.getTypes());
		locationInfo.setText(place.toString());
		
    	if (place.getWebsite() != null)
    	{
    		website.setOnClickListener(this);
    		website.setText(place.getWebsite());
    	}
    	
    	if (place.getNumber() != null)
    	{
    		tel.setOnClickListener(this);
    		tel.setText(place.getNumber());
    	}		
    }
    
    private void renderViews()
    {
		addressBar.setText(placeDetail.getResult().getFormatted_address());
		categorybar.setText(placeDetail.getResult().getTypes().get(0));
		locationInfo.setText(placeDetail.getResult().toString());
		
    	if (placeDetail.getResult().getWebsite() != null)
    	{
    		website.setOnClickListener(this);
    		website.setText(placeDetail.getResult().getWebsite());
    	}
    	
    	if (placeDetail.getResult().getFormatted_phone_number() != null)
    	{
    		tel.setOnClickListener(this);
    		tel.setText(placeDetail.getResult().getFormatted_phone_number());
    	}		
    }
    
    private void addPlaceToDB()
    {
    	String location = placeDetail.getResult().getGeometry().getLocation().getLat() + "," + placeDetail.getResult().getGeometry().getLocation().getLng();
    	ContentValues cv = new ContentValues();
    	
    	cv.put(PlaceSQLiteHelper.PLACE_NAME, returnValidName(placeDetail.getResult().getName()));
    	cv.put(PlaceSQLiteHelper.PLACE_VICINITY, placeDetail.getResult().getVicinity());
    	cv.put(PlaceSQLiteHelper.PLACE_ADDRESS, placeDetail.getResult().getFormatted_address());
    	cv.put(PlaceSQLiteHelper.PLACE_TYPES, placeDetail.getResult().types.get(0));
    	cv.put(PlaceSQLiteHelper.PLACE_LOCATION, location);
    	cv.put(PlaceSQLiteHelper.PLACE_TEL, placeDetail.getResult().getFormatted_phone_number());
    	cv.put(PlaceSQLiteHelper.PLACE_WEBSITE, placeDetail.getResult().getWebsite());
    	cv.put(PlaceSQLiteHelper.PLACE_LAT, placeDetail.getResult().getGeometry().getLocation().getLat());
    	cv.put(PlaceSQLiteHelper.PLACE_LON, placeDetail.getResult().getGeometry().getLocation().getLng());
    	
		dbPlace = new PlaceSQLiteHelper(getApplicationContext());
		long i = dbPlace.addVisitedPlace(cv);
		Log.i("Tourist Guide", "INSERT RESULT: " + Long.toString(i));
		dbPlace.close();
    }

	public void onClick(View v)
	{		
		switch(v.getId())
		{
			case R.id.textTel:
		        Intent i = new Intent(Intent.ACTION_DIAL);
		        
				if (placeType.equalsIgnoreCase("explore"))
					number = "tel:" + placeDetail.getResult().getFormatted_phone_number();
				else 
					number = "tel:" + place.getNumber();
				
		        i.setData(Uri.parse(number));
		        startActivity(i);			
			break;
			case R.id.textWebsite:
				if (placeType.equalsIgnoreCase("explore"))
					startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(placeDetail.getResult().getWebsite())));
				else 
					startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(place.getWebsite())));
			break;
		}		
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
	
	private String returnValidName(String placeName)
	{
		if (placeName.contains("'"))
		{
			return placeName = placeName.replace("'", "");
		}
		else {
			return placeName;
		}
	}
}