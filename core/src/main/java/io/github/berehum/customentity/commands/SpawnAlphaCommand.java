package io.github.berehum.customentity.commands;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnAlphaCommand implements CommandExecutor {

    private final INMSUtils nmsUtils;

    public SpawnAlphaCommand(INMSUtils nmsUtils) {
        this.nmsUtils = nmsUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to execute this command!");
            return true;
        }
        Player player = (Player) sender;
        CustomEntity customEntity = nmsUtils.createWolfAlpha(player.getLocation());
        customEntity.spawn();
        return true;
    }
}
