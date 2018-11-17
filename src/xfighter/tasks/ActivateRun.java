/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.tasks;

import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import xfighter.Settings;
import xfighter.TimerHelper;

public class ActivateRun extends Task {
    
    // Timer to check if we want to acivate run
    private static long activationCheckTimer;
    private static long waitForRandomMillis;
    
    @Override
    public boolean validate() {
        if ((System.currentTimeMillis() - activationCheckTimer) > waitForRandomMillis 
                && !Movement.isRunEnabled()
                && Movement.getRunEnergy() >= Random.mid(20, 100)) {
            return true;
        } else if (waitForRandomMillis == 0){
            activationCheckTimer = System.currentTimeMillis();
            waitForRandomMillis = Random.mid(10000, 100000);
            System.out.println("Checking run energy again in: " + TimerHelper.formatInterval(waitForRandomMillis, false) + " after it runs out.");
            return false;
        }
        return false;
    }

    @Override
    public int execute() {
        
        Movement.toggleRun(true);
        Settings.status = "Toggling run on.";
        waitForRandomMillis = 0;
        return Random.mid(100, 500);
    }
    
    
    
}
