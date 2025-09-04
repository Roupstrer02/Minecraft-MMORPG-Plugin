package me.roupen.firstpluginthree.PlayerInteractions;

import me.roupen.firstpluginthree.wands.wand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.Arrays;

public class WandCrafting {

    public static void Interact(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE)))
        {
            /*
            * The Steps of Wand Crafting:
            * 1. Check for Valid recipe in toolbar
            * 2. If valid:
            *       Create a wand in the player's middle toolbar slot
            * 3. if not valid:
            *       Send appropriate message as to why
            * */

            Inventory PlayerEnv = player.getInventory();
            ArrayList<ItemStack> hotbar = new ArrayList<>();
            ArrayList<Material> ValidCoreTypes = new ArrayList<Material>();

            ValidCoreTypes.add(Material.STICK);
            ValidCoreTypes.add(Material.AMETHYST_SHARD);
            ValidCoreTypes.add(Material.BLAZE_ROD);
            ValidCoreTypes.add(Material.ECHO_SHARD);

            Material[] componentTypesList = {
                    Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD, Material.NETHERITE_INGOT,
                    Material.LAPIS_BLOCK, Material.MAGMA_CREAM, Material.GHAST_TEAR, Material.NETHER_STAR, Material.ENDER_PEARL, Material.REDSTONE_BLOCK,
                    Material.QUARTZ_BLOCK, Material.PRISMARINE_SHARD, Material.GLOWSTONE, Material.BLAZE_POWDER, Material.DRAGON_BREATH, Material.SLIME_BALL

            };

            ArrayList<Material> ValidComponentTypes = new ArrayList<>(Arrays.asList(componentTypesList));



            for (int i = 0; i < 9; i++)
            {
                hotbar.add(PlayerEnv.getItem(i));
            }

            if ((hotbar.get(4) != null) && (ValidCoreTypes.contains(hotbar.get(4).getType())) && !(wand.IsWand(hotbar.get(4))))
            {
                Material CoreType = hotbar.get(4).getType();
                boolean validItemCount = true;
                //checks if there is only one item in each slot of hotbar
                for (int i = 0; i < 9; i++) {
                    if ((hotbar.get(i) != null) && hotbar.get(i).getAmount() > 1) {
                        validItemCount = false;
                    }
                }

                if (validItemCount)
                {
                    //checks if only valid ingredients are present in hotbar
                    boolean validItemTypes = true;
                    for (int i = 0; i < 9; i++) {
                        if ( (i != 4) && (hotbar.get(i) != null) && !ValidComponentTypes.contains(hotbar.get(i).getType())) {
                            player.sendMessage("fail" + " " + i);
                            validItemTypes = false;
                        }
                    }

                    if (validItemTypes) {
                        //craft the wand
                        player.sendMessage(Component.text("Crafting success!", Style.style(NamedTextColor.AQUA, TextDecoration.ITALIC)));

                        for (int i = 0; i < 9; i++)
                        {
                            player.getInventory().setItem(i, null);
                        }

                        player.getInventory().setItem(4, wand.WandToItem(WandCrafting.create(hotbar)));

                    }else{
                        //invalid type of items
                        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1f, 1f);
                        player.sendMessage(Component.text("Crafting failed: Invalid crafting components detected", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                    }
                }else{
                    //invalid amounts of ingredients (more than 1)
                    player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1f, 1f);
                    player.sendMessage(Component.text("Crafting failed: Invalid item counts in hotbar (there should be 1 item in each item stack of your hotbar)", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                }
            }else{
                //invalid core type
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1f, 1f);
                player.sendMessage(Component.text("Crafting failed: Invalid core type in center of hotbar", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
            }

            event.setCancelled(true);
        }
    }

    public static wand create(ArrayList<ItemStack> ingredients) {

        Material coreType = ingredients.get(4).getType();
        wand NewWand = new wand(coreType, "Spell Wand");
        Material nextIngredient;
        ingredients.remove(4);

        for (ItemStack ingredient : ingredients)
        {
            /*
            * Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD, Material.NETHERITE_INGOT,
              Material.LAPIS_BLOCK, Material.MAGMA_CREAM, Material.GHAST_TEAR, Material.NETHER_STAR, Material.ENDER_PEARL, Material.REDSTONE_BLOCK,
              Material.QUARTZ_BLOCK, Material.PRISMARINE_SHARD, Material.GLOWSTONE, Material.BLAZE_POWDER, Material.DRAGON_BREATH, Material.SLIME_BALL
            * */

            //Make sure it's not possible to reach obscenely high levels of efficiency ==================== Different calc function? == Diminishing returns?
            if (ingredient != null) {
                nextIngredient = ingredient.getType();
                switch (nextIngredient) {
                    case COPPER_INGOT:
                        NewWand.setSpellCostModifier(NewWand.getSpellCostModifier() - ( 0.04  * (NewWand.getTier() * 0.5)));
                        break;
                    case IRON_INGOT:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.03 * (NewWand.getTier() * 0.5)));
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.04 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.03 * (NewWand.getTier() * 0.5)));
                        break;
                    case GOLD_INGOT:
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.08 * (NewWand.getTier() * 0.5)));
                        break;
                    case DIAMOND:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.08 * (NewWand.getTier() * 0.5)));
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.08 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.04 * (NewWand.getTier() * 0.5)));
                        break;
                    case EMERALD:
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.05 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.08 * (NewWand.getTier() * 0.5)));
                        break;
                    case NETHERITE_INGOT:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.15 * (NewWand.getTier() * 0.5)));
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.15 * (NewWand.getTier() * 0.5)));
                        break;
                    case LAPIS_BLOCK:
                        NewWand.setSpellCostModifier(NewWand.getSpellCostModifier() - ( 0.10  * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.03 * (NewWand.getTier() * 0.5)));
                        break;
                    case MAGMA_CREAM:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.07 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.055 * (NewWand.getTier() * 0.5)));
                        break;
                    case GHAST_TEAR:
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.08 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.04 * (NewWand.getTier() * 0.5)));
                        break;
                    case NETHER_STAR:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.20 * (NewWand.getTier() * 0.5)));
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.20 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.10 * (NewWand.getTier() * 0.5)));
                        break;
                    case ENDER_PEARL:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.05 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.07 * (NewWand.getTier() * 0.5)));
                        break;
                    case REDSTONE_BLOCK:
                        NewWand.setSpellCostModifier(NewWand.getSpellCostModifier() - ( 0.18  * (NewWand.getTier() * 0.5)));
                        break;
                    case QUARTZ_BLOCK:
                        NewWand.setSpellCostModifier(NewWand.getSpellCostModifier() - ( 0.14  * (NewWand.getTier() * 0.5)));
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.10 * (NewWand.getTier() * 0.5)));
                        break;
                    case PRISMARINE_SHARD:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.09 * (NewWand.getTier() * 0.5)));
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.06 * (NewWand.getTier() * 0.5)));
                        break;
                    case GLOWSTONE:
                        NewWand.setSpellCostModifier(NewWand.getSpellCostModifier() - (0.05  * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.10 * (NewWand.getTier() * 0.5)));
                        break;
                    case BLAZE_POWDER:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.25 * (NewWand.getTier() * 0.5)));
                        break;
                    case DRAGON_BREATH:
                        NewWand.setOffenseSpellPowerModifier(NewWand.getOffenseSpellPowerModifier() + (0.30 * (NewWand.getTier() * 0.5)));
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.05 * (NewWand.getTier() * 0.5)));
                        break;
                    case SLIME_BALL:
                        NewWand.setDefenseSpellPowerModifier(NewWand.getDefenseSpellPowerModifier() + (0.10 * (NewWand.getTier() * 0.5)));
                        NewWand.setUtilitySpellPowerModifier(NewWand.getUtilitySpellPowerModifier() + (0.05 * (NewWand.getTier() * 0.5)));
                        break;
                }
            }
        }

        NewWand.setSpellCostModifier(Math.max(0.6, NewWand.getSpellCostModifier()));
        return NewWand;

    }

}
