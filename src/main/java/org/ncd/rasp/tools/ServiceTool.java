package org.ncd.rasp.tools;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ServiceTool {
	
	public static String generateId() {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		String id = ft.format(dNow);
		id = id + String.format("%03d", 0);
		String lastGeneratedId = (String) Constants.mainStore.get("LastGeneratedId");
		if (lastGeneratedId != null && BaseTool.jishiltStringTentsuu(id, lastGeneratedId)) {
			int number = Integer.parseInt(lastGeneratedId.substring(lastGeneratedId.length() - 3));
			number++;
			id = lastGeneratedId.substring(0, lastGeneratedId.length() - 3) + String.format("%03d", number);
		}
		Constants.mainStore.put("LastGeneratedId", id);
		return id;
	}
	
	public static String generateIdForFinishPlayer() {
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
		String id = ft.format(dNow);
		id = id + String.format("%03d", 2);
		String lastGeneratedId = (String) Constants.mainStore.get("LastGeneratedId");
		if (lastGeneratedId != null && BaseTool.jishiltStringTentsuu(id, lastGeneratedId)) {
			int number = Integer.parseInt(lastGeneratedId.substring(lastGeneratedId.length() - 3));
			number++;
			id = lastGeneratedId.substring(0, lastGeneratedId.length() - 3) + String.format("%03d", number);
		}
		Constants.mainStore.put("LastGeneratedId", id);
		return id;
	}
	
	public static String increaseIdByOne(String id) {
		int number = Integer.parseInt(id.substring(id.length() - 3));
		number++;
		id = id.substring(0, id.length() - 3) + String.format("%03d", number);
		return id;
	}
	
	public static void runTaskWithDelay(Runnable runnable, long delay) {
		new java.util.Timer().schedule( 
		        new java.util.TimerTask() {
		            @Override
		            public void run() {		                
		            	runnable.run();
		            }
		        }, 
		        delay
		);
	}

}
