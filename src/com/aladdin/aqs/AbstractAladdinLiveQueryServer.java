package com.aladdin.aqs;

import java.io.IOException;

import com.aladdin.cloud.AladdinCloudServer;

abstract class AbstractAladdinLiveQueryServer extends AbstractAladdinQueryService<AladdinCloudServer> implements AladdinLiveQueryServer
{
	AbstractAladdinLiveQueryServer(AladdinCloudServer aladdinCloud, String serverName) throws ClassNotFoundException, IOException
	{
		super(aladdinCloud, serverName);
	}
}