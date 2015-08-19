package aston.cs3040.touristguide.activites.addplaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.GeoTagActivity;
import aston.cs3040.touristguide.activites.GuideActivity;
import aston.cs3040.touristguide.activites.HistoryActivity;
import aston.cs3040.touristguide.activites.SettingsActivity;
import aston.cs3040.touristguide.activites.TouristGuideMainMenu;
import aston.cs3040.touristguide.activites.map.LocationsMapView;

public class AddPlaceChoice extends Activity  implements OnClickListener
{
	Button current, history;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.addchoice);
        
        current = (Button) this.findViewById(R.id.buttonCurrent);
        history = (Button) this.findViewById(R.id.buttonHistory);
        
        current.setOnClickListener(this);
        history.setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.add_menu_group, menu);
	    return true;
	}
	
	public void onClick(View v)
	{
		if (v.getId() == R.id.buttonCurrent)
			startActivity(new Intent(getApplicationContext(), AddPlaceActivity.class));
		else if (v.getId() == R.id.buttonHistory)
			startActivity(new Intent(getApplicationContext(), AddPlaceHistory.class));	
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
}