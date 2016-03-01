package com.aladdin.sybase.schema.mapper.xml;

import java.io.File;
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
			CONTEXT = JAXBContext.newInstance(SchemaMapping.class);
			UNMARSHALLER = CONTEXT.createUnmarshaller();
		}
		catch(JAXBException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static SchemaMapping deserialize(File file) throws JAXBException
	{
		return (SchemaMapping) UNMARSHALLER.unmarshal(file);
	}

	public static SchemaMapping deserialize(String xml) throws JAXBException
	{
		return (SchemaMapping) UNMARSHALLER.unmarshal(new StringReader(xml));
	}
}
