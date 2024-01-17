package com.github.messycraft.consolecommands.commands;

import com.github.messycraft.consolecommands.ConsoleCommands;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.*;

public class executeplayer implements CommandExecutor, TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        String arg = new String();
        for (int i=1;i<args.length;i++)
        {
            arg = arg + args[i] + " ";
        }
        if (args.length < 2)
        {
            sender.sendMessage("§eUsage: §6/executeplayer <player> [c:<text>/<command>]");
        }
        else
        {
            if (sender.hasPermission("consolecommands.execute.player"))
            {
                if(getPlayer(args[0]) == null)
                {
                    sender.sendMessage("§ePlayer §3" + args[0] + " §e was not found");
                }
                else
                {
                    Plugin mainclass = ConsoleCommands.getProvidingPlugin(ConsoleCommands.class);
                    if (arg.startsWith("c:"))
                    {
                        Player player = getPlayer(args[0]);
                        player.chat(arg.substring(2));
                        sender.sendMessage("§eForced §3" + args[0] + " §e to send a message");
                    }
                    else
                    {
                        if (mainclass.getConfig().getBoolean("allow_lp_command"))
                        {
                            cmdBannedP(arg,sender,getPlayer(args[0]));
                        }
                        else
                        {
                            Boolean allow = true;
                            if (arg.startsWith("lp")) allow = false;
                            if (allow && arg.startsWith("luckperms")) allow = false;
                            if (allow && arg.startsWith("perm")) allow = false;
                            if (allow && arg.startsWith("perms")) allow = false;
                            if (allow && arg.startsWith("permission")) allow = false;
                            if (allow && arg.startsWith("permissions")) allow = false;
                            if (allow)
                            {
                                cmdBannedP(arg,sender,getPlayer(args[0]));
                            }
                            else
                            {
                                sender.sendMessage("§cYou can't use LuckPerms-related commands as other players!");
                            }
                        }
                    }
                }
            }
            else
            {
                sender.sendMessage("§cYou don't have permission to do that.");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (args.length == 2)
        {
            List<String> list = new ArrayList<>();
            list.add("c:");
            return list;
        }
        return null;
    }

    public void cmdBannedP(String cmd, CommandSender sender, Player to)
    {
        Plugin m = ConsoleCommands.getProvidingPlugin(ConsoleCommands.class);
        List<String> list = m.getConfig().getStringList("bannedcmd_asplayer");
        boolean banned = false;
        for (int i=0;i<list.size();i++)
        {
            if (cmd.toLowerCase().startsWith(list.get(i).toLowerCase() + " "))
            {
                banned = true;
                break;
            }
        }
        if (!banned)
        {
            if (getServer().dispatchCommand(to, cmd))
            {
                sender.sendMessage("§eForced §3" + to.getName() + " §eexecute command §6" + cmd);
            }
            else
            {
                sender.sendMessage("§eCommand §6" + cmd + "§enot found");
            }
        }
        else
        {
            sender.sendMessage("§cYou can't execute the command as other player.");
        }
    }
}