/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.tasks;

import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import xfighter.Settings;

public class DoBank extends Task{

    @Override
    public boolean validate() {
        return Area.surrounding(BankLocation.getNearestWithdrawable().getPosition(), 3).contains(Players.getLocal())
               && needBanking();
    }

    @Override
    public int execute() {
        
        Settings.status = "Banking.";
        
        if (Bank.isOpen()) {
            
            System.out.println("Bank is open");
            
            if (!Inventory.isEmpty()) {
                if(Bank.depositAllExcept(Settings.itemsNotToDeposit)){
                    System.out.println("Deposited items");
                    Time.sleep(Random.mid(1012, 1522));
                } else {
                    System.out.println("Failed to deposit items.");
                    return Random.mid(1442, 2522);
                }
            }
            
            
            if (Settings.eating()) {
                Settings.status = "Withdrawing food.";
                if (Bank.withdraw(Settings.foodToEat, Settings.foodWithdrawAmount)) {
                   return Random.mid(1561, 2241);
                } else {
                    System.out.println("Failed to withdraw food.");
                }
            }
            
        } else {
            Bank.open();
            return Random.mid(1452, 2341);
        }
        return Random.mid(1405, 1941);
    }
    
    public static boolean needBanking() {
        return (!Settings.eating() && Inventory.isFull())
            || (Settings.eating() && !Inventory.contains(Settings.foodToEat));
    }
    
}
