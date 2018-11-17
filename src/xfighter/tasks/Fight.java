/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.tasks;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import xfighter.PlayerAnimationTimer;
import xfighter.Settings;

public class Fight extends Task {
    
    @Override
    public boolean validate() {
        return  Settings.fightArea.getRectArea().contains(Players.getLocal())
               && !PlayerAnimationTimer.isAnimating()
               && !Players.getLocal().isMoving()
               && !Loot.canLoot()
               && !Eat.needSpaceForLoot()
               && !WalkToBank.walkToBank();
    }
    
    @Override
    public int execute() {
        
        if (!Combat.isAutoRetaliateOn()) {
            Combat.toggleAutoRetaliate(true);
        }
        
        if (getNearestNpc(false) == null) {
            return 100;
        }
        // We might want to save this and see when it dies. We could wait for a loot since the NPC animates when it dies.
        Npc npc = getNearestNpc(false);
        
        npc.interact("Attack");
        Settings.status = "Attacking NPC";
        
        return Random.polar(100, 1000);
    }
    
    public static Npc getNearestNpc(boolean interactingWithPlayer) {
        
        // Get NPCs in the fight area and sort by distance to player
        Optional<Npc> returnVal;
        Npc[] npcs = Npcs.getSorted(new compareByDistance(), Settings.NPC);
        Stream<Npc> searchNpc = Arrays.asList(npcs).stream().filter(x -> Settings.fightArea.getRectArea().contains(x)
                                                                 && x.isPositionInteractable()
                                                                 && x.isPositionWalkable());
        
        // HealtBarVisible is a shitty one.
        // It only tell's that the NPC is being attacked.
        // This changes for example on two-handed weapons since the hit time takes so long.
        if (interactingWithPlayer) {
            returnVal = searchNpc.filter(x -> (x.isAnimating() || x.isHealthBarVisible())
                                           && x.getHealthPercent() != 0).findFirst();
        } else {
            returnVal = searchNpc.filter(x -> !x.isAnimating() && !x.isHealthBarVisible()).findFirst();
        }
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            return null;
        }
        
    }
    
}

class compareByDistance implements Comparator<Npc> {

    @Override
    public int compare(Npc o1, Npc o2) {
        double o1Dist = o1.getPosition().distance(Players.getLocal());
        double o2Dist = o2.getPosition().distance(Players.getLocal());
        if (o1Dist < o2Dist) {
            return -1;
        } else if (o1Dist == o2Dist) {
            return 0;
        }
        return 1;
    }

}