package aston.cs3040.model;

import com.google.api.client.util.Key;

public class MyDeleteResult
{
	@Key
	private String status;
	
	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}
}
