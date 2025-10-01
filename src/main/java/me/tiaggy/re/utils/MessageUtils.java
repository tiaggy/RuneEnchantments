package me.tiaggy.re.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class MessageUtils {
    private static final HashMap<String, String> messages = new HashMap<>();

    public static void loadMessages(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (String key : Objects.requireNonNull(config.getConfigurationSection("messages")).getKeys(true)) {
            messages.put(key, config.getString("messages." + key));
        }
    }

    public static Component getMessage(String key){
        if (messages.containsKey(key)) {
            return Component.text(colorize(messages.get(key)));
        }else{
            return Component.text("No message for key '"+ key+"' found.");
        }
    }

    public static String colorize(String msg) {
        if (msg == null) return null;
        StringBuilder coloredMsg = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == '&')
                coloredMsg.append('§');
            else
                coloredMsg.append(msg.charAt(i));
        }
        return coloredMsg.toString();
    }
}
