package org.ncd.rasp.tools;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class Constants {
	
	public static final String TimeZone = "Asia/Ulaanbaatar";
    public static final String OgnooniiFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String TsagiinFormat = "HH:mm:ss";
    public static final String EngiinOgnooniiFormat = "yyyy-MM-dd";    

    public static final String TusgaiTemdegtuud = "[!@#$%^&*()--+=,?\":;`~'/\\{} |<>]";
    public static final String KhoriglokhTemdegtuud = "[!@#$%^&*()--+=,?\":;`~'/\\{}|<>]";

    public static final int ORONGIIN_NARIIVCHLAL = 2;
    public static final RoundingMode ORON_BODOKH_ARGA = RoundingMode.HALF_UP;
    
    public static final String SOUND_FILE_PATH = "/home/pi/rasp/sounds/";
    public static final String AD_SOUND_FILE_PATH = "/home/pi/rasp/sounds/ad/";
    public static final String SCHEDULED_SOUND_FILE_PATH = "/home/pi/rasp/sounds/scheduled/";
    
    public static final Map<String, Object> mainStore = new HashMap<>();
    public static final Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

}
