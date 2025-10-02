package me.tiaggy.re.rune;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static me.tiaggy.re.rune.RuneManager.getRuneManager;

public class Rune {

    private final @NonNull TextComponent name;

    private final @NonNull TextComponent description;

    private final @NonNull NamespacedKey namespacedKey;

    private final @NonNull Tag<Material> enchantableItemsTag;

    public Rune(@NonNull TextComponent name, @NonNull TextComponent description, @NonNull NamespacedKey namespacedKey, @NonNull Tag<Material> enchantableItemsTag) {
        this.name = name;
        this.description = description;
        this.namespacedKey = namespacedKey;
        this.enchantableItemsTag = enchantableItemsTag;
        getRuneManager().addRune(namespacedKey.getKey(), this);
    }

    public boolean isEnchantable(ItemStack itemStack){
        return enchantableItemsTag.isTagged(itemStack.getType()) && !isEnchanted(itemStack);
    }

    public boolean isEnchanted(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if (meta !=null) {
            PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
            return dataContainer.has(namespacedKey);
        }
        return false;
    }
    
    public void enchant(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();

        // Adds new persistent item data: adds boolean value true for rune key.
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        dataContainer.set(namespacedKey, PersistentDataType.BOOLEAN, true);

        // Adds new line to the item lore.
        List<Component> lore = new ArrayList<>();
        if (meta.lore() != null) {
            lore.addAll(meta.lore());
        }
        lore.add(name);
        meta.lore(lore);

        itemStack.setItemMeta(meta);
    }

    public @NonNull TextComponent name(){
        return name;
    }

    public @NonNull TextComponent description(){
        return description;
    }

}
