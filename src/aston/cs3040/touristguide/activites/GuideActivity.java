package aston.cs3040.touristguide.activites;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;
import aston.cs3040.touristguide.R;
import aston.cs3040.touristguide.activites.addplaces.AddPlaceChoice;
import aston.cs3040.touristguide.activites.map.LocationsMapView;

public class GuideActivity extends Activity implements OnGesturePerformedListener
{
	private ViewFlipper flipper;
	private final String START_GESTURE = "right_swipe";
	private final String BACK_GESTURE = "left_swipe";
	private GestureLibrary gestureLib;
	private int viewSize = 3;
	private int pos = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //Get reference to main menu
        setContentView(R.layout.guide);
        
        flipper = (ViewFlipper) this.findViewById(R.id.flipper);
        
		GestureOverlayView gestureView = (GestureOverlayView) this.findViewById(R.id.gestureSlider);
		gestureView.setGestureVisible(false);
		gestureView.addOnGesturePerformedListener(this);
		
		gestureLib = GestureLibraries.fromRawResource(this,R.raw.gestures);		
		
		if (!gestureLib.load()) 
		{
			// If for some reason gesture didn't load
			// Maybe add a button or timer to next activity
		}     
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Tourist Guide Welcome");
		dialog.setMessage("Welcome to the interactive Tourist Guide, use left and right sliding gestures to interact with the guide.");
		dialog.show();
	}
	
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture)
	{
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);		
		processGesturePerformed(predictions);
	}
	
	private void processGesturePerformed(List<Prediction> gestures)
	{
		if(isValidGesture(gestures.size()))
		{
			//Get the first and highest prediction
			Prediction prediction = gestures.get(0);
			
			if (prediction.score > 1.0)
			{
				if(prediction.name.equals(BACK_GESTURE))
				{
					if ((pos + 1) < viewSize) 
					{
						pos++;
						flipper.setInAnimation(inFromRightAnimation());
						flipper.setOutAnimation(outToLeftAnimation());
						flipper.showNext();
					}
				}
				
				else if (prediction.name.equals(START_GESTURE))
				{
					if (pos > 0) 
					{
						pos--;
						flipper.setInAnimation(inFromLeftAnimation());
				        flipper.setOutAnimation(outToRightAnimation());
				        flipper.showPrevious(); 
					}
				}
			}
		}
	}
	
	private boolean isValidGesture(int size)
	{
		if(size > 0) 
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	private Animation inFromRightAnimation()
	{
		Animation inFromRight = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,
			+1.0f,
			Animation.RELATIVE_TO_PARENT, 
			0.0f,
			Animation.RELATIVE_TO_PARENT,
			0.0f, 
			Animation.RELATIVE_TO_PARENT,
			0.0f
		);
		
		inFromRight.setDuration(500);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		
		return inFromRight;
	}
	
	private Animation outToLeftAnimation()
	{
		Animation outtoLeft = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,
			0.0f,
			Animation.RELATIVE_TO_PARENT,
			-1.0f,
			Animation.RELATIVE_TO_PARENT,
			0.0f,
			Animation.RELATIVE_TO_PARENT,
			0.0f
		);
		
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		
		return outtoLeft;
	}

	private Animation inFromLeftAnimation() 
	{
		Animation inFromLeft = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,
			-1.0f,
			Animation.RELATIVE_TO_PARENT,
			0.0f,
			Animation.RELATIVE_TO_PARENT,
			0.0f,
			Animation.RELATIVE_TO_PARENT,
			0.0f
		);
	
		inFromLeft.setDuration(500);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
	
		return inFromLeft;
	}
	
	private Animation outToRightAnimation()
	{
		Animation outtoRight = new TranslateAnimation(
			Animation.RELATIVE_TO_PARENT,
			0.0f,
			Animation.RELATIVE_TO_PARENT, 
			+1.0f,
			Animation.RELATIVE_TO_PARENT, 
			0.0f,
			Animation.RELATIVE_TO_PARENT,
			0.0f
		);
		
		outtoRight.setDuration(500);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		
		return outtoRight;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.guide_menu_group, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.guide_menu_home:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), TouristGuideMainMenu.class));		           
	            return true;
	        case R.id.guide_menu_explore:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), LocationsMapView.class));			           
	            return true;
	        case R.id.guide_menu_add:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), AddPlaceChoice.class));		            
	            return true;
	        case R.id.guide_menu_game:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), GeoTagActivity.class));		           
	            return true;
	        case R.id.guide_menu_history:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), HistoryActivity.class));	            
	            return true;
	        case R.id.guide_menu_settings:
	        	finish();
	        	startActivity(new Intent(getApplicationContext(), SettingsActivity.class));		           
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}