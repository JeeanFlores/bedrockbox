package io.github.jeeanflores.bedrockBox.listeners;

import io.github.jeeanflores.bedrockBox.TntChallengeConstants;
import io.github.jeeanflores.bedrockBox.TntChallengePlugin;
import io.github.jeeanflores.bedrockBox.utils.CuboidUtils;
import io.github.jeeanflores.bedrockBox.utils.WorldCuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockListener implements Listener {

    private static final WorldCuboid cuboid = TntChallengeConstants.PROTECTED_CUBOID;

    private static BukkitRunnable runnable;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Block block = event.getBlock();
        Location location = block.getLocation();

        WorldCuboid cuboid = this.cuboid;

        if (cuboid.contains(location, true)) {

            int y = block.getY();

            if (block.getY() == cuboid.getMaxY()) {
                if (isLastLayerCompleted()) {

                    setCountdownTask();
                }
            }

            CuboidUtils.setBlockByLayer(cuboid, y, block);
        }
    }

    @EventHandler
    public void onTNTExplosion(EntityExplodeEvent event) {

        List<Block> blocksToExplode = new ArrayList<>();

        for (Block block : event.blockList()) {

            if (cuboid.contains(block.getLocation(), true)) {
                blocksToExplode.add(block);
            }
        }

        event.blockList().clear();
        event.blockList().addAll(blocksToExplode);
    }

    public static void setCountdownTask() {

        if (runnable != null) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("", "§aIniciando a Contagem...");
        }

        AtomicInteger countdown = new AtomicInteger(15);

        runnable =  new BukkitRunnable() {

            private int ticksElapsed = 0;
            private int currentDelay = 20;

            @Override
            public void run() {

                if (!isLastLayerCompleted()) {

                    cancel();

                    runnable = null;

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("", "§cCancelado");
                    }

                    return;
                }

                ticksElapsed++;

                if (ticksElapsed >= currentDelay) {
                    ticksElapsed = 0;

                    int count_ = countdown.getAndDecrement();

                    if (count_ == 0) {

                        cancel();

                        runnable = null;

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle("§a§lVITÓRIA", "");
                            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0F, 1.0F);
                        }

                        cuboid.destroy(false);
                        CuboidUtils.fireworksAtBase(cuboid);

                        return;
                    }

                    if (count_ <= 3) {
                        currentDelay = 70;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(String.format("§6%s", count_), "");
                        player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1.0F, 1.0F);
                    }
                }
            }
        };

        runnable.runTaskTimer(TntChallengePlugin.getInstance(), 0L, 1L);
    }


    private static boolean isLastLayerCompleted() {

        for (int x = cuboid.getMinX(); x <= cuboid.getMaxX(); x++) {
            for (int z = cuboid.getMinZ(); z <= cuboid.getMaxZ(); z++) {
                Block block = cuboid.getBukkitWorld().getBlockAt(x, cuboid.getMaxY(), z);


                if (block.getType() == Material.AIR) {
                    return false;
                }
            }
        }

        return true;
    }
}
