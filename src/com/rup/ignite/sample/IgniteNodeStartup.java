package com.rup.ignite.sample;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

public class IgniteNodeStartup {

	public static void main(String[] args) throws IgniteException {
		Ignition.start("config/example-ignite.xml");
	}
}
