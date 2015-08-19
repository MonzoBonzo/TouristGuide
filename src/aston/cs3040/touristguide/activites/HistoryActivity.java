package aston.cs3040.touristguide.activites;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.addplaces.AddPlaceChoice;
import aston.cs3040.touristguide.activites.map.LocationInformationView;
import aston.cs3040.touristguide.activites.map.LocationsMapView;
import aston.cs3040.touristguide.db.PlaceDB;
import aston.cs3040.touristguide.db.PlaceSQLiteHelper;

public class HistoryActivity extends Activity implements OnItemClickListener
{
	private PlaceSQLiteHelper dbPlaces; 
	private List<PlaceDB> placeDBs;
	private List<String> places;
	private Cursor cursor;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //Get reference to main menu
        setContentView(R.layout.history);
        
        places = new ArrayList<String>();
        placeDBs = new ArrayList<PlaceDB>();
        dbPlaces = new PlaceSQLiteHelper(getApplicationContext());
        cursor = dbPlaces.getAllVisitedPlaces();
        
        TextView text = (TextView) this.findViewById(R.id.historyLength);        
        text.setText("Sites Size: " + cursor.getCount());
        
        cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			PlaceDB place = new PlaceDB();
			place.setPlaceDB(
				cursor.getString(1), 
				cursor.getString(2), 
				cursor.getString(3), 
				cursor.getString(6),
				cursor.getString(4), 
				cursor.getString(7), 
				cursor.getString(5),
				cursor.getDouble(8),
				cursor.getDouble(9)				
			);
			
			placeDBs.add(place);
			cursor.moveToNext();
		} 
		
		for (PlaceDB place : placeDBs)
		{
			places.add(place.toString());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this,
			R.layout.place_title,
			R.id.placeTitle,
			places
		);
		
		ListView list = (ListView) this.findViewById(R.id.historyplacelist);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}
	
	@Override
	protected void onDestroy()
	{
		cursor.close();
		dbPlaces.close();
		super.onDestroy();
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
		PlaceDB place = placeDBs.get(position);
		Intent i = new Intent(getApplicationContext(), LocationInformationView.class);
		i.putExtra("type", "history");
		i.putExtra("placeDB", place);
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.history_menu_group, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId())
	    {
	        case R.id.history_menu_home:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), TouristGuideMainMenu.class));	           
	            return true;
	        case R.id.history_menu_explore:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), LocationsMapView.class));		           
	            return true;
	        case R.id.history_menu_add:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), AddPlaceChoice.class));		            
	            return true;
	        case R.id.history_menu_guide:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GuideActivity.class));		           
	            return true;
	        case R.id.history_menu_game:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GeoTagActivity.class));	            
	            return true;
	        case R.id.history_menu_settings:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), SettingsActivity.class));			           
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}