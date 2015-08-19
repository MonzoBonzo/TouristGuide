package aston.cs3040.touristguide.db;

import java.io.Serializable;

public class PlaceDB implements Serializable
{
	private static final long serialVersionUID = 8598409634061313128L;
	
	private String name;
	private String vicinity;
	private String formatted_address;
	private String formatted_phone_number;
	private String types;
	private String website;
	private String location;
	private Double latitude;
	private Double longitude;
	
	public void setPlaceDB(
		String name,
		String vicinity,
		String address,
		String tel,
		String types,
		String website,
		String location,
		Double latitude,
		Double longitude		
	)
	{
		this.name = name;
		this.vicinity = vicinity;
		this.formatted_address = address;
		this.formatted_phone_number = tel;
		this.types = types;
		this.website = website;
		this.location = location;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString()
	{
		return name + "\n" + vicinity + "\n" + types + "\n" + location;
	}	
	
	public String getName()
	{
		return this.name;
	}
	
	public String getVicinity()
	{
		return this.vicinity;
	}
	
	public String getAddress()
	{
		return this.formatted_address;
	}
	
	public String getNumber()
	{
		return this.formatted_phone_number;
	}
	
	public String getTypes()
	{
		return this.types;
	}
	
	public String getWebsite()
	{
		return this.website;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public Double getLatitude()
	{
		return this.latitude;
	}
	
	public Double getLongitude()
	{
		return this.longitude;
	}
}