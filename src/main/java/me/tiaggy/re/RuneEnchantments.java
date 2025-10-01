package me.tiaggy.re;

import me.tiaggy.re.command.RuneCommand;
import me.tiaggy.re.listener.RuneListener;
import me.tiaggy.re.listener.VeinSmeltListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static me.tiaggy.re.rune.RuneManager.getRuneManager;
import static me.tiaggy.re.utils.MessageUtils.loadMessages;

public final class RuneEnchantments extends JavaPlugin {

    private static RuneEnchantments instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        if(!messagesFile.exists()){
            saveResource("messages.yml", false);
        }
        loadMessages(messagesFile);

        getCommand("rune").setExecutor(new RuneCommand());

        Bukkit.getPluginManager().registerEvents(new RuneListener(), this);
        Bukkit.getPluginManager().registerEvents(new VeinSmeltListener(), this);

        getLogger().info("Runes loaded "+ getRuneManager().getRunes().size());
        getLogger().info("Plugin successfully enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin successfully disabled.");
    }

    public static RuneEnchantments getInstance() {
        return instance;
    }
}
