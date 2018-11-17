/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter;

import static xfighter.Locations.FIGHT_AREA;

/**
 *
 * @author Juha
 */
interface ILocations {
    public Location GetFightArea();
    public void SetFightArea(Location location);
}
