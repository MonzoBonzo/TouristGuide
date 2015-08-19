package aston.cs3040.touristguide.activites.addplaces;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import aston.cs3040.model.MyAddResult;
import aston.cs3040.model.MyDeleteResult;
import aston.cs3040.touristguide.Config;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.GeoTagActivity;
import aston.cs3040.touristguide.activites.GuideActivity;
import aston.cs3040.touristguide.activites.HistoryActivity;
import aston.cs3040.touristguide.activites.SettingsActivity;
import aston.cs3040.touristguide.activites.TouristGuideMainMenu;
import aston.cs3040.touristguide.activites.map.LocationsMapView;
import aston.cs3040.touristguide.db.PlaceDB;
import aston.cs3040.touristguide.service.TouristHttpTransport;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.jackson.JacksonFactory;

public class AddPlaceActivity extends Activity implements OnClickListener, OnItemSelectedListener
{
	private HttpRequestFactory hrf;
	private Resources res;
	private String addPlace;
	private String deletePlace;
	private MyAddResult addResult;
	private MyDeleteResult deleteResult;
	private Button button1, button2;
	private EditText placeName;
	private TextView addressText;
	private Spinner placeTypes;
	private Geocoder coder;
	private List<Address> address;
	private String text;
	private String type = "";
    private PlaceDB place;
    private Bundle extras;
    private String placeType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.addplace);
		
		placeTypes = (Spinner) findViewById(R.id.spinnerPlaceTypes);
		ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(
			this,
			R.array.typeList,
			android.R.layout.simple_spinner_item
		);
		spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		placeTypes.setAdapter(spinAdapter);
		placeTypes.setSelected(false);
		placeTypes.setOnItemSelectedListener(this);
		
		placeName = (EditText) this.findViewById(R.id.nameEditText);		
		
		addressText = (TextView) this.findViewById(R.id.addressText);
		
		button1 = (Button) this.findViewById(R.id.addplacebutton);
		button1.setOnClickListener(this);
		
		button2 = (Button) this.findViewById(R.id.deleteplacebutton);
		button2.setOnClickListener(this);
		
		coder = new Geocoder(getApplicationContext());
		
		Intent i = this.getIntent();
		extras = i.getExtras();
		
		if (extras != null)
		{
			placeType = extras.getString("type");
			
			if (!placeType.equalsIgnoreCase("history"))
			{
			
				try {
					address = coder.getFromLocation(Config.LATTIDUDE, Config.LONGITUDE, 5);
					
					if (address.size() > 0)
					{
						addressText.setText(
							"Address: " + "\n"
							+ address.get(0).getThoroughfare() + ", " 
							+ address.get(0).getSubLocality() + "\n"
							+ address.get(0).getLocality() + ", "
							+ address.get(0).getCountryName() + "\n"
							+ address.get(0).getPostalCode() 
						);
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			else {
				place = extras != null ? (PlaceDB) extras.getSerializable("placeDB") : null;
				
				try {
					address = coder.getFromLocation(place.getLatitude(), place.getLongitude(), 5);
					
					if (address.size() > 0)
					{
						addressText.setText(
							"Address: " + "\n"
							+ address.get(0).getThoroughfare() + ", " 
							+ address.get(0).getSubLocality() + "\n"
							+ address.get(0).getLocality() + ", "
							+ address.get(0).getCountryName() + "\n"
							+ address.get(0).getPostalCode() 
						);
					}
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			try {
				address = coder.getFromLocation(Config.LATTIDUDE, Config.LONGITUDE, 5);
				
				if (address.size() > 0)
				{
					addressText.setText(
						"Address: " + "\n"
						+ address.get(0).getThoroughfare() + ", " 
						+ address.get(0).getSubLocality() + "\n"
						+ address.get(0).getLocality() + ", "
						+ address.get(0).getCountryName() + "\n"
						+ address.get(0).getPostalCode() 
					);
				}
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		res = getResources();
	}
	
	private void addPlace()
	{
		text = placeName.getText().toString();

		if (address.size() > 0 && !text.equalsIgnoreCase("") && !type.equalsIgnoreCase(""))
		{
			addPlace = "{\"location\":{\"lat\":" + address.get(0).getLatitude() + ",\"lng\":" + address.get(0).getLongitude() + "},\"accuracy\":50,\"name\":\"" + text + "\", \"types\": [\"" +  type + "\"], \"language\":\"en\"}";
			hrf = TouristHttpTransport.createRequestFactory();
			try 
			{
				HttpRequest request = hrf.buildPostRequest(
					new GenericUrl(res.getString(R.string.places_add_url)),
					ByteArrayContent.fromString(null, addPlace)
				);
				
				GoogleHeaders headers = new GoogleHeaders();
				headers.setContentType("application/json");
				request.setHeaders(headers);
				
				JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
				request.addParser(parser);
				
				request.getUrl().put("sensor", false);			
				request.getUrl().put("key", res.getString(R.string.google_places_key));
				
				addResult = request.execute().parseAs(MyAddResult.class);
				Toast.makeText(this, text + " has been added.", Toast.LENGTH_SHORT).show();
			}		 
			catch (IOException e)
			{
				Log.e("Tourist Guide", e.getMessage());
			}			
		}
	}
	
	private void deletePlace()
	{
		deletePlace = "{'reference':'" + addResult.getReference() + "'}";
		hrf = TouristHttpTransport.createRequestFactory();
		try 
		{
			HttpRequest request = hrf.buildPostRequest(
				new GenericUrl(res.getString(R.string.places_delete_url)),
				ByteArrayContent.fromString(null, deletePlace)
			);
			request.getUrl().put("key", res.getString(R.string.google_places_key));
			request.getUrl().put("sensor", false);
			
			GoogleHeaders headers = new GoogleHeaders();
			headers.setContentType("application/json");
			request.setHeaders(headers);
			
			JsonHttpParser parser = new JsonHttpParser(new JacksonFactory());
			request.addParser(parser);
			
			deleteResult = request.execute().parseAs(MyDeleteResult.class);
		}		 
		catch (IOException e)
		{
			Log.e("Tourist Guide", e.getMessage());
		}
	}

	public void onClick(View v)
	{
		if(v.getId() == R.id.addplacebutton)
		{
			addPlace();	
			Log.i("Tourist Guide", (addResult != null) ? addResult.getStatus() : "Request Not Sent");			
		}
		else if (v.getId() == R.id.deleteplacebutton)
		{
			deletePlace();	
			Log.i("Tourist Guide", (deleteResult != null) ? deleteResult.getStatus() : "Request Not Sent");
		}		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.add_menu_group, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId())
	    {
	        case R.id.add_menu_home:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), TouristGuideMainMenu.class));	           
	            return true;
	        case R.id.add_menu_explore:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), LocationsMapView.class));			           
	            return true;
	        case R.id.add_menu_guide:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GuideActivity.class));	            
	            return true;
	        case R.id.add_menu_game:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GeoTagActivity.class));		           
	            return true;
	        case R.id.add_menu_history:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), HistoryActivity.class));	            
	            return true;
	        case R.id.add_menu_settings:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), SettingsActivity.class));			           
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		type = parent.getItemAtPosition(pos).toString().trim();		
	}

	public void onNothingSelected(AdapterView<?> parent)
	{
		
	}
}