package me.tiaggy.re.listener;

import me.tiaggy.re.rune.VeinSmelt;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

import static me.tiaggy.re.rune.RuneManager.getRuneBook;

public class VeinSmeltListener implements Listener {
    private final VeinSmelt rune = new VeinSmelt();

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        LootTable table = event.getLootTable();
        NamespacedKey key = table.getKey();
        String id = key.toString();

        if (rune.getLootTables().contains(id)) {
            if (Math.random() <= rune.getSpawnChance()) {
                event.getLoot().add(getRuneBook(rune));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(player.getActiveItemHand());

        if (rune.isEnchanted(itemStack)){
            rune.onBlockBreak(event, itemStack);
        }
    }
}
