/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter;

import java.util.List;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.AnimationListener;
import org.rspeer.runetek.event.types.AnimationEvent;

/*
    The goal of the animation timer is to determine the average animation time for the current fighting setup.
    We get an event every time an animation starts and finishes and also the timestamps.
*/
public class PlayerAnimationTimer implements AnimationListener {
   
    public long startTime;
    public long updatedTime;
    public static long endTime;
    public static long length;
    public static String eventType;
    public static long animationCount;
    public static long cumulativeLength;
    private static final long tweakAmount = 150;
    private static int lastAnimationEventType;
    private static long longestAnimationTime;

    @Override
    public void notify(AnimationEvent ae) {
        if (ae.getSource() == Players.getLocal()) {
            switch (ae.getType()) {
                case AnimationEvent.TYPE_STARTED:
                    startTime = ae.getTime();
                    lastAnimationEventType = AnimationEvent.TYPE_STARTED;
                case AnimationEvent.TYPE_UPDATED:
                    startTime = ae.getTime();
                case AnimationEvent.TYPE_EXTENDED:
                    startTime = ae.getTime();
                case AnimationEvent.TYPE_FINISHED:
                {
                    endTime = ae.getTime();
                    length = endTime - startTime;
                    
                    if(length > longestAnimationTime) {
                        longestAnimationTime = length;
                    }
                    
                    if (length != 0) {
                        cumulativeLength += length;
                        animationCount++;
                    }
                    lastAnimationEventType = AnimationEvent.TYPE_FINISHED;
                }
            }
        }
    }
    
    public static long getAvgTime() {
        if (animationCount == 0 ) return 1500;
        return cumulativeLength/animationCount;
    }
    
    public static long getLongestTime() {
        return longestAnimationTime;
    }
    
    public static boolean isAnimating() {
        if (longestAnimationTime == 0) longestAnimationTime = 1500;   
        return System.currentTimeMillis() - endTime < (longestAnimationTime + tweakAmount)
                && lastAnimationEventType == AnimationEvent.TYPE_FINISHED;
    }
    
//    public static boolean isAnimating() {
//        if (endTime == 0 || cumulativeLength == 0 || animationCount == 0) return false;
//        return System.currentTimeMillis() - endTime < (getAvgTime()) + tweakAmount
//            && lastAnimationEventType == AnimationEvent.TYPE_FINISHED;
//    }
    
}
