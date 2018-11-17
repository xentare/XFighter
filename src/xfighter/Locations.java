/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter;

public final class Locations implements ILocations {
    
    public static Location FIGHT_AREA;

    @Override
    public Location GetFightArea() {
        return FIGHT_AREA;
    }

    @Override
    public void SetFightArea(Location location) {
        FIGHT_AREA = location;
    }
    
}
