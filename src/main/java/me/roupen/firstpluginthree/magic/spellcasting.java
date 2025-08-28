package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.Zelandris;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class spellcasting {

    public spellcasting() {

    }

    public void cast(Player caster, String input_spell)
    {
        int period = 1;
        long delay = 0L;
        BukkitTask castedSpell = null;

        try {
            switch (input_spell)
            {
                //Pyromancy
                case "Fireball":
                    castedSpell = new PyroFireball(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Flame Dash":
                    castedSpell = new PyroFlameDash(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Flame Booster":
                    castedSpell = new PyroFlameBooster(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Meteor Fall":
                    castedSpell = new PyroMeteorFall(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;

                //Technomancy
                case "Steam Rocket Pack":
                    castedSpell = new TechSteamRocketPack(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Chrono Thief":
                    castedSpell = new TechChronothief(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Fault In The Armor":
                    castedSpell = new TechFaultInArmor(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Snipe":
                    castedSpell = new TechSnipe(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;

                //Divinity
                case "Healing Orb":
                    castedSpell = new DivineHealingOrb(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Strength Of Faith":
                    castedSpell = new DivineStrengthOfFaith(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Angel Wings":
                    castedSpell = new DivineAngelWings(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "We Yield To None":
                    castedSpell = new DivineWeYieldToNone(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;

                //Naturemancy
                case "Nature's Bounty":
                    castedSpell = new NatureNaturesBounty(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Nature's Diversity":
                    castedSpell = new NatureNaturesDiversity(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Nature's Workbench":
                    castedSpell = new NatureNaturesWorkbench(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
                case "Nature's Storage":
                    castedSpell = new NatureNaturesStorage(caster).runTaskTimer(Zelandris.getMyPlugin(), delay, period);
                    break;
            }
        } catch (NoClassDefFoundError e) {
            if (castedSpell != null) {
                castedSpell.cancel();
            }
            System.out.println("A spell has caused a NoClassDefFoundError:\n");
            System.out.println(Arrays.toString(e.getStackTrace()));
            caster.sendMessage(
                    Component.text(
                            "If you're seeing this message, please contact me (Roupen) as a bug of interest just occured, just tell me that \"The bug happened again\", I'll get the idea, many thanks!",
                            Style.style(TextDecoration.BOLD)));
        }

    }
}
