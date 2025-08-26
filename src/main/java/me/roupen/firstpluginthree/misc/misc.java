package me.roupen.firstpluginthree.misc;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class misc {

    public static void JoinAnnouncement(Player player) {

        Component AnnouncementMessage =
                Component.text("==========");

        player.sendMessage(AnnouncementMessage);
    }

    //a list of all item meterial types that can spawn as equipment
    public static ArrayList<Material> allArmorEquipmentMats = new ArrayList<>(Arrays.asList(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS,
            Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD,
            Material.BOW, Material.CROSSBOW
    ));

    //old rounding function used before DecimalFormat was understood
    public static double round(double x) {
        return ((Math.floor(100 * x)) / 100);
    }

    //kills all undead mobs at daybreak
    public static void UndeadBurn(World world) {
        List<LivingEntity> mobs = world.getLivingEntities();

        for (LivingEntity mob : mobs) {
            if ((mob instanceof Monster) && mob.getWorld().getEnvironment() == World.Environment.NORMAL) {
                mob.remove();
            }
        }
    }

    public static boolean isInteractable(Block block)
    {
        Material type = block.getType();
        boolean interactable = type.isInteractable();
        if (!interactable)
            return false;

        switch (type)
        {
            case ACACIA_STAIRS:
            case ANDESITE_STAIRS:
            case BIRCH_STAIRS:
            case BLACKSTONE_STAIRS:
            case BRICK_STAIRS:
            case COBBLESTONE_STAIRS:
            case CRIMSON_STAIRS:
            case DARK_OAK_STAIRS:
            case DARK_PRISMARINE_STAIRS:
            case DIORITE_STAIRS:
            case END_STONE_BRICK_STAIRS:
            case GRANITE_STAIRS:
            case JUNGLE_STAIRS:
            case MOSSY_COBBLESTONE_STAIRS:
            case MOSSY_STONE_BRICK_STAIRS:
            case NETHER_BRICK_STAIRS:
            case OAK_STAIRS:
            case POLISHED_ANDESITE_STAIRS:
            case POLISHED_BLACKSTONE_BRICK_STAIRS:
            case POLISHED_BLACKSTONE_STAIRS:
            case POLISHED_DIORITE_STAIRS:
            case POLISHED_GRANITE_STAIRS:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_STAIRS:
            case PURPUR_STAIRS:
            case QUARTZ_STAIRS:
            case RED_NETHER_BRICK_STAIRS:
            case RED_SANDSTONE_STAIRS:
            case SANDSTONE_STAIRS:
            case SMOOTH_QUARTZ_STAIRS:
            case SMOOTH_RED_SANDSTONE_STAIRS:
            case SMOOTH_SANDSTONE_STAIRS:
            case SPRUCE_STAIRS:
            case STONE_BRICK_STAIRS:
            case STONE_STAIRS:
            case WARPED_STAIRS:

            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case CRIMSON_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case MOVING_PISTON:
            case NETHER_BRICK_FENCE:
            case OAK_FENCE:
            case PUMPKIN:
            case REDSTONE_ORE:
            case REDSTONE_WIRE:
            case SPRUCE_FENCE:
            case WARPED_FENCE:
                return false;
            default:
                return true;
        }

    }

    public static void BuildCircle(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals("Roupstrer02")) {

            if (player.getInventory().getItemInMainHand().getType() == Material.STICK) {

                World world = player.getWorld();
                Location loc = player.getLocation();
                int X = loc.getBlockX();
                int Y = loc.getBlockY();
                int Z = loc.getBlockZ();
                int Xdirection = 1;
                int Zdirection = 1;
                ItemStack config = player.getInventory().getItemInOffHand();
                int radius = config.getAmount();
                Material mat = config.getType();

                double lambda = 0.5;

                int[][] map = new int[radius][radius];
                for (int k = 0; k < 4; k++) {
                    for (int i = 0; i < radius; i++) {
                        for (int j = 0; j < radius; j++) {
                            double radval = Math.sqrt(Math.pow(i+1, 2) + Math.pow(j+1, 2));
                            if (radval >= (radius - lambda) && radval <= (radius + lambda)) {
                                //map[i][j] = 1;
                                world.getBlockAt(X + (i * Xdirection), Y,Z+(j * Zdirection)).setType(mat);
                            }
                        }
                    }
                    if (k % 2 == 0) {
                        Xdirection = -Xdirection;
                    }else{
                        Zdirection = -Zdirection;
                    }
                }


            }
        }
    }
    public static void LongRangeBuild(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (player.getName().equals("Roupstrer02")) {


            if (player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
                World world = player.getWorld();
                Location loc = player.getLocation();
                loc.add(0,player.getEyeHeight(),0);
                ItemStack config = player.getInventory().getItemInOffHand();
                Material mat = config.getType();
                Block currentBlock;

                BlockIterator iter = new BlockIterator(player, 100);
                BlockIterator iterOneBehind = new BlockIterator(player, 100);
                iter.next();
                Block previousBlock = iterOneBehind.next();
                while (iter.hasNext()) {
                    currentBlock = iter.next();

                    if (currentBlock.getType() != Material.AIR) {
                        previousBlock.setType(mat);
                    }
                    iterOneBehind.next();
                }
            }
        }
    }
    public static String IntToRomanInteger(int num) {
        num += 1;
        StringBuilder newRomanNum = new StringBuilder();

        while (num > 0) {

            if (num >= 100) {
                num -= 100;
                newRomanNum.append("C");
            }
            else if (num >= 90) {
                num -= 90;
                newRomanNum.append("XC");
            }
            else if (num >= 50) {
                num -= 50;
                newRomanNum.append("L");
            }
            else if (num >= 40) {
                num -= 40;
                newRomanNum.append("XL");
            }
            else if (num >= 10) {
                num -= 10;
                newRomanNum.append("X");
            }
            else if (num >= 9) {
                num -= 9;
                newRomanNum.append("IX");
            }
            else if (num >= 5) {
                num -= 5;
                newRomanNum.append("V");
            }
            else if (num >= 4) {
                num -= 4;
                newRomanNum.append("IV");
            }
            else {
                num -= 1;
                newRomanNum.append("I");
            }
        }


        return newRomanNum.toString();

    }

    public static String getplayerHPDisplay(double HP_Value) {

        String str = "";

        if (HP_Value > 10000) {
            HP_Value /= 1000;
            str += ((int) Math.round(HP_Value)) + "k";

        }else{
            str += ((int) Math.round(HP_Value));
        }
        return str;
    }

}
