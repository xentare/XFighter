package xfighter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.position.Position;
import xfighter.GUI.ListNpc;
import xfighter.GUI.XFighterGUI;


public class Settings {
    
    public static boolean progressiveMode;
    public static int progressiveModeLevelAttack;
    public static int progressiveModeLevelStrength;
    public static int progressiveModeLevelDefence;
    public static int proggressiveModeLevelIncrement;
    public static boolean started;
    public static boolean paintEnabled = false;
    public static boolean renderFightArea = false;
    public static boolean banking = true;
    public static Location fightArea;
    public static final long attackStartingXp = Skills.getExperience(Skill.ATTACK);
    public static final long strengthStartingXp = Skills.getExperience(Skill.STRENGTH);
    public static final long defenceStartingXp = Skills.getExperience(Skill.DEFENCE);
    public static final long magicStartingXp = Skills.getExperience(Skill.MAGIC);
    public static int hpPercentToEat = 50;
    public static String foodName;
    public static boolean eatForSpace = true;
    public static Predicate<Item> foodToEat = f -> f.getName().contains(foodName);
    public static int foodWithdrawAmount = 10;
    public static Predicate<Item> itemsNotToDeposit = i -> i.getName().contains("Air rune") || i.getName().contains("Mind rune");
    
    public static String status;
    
    // NPCs can't be compared by .getName() Somehow it doesn't work...
    // Strings work with .contain().....
    public static List<ListNpc> NPCsToFight = new ArrayList<>();
    public static Predicate<Npc> NPC = b -> NPCsToFight.stream().anyMatch(x -> x.getId() == b.getId());
    
    public static List<String> ItemsToLoot = new ArrayList<>();
    public static Predicate<Pickable> itemsPred = i -> ItemsToLoot.stream().anyMatch(x -> i.getName().contains(x)
                                                                                && fightArea.getRectArea().contains(i));
    
    public static final long startTime = System.currentTimeMillis();
    
    public static boolean readyToStart() {
        return fightArea != null && NPCsToFight != null && started;
    }
 
    public static boolean eating() {
       return !foodName.isEmpty();
    }
    
    public static long getStartingXp(Skill s) {
        switch(s) {
            case ATTACK: return attackStartingXp;
            case STRENGTH: return strengthStartingXp;
            case DEFENCE: return defenceStartingXp;
            case MAGIC: return magicStartingXp;
        }
        return 0;
    }
    
    public static void save(String path, String fileName) {    
        try
        {
            GsonBuilder gsonBuilder  = new GsonBuilder();
            // Allowing the serialization of static fields    

            gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
            // Creates a Gson instance based on the current configuration
            Gson gson = gsonBuilder.create();
            String json = gson.toJson(new SaveSettings(true));
            Paths.get(String.format("{0}{1}{2}", path, File.pathSeparator, fileName));
            try (FileWriter writer = new FileWriter(Paths.get(fileName + ".json").toFile(), false)) {
                writer.write(json);
            }
            System.out.println(json);
        }
        catch (Exception e)
        {
            System.out.println("Error when saving to file.");
            System.out.println(e);
        }
    }
    
}