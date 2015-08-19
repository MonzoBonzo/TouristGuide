package aston.cs3040.touristguide.activites.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import aston.cs3040.model.Cluster;
import aston.cs3040.model.Place;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.GeoTagActivity;
import aston.cs3040.touristguide.activites.GuideActivity;
import aston.cs3040.touristguide.activites.HistoryActivity;
import aston.cs3040.touristguide.activites.SettingsActivity;
import aston.cs3040.touristguide.activites.TouristGuideMainMenu;
import aston.cs3040.touristguide.activites.addplaces.AddPlaceChoice;

public class ClusterViewActivity extends Activity implements OnItemClickListener
{
	private Cluster cluster;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		setContentView(R.layout.clusterview);
		
		Intent i = this.getIntent();
		Bundle extras = i.getExtras();
		int size = extras != null ? (int) extras.getInt("size") : null;

		TextView text = (TextView) this.findViewById(R.id.textView2);
		text.setText("Size: " + size);
		
		cluster = extras != null ? (Cluster) extras.getSerializable("cluster") : null;
		
		List<String> places = new ArrayList<String>(cluster.getResults().size());
		
		for (Place place : cluster.getResults())
		{
			places.add(place.toString());
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this,
			R.layout.place_title,
			R.id.placeTitle,
			places
		);
		
		ListView list = (ListView) this.findViewById(R.id.clusterplacelist);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
		Place place = cluster.getResults().get(position);
		Intent i = new Intent(getApplicationContext(), LocationInformationView.class);
		i.putExtra("type", "explore");
		i.putExtra("place", place);
		startActivity(i);
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
}