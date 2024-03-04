package me.roupen.firstpluginthree.PlayerInteractions;

import me.roupen.firstpluginthree.customgui.GuiUtility;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

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
            if (Objects.requireNonNull(event.getCurrentItem()).getType() == Material.DIAMOND) {
                player.closeInventory();
                GuiUtility.CreateUpgradeGui(player);
                event.setCancelled(true);
            }
            else
            {
                event.setCancelled(true);
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
                else if (event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {
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
                else {

                }
                PlayerUtility.setPlayerStats(player, stats);
            }
            event.setCancelled(true);
        }
    }
}
