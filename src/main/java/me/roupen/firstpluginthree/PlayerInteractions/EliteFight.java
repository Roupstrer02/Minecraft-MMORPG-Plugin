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
import java.util.Arrays;

import static me.roupen.firstpluginthree.customgui.GuiUtility.CreateRuneGui;

public class EliteFight {

    //determines the location of the Arena Fight Starting Buttons, editing these changes where the button is supposed to be
    private static final Location ArenaInteractLoc1 = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), 42,-12,1273);
    private static final Location ArenaInteractLoc2 = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), 43,-12,1274);
    private static final ArrayList<Location> LarianLowInteracts = new ArrayList<>(Arrays.asList(ArenaInteractLoc1, ArenaInteractLoc2));
    private static final Location ArenaInteractLoc3 = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), 24,-12,1291);
    private static final Location ArenaInteractLoc4 = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), 25,-12,1292);
    private static final ArrayList<Location> LarianMidInteracts = new ArrayList<>(Arrays.asList(ArenaInteractLoc3, ArenaInteractLoc4));
    private static final Location ArenaInteractLoc5 = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), 43,-12,1291);
    private static final Location ArenaInteractLoc6 = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), 42,-12,1292);
    private static final ArrayList<Location> LarianInteracts = new ArrayList<>(Arrays.asList(ArenaInteractLoc5, ArenaInteractLoc6));
    private static final Location spawnLoc = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), -30, -41, 1167);
    private static final Location teleportLoc = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), -30, -41, 1185);
    private static final Location BossEndLocation = new Location(FirstPluginThree.getMyPlugin().getServer().getWorld("world"), -275,73,354);
    private static final ArrayList<String> eliteEntTypes = new ArrayList<String>() {{
        add("MythicMob{AbyssWatcherTest}");
        add("MythicMob{LarianLow}");
        add("MythicMob{LarianMid}");
        add("MythicMob{Larian}");
    }};

    private static boolean buttonInteractCheck(Location interactLocation, ArrayList<Location> ArenaButtonLocs) {

        for (Location loc : ArenaButtonLocs) {
            if (interactLocation.getX() == loc.getX() && interactLocation.getY() == loc.getY() && interactLocation.getZ() == loc.getZ()) {
                return true;

            }
        }
        return false;
    }

    public static void Interact(PlayerInteractEvent event) throws InvalidMobTypeException {

        Player player = event.getPlayer();

        if
        (
            event.getAction() == Action.RIGHT_CLICK_BLOCK &&
            event.getClickedBlock().getType().equals(Material.STONE_BUTTON) &&
            FirstPluginThree.PlayersInBossFight.isEmpty() &&
            player.getWorld().getEnvironment() == World.Environment.NORMAL
        )
        {
            Location InteractedLoc = new Location(player.getWorld(), event.getClickedBlock().getX(), event.getClickedBlock().getY(), event.getClickedBlock().getZ());
            if (buttonInteractCheck(InteractedLoc, LarianLowInteracts) || buttonInteractCheck(InteractedLoc, LarianMidInteracts) || buttonInteractCheck(InteractedLoc, LarianInteracts))
            {
                PlayerStats pStats = PlayerUtility.getPlayerStats(player);
                ArrayList<Player> party = new ArrayList<>(pStats.getParty());
                FirstPluginThree.PlayersInBossFight = party;

                player.getWorld().sendMessage(Component.text("An Elite of Zelandris has been Challenged", Style.style(NamedTextColor.GOLD, TextDecoration.BOLD)));
                for (Player p : party) {
                    p.teleport(teleportLoc);


                    PlayerUtility.getPlayerStats(p).setInBossFight(true);
                }
                event.setCancelled(true);
            }
            if (buttonInteractCheck(InteractedLoc, LarianLowInteracts))
            {
                player.sendMessage("ArenaButton1 pressed");
                elite LarianLow = new elite("Larian the Night Hunter");
                LarianLow.spawn(spawnLoc);

            } else if (buttonInteractCheck(InteractedLoc, LarianMidInteracts)) {
                player.sendMessage("ArenaButton2 pressed");
                elite LarianMid = new elite("Larian the Berserker");
                LarianMid.spawn(spawnLoc);

            } else if (buttonInteractCheck(InteractedLoc, LarianInteracts)) {
                player.sendMessage("ArenaButton3 pressed");
                elite Larian = new elite("Larian the Nightmare");
                Larian.spawn(spawnLoc);
            }

        }

    }

    public static void EndBossFight(MythicMobDeathEvent event) {
        ActiveMob ent = event.getMob();
        if (eliteEntTypes.contains(ent.getType().toString())) {

            //FIND WHAT PARTS OF THIS CAN BE FACTORED OUT AND SHARED AMONGST ALL OTHER BOSSES (More Efficient)

            event.getEntity().getWorld().sendMessage(Component.text("An Elite of Zelandris has been Defeated", Style.style(NamedTextColor.GOLD)));

            //else if another boss -> drop its loot instead
            for (Player p : FirstPluginThree.PlayersInBossFight) {
                Entity bukkitEnt = ent.getEntity().getBukkitEntity();
                LivingEntity bossEnt = (LivingEntity) bukkitEnt;

                MobStats bossStatBlock = MobUtility.getMobStats(bossEnt);

                p.sendMessage(Component.text("Each party member gains " + bossStatBlock.EXPtoGive(PlayerUtility.getPlayerStats(p).getLevel()) + " EXP", Style.style(NamedTextColor.GOLD)));
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

                //give players boss drops
                if (ent.getType().toString().equals("MythicMob{AbyssWatcherTest}")) {
                    GiveAbyssWatcherDrops(p);
                } else if (ent.getType().toString().equals("MythicMob{LarianLow}")) {
                    GiveLarianLowDrops(p);
                } else if (ent.getType().toString().equals("MythicMob{LarianMid}")) {
                    GiveLarianMidDrops(p);
                } else if (ent.getType().toString().equals("MythicMob{Larian}")) {
                    GiveLarianDrops(p);
                }

                EliteArenaLeaveCountdown countDown = new EliteArenaLeaveCountdown(BossEndLocation);
                countDown.runTaskTimer(FirstPluginThree.getMyPlugin(), 0, 20);
            }
        }

    }

    private static void GiveAbyssWatcherDrops(Player p) {
        Inventory inv = p.getInventory();
        inv.addItem(Rune.GenerateRandomTier1Rune());
    }
    private static void GiveLarianLowDrops(Player p) {
        Inventory inv = p.getInventory();
        inv.addItem(Rune.GenerateRandomTier1Rune());
    }
    private static void GiveLarianMidDrops(Player p) {
        Inventory inv = p.getInventory();
        inv.addItem(Rune.GenerateRandomTier1Rune());
        inv.addItem(Rune.GenerateRandomTier1Rune());
    }
    private static void GiveLarianDrops(Player p) {
        Inventory inv = p.getInventory();
        inv.addItem(Rune.GenerateRandomTier1Rune());
        inv.addItem(Rune.GenerateRandomTier1Rune());
        inv.addItem(Rune.GenerateRandomTier1Rune());
    }
}
