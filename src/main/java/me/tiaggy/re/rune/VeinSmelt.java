package me.tiaggy.re.rune;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.tiaggy.re.RuneEnchantments.getInstance;
import static me.tiaggy.re.utils.MessageUtils.colorize;

@Getter
public class VeinSmelt extends Rune {

    private final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(
            new File(getInstance().getDataFolder(), "config.yml")
    );

    private final double spawnChance = configuration.getDouble("veinsmelt.spawn_chance");

    private final boolean isVeinMine = configuration.getBoolean("veinsmelt.VeinMine");

    private final boolean isAutoSmelt = configuration.getBoolean("veinsmelt.AutoSmelt");

    private final List<String> lootTables = List.of(
            "minecraft:chests/nether_bridge",
            "minecraft:chests/stronghold_library"
    );

    private final Set<SmeltRecipe> smeltRecipes = Set.of(
            new SmeltRecipe(Material.GOLD_ORE, Material.GOLD_INGOT),
            new SmeltRecipe(Material.DEEPSLATE_GOLD_ORE, Material.GOLD_INGOT),
            new SmeltRecipe(Material.IRON_ORE, Material.IRON_INGOT),
            new SmeltRecipe(Material.DEEPSLATE_IRON_ORE, Material.IRON_INGOT)
    );

    public VeinSmelt() {
        super(Component.text(colorize("&6Rune: VeinSmelt")),
                Component.text(colorize("&eAncient Rune infused with smelting power...")),
                new NamespacedKey(getInstance(), "veinsmelt"),
                Tag.ITEMS_ENCHANTABLE_MINING);
    }

    public void onBlockBreak(BlockBreakEvent event, ItemStack tool) {
        Block block = event.getBlock();

        Material material = block.getType();

        // filter ores only
        if (material.name().contains("ORE")) {

            SmeltRecipe smeltRecipe = null;
            // if AutoSmelt is enabled, try to find SmeltRecipe for block material, if found, cancel event (we need to change the drop)
            if(isAutoSmelt) {
                smeltRecipe = smeltRecipes.stream().filter(recipe -> recipe.getReactant().equals(material)).findFirst().orElse(null);
                if (smeltRecipe != null) {
                    event.setCancelled(true);
                }
            }

            // if VeinMine is enabled break ores in 3x3 area
            if(isVeinMine) {
                breakCube(block, material, new ArrayList<>(), smeltRecipe, tool);
            }else{
                breakBlock(block, smeltRecipe, tool);
            }
        }
    }

    private void breakCube(Block block, Material material, List<Block> checkedBlocks, SmeltRecipe smeltRecipe, ItemStack tool) {
        // 3x3 area limit
        if (checkedBlocks.size() > 27) return;

        // add block in checkedBlocks for recursive of this method
        checkedBlocks.add(block);
        Block firstBlock = checkedBlocks.getFirst();

        // if block is the same material, recursive break its adjacent unchecked blocks that are in 3x3 area from first block
        if (material.equals(block.getType())) {
            if (Math.abs(firstBlock.getX() - block.getX()) < 2 &&
                    Math.abs(firstBlock.getY() - block.getY()) < 2 &&
                    Math.abs(firstBlock.getZ() - block.getZ()) < 2) {
                breakBlock(block, smeltRecipe, tool);

                List<Block> adjBlocks = List.of(
                        block.getRelative(1, 0, 0),
                        block.getRelative(-1, 0, 0),
                        block.getRelative(0, 1, 0),
                        block.getRelative(0, -1, 0),
                        block.getRelative(0, 0, 1),
                        block.getRelative(0, 0, -1)
                );
                adjBlocks.forEach(
                        b -> {
                            if (!checkedBlocks.contains(b)) {
                                breakCube(b, material, checkedBlocks, smeltRecipe, tool);
                            }
                        }
                );
            }
        }
    }

    private void breakBlock(Block block, SmeltRecipe smeltRecipe, ItemStack tool){
        if (smeltRecipe == null) {
            block.breakNaturally(tool);
        } else {

            // Change drop depending on SmeltRecipe
            block.setType(Material.AIR);
            Location location = block.getLocation();
            World world = location.getWorld();
            world.dropItem(location, new ItemStack(smeltRecipe.getProduct()));
            world.spawnParticle(Particle.LAVA, location, 1);
            world.playSound(location, Sound.BLOCK_FURNACE_FIRE_CRACKLE,1, 1);
        }
    }

    @AllArgsConstructor
    @Getter
    private class SmeltRecipe{

        private final Material reactant;

        private final Material product;

    }
}
