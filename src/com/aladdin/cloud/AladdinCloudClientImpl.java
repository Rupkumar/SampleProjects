package com.aladdin.cloud;

import com.aladdin.database.DatabaseHelper;

class AladdinCloudClientImpl extends AbstractAladdinCloud implements AladdinCloudClient
{
	AladdinCloudClientImpl(String file, DatabaseHelper db)
	{
		super(file, db, true);
	}
}