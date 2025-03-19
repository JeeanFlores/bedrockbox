package io.github.jeeanflores.bedrockBox.commands.completer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TntChallengeCommandCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("tntchallenge")) {
            if (strings.length == 1) {

                list.add("build");
                list.add("reset");
                list.add("tnt");
                list.add("rain");
                list.add("zeus");
            } /*else if (strings.length == 2 || strings.length == 4) {

                Bukkit.getOnlinePlayers().forEach(player -> list.add(player.getName()));
            }

            if (strings.length == 2 || strings.length == 3){
                list.add("10");
                list.add("20");
                list.add("30");
                list.add("40");
                list.add("50");
            }*/
        }

        return list;
    }
}
