package aston.cs3040.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cluster implements Serializable
{
	private static final long serialVersionUID = -5279715790916200393L;
	
	private List<Place> places;
	
	public Cluster()
	{
		places = new ArrayList<Place>();
	}

	public List<Place> getResults()
	{
		return places;
	}

	public void addPlace(Place places)
	{
		this.places.add(places);
	}

	@Override
	public String toString()
	{
		StringBuilder string = new StringBuilder();
		
		for (Place place : places)
		{
			string.append(place.getName() + " ");
		}
		
		return string.toString();
	}
}