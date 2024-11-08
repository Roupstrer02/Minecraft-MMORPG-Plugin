package me.roupen.firstpluginthree.elite;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EliteArenaLeaveCountdown extends BukkitRunnable {
    int progress = 10; //10 seconds of ticks

    Component FirstMessage = Component.text("Leaving Challenge Arena in...", Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC));
    Component SecondMessage = Component.text("3...", Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC));
    Component ThirdMessage = Component.text("2...", Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC));
    Component FourthMessage = Component.text("1...", Style.style(NamedTextColor.GRAY, TextDecoration.ITALIC));

    private final Location Destination;

    public EliteArenaLeaveCountdown(Location dest) {
        Destination = dest;
    }

    @Override
    public void run() {

        switch (progress) {
            case 5:
                for (Player p : FirstPluginThree.PlayersInBossFight) {
                    p.sendMessage(FirstMessage);
                }
                break;
            case 3:
                for (Player p : FirstPluginThree.PlayersInBossFight) {
                    p.sendMessage(SecondMessage);
                }
                break;
            case 2:
                for (Player p : FirstPluginThree.PlayersInBossFight) {
                    p.sendMessage(ThirdMessage);
                }
                break;
            case 1:
                for (Player p : FirstPluginThree.PlayersInBossFight) {
                    p.sendMessage(FourthMessage);
                }
                break;
            case 0:
                for (Player p : FirstPluginThree.PlayersInBossFight) {
                    p.teleport(Destination);
                    p.setGameMode(GameMode.SURVIVAL);
                    PlayerUtility.getPlayerStats(p).setInBossFight(false);
                }
                FirstPluginThree.PlayersInBossFight.clear();
                this.cancel();
        }


        progress -= 1;
    }
}
