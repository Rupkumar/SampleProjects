package com.aladdin.cloud;

import com.aladdin.database.DatabaseHelper;

class AladdinCloudServerImpl extends AbstractAladdinCloud implements AladdinCloudServer
{
	AladdinCloudServerImpl(String file, DatabaseHelper db)
	{
		super(file, db, false);
	}
}