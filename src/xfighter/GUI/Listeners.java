/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.GUI;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.rspeer.runetek.api.ClientSupplier;
import xfighter.Settings;

public final class Listeners implements Serializable {
    
    XFighterGUI gui;
    
    public Listeners(XFighterGUI gui) {
        this.gui = gui;
        attackableNpcsMouseListener();
        fightAreaCentralTileFieldsKeyListener();
        fightAreaSizeKeyListener();
        selectedNpcsMouseListener();
        itemListListener();
        openGUIListener();
    }
    
    // Add NPC to selected ones
    public void attackableNpcsMouseListener() {
        gui.attackableNpcsField.addMouseListener( new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index < 0) return;
                      XFighterGUI.addNpcToList(index);
                }
            }
        });
    }
    
    // Remove NPC from selected ones
    public void selectedNpcsMouseListener() {
        gui.selectedNpcsField.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index < 0) return;
                    XFighterGUI.removeNpcFromList(index);
                }
            }
        });
    }
    
    public void fightAreaCentralTileFieldsKeyListener() {
        XFighterGUI.fightAreaCentralTileXField.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //gui.setFightArea(true);
            }
        });
        XFighterGUI.fightAreaCentralTileYField.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //XFighterGUI.setFightArea(true);
            }
        });
    }
    
    public void fightAreaSizeKeyListener() {
        XFighterGUI.fightAreaSizeField.addKeyListener( new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //XFighterGUI.setFightArea(false);
            }
        });
    }
    
    public void itemListListener() {
        
        XFighterGUI.itemTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    XFighterGUI.itemTableModel.addRow(new Object[] {});
                }
            }
            
        });
        
        XFighterGUI.itemTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                
                String value;
                
                Settings.ItemsToLoot.clear();
                
                for(int i = 0; i < XFighterGUI.itemTable.getRowCount(); i++) {
                    value = (String)XFighterGUI.itemTable.getValueAt(i, 0);
                    if (value != null && !value.isEmpty()) {
                        Settings.ItemsToLoot.add(value);
                    }

                }
                Settings.ItemsToLoot.stream().forEach((itm) -> {
                    System.out.println(itm);
                });
                
            }
            
            
        });
        
    }
    
    public void openGUIListener(){
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
                if(event instanceof MouseEvent){
                    MouseEvent evt = (MouseEvent)event;
                    if(evt.getID() == MouseEvent.MOUSE_CLICKED){
                        if (XFighterGUI.openGUIButton.contains(new Point(evt.getX(), evt.getY()))){ 
                            XFighterGUI.getFrame().setVisible(!XFighterGUI.isVisible());
                        }
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }
    
}
