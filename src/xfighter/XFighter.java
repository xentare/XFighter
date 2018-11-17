package xfighter;

import java.util.Arrays;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import xfighter.GUI.XFighterGUI;
import org.rspeer.runetek.event.listeners.AnimationListener;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.listeners.TargetListener;
import org.rspeer.runetek.event.types.AnimationEvent;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.TargetEvent;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import xfighter.tasks.ActivateRun;
import xfighter.tasks.DoBank;
import xfighter.tasks.Eat;
import xfighter.tasks.Fight;
import xfighter.tasks.Loot;
import xfighter.tasks.WalkToBank;
import xfighter.tasks.WalkToFightArea;

@ScriptMeta(name = "XFighter", desc="Fights NPCs", developer="Xentare", category = ScriptCategory.COMBAT)
public class XFighter extends TaskScript implements RenderListener, TargetListener, AnimationListener {

    //sujen@amadamus.com
    //kalle123
    //x:2918 y:3269
    private static final Task[] TASKS = {new Eat(), new Loot(), new Fight(), new WalkToFightArea(), new ActivateRun(),new WalkToBank(), new DoBank()};
    private static XFighterPaint paint;
    private static PlayerAnimationTimer timer;

    @Override
    public void onStart() {
        
        initialize();
        submit(TASKS);
    }
    
    @Override
    public void onStop() {
        XFighterGUI.getFrame().dispose();
    }

    @Override
    public int loop() {
        if (Settings.readyToStart()) {
            return super.loop();
        }
        return 0;
    }
    
    
    @Override
    public String getArgs() {
        return super.getArgs(); //To change body of generated methods, choose Tools | Templates.
    }

    private void initialize() {
        new XFighterGUI();
        paint = new XFighterPaint();
        timer = new PlayerAnimationTimer();
    }

    @Override
    public void notify(RenderEvent re) {
        paint.paint(re);
    }

    @Override
    public void notify(TargetEvent te) {
        XFighterGUI.update(te);
        if (Arrays.asList(te.getTarget().getActions()).stream().anyMatch(action -> action.contains("Dismiss"))) {
            System.out.println("Found a random event with name: " + te.getTarget().getName());
            Time.sleep(1000);
        }
    }

    @Override
    public void notify(AnimationEvent ae) {
        
        ae.forward(timer);
        
    }
    
}
