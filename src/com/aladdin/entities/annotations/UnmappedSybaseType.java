package com.aladdin.entities.annotations;

public class UnmappedSybaseType extends RuntimeException
{
	private static final long serialVersionUID = -1543332171102620178L;

	public UnmappedSybaseType(int columnType)
	{
		super("The Sybase column type: " + columnType + " could not be mapped!");
	}
}