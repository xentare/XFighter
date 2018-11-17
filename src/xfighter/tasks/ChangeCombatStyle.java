/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.tasks;

import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.script.task.Task;
import xfighter.Settings;

public class ChangeCombatStyle extends Task{

    private final int startingLevelAttack = Skills.getCurrentLevel(Skill.ATTACK);
    private final int startingLevelStrength = Skills.getCurrentLevel(Skill.STRENGTH);
    private final int startingLevelDefence = Skills.getCurrentLevel(Skill.DEFENCE);
    
    @Override
    public boolean validate() {
        return Settings.progressiveMode
            && ((Combat.getSelectedStyle() == Skill.ATTACK.getIndex() && Skills.getCurrentLevel(Skill.ATTACK) == startingLevelAttack + Settings.proggressiveModeLevelIncrement)
                || (Combat.getSelectedStyle() == Skill.STRENGTH.getIndex() && Skills.getCurrentLevel(Skill.STRENGTH) == startingLevelStrength + Settings.proggressiveModeLevelIncrement)
                || (Combat.getSelectedStyle() == Skill.DEFENCE.getIndex() && Skills.getCurrentLevel(Skill.DEFENCE) == startingLevelDefence + Settings.proggressiveModeLevelIncrement));
    }

    @Override
    public int execute() {
        System.out.println("Changing combat style.");
        return 100;
    }
    
}
