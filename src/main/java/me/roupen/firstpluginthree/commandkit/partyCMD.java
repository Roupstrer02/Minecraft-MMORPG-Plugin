package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class partyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerStats Pstats = PlayerUtility.getPlayerStats(player);
            if (!Pstats.isInBossFight()) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "invite":
                            if (args.length == 2) {
                                if (Bukkit.getPlayer(args[1]) != null) {
                                    Player target = Bukkit.getPlayer(args[1]);
                                    if (target != player) {
                                        if (!Pstats.getParty().contains(target)) {
                                            PlayerStats Targetstats = PlayerUtility.getPlayerStats(target);
                                            Targetstats.setPendingInvite(player);
                                            target.sendMessage(Component.text("You've received a party invite from " + player.getName(), Style.style(NamedTextColor.YELLOW, TextDecoration.ITALIC)));
                                        } else {
                                            player.sendMessage(Component.text("Player already in party", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                                        }
                                    } else {
                                        player.sendMessage(Component.text("You know that's not how this works...", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                                    }
                                } else {
                                    player.sendMessage(Component.text("Player not found", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                                }
                            } else {
                                player.sendMessage(Component.text("Incorrect format: /party invite <PlayerName>", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                            }
                            break;
                        case "accept":

                            PlayerStats AcceptingMember = PlayerUtility.getPlayerStats(player);

                            //checks to see if an invitation has been received
                            if (AcceptingMember.getPendingInvite() != null) {

                                //delete player from the party list of every old party member
                                for (Player p : AcceptingMember.getParty()) {
                                    if (p != player) {
                                        PlayerStats PartyMemberStats = PlayerUtility.getPlayerStats(p);
                                        PartyMemberStats.removeMemberFromParty(player);
                                    }
                                }
                                //add player to party of players in target's party
                                PlayerStats Tstats = PlayerUtility.getPlayerStats(AcceptingMember.getPendingInvite());
                                ArrayList<Player> TOldParty = new ArrayList<>();
                                TOldParty.addAll(Tstats.getParty());
                                for (Player p : TOldParty) {
                                    if (p != player) {
                                        PlayerStats PartyMemberStats = PlayerUtility.getPlayerStats(p);
                                        PartyMemberStats.addMemberToParty(player);
                                    } else {
                                        break;
                                    }
                                }
                                //target's party = player's party
                                ArrayList<Player> newAcceptingMemberParty = new ArrayList<>();
                                newAcceptingMemberParty.addAll(Tstats.getParty());
                                AcceptingMember.setParty(newAcceptingMemberParty);

                                for (Player p : AcceptingMember.getParty()) {
                                    p.sendMessage(Component.text(player.getName() + " has joined the party!", Style.style(NamedTextColor.GREEN)));
                                }
                                // if no invite has been received this session
                            } else {
                                player.sendMessage(Component.text("You have no pending invite to accept", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                            }


                            break;

                        case "leave":
                            //delete player from all party member's lists
                            PlayerStats LeavingMember = PlayerUtility.getPlayerStats(player);
                            ArrayList<Player> LeavingMemberOGParty = new ArrayList<>();
                            LeavingMemberOGParty.addAll(LeavingMember.getParty());

                            for (Player p : LeavingMemberOGParty) {
                                if (p != player) {
                                    PlayerStats PartyMemberStats = PlayerUtility.getPlayerStats(p);

                                    PartyMemberStats.removeMemberFromParty(player);
                                    LeavingMember.removeMemberFromParty(p);
                                }
                            }
                            break;

                        case "help":
                            Component message = Component.text("=================================================\n");
                            message = message.append(Component.text("/party - view the list of current party members\n"));
                            message = message.append(Component.text("/party invite <Player> - invites that player to your party\n"));
                            message = message.append(Component.text("/party accept - Accepts most recent invite\n"));
                            message = message.append(Component.text("================================================="));
                            player.sendMessage(message);
                            break;
                        default:
                            player.sendMessage(Component.text("Incorrect use of /party command, write \"/party help\" for more info", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                            break;
                    }
                } else {
                    player.sendMessage(PlayerUtility.getPlayerStats(player).getViewPartyMessage());
                }
            } else {
                player.sendMessage(Component.text("Cannot use /party command during a bossfight", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
            }
        }
        return true;
    }
}
