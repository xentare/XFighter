package xfighter;

import java.util.List;
import xfighter.GUI.ListNpc;

public class SaveSettings {

    private int fightAreaSize;
    private int fightAreaX;
    private int fightAreaY;
    private String foodName;
    private int foodWithdrawAmount;
    private List<ListNpc> NPCsToFight;
    private String[] itemsToLoot;
    
    public SaveSettings() {
        
    }
    
    public SaveSettings(boolean invoke) {
        this.fightAreaSize = Settings.fightArea.getSize();
        this.fightAreaX = Settings.fightArea.getCentralTile().getX();
        this.fightAreaY = Settings.fightArea.getCentralTile().getY();
        this.foodName = Settings.foodName;
        this.foodWithdrawAmount = Settings.foodWithdrawAmount;
        this.NPCsToFight = Settings.NPCsToFight;
        this.itemsToLoot = (String[]) Settings.ItemsToLoot.toArray();
    }

    public int getFightAreaX() {
        return fightAreaX;
    }

    public int getFightAreaY() {
        return fightAreaY;
    }
    
    public int getFightAreaSize() {
        return fightAreaSize;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getFoodWithdrawAmount() {
        return foodWithdrawAmount;
    }

    public List<ListNpc> getNPCsToFight() {
        return NPCsToFight;
    }

    public String[] getItemsToLoot() {
        return itemsToLoot;
    }
}
