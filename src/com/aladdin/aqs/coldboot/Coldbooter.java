package com.aladdin.aqs.coldboot;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import com.aladdin.aqs.AladdinLiveQueryService;
import com.aladdin.aqs.coldboot.xml.Serializer;

@FunctionalInterface
public interface Coldbooter
{
	void doColdboot(AladdinLiveQueryService<?> queryService) throws Exception;

	public static com.aladdin.aqs.coldboot.ColdbootPolicy buildPolicyFromConfigFile(String coldbootConfigFile) throws IOException, JAXBException,
			ClassNotFoundException
	{
		return Serializer.deserialize(new File(coldbootConfigFile));
	}

	public static Coldbooter buildColdbooterFromConfigString(String coldbootConfig) throws JAXBException, ClassNotFoundException, IOException
	{
		return Serializer.deserialize(coldbootConfig);
	}

	public static String combineLines(Collection<String> strs)
	{
		StringBuilder sb = new StringBuilder();

		for(String str : strs)
		{
			if(sb.length() > 0)
				sb.append('\n');

			sb.append(str);
		}

		return sb.toString();
	}
}