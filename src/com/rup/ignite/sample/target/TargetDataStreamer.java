package com.rup.ignite.sample.target;

import java.util.List;


import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import com.rup.ignite.sample.target.model.AllocTargetMaster;

public class TargetDataStreamer {

	public static void main(String[] args) throws IgniteException {
		Ignition.setClientMode(true);
		

		try (Ignite ignite = Ignition.start("config/example-ignite.xml")) {
		    
		    try (IgniteDataStreamer<Integer, AllocTargetMaster> stmr = ignite.dataStreamer(TargetCacheConfig.targetMasterCache().getName())) {
		        stmr.allowOverwrite(true);
		     
		        for (Integer i = 1; i < 10000; i++) {
		            AllocTargetMaster master = new AllocTargetMaster();
		            master.setAllocTargetMasterId(i);
		            master.setAllocTargetName("Target Name " + i);
		            stmr.addData(i, master);
		        }
		    }
			
		}

	}
}
