package com.github.messycraft.consolecommands.commands;

import com.github.messycraft.consolecommands.ConsoleCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.bukkit.Bukkit.getServer;

public class loopcmd implements CommandExecutor, TabExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        String arg = new String();
        for (int i=1;i<args.length;i++)
        {
            arg = arg + args[i] + " ";
        }

        // Anti-crash
        List<String> noloop = new ArrayList<>();
        for (int i=0;i<9;i++)
        {
            noloop.add("loopcmd " + i);
        }
        if (containsIgnoreCase(arg, noloop))
        {
            sender.sendMessage("&cDon't recursion execute this command.");
            return false;
        }

        // Loop commands
        if (args.length < 2)
        {
            sender.sendMessage("§eUsage: §6/loopcmd <looptimes> <command>");
        }
        else
        {
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (pattern.matcher(args[0]).matches() && !args[0].equals("+") && !args[0].equals("-"))
            {
                int looptimes = Integer.parseInt(args[0]);
                if (looptimes > 0)
                {
                    if (sender.hasPermission("consolecommands.loopcmd"))
                    {
                        Plugin mainclass = ConsoleCommands.getProvidingPlugin(ConsoleCommands.class);
                        if (looptimes <= mainclass.getConfig().getInt("times_limit") || mainclass.getConfig().getInt("times_limit") == -1)
                        {
                            if (getServer().dispatchCommand(sender, arg))
                            {
                                for (int i = 1; i < looptimes; i++)
                                {
                                    getServer().dispatchCommand(sender, arg);
                                }
                                sender.sendMessage("§eExecuting command §6" + arg + "§eby §b" + looptimes + " §etimes.");
                            }
                            else
                            {
                                sender.sendMessage("§eCommand §6" + arg + "§enot found");
                            }
                        }
                        else
                        {
                            sender.sendMessage("&c You are trying to execute that command &6" + looptimes + " &etimes, but you can't execute commands beyond &6" + mainclass.getConfig().getInt("times_limit") + " §etimes.");
                        }
                    }
                    else
                    {
                        sender.sendMessage("§cYou don't have permission to do that.");
                    }
                }
                else
                {
                    sender.sendMessage("§eUsage: §6/loopcmd <looptimes> <command>");
                    sender.sendMessage("§c<looptimes> Requires an integer! ");
                }
            }
            else
            {
                sender.sendMessage("§eUsage: §6/loopcmd <looptimes> <command>");
                sender.sendMessage("§c<looptimes> Requires an integer! ");
            }
        }
        return false;
    }

    // Tab complete
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args)
    {
        if (args.length == 1)
        {
            List<String> list = new ArrayList<>();
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");
            list.add("5");
            list.add("6");
            list.add("7");
            list.add("8");
            list.add("9");
            return list;
        }
        return null;
    }

    public boolean containsIgnoreCase(String s, List<String> list)
    {
        boolean c = false;
        s = s.toLowerCase();
        for (int i=0;i<list.size();i++)
        {
            if (s.contains(list.get(i)))
            {
                c = true;
                break;
            }
        }
        return c;
    }
}
