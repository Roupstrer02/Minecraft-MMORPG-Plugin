package me.roupen.firstpluginthree.PlayerInteractions;

import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.playerequipment.Rune;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.roupen.firstpluginthree.customgui.GuiUtility.CreateRuneGui;

public class RuneForge {

    public static void Interact(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType().equals(Material.ANVIL) || event.getClickedBlock().getType().equals(Material.CHIPPED_ANVIL) || event.getClickedBlock().getType().equals(Material.DAMAGED_ANVIL)))
        {
            CreateRuneGui(player);
            event.setCancelled(true);
        }
    }

    public static void ClickMenu(InventoryClickEvent event)
    {
        int slot = event.getSlot();
        Player player = (Player) event.getWhoClicked();

        ItemStack tool;
        ItemStack rune;
        String invtitle = event.getView().title().toString();

        if (!(event.isShiftClick() || event.getClick() == ClickType.DOUBLE_CLICK))
        {
            if ((event.getClickedInventory()).getHolder() == null && invtitle.contains("content=\"Rune Forge\"")) {
                if (slot == 12) {
                    //when putting the tool in
                    if (event.getCurrentItem() == null) {
                        tool = player.getItemOnCursor();

                        if (event.getClickedInventory().getItem(30) != null) {
                            rune = event.getClickedInventory().getItem(30);

                            if (Rune.isRune(rune)) {
                                if (!Rune.isRune(tool)) {
                                    PlayerEquipment result_tool_equip = PlayerEquipment.ItemToEquipment(tool);
                                    result_tool_equip.setRunes(new Rune[]{Rune.ItemToRune(rune)});
                                    event.getClickedInventory().setItem(23, PlayerEquipment.EquipmentToItem(result_tool_equip));
                                } else {
                                    if ((Rune.ItemToRune(rune).getLevel() == 1 && Rune.ItemToRune(tool).getLevel() == 1)) {
                                        Rune combinedRune = Rune.CombineRunes(Rune.ItemToRune(tool), Rune.ItemToRune(rune));
                                        event.getClickedInventory().setItem(23, combinedRune.BuildItem());
                                    }

                                }
                            }
                        }
                    }
                    //when picking the tool back up
                    else {
                        event.getClickedInventory().setItem(23, null);
                    }
                } else if (slot == 30) {
                    //when putting the rune in
                    if (event.getCurrentItem() == null) {
                        rune = player.getItemOnCursor();
                        if (Rune.isRune(rune)) {
                            //updates the tool in case the user put the tool first
                            if (event.getClickedInventory().getItem(12) != null) {
                                tool = event.getClickedInventory().getItem(12);

                                if (Rune.isRune(tool)) {
                                    if ((Rune.ItemToRune(rune).getLevel() == 1 && Rune.ItemToRune(tool).getLevel() == 1)) {
                                        Rune combinedRune = Rune.CombineRunes(Rune.ItemToRune(tool), Rune.ItemToRune(rune));
                                        event.getClickedInventory().setItem(23, combinedRune.BuildItem());
                                    }
                                } else {
                                    PlayerEquipment result_tool_equip = PlayerEquipment.ItemToEquipment(tool);
                                    result_tool_equip.setRunes(new Rune[]{Rune.ItemToRune(rune)});
                                    event.getClickedInventory().setItem(23, PlayerEquipment.EquipmentToItem(result_tool_equip));
                                }

                            }
                        }
                        //when picking the rune back out
                        else {
                            event.setCancelled(true);
                        }
                    } else {
                        event.getClickedInventory().setItem(23, null);
                    }
                } else if (slot == 23) {
                    if (event.getCurrentItem() != null) {
                        ItemStack item1 = event.getClickedInventory().getItem(12);
                        ItemStack item2 = event.getClickedInventory().getItem(30);
                        item1.setAmount(item1.getAmount() - 1);
                        item2.setAmount(item2.getAmount() - 1);
                        event.getClickedInventory().setItem(12, item1);
                        event.getClickedInventory().setItem(30, item2);
                        if (item1.getAmount() > 0 && item2.getAmount() > 0)
                        {
                            //supposed to create a new copy of the crafted item if there's enough ingredient items to make it
                            player.getInventory().addItem(event.getCurrentItem());
                            event.setCancelled(true);
                        } else {
                            player.getInventory().addItem(event.getCurrentItem());
                            event.getClickedInventory().setItem(23, null);
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
        //if the event is a shift click, just cancel it, it's not worth dealing with...
        else{
            event.setCancelled(true);
        }
    }

    public static void EarlyExit(InventoryCloseEvent event)
    {
        ItemStack returneditem1;
        ItemStack returneditem2;

        if (Objects.requireNonNull(event.getInventory()).getHolder() == null && event.getView().title().equals(Component.text("Rune Forge")))
        {
            returneditem1 = event.getInventory().getItem(12);
            returneditem2 = event.getInventory().getItem(30);
            if (returneditem1 != null)
                event.getPlayer().getInventory().addItem(returneditem1);
            if (returneditem2 != null)
                event.getPlayer().getInventory().addItem(returneditem2);
        }
    }


}
