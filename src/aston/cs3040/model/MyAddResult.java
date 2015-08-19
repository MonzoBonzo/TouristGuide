package aston.cs3040.model;

import com.google.api.client.util.Key;

public class MyAddResult
{	
	@Key
	private String id;

	@Key
	private String reference;
	
	@Key
	private String status;
	
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}
	
	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}