package me.tiaggy.re.rune;

import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class RuneManager {

    private static RuneManager runeManager;

    public static RuneManager getRuneManager(){
        if (runeManager == null){
            runeManager = new RuneManager();
        }
        return runeManager;
    }

    private final HashMap<String, Rune> runes = new HashMap<>();

    public void addRune(String key, Rune rune){
        runes.put(key, rune);
    }

    public HashMap<String, Rune> getRuneMap(){
        return runes;
    }

    public Collection<Rune> getRunes(){
        return runes.values();
    }

    public static ItemStack getRuneBook(Rune rune){
        ItemStack itemStack = new ItemStack(Material.BOOK);
        rune.enchant(itemStack);
        ItemMeta meta = itemStack.getItemMeta();

        meta.customName(rune.name());
        meta.lore(List.of(rune.description()));

        meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
