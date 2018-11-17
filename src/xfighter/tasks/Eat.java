/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.tasks;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.local.Health;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import xfighter.Settings;

public class Eat extends Task{

    @Override
    public boolean validate() {
        return Settings.eating()
            && (Health.getPercent() < Settings.hpPercentToEat
            || needSpaceForLoot());
    }

    @Override
    public int execute() {
        
        System.out.println("Eating...");
        
        if (Inventory.contains(Settings.foodName)) { 
            Item food = Inventory.getFirst(Settings.foodToEat);
            food.interact("Eat");
            Settings.status = "Eating.";
        }
        return Random.mid(816, 2592);
        
    }
    
    public static boolean needSpaceForLoot() {
        return Settings.eatForSpace && Pickables.getNearest(Settings.itemsPred) != null
            && Inventory.isFull();
    }
    
}
