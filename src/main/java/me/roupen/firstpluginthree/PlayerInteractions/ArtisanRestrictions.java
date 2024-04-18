package me.roupen.firstpluginthree.PlayerInteractions;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.artisan.CookingRecipes;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ArtisanRestrictions {

    private static void levelTooLowMsg(Player player, int level)
    {
        player.sendMessage(Component.text("Artisan level too low - level " + level + " required", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.25f, 1.0f);
    }



    public static void Interact(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        //Player needs artisan level 5 to access the smoker
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
            (event.getClickedBlock().getType().equals(Material.SMOKER)) &&
            (PlayerUtility.getPlayerStats(player).getArtisan() < 5))
        {
            levelTooLowMsg(player, 5);
            event.setCancelled(true);
        }

        //Player needs artisan level 10 to access the brewing stand
        else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
           (event.getClickedBlock().getType().equals(Material.BREWING_STAND)) &&
           (PlayerUtility.getPlayerStats(player).getArtisan() < 10))
        {
            levelTooLowMsg(player, 10);
            event.setCancelled(true);
        }

    }

    public static void ClickMenu(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        InventoryType invType = event.getClickedInventory().getType();
        InventoryType.SlotType slotType = event.getSlotType();
        ItemStack clickedItem = event.getCurrentItem();
        CookingRecipes cookingRecipes = FirstPluginThree.getCookingrecipes();
        int ArtisanLevel = PlayerUtility.getPlayerStats(player).getArtisan();

        //Artisan restrictions for crafting table
        if ((invType == InventoryType.WORKBENCH) && (slotType == InventoryType.SlotType.RESULT)) {

            //Sweet Bread
            if (clickedItem.equals(cookingRecipes.getItems().get("Sweet Bread")) && ArtisanLevel < 15)
            {
                levelTooLowMsg(player, 15);
                event.setCancelled(true);
            }

            //Unfermented Mead
            else if (clickedItem.equals(cookingRecipes.getItems().get("Unfermented Mead")) && ArtisanLevel < 10)
            {
                levelTooLowMsg(player, 10);
                event.setCancelled(true);
            }

            //Beer Wort
            else if (clickedItem.equals(cookingRecipes.getItems().get("Beer Wort")) && ArtisanLevel < 18)
            {
                levelTooLowMsg(player, 18);
                event.setCancelled(true);
            }
            else if (clickedItem.equals(cookingRecipes.getItems().get("Ithilian Fern Powder")) && ArtisanLevel < 8)
            {
                levelTooLowMsg(player, 8);
                event.setCancelled(true);
            }
        }

    }

}
