package me.roupen.firstpluginthree.PlayerInteractions;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.customgui.GuiUtility;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class ProfileMenu {

    public static void ClickMenu(InventoryClickEvent event)
    {

        Player player = (Player) event.getWhoClicked();
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        String invtitle = event.getView().title().toString();

        //when the player clicks in the "Player stats" menu
        if (invtitle.contains("content=\"Player Stats\""))
        {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.DIAMOND) {
                player.closeInventory();
                GuiUtility.CreateUpgradeGui(player);
                event.setCancelled(true);
            }
            else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.WRITTEN_BOOK) {
                player.closeInventory();
                player.sendMessage(Component.text("=================================\nClick here to check out the wiki!\n=================================",
                        Style.style(TextDecoration.BOLD, TextDecoration.ITALIC)).clickEvent(ClickEvent.openUrl("https://roupstrer02.github.io/")));
                event.setCancelled(true);
            }
            else if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.AMETHYST_BLOCK) {
                if (FirstPluginThree.PlayersInBossFight.contains(player)) {
                    player.sendMessage(Component.text("You should've thought of that before challenging an Elite", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                    player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 0.8f);
                }else {
                    player.closeInventory();
                    GuiUtility.CreateSpellBookGui(player);
                    event.setCancelled(true);
                }
            }
        }
        //when the player clicks in the "Stat Improvements" menu
        else if (invtitle.contains("content=\"Stat Improvements\""))
        {
            if ((event.getCurrentItem() != null) && (event.getCurrentItem().getType() == Material.GRAY_DYE))
            {GuiUtility.CreateProfileGui(player);}

            if (stats.getSkillPoints() > 0) {
                if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                    stats.addVitality(1);
                    stats.addSkillPoints(-1);
                    GuiUtility.CreateUpgradeGui(player);
                }
                else if (event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) {
                    stats.addResilience(1);
                    stats.addSkillPoints(-1);
                    GuiUtility.CreateUpgradeGui(player);
                }
                else if (event.getCurrentItem().getType() == Material.BLUE_STAINED_GLASS_PANE) {
                    stats.addIntelligence(1);
                    stats.addSkillPoints(-1);
                    GuiUtility.CreateUpgradeGui(player);
                }
                else if (event.getCurrentItem().getType() == Material.ORANGE_STAINED_GLASS_PANE) {
                    stats.addStrength(1);
                    stats.addSkillPoints(-1);
                    GuiUtility.CreateUpgradeGui(player);
                }
                else if (event.getCurrentItem().getType() == Material.CYAN_STAINED_GLASS_PANE) {
                    stats.addWisdom(1);
                    stats.addSkillPoints(-1);
                    GuiUtility.CreateUpgradeGui(player);
                }
                else if (event.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                    stats.addDexterity(1);
                    stats.addSkillPoints(-1);
                    GuiUtility.CreateUpgradeGui(player);
                }
                else if (event.getCurrentItem().getType() == Material.MAGENTA_STAINED_GLASS_PANE) {
                    stats.addArtisan(1);
                    stats.addSkillPoints(-1);
                    GuiUtility.CreateUpgradeGui(player);
                }
                PlayerUtility.setPlayerStats(player, stats);
            }
            event.setCancelled(true);
        }
        //When the player clicks in the spell book menu
        else if (invtitle.contains("content=\"Spell Book\""))
        {
            Style spellSelectMessageStyle = Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC);
            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
            if (event.getCurrentItem().getType() == Material.GRAY_DYE) {
                GuiUtility.CreateProfileGui(player);
            }

            if (event.getCurrentItem().getType() == Material.FIRE_CHARGE) {
                if (event.getSlot() < 18) {
                    stats.updateSpellBook(0,"Fireball");
                    player.sendMessage(Component.text("Standard spell set: ", spellSelectMessageStyle).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 27) {
                    stats.updateSpellBook(1,"Flame Dash");
                    player.sendMessage(Component.text("Dash spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 36) {
                    stats.updateSpellBook(2,"Flame Booster");
                    player.sendMessage(Component.text("Aerial spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 45) {
                    stats.updateSpellBook(3,"Meteor Fall");
                    player.sendMessage(Component.text("Ultimate spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
            }

            else if (event.getCurrentItem().getType() == Material.REDSTONE) {
                if (event.getSlot() < 18) {
                    stats.updateSpellBook(0,"Snipe");
                    player.sendMessage(Component.text("Standard spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 27) {
                    stats.updateSpellBook(1,"Fault In The Armor");
                    player.sendMessage(Component.text("Dash spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 36) {
                    stats.updateSpellBook(2,"Steam Rocket Pack");
                    player.sendMessage(Component.text("Aerial spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 45) {
                    stats.updateSpellBook(3,"Chrono Thief");
                    player.sendMessage(Component.text("Ultimate spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
            }

            else if (event.getCurrentItem().getType() == Material.NETHER_STAR) {
                if (event.getSlot() < 18) {
                    stats.updateSpellBook(0,"Healing Orb");
                    player.sendMessage(Component.text("Standard spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 27) {
                    stats.updateSpellBook(1,"Strength Of Faith");
                    player.sendMessage(Component.text("Dash spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 36) {
                    stats.updateSpellBook(2,"Angel Wings");
                    player.sendMessage(Component.text("Aerial spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 45) {
                    stats.updateSpellBook(3,"We Yield To None");
                    player.sendMessage(Component.text("Ultimate spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
            }

            else if (event.getCurrentItem().getType() == Material.OAK_WOOD) {
                if (event.getSlot() < 18) {
                    stats.updateSpellBook(0,"Nature's Bounty");
                    player.sendMessage(Component.text("Standard spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 27) {
                    stats.updateSpellBook(1,"Nature's Storage");
                    player.sendMessage(Component.text("Dash spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 36) {
                    stats.updateSpellBook(2,"Nature's Workbench");
                    player.sendMessage(Component.text("Aerial spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
                else if (event.getSlot() < 45) {
                    stats.updateSpellBook(3,"Nature's Diversity");
                    player.sendMessage(Component.text("Ultimate spell set: ", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)).append(Component.text(itemName, spellSelectMessageStyle)));
                }
            }

            event.setCancelled(true);

        }
    }
}
