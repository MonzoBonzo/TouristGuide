// TODO Implement (Gestures, Intent, Style) for Start Screen of the application.
package aston.cs3040.touristguide.activites;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import aston.cs3040.touristguide.R;

public class TouristGuideSplashActivity extends Activity implements OnGesturePerformedListener
{
	private final String START_GESTURE = "right_swipe";
	private GestureLibrary gestureLib;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.startscreen);
             
		GestureOverlayView gestureView = (GestureOverlayView) this.findViewById(R.id.gesture);
		gestureView.setGestureVisible(false);
		gestureView.addOnGesturePerformedListener(this);
		
		gestureLib = GestureLibraries.fromRawResource(this,R.raw.gestures);		
		
		if (!gestureLib.load()) 
		{
			// If for some reason gesture didn't load
			// Maybe add a button or timer to next activity
		}		
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
				if(prediction.name.equals(START_GESTURE))
				{
					Toast.makeText(this, "Welcome to the Tourist Guide.", Toast.LENGTH_LONG).show();
					startActivity (new Intent(getApplicationContext(), TouristGuideMainMenu.class));
					finish();
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
	
	@Override
	protected void onPause()
	{
		super.onPause();
		overridePendingTransition(0,0);
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();
		overridePendingTransition(0,0);
	}
}