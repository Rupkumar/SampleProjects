package com.aladdin.aqs.coldboot.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Serializer
{
	private final static JAXBContext CONTEXT;

	private final static Unmarshaller UNMARSHALLER;

	static
	{
		try
		{
			CONTEXT = JAXBContext.newInstance(ColdbootPolicy.class);
			UNMARSHALLER = CONTEXT.createUnmarshaller();
		}
		catch(JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static com.aladdin.aqs.coldboot.ColdbootPolicy deserialize(File file) throws JAXBException, ClassNotFoundException, IOException
	{
		return new com.aladdin.aqs.coldboot.ColdbootPolicy((ColdbootPolicy) UNMARSHALLER.unmarshal(file));
	}

	public static com.aladdin.aqs.coldboot.ColdbootPolicy deserialize(String xml) throws JAXBException, ClassNotFoundException, IOException
	{
		return new com.aladdin.aqs.coldboot.ColdbootPolicy((ColdbootPolicy) UNMARSHALLER.unmarshal(new StringReader(xml)));
	}
}
