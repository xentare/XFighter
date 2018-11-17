/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.List;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.ClientSupplier;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.debug.MovementDebug;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.Projection;
import org.rspeer.runetek.api.scene.Scene;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.providers.RSClient;
import xfighter.GUI.XFighterGUI;
import xfighter.tasks.Fight;
import xfighter.tasks.Loot;
import xfighter.tasks.WalkToBank;
import xfighter.tasks.WalkToFightArea;

public class XFighterPaint {
    
    private static RSClient rsClient;
    private static Canvas rsCanvas;
    private static Graphics rsGraphics;
    
    public XFighterPaint() {
        rsClient = ClientSupplier.get();
        rsCanvas = rsClient.getCanvas();
        rsGraphics = rsCanvas.getGraphics();
    }    
    
    public void paint(RenderEvent re) {
        Graphics g = re.getSource();
        if (!Settings.paintEnabled) return;
        
        if (Helpers.isInGame()) {
            
            if (Settings.fightArea != null && 
                Settings.fightArea.getCentralTile().getX() != 0 && 
                Settings.fightArea.getCentralTile().getY() != 0 &&
                Settings.fightArea.getSize() != 0 &&
                Settings.renderFightArea &&
                Settings.fightArea.getCentralTile().distance() < 20){

                Area fightArea = Settings.fightArea.getRectArea();
                List<Position> tiles = fightArea.getTiles();
                
                
                // fightArea.outline(g);
                
                for (Position tile : tiles) {
                    if (Scene.isBlocked(tile)) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.BLACK);
                    }
                    g.drawPolygon(Projection.getTileShape(tile));
                }
                
                // Closest fight area tile
                if(WalkToFightArea.getClosestFightAreaTile() != null) {
                    g.setColor(Color.ORANGE);
                    g.drawPolygon(Projection.getTileShape(WalkToFightArea.getClosestFightAreaTile().get()));
                }
                
            }
            
            if (Settings.NPC != null && false) {
                g.setColor(Color.CYAN);
                Npc nearestInteracting = Fight.getNearestNpc(true);
                if (nearestInteracting != null) {
                    g.drawPolygon(Projection.getTileShape(nearestInteracting.getPosition()));
                    //nearestInteracting.getAnimationDelay()
                    g.setFont(new Font("Calibri", Font.PLAIN, 12));
                    g.drawString("IsAnimating: " + nearestInteracting.isAnimating(), nearestInteracting.getPosition().toScreen().getX(),
                                                                                    nearestInteracting.getPosition().toScreen().getY());
                    
                    g.drawString("Animation: " + nearestInteracting.getAnimation(), nearestInteracting.getPosition().toScreen().getX(),
                                                                                    nearestInteracting.getPosition().toScreen().getY()+22);
                    g.drawString("HP: " + nearestInteracting.getHealthPercent() + "%", nearestInteracting.getPosition().toScreen().getX(),
                                                                                        nearestInteracting.getPosition().toScreen().getY()+44);
                    g.drawString("HP bar visible: " + nearestInteracting.isHealthBarVisible(), nearestInteracting.getPosition().toScreen().getX(),
                                                                                        nearestInteracting.getPosition().toScreen().getY()+66);
                    g.drawString("Distance: " + nearestInteracting.distance(Players.getLocal().getPosition()), nearestInteracting.getPosition().toScreen().getX(),
                                                                                        nearestInteracting.getPosition().toScreen().getY()+88);
                }
                
                g.setColor(Color.WHITE);  
                Npc nearest = Fight.getNearestNpc(false);
                if (nearest != null) {
                    g.drawPolygon(Projection.getTileShape(nearest.getPosition()));                      
                }
            }
            
            // Player interaction
            g.setColor(Color.red);
            g.drawString("Animating: " + PlayerAnimationTimer.isAnimating(), Players.getLocal().getPosition().toScreen().getX(), 
                                                                        Players.getLocal().getPosition().toScreen().getY());
            g.drawString("AVG time: " + PlayerAnimationTimer.getAvgTime(), Players.getLocal().getPosition().toScreen().getX(), 
                                                                        Players.getLocal().getPosition().toScreen().getY()+12);
            g.drawString("Longest time: " + PlayerAnimationTimer.getLongestTime(), Players.getLocal().getPosition().toScreen().getX(), 
                                                                        Players.getLocal().getPosition().toScreen().getY()+24);
            
            
            // Loot items
            g.setColor(Color.CYAN);
            g.setFont(new Font("Calibri", Font.PLAIN, 8));
            for (Pickable loot : Loot.getLootsInFightArea()) {
                Rectangle lootPos = Projection.getTileShape(loot.getPosition()).getBounds();
                g.drawString(loot.getName(), (int)lootPos.getCenterX() - 10, (int)lootPos.getCenterY() - 8);
            }
       
            
        }
        
        // Basic paint
        g.setColor(Color.BLACK);
        g.fillRect(550, 205, 190, 250);
        
        // XP/H
        g.setColor(Color.WHITE);
        g.setFont(new Font("Calibri", Font.PLAIN, 11));
        g.drawString("Time running: " + TimerHelper.getTimeRunning(), 555, 220);
        g.drawString("Status: " + Settings.status, 555, 232);
        g.drawString("Eating: " + Settings.eating(), 555, 244);
        g.drawString("Closest bank: " + BankLocation.getNearestWithdrawable().name(), 555, 266);
        g.drawString("AttackXP/h: " + xpHour(Skill.ATTACK), 555, 278);
        g.drawString("StrengthXP/h: " + xpHour(Skill.STRENGTH), 555, 290);
        g.drawString("DefenceXP/h: " + xpHour(Skill.DEFENCE), 555, 302);
        g.drawString("MagicXP/h: " + xpHour(Skill.MAGIC), 555, 312);
        //g.drawString("StartingXp: " + Settings.getStartingXp(Skill.ATTACK), 555, 302);
        //g.drawString("RunningH: " + new DecimalFormat("#.#####").format((TimerHelper.getRunningHours())), 555, 326);
        
        // Open GUI
        g.drawString("Open GUI", (int)XFighterGUI.openGUIButton.getX(), (int)XFighterGUI.openGUIButton.getY() + g.getFont().getSize());
        g.drawPolygon(RectangleToPolygon(XFighterGUI.openGUIButton));
        //g.drawString("X: "+ClientSupplier.get().getBaseX() + " Y: " + ClientSupplier.get().getBaseY(), 555, 400);
        //g.drawString("MX: "+ClientSupplier.get().getMouseX() + " MY: " + ClientSupplier.get().getMouseY(), 555, 410);
        
        //Movement debug
        MovementDebug db = Movement.getDebug();
        if (WalkToBank.pathToBank != null && WalkToBank.pathToBank.getLastTilePath() != null) {
            //db.drawHpaPath(WalkToBank.pathToBank, g);
        }
        
    }
    
    public static Polygon RectangleToPolygon(Rectangle rect) {
        int[] xpoints = {rect.x, rect.x + rect.width, rect.x + rect.width, rect.x};
        int[] ypoints = {rect.y, rect.y, rect.y + rect.height, rect.y + rect.height};
        return new Polygon(xpoints, ypoints, 4); 
    }
    
    public static double xpHour(Skill skill) {
        double earnedXp = Skills.getExperience(skill) - Settings.getStartingXp(skill);
        return (long) (earnedXp / TimerHelper.getRunningHours());
    }
    
}
