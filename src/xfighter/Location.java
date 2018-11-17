package xfighter;

import java.util.List;
import org.rspeer.runetek.adapter.Positionable;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;

public final class Location {
           
    private static Area rectArea;
    private static int size;
    
    public Location() {
        
    }
    
    public Location(Position centralTile, int size) {
        this.rectArea = Area.surrounding(centralTile, size);
        this.size = size;
    }
    
    public Area getRectArea() {
        return rectArea;
    }
    
    public Position getCentralTile() {
        return rectArea.getCenter();
    }
    
//    public void setCentralTile(Position pos) {
//        this.rectArea = Area.surrounding(pos, size);
//    }
    
    public int getSize() {
        return size;
    }
    
//    public void setSize(int s) {
//        this.size = s;
//    }
    
    @Override
    public String toString() {
        return ("CentralX: " + getCentralTile().getX() + " CentralY: " + getCentralTile().getY() + " Size: " + size);
    }
    
}
