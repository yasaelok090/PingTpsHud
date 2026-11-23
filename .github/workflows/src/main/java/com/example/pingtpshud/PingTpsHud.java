package com.example.pingtpshud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PingTpsHud extends JavaPlugin implements Listener {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final Set<UUID> disabled = new HashSet<>();
    private BukkitTask task;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("hud").setExecutor(new HudCommand(this));

        task = Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (disabled.contains(player.getUniqueId())) continue;
                player.sendActionBar(buildText(player));
            }
        }, 20L, 20L);
    }

    @Override
    public void onDisable() {
        if (task != null) task.cancel();
    }

    private Component buildText(Player player) {
        int ping = player.getPing();
        double tps = Bukkit.getTPS()[0];
        String pingColor = ping < 60 ? "<green>" : ping < 150 ? "<yellow>" : "<red>";
        String text = pingColor + "Ping: <white>" + ping + " ms"
                + "  <dark_gray>|  <gray>TPS: <white>" + String.format("%.2f", tps);
        return MM.deserialize(text);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        disabled.remove(event.getPlayer().getUniqueId());
    }

    public Set<UUID> getDisabled() {
        return disabled;
    }
}
