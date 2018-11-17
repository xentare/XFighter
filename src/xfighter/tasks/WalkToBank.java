/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.rspeer.runetek.adapter.Positionable;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.path.BresenhamPath;
import org.rspeer.runetek.api.movement.path.HpaPath;
import org.rspeer.runetek.api.movement.path.Path;
import org.rspeer.runetek.api.movement.pathfinding.region.astar.AStar;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import xfighter.Settings;

public class WalkToBank extends Task{

    public static HpaPath pathToBank;
    
    @Override
    public boolean validate() {
        return Settings.banking
               && walkToBank()
               && !Area.surrounding(BankLocation.getNearestWithdrawable().getPosition(), 3).contains(Players.getLocal());
    }

    @Override
    public int execute() {
        
        Settings.status = "Walking to bank";
        
        Path path = Movement.buildPath(Players.getLocal().getPosition(),BankLocation.getNearestWithdrawable().getPosition());
        if (path != null) {
            path.walk();   
        }

        return Random.mid(4000, 5000);
    }
    
    public static boolean walkToBank(){
        return  (Inventory.isFull() && !Settings.eating())
               || (Settings.eating() && !Inventory.contains(Settings.foodToEat));
    }
    
}
