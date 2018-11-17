/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.tasks;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.path.Path;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import xfighter.PlayerAnimationTimer;
import xfighter.Settings;

/**
 *
 * @author Juha
 */
public class WalkToFightArea extends Task {

    @Override
    public boolean validate() {
        return !Settings.fightArea.getRectArea().contains(Players.getLocal())
                && !PlayerAnimationTimer.isAnimating()
                && !Players.getLocal().isMoving()
                && !Inventory.isFull()
                && (!DoBank.needBanking());
    }

    @Override
    public int execute() {
        
        Position walkTo = Settings.fightArea.getCentralTile();
        
        if (walkTo != null) {
            Path path = Movement.buildPath(walkTo);
            path.walk();
            Settings.status = "Walking to fight area.";
        }
        
        return Random.mid(1000, 2000);
    }
    
    public static Optional<Position> getClosestFightAreaTile() {
        
        if (Settings.fightArea == null) return null;
        
        List<Position> tiles = Settings.fightArea.getRectArea().getTiles();
        
        if (tiles != null && !tiles.isEmpty()) {
            tiles.sort(new comparePositionByDistance());
            return tiles.stream().filter(x -> x.isPositionWalkable() == true).findFirst();
        }
        return null;
    }
    
}

class comparePositionByDistance implements Comparator<Position> {

    @Override
    public int compare(Position o1, Position o2) {
        if (o1.distance() < o2.distance()) {
            return -1;
        } else if (o1.distance() > o2.distance()) {
            return 1;
        } else {
            return 0;
        }
    }
    
}