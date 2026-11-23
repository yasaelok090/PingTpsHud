package com.example.pingtpshud;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class HudCommand implements CommandExecutor {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private final PingTpsHud plugin;

    public HudCommand(PingTpsHud plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MM.deserialize("<red>Hanya bisa dipakai player."));
            return true;
        }

        Set<UUID> disabled = plugin.getDisabled();
        if (disabled.contains(player.getUniqueId())) {
            disabled.remove(player.getUniqueId());
            player.sendMessage(MM.deserialize("<green>HUD diaktifkan."));
        } else {
            disabled.add(player.getUniqueId());
            player.sendActionBar(Component.empty());
            player.sendMessage(MM.deserialize("<yellow>HUD dimatikan."));
        }
        return true;
    }
}
