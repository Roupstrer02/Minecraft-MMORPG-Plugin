package me.roupen.firstpluginthree.PlayerInteractions;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.elite.EliteArenaLeaveCountdown;
import me.roupen.firstpluginthree.elite.elite;
import me.roupen.firstpluginthree.playerequipment.Rune;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

import static me.roupen.firstpluginthree.customgui.GuiUtility.CreateRuneGui;

public class EliteFight {

    private static ArrayList<String> eliteEntTypes = new ArrayList<String>() {{
        add("MythicMob{AbyssWatcherTest}");
    }};
    public static void Interact(PlayerInteractEvent event) throws InvalidMobTypeException {

        Player player = event.getPlayer();

        //determines the location of the Arena Fight Starting Button, editing this changes where the button is supposed to be
        Location ArenaInteractLoc = new Location(player.getWorld(), -275,73,353);

        if (
                event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                event.getClickedBlock().getType().equals(Material.STONE_BUTTON)
        )
        {
            Location InteractedLoc = new Location(player.getWorld(), event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ());
            if (
                    ArenaInteractLoc.getX() == InteractedLoc.getX() &&
                    ArenaInteractLoc.getY() == InteractedLoc.getY() &&
                    ArenaInteractLoc.getZ() == InteractedLoc.getZ() &&
                    FirstPluginThree.PlayersInBossFight.isEmpty()
            )
            {
                //set spawn locations for boss and players
                Location spawnLoc = new Location(player.getWorld(), -270,72,357);
                Location teleportLoc = new Location(player.getWorld(), -272,72,354);
                PlayerStats pStats = PlayerUtility.getPlayerStats(player);
                ArrayList<Player> party = new ArrayList<>(pStats.getParty());
                FirstPluginThree.PlayersInBossFight = party;
                for (Player p : party) {
                    p.teleport(teleportLoc);
                    p.sendMessage(Component.text("An Elite of Zelandris has been Challenged", Style.style(NamedTextColor.GOLD, TextDecoration.BOLD)));
                    p.setGameMode(GameMode.ADVENTURE);
                    PlayerUtility.getPlayerStats(p).setInBossFight(true);
                }

                elite AbyssWatcher = new elite("Abyss Watcher");
                AbyssWatcher.spawn(spawnLoc);
                event.setCancelled(true);
            }
        }

    }

    public static void EndBossFight(MythicMobDeathEvent event) {
        ActiveMob ent = event.getMob();
        if (eliteEntTypes.contains(ent.getType().toString())) {

            //FIND WHAT PARTS OF THIS CAN BE FACTORED OUT AND SHARED AMONGST ALL OTHER BOSSES (More Efficient)

            //else if another boss -> drop its loot instead
            for (Player p : FirstPluginThree.PlayersInBossFight) {
                Entity bukkitEnt = ent.getEntity().getBukkitEntity();
                LivingEntity bossEnt = (LivingEntity) bukkitEnt;
                Location BossEndLocation = new Location(bossEnt.getWorld(), -275,73,354);
                MobStats bossStatBlock = MobUtility.getMobStats(bossEnt);
                p.sendMessage(Component.text("An Elite of Zelandris has been Defeated", Style.style(NamedTextColor.GOLD)));
                p.sendMessage(Component.text("Each party member gains " + bossStatBlock.EXPtoGive() + " EXP", Style.style(NamedTextColor.GOLD)));
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

                //give players boss drops
                if (ent.getType().toString().equals("MythicMob{AbyssWatcherTest}")) {
                    GiveAbyssWatcherDrops(p);
                }
                //else if other boss -> drop their loot

                EliteArenaLeaveCountdown countDown = new EliteArenaLeaveCountdown(BossEndLocation);
                countDown.runTaskTimer(FirstPluginThree.getMyPlugin(), 0, 20);
            }
        }

    }

    private static void GiveAbyssWatcherDrops(Player p) {
        Inventory inv = p.getInventory();
        inv.addItem(Rune.GenerateRandomTier1Rune());
    }
}
