package io.github.jeeanflores.bedrockBox.commands;

import io.github.jeeanflores.bedrockBox.TntChallengeConstants;
import io.github.jeeanflores.bedrockBox.TntChallengePlugin;
import io.github.jeeanflores.bedrockBox.listeners.BlockListener;
import io.github.jeeanflores.bedrockBox.utils.CuboidUtils;
import io.github.jeeanflores.bedrockBox.utils.WorldCuboid;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import static io.github.jeeanflores.bedrockBox.utils.CuboidUtils.setBlockByLayer;

public class TntChallengeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        WorldCuboid cuboid = TntChallengeConstants.PROTECTED_CUBOID;

        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("reset")) {

                if (args.length > 1) {

                    String text = args[1];

                    for (Player player0 : Bukkit.getOnlinePlayers()) {
                        if (!text.isEmpty()) {
                            player0.sendTitle("§a§l" + text, "§fEnviou §e1x Reset");
                        }
                    }

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            cuboid.getBukkitWorld().strikeLightning(cuboid.getCenter());
                            cuboid.destroy(false);
                        }
                    }.runTaskLater(TntChallengePlugin.getInstance(), 30L);

                }

                return true;
            }
            else if (args[0].equalsIgnoreCase("teste")) {

                if (args.length > 1) {

                    String text = args[1];

                    for (Player player0 : Bukkit.getOnlinePlayers()) {
                        if (!text.isEmpty()) {
                            player0.sendTitle("§a§l" + text, "§fEnviou §e1x YAY");
                        }
                    }

                    CuboidUtils.fireworksAtBase(cuboid);
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("rain")) {

                if (args.length > 1) {

                    String text = args[1];

                    for (Player player0 : Bukkit.getOnlinePlayers()) {
                        if (!text.isEmpty()) {
                            player0.sendTitle("§a§l" + text, "§fEnviou §e1x TNT Rain");
                        }
                    }

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            CuboidUtils.rainTnt(cuboid.getCenter(), 20, false);
                        }
                    }.runTaskLater(TntChallengePlugin.getInstance(), 30L);

                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("zeus")) {

                if (args.length > 1) {

                    String text = args[1];

                    for (Player player0 : Bukkit.getOnlinePlayers()) {
                        if (!text.isEmpty()) {
                            player0.sendTitle("§a§l" + text, "§fEnviou §e1x TNT de Zeus");
                        }
                    }

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            CuboidUtils.rainTnt(cuboid.getCenter(), 40, true);
                        }
                    }.runTaskLater(TntChallengePlugin.getInstance(), 30L);

                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("build")) {

                if (args.length > 1) {

                    String text = args[1];

                    for (Player player0 : Bukkit.getOnlinePlayers()) {
                        if (!text.isEmpty()) {
                            player0.sendTitle("§a§l" + text, "§fEnviou §eBuild");
                        }
                    }

                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            fillCuboidWithBlocks(cuboid);

                            for (Player player0 : Bukkit.getOnlinePlayers()) {

                                Location location = player0.getLocation();

                                if (cuboid.contains(location, true)) {

                                    player0.teleport(new Location(
                                            cuboid.getBukkitWorld(),
                                            location.getX(),
                                            cuboid.getMaxY() + 1,
                                            location.getZ(),
                                            location.getYaw(),
                                            location.getPitch()
                                    ));
                                }
                            }

                            BlockListener.setCountdownTask();
                        }
                    }.runTaskLater(TntChallengePlugin.getInstance(), 30L);

                }

                return true;
            }
            else if (args[0].equalsIgnoreCase("tnt")) {

                if (args.length > 1) {

                    String target = args[1];

                    Player player = Bukkit.getPlayer(target);

                    int amount = Integer.parseInt(args[2]);
                    int power = Integer.parseInt(args[3]);
                    double delay = Double.parseDouble(args[4]);

                    String sender0 = args[5];

                    if (!sender0.isEmpty()) {

                        player.sendTitle("", String.format("§c§l%s §fEnviou §ex%s TNT", sender0, (amount <= 0 ? 1 : amount)));
                    }

                    CuboidUtils.detonateFirework(player.getLocation(), 15);

                    if (amount > 1) {

                        new BukkitRunnable() {
                            int count = 0;

                            @Override
                            public void run() {

                                if (count < amount) {

                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

                                    CuboidUtils.sendTnt(player.getLocation(), power, delay);

                                    count++;
                                } else {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(TntChallengePlugin.getInstance(), 0L, 2L);

                    } else {

                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

                        CuboidUtils.sendTnt(player.getLocation(), power, delay);
                    }

                }
            }

            return true;
        }

        String version = TntChallengePlugin.getInstance().getDescription().getVersion();

        Player player = (Player) sender;

        player.sendMessage(String.format("§aTNTChallengePlugin §ev%s §acriado por §eJeanFlores§a.", version));
        return true;

    }

    private void fillCuboidWithBlocks(WorldCuboid cuboid) {

        for (int x = cuboid.getMinX(); x <= cuboid.getMaxX(); x++) {
            for (int y = cuboid.getMinY(); y <= cuboid.getMaxY(); y++) {
                for (int z = cuboid.getMinZ(); z <= cuboid.getMaxZ(); z++) {
                    Block block = cuboid.getBukkitWorld().getBlockAt(x, y, z);
                    setBlockByLayer(cuboid, y, block);
                }
            }
        }
    }

}
