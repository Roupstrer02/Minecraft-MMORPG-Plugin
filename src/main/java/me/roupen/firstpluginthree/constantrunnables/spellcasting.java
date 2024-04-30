package me.roupen.firstpluginthree.constantrunnables;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.magic.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class spellcasting {
    public static void cast(Player caster, String input_spell)
    {
        int period = 1;
        long delay = 0L;

        switch (input_spell)
        {
            //Pyromancy
            case "Fireball":
                new Fireball(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
                break;
            case "Flame Dash":
                new FlameDash(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
                break;
            case "Flame Booster":
                new FlameBooster(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
                break;
            case "Meteor Fall":
                new MeteorFall(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
                break;

            //Technomancy
            case "Steam Rocket Pack":
                new SteamRocketPack(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
                break;
            case "Chrono Thief":
                new Chronothief(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
                break;
            default:
        }
    }
}
