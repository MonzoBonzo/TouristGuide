// TODO Implement (Button Icons, Intent, Style) for Main Menu of the application. 
package aston.cs3040.touristguide.activites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import aston.cs3040.touristguide.Const;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.addplaces.AddPlaceChoice;
import aston.cs3040.touristguide.activites.map.LocationsMapView;

public class TouristGuideMainMenu extends Activity implements OnClickListener
{	
	private LinearLayout section1;
	private LinearLayout section1Explore;
	private LinearLayout section1AddSite;
	
	private LinearLayout section2;
	private LinearLayout section2Profile;
	private LinearLayout section2Game;
	
	private LinearLayout section3;
	private LinearLayout section3History;
	private LinearLayout section3Settings;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //Get reference to main menu
        setContentView(R.layout.mainmenu);
        
        setupView();
        
        //Get the Image icon buttons
        ImageView exploreView = (ImageView) this.findViewById(R.id.explore);
        ImageView addsiteView = (ImageView) this.findViewById(R.id.add_site);
        ImageView profileView = (ImageView) this.findViewById(R.id.profile);
        ImageView gameView    = (ImageView) this.findViewById(R.id.game);
        ImageView historyView = (ImageView) this.findViewById(R.id.history);
        ImageView settingsView = (ImageView) this.findViewById(R.id.settings);
        
        exploreView.setOnClickListener(this);
        addsiteView.setOnClickListener(this);
        profileView.setOnClickListener(this);
        gameView.setOnClickListener(this);
        historyView.setOnClickListener(this);
        settingsView.setOnClickListener(this);
    }
    
	public void onClick(View view) 
	{		
		switch (view.getId())
		{
			
		//Explore button
			case R.id.explore:
				//Toast.makeText(this, "Explore", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), LocationsMapView.class));
				break;
			//Add a new site button
			case R.id.add_site:
				//Toast.makeText(this, "Add Site", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), AddPlaceChoice.class));
				break;
			//Profile button
			case R.id.profile:
				//Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), GuideActivity.class));				
				break;
			//Geo-Tag button
			case R.id.game:
				//Toast.makeText(this, "Geo-Tagger Game", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), GeoTagActivity.class));
				break;
			//History button
			case R.id.history:
				//Toast.makeText(this, "History", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
				break;
			//System Settings button
			case R.id.settings:
				//Toast.makeText(this, "System Settings", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
				break;
			default:
				Log.i("TOURIST_GUIDE", "Unknown Button");
				break;
		}
	}
	
	private void setupView()
	{
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
		
		switch (displayMetrics.densityDpi)
		{
			case DisplayMetrics.DENSITY_HIGH:	
				Log.i("Tourist Guide", "High Density Screen");
				changeResolution(
					Const.MENU_BUTTON_HIGH_DPI_WIDTH,
					Const.MENU_BUTTON_HIGH_DPI_HEIGHT
				);
				break;
			
			case DisplayMetrics.DENSITY_MEDIUM:		
				Log.i("Tourist Guide", "Medium Density Screen");
				changeResolution(
					Const.MENU_BUTTON_MEDIUM_DPI_WIDTH,
					Const.MENU_BUTTON_MEDIUM_DPI_HEIGHT
				);
				break;
			
			case DisplayMetrics.DENSITY_LOW:	
				Log.i("Tourist Guide", "Low Density Screen");
				changeResolution(
					Const.MENU_BUTTON_LOW_DPI_WIDTH,
					Const.MENU_BUTTON_LOW_DPI_HEIGHT
				);
				break;
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(0,0);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		overridePendingTransition(0,0);
	}

	private void changeResolution(int buttonWidth, int buttonHeight)
	{

        this.section1 = (LinearLayout) this.findViewById(R.id.section1);
        this.section1.setLayoutParams(new LinearLayout.LayoutParams(
        	LayoutParams.FILL_PARENT, 
        	buttonHeight
        ));
        
        this.section1Explore = (LinearLayout) this.findViewById(R.id.section1Explore);
        this.section1Explore.setLayoutParams(new LinearLayout.LayoutParams(
        	buttonWidth,
        	buttonHeight
        ));
        
        this.section1AddSite = (LinearLayout) this.findViewById(R.id.section1Add);
        this.section1AddSite.setLayoutParams(new LinearLayout.LayoutParams(
        	buttonWidth,
        	buttonHeight
        ));
        
        this.section2 = (LinearLayout) this.findViewById(R.id.section2);
        this.section2.setLayoutParams(new LinearLayout.LayoutParams(
        	LayoutParams.FILL_PARENT, 
        	buttonHeight
        ));
        
        this.section2Profile = (LinearLayout) this.findViewById(R.id.section2Profile);
        this.section2Profile.setLayoutParams(new LinearLayout.LayoutParams(
        	buttonWidth,
        	buttonHeight
        ));
        
        this.section2Game = (LinearLayout) this.findViewById(R.id.section2Game);
        this.section2Game.setLayoutParams(new LinearLayout.LayoutParams(
        	buttonWidth,
        	buttonHeight
        ));
        
        this.section3 = (LinearLayout) this.findViewById(R.id.section3);
        this.section3.setLayoutParams(new LinearLayout.LayoutParams(
        	LayoutParams.FILL_PARENT, 
        	buttonHeight
        ));
        
        this.section3History = (LinearLayout) this.findViewById(R.id.section3History);
        this.section3History.setLayoutParams(new LinearLayout.LayoutParams(
        	buttonWidth,
        	buttonHeight
        ));
        
        this.section3Settings = (LinearLayout) this.findViewById(R.id.section3Settings);
        this.section3Settings.setLayoutParams(new LinearLayout.LayoutParams(
        	buttonWidth,
        	buttonHeight
        ));
	}
}