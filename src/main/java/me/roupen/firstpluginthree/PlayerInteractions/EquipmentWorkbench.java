package me.roupen.firstpluginthree.PlayerInteractions;

import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static me.roupen.firstpluginthree.customgui.GuiUtility.CreateEquipmentWorkbenchGui;
import static me.roupen.firstpluginthree.customgui.GuiUtility.CreateRuneGui;

//=========================================================================================================
//                                          Out Of Order
//                     As of 2023-12-16, the Equipment Workbench is no longer used
//=========================================================================================================
public class EquipmentWorkbench {

    public static void Interact(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.SMITHING_TABLE)))
        {
            CreateEquipmentWorkbenchGui(player);
            event.setCancelled(true);
        }
    }

    public static void ClickMenu(InventoryClickEvent event)
    {
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();

        String invtitle = event.getView().title().toString();

        PlayerEquipment new_equipment;

        //The block that handles giving the player the item they want and checking if they have the materials to make it
        if (Objects.requireNonNull(event.getClickedInventory()).getHolder() == null && invtitle.contains("content=\"Equipment Workbench\""))
        {
            switch (slot){
                case 11:
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 4) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 2)) {
                        new_equipment = new PlayerEquipment(0, Material.WOODEN_SWORD, "Dagger");
                        new_equipment.setDamage(10.0);
                        new_equipment.setStaminaCost(3.0);
                        new_equipment.setName("Dagger");
                        player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 4));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 2));
                        player.getInventory().addItem(PlayerEquipment.EquipmentToItem(new_equipment));
                    }else{
                        player.sendMessage(Component.text("You don't have sufficient materials!", Style.style(NamedTextColor.RED)));
                    }
                    event.setCancelled(true);
                    break;
                case 13:
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 8) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 4)) {
                        new_equipment = new PlayerEquipment(0, Material.WOODEN_SWORD, "Longsword");
                        new_equipment.setDamage(20.0);
                        new_equipment.setStaminaCost(5.0);
                        new_equipment.setName("Longsword");
                        player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 8));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 4));
                        player.getInventory().addItem(PlayerEquipment.EquipmentToItem(new_equipment));
                    }else{
                        player.sendMessage(Component.text("You don't have sufficient materials!", Style.style(NamedTextColor.RED)));
                    }
                    event.setCancelled(true);
                    break;
                case 15:
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 16) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 8)) {
                        new_equipment = new PlayerEquipment(0, Material.WOODEN_SWORD, "Greatsword");
                        new_equipment.setDamage(35.0);
                        new_equipment.setStaminaCost(14.0);
                        new_equipment.setName("Greatsword");
                        player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 16));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 8));
                        player.getInventory().addItem(PlayerEquipment.EquipmentToItem(new_equipment));
                    }else{
                        player.sendMessage(Component.text("You don't have sufficient materials!", Style.style(NamedTextColor.RED)));
                    }
                    event.setCancelled(true);
                    break;
                case 29:
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.STRING), 5) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 12)) {
                        new_equipment = new PlayerEquipment(0, Material.BOW, "Shortbow");
                        new_equipment.setDamage(15.0);
                        new_equipment.setStaminaCost(6.0);
                        new_equipment.setName("Shortbow");
                        player.getInventory().removeItem(new ItemStack(Material.STRING, 5));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 12));
                        player.getInventory().addItem(PlayerEquipment.EquipmentToItem(new_equipment));
                    }else{
                        player.sendMessage(Component.text("You don't have sufficient materials!", Style.style(NamedTextColor.RED)));
                    }
                    event.setCancelled(true);
                    break;
                case 31:
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.STRING), 8) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 20)) {
                        new_equipment = new PlayerEquipment(0, Material.BOW, "Longbow");
                        new_equipment.setDamage(30.0);
                        new_equipment.setStaminaCost(11.0);
                        new_equipment.setName("Longbow");
                        player.getInventory().removeItem(new ItemStack(Material.STRING, 8));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 20));
                        player.getInventory().addItem(PlayerEquipment.EquipmentToItem(new_equipment));
                    }else{
                        player.sendMessage(Component.text("You don't have sufficient materials!", Style.style(NamedTextColor.RED)));
                    }
                    event.setCancelled(true);
                    break;
                case 33:
                    if (player.getInventory().containsAtLeast(new ItemStack(Material.STRING), 5) && player.getInventory().containsAtLeast(new ItemStack(Material.STICK), 8) && player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), 2)) {
                        new_equipment = new PlayerEquipment(0, Material.CROSSBOW, "Crossbow");
                        new_equipment.setDamage(20.0);
                        new_equipment.setStaminaCost(7.0);
                        new_equipment.setName("Crossbow");
                        player.getInventory().removeItem(new ItemStack(Material.STRING, 5));
                        player.getInventory().removeItem(new ItemStack(Material.STICK, 8));
                        player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, 2));
                        player.getInventory().addItem(PlayerEquipment.EquipmentToItem(new_equipment));
                    }else{
                        player.sendMessage(Component.text("You don't have sufficient materials!", Style.style(NamedTextColor.RED)));
                    }
                    event.setCancelled(true);
                    break;
                default:
                    event.setCancelled(true);
            }
        }
    }
}
