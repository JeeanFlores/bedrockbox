package io.github.jeeanflores.bedrockBox.utils;

import io.github.jeeanflores.bedrockBox.TntChallengePlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CuboidUtils {

    public static void sendTnt(Location location, float range, Double fuse) {

        TNTPrimed tntPrimed = location.getWorld().spawn(location, TNTPrimed.class);

        location.getWorld().spawnParticle(Particle.FLAME, location, 10, 0.5, 0.5, 0.5, 0.01);

        tntPrimed.setFuseTicks(getDelay(fuse));

        tntPrimed.setYield(range);

    }

    private static int getDelay(Double fuse) {
        return (fuse != null) ? Math.max(1, (int) (fuse * 20)) : 80;
    }

    public static void rainTnt(Location location, int amount, boolean lightning) {

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {

                if (count < amount) {

                    int[][] offsets = {
                            {0, 12, 0},
                            {0, 12, 2},
                            {2, 12, 2},
                            {0, 12, -2},
                            {-2, 12, -2},
                            {-2, 12, 2},
                            {2, 12, 0},
                            {-2, 12, 0},
                            {2, 12, -2}
                    };

                    for (int[] offset : offsets) {
                        Location location0 = location.clone().add(offset[0], offset[1], offset[2]);

                        if (lightning) {
                            location0.getWorld().strikeLightning(location0);
                        }

                        CuboidUtils.detonateFirework(location0, 15);

                        CuboidUtils.sendTnt(location0, 3, 2d);
                    }

                    count++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(TntChallengePlugin.getInstance(), 0L, 10L);



    }

    public static void detonateFirework(Location location, int delay) {

        Firework firework = location.getWorld().spawn(location, Firework.class);

        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.CREEPER)
                .withColor(Color.PURPLE, Color.YELLOW, Color.BLUE, Color.RED)
                .withColor(Color.GREEN)
                .withFade(Color.fromRGB(16761035))
                .flicker(false)
                .trail(false)
                .build();

        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(effect);
        meta.setPower(1);

        firework.setFireworkMeta(meta);

        new BukkitRunnable() {
            @Override
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(TntChallengePlugin.getInstance(), delay);
    }

    public static void fireworksAtBase(WorldCuboid cuboid) {
        World world = cuboid.getBukkitWorld();

        for (int x = cuboid.getMinX(); x <= cuboid.getMaxX(); x++) {
            for (int z = cuboid.getMinZ(); z <= cuboid.getMaxZ(); z++) {
                Location location = new Location(world, x, cuboid.getMinY(), z);
                if (cuboid.contains(location, true)) {
                    detonateFirework(center(location), 20);
                }
            }
        }
    }

    public static Location center(Location loc) {
        return new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static void setBlockByLayer(WorldCuboid cuboid, int y, Block block) {
        int layerThreshold = 3;

        if (y >= cuboid.getMinY() && y < cuboid.getMinY() + layerThreshold) {

            block.setType(Material.IRON_BLOCK);

        } else if (y >= cuboid.getMinY() + layerThreshold && y < cuboid.getMinY() + 2 * layerThreshold) {

            block.setType(Material.DIAMOND_BLOCK);
        } else if (y >= cuboid.getMinY() + 2 * layerThreshold && y < cuboid.getMinY() + 3 * layerThreshold) {

            block.setType(Material.EMERALD_BLOCK);
        } else if (y >= cuboid.getMinY() + 3 * layerThreshold && y <= cuboid.getMaxY()) {

            block.setType(Material.AMETHYST_BLOCK);
        }
    }
}
