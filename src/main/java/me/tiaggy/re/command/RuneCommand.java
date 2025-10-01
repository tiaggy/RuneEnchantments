package me.tiaggy.re.command;

import me.tiaggy.re.rune.Rune;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.tiaggy.re.rune.RuneManager.getRuneBook;
import static me.tiaggy.re.rune.RuneManager.getRuneManager;
import static me.tiaggy.re.utils.MessageUtils.getMessage;

public class RuneCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        //Send help message if it's not give subcommand
        if(args.length == 0 || !args[0].equalsIgnoreCase("give")) {
            commandSender.sendMessage(getMessage("command.rune.help"));
            return true;
        }

        if(args.length != 3) {
            commandSender.sendMessage(getMessage("command.rune.give.usage"));
            return true;
        }

        if(!commandSender.hasPermission("re.rune.give")) {
            commandSender.sendMessage(getMessage("command.rune.give.no_permission"));
            return true;
        }

        Player player = Bukkit.getPlayerExact(args[1]);
        if (player == null){
            commandSender.sendMessage(getMessage("command.rune.give.invalid_player_name"));
            return true;
        }

        HashMap<String, Rune> runeMap = getRuneManager().getRuneMap();
        if (!runeMap.containsKey(args[2])){
            commandSender.sendMessage(getMessage("command.rune.give.invalid_rune_name"));
            return true;
        }
        Rune rune = runeMap.get(args[2]);
        player.give(getRuneBook(rune));
        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

        commandSender.sendMessage(getMessage("command.rune.give.success"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> list = new ArrayList<>();
        if  (args.length == 1){
            if ("give".startsWith(args[0])) {
                list.add("give");
            }
        }

        if(args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
                Bukkit.getOnlinePlayers().stream().filter(player -> player.getName().startsWith(args[1])).forEach(player -> list.add(player.getName()));
            }else if (args.length == 3) {
                getRuneManager().getRuneMap().keySet().stream().filter(key -> key.startsWith(args[2])).forEach(list::add);
            }
        }
        return list;
    }
}
