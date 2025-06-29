package me.roupen.firstpluginthree.misc;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
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


}
