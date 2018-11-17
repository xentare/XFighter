/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class TimerHelper {
    
    public static String getTimeRunning() {
        return formatInterval(System.currentTimeMillis() - Settings.startTime, false);
    }
    
    public static double getRunningHours() {
        double runningTime = System.currentTimeMillis() - Settings.startTime;
        double hours = runningTime / (double)(1000 * 60 * 60);
        return hours;
    }
    
    public static String formatInterval(final long interval, boolean millisecs )
    {
        final long hr = TimeUnit.MILLISECONDS.toHours(interval);
        final long min = TimeUnit.MILLISECONDS.toMinutes(interval) %60;
        final long sec = TimeUnit.MILLISECONDS.toSeconds(interval) %60;
        final long ms = TimeUnit.MILLISECONDS.toMillis(interval) %1000;
        if( millisecs ) {
            return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
        } else {
            return String.format("%02d:%02d:%02d", hr, min, sec );
        }
    }
    
}
