package xfighter.tasks;

import java.util.Arrays;
import java.util.List;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import xfighter.PlayerAnimationTimer;
import xfighter.Settings;

public class Loot extends Task {

    // We want to loot when: 
    // 1. There is loot in the fight area
    // 2. AND We have inv space
    // 3. If we don't have inv space, we might want to eat
    @Override
    public boolean validate() {
        
        return Settings.fightArea.getRectArea().contains(Players.getLocal())
            && !PlayerAnimationTimer.isAnimating()
            && !Players.getLocal().isMoving()
            && canLoot();
    }

    @Override
    public int execute() {
        
        Pickable loot = Pickables.getNearest(Settings.itemsPred);
        
        if (loot != null) {
            loot.interact("Take");
            Settings.status = "Looting: " + loot.getName();
        }
        return Random.polar(100, 1000);
    }
    
    public static List<Pickable> getLootsInFightArea() {
        return Arrays.asList(Pickables.getLoaded(Settings.itemsPred));
    }
    
    public static boolean canLoot() {
        return Pickables.getNearest(Settings.itemsPred) != null
            && !Inventory.isFull();
    }
    
}
