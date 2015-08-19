package aston.cs3040.touristguide.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.addplaces.AddPlaceChoice;
import aston.cs3040.touristguide.activites.map.LocationsMapView;

public class GeoTagActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //Get reference to main menu
        setContentView(R.layout.game);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.game_menu_group, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId())
	    {
	        case R.id.game_menu_home:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), TouristGuideMainMenu.class));	           
	            return true;
	        case R.id.game_menu_explore:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), LocationsMapView.class));		           
	            return true;
	        case R.id.game_menu_add:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), AddPlaceChoice.class));	            
	            return true;
	        case R.id.game_menu_guide:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GuideActivity.class));		           
	            return true;
	        case R.id.game_menu_history:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), HistoryActivity.class));	            
	            return true;
	        case R.id.game_menu_settings:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), SettingsActivity.class));			           
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}