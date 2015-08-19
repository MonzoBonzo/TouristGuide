package aston.cs3040.touristguide;

public class Config 
{
	public static final String RADIUS = "350";	
	/** Corporation street before the bend, in front of pallasades*/
	public static double LATTIDUDE = 52.48637;
	public static double LONGITUDE = -1.888297;
	public static final String LOCATION = "52.479082,-1.897821";
	
	public static final String PLACE_MUSEUM = "Museum";
	public static final String PLACE_FOOD_AND_DRINK = "Food";
	public static final String PLACE_ENTERTAINMENT = "Entertainment";
	public static final String PLACE_LANDMARKS = "Landmarks";
	public static final String PLACE_SHOPPING = "Shopping";
	public static final String PLACE_RESTAURANTS = "Restaurants";
	
	public static void setLocation(double lat, double lon)
	{
		LATTIDUDE = lat;
		LONGITUDE = lon;
	}
}