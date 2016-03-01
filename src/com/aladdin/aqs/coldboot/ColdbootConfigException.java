package com.aladdin.aqs.coldboot;

public class ColdbootConfigException extends RuntimeException
{
	private static final long serialVersionUID = 2317691448799122141L;

	public ColdbootConfigException(String msg)
	{
		super(msg);
	}

	public ColdbootConfigException(String msg, Throwable e)
	{
		super(msg, e);
	}
}