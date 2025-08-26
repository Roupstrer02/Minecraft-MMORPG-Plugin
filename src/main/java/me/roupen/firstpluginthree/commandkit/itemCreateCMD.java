package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class itemCreateCMD implements CommandExecutor {

    private final String[] fightingStyles = {"rogue", "knight", "slayer"};
    private final String[] mobtypes = {"ZOMBIE", "SKELETON", "CREEPER", "SPIDER", "ENDERMAN"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        //creates an enemy of a given level, for testing purposes
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (args.length == 4) {

                int level = Integer.parseInt(args[0]);
                int rarity = Integer.parseInt(args[1]);
                String mobType = args[2];
                String toolType = args[3];

                if (player.getInventory().firstEmpty() > -1)
                    player.getInventory().addItem(PlayerEquipment.EquipmentToItem(
                            PlayerEquipment.GenerateSpecificEquipment(level, rarity, mobType.toUpperCase(), toolType)
                    ));
            }
            else if (args.length == 2) {
                int level = Integer.parseInt(args[1]);
                Random rd = new Random();

                player.getInventory().addItem(
                        PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Helmet")),
                        PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Chestplate")),
                        PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Leggings")),
                        PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Boots"))
                );

                //For the Rogue set
                if (args[0].toLowerCase().equals(fightingStyles[0])) {
                    player.getInventory().addItem(
                            PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Dagger")),
                            PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Dagger"))
                    );
                }

                //For the Knight set
                else if (args[0].toLowerCase().equals(fightingStyles[1])) {
                    player.getInventory().addItem(
                            PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Longsword")),
                            PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Shield"))
                    );
                }

                //For the Slayer set
                else if (args[0].toLowerCase().equals(fightingStyles[2])) {
                    player.getInventory().addItem(
                            PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateSpecificEquipment(level, rd.nextInt(5), mobtypes[rd.nextInt(5)], "Greatsword"))
                    );
                }
                else {
                    player.sendMessage(Component.text("Fighting style not found", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                }
            }
        }

        return true;
    }
}
