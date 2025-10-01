package me.tiaggy.re.listener;

import me.tiaggy.re.rune.Rune;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static me.tiaggy.re.rune.RuneManager.getRuneManager;

public class RuneListener implements Listener {

    private final HashMap<UUID, AnvilInventory> inventoryHashMap = new HashMap<>();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        UUID uuid = event.getPlayer().getUniqueId();
        inventoryHashMap.remove(uuid);
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack firstItem = inventory.getFirstItem();
        ItemStack secondItem = inventory.getSecondItem();

        /*
        *   If there are two items in anvil and second is a book, check if a book is rune-enchanted and get this rune.
        *   If so, check if first item is enchantable by this rune and if it's possible, show the result and save inventory to further click listening.
        */
        if(firstItem!=null && secondItem!=null && secondItem.getType().equals(Material.BOOK)) {
            Optional<Rune> rune =  getRuneManager().getRunes().stream().filter(r -> r.isEnchanted(secondItem)).findAny();
            if (rune.isPresent() && rune.get().isEnchantable(firstItem)){
                ItemStack newItem = firstItem.clone();
                rune.get().enchant(newItem);
                event.setResult(newItem);

                inventoryHashMap.put(event.getViewers().getFirst().getUniqueId(), inventory);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if (event.isLeftClick()){
            Inventory inventory = event.getClickedInventory();

            /*
             *   If it's anvil menu with our result, and player is clicking on a result, set ingredients to air and give result to cursor.
             */

            if (inventory instanceof  AnvilInventory anvilInventory){
                UUID uuid = event.getWhoClicked().getUniqueId();
                if (inventoryHashMap.containsKey(uuid) && anvilInventory.equals(inventoryHashMap.get(uuid))){
                    ItemStack result  = anvilInventory.getResult();

                    if (result!=null && anvilInventory.getResult().equals(event.getCurrentItem())) {
                        if (anvilInventory.getFirstItem()!=null && anvilInventory.getSecondItem()!=null) {
                            anvilInventory.setResult(new ItemStack(Material.AIR));
                            anvilInventory.setFirstItem(new ItemStack(Material.AIR));
                            anvilInventory.setSecondItem(anvilInventory.getSecondItem().subtract());

                            event.setCursor(result);
                            inventoryHashMap.remove(uuid);
                        }
                    }
                }
            }
        }
    }

}
