package com.foursquare.takehome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.foursquare.takehome.Constants.TIME_FORMAT;
import static com.foursquare.takehome.Constants.TIME_ZONE;

/**
 *  Single utility class for time conversion from unix time to real time as GMT with 12 hr clock format
 *  Time builder for view with arrival time and leave time shown as per screenshot
 */

public class Utility {

    public static String TimeBuilderForView(String arrivalTime, String leaveTime) {

        return arrivalTime + " - " + leaveTime;

    }

    // Method implementation references from Android Date format examples

    public static String convertUnixTimeToRealTime(Long eTime) {
        Date date = new Date(eTime * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        String realTime = simpleDateFormat.format(date);
        return realTime;
    }
}
