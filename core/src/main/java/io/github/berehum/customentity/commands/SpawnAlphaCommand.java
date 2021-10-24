package io.github.berehum.customentity.commands;

import io.github.berehum.customentity.utils.nms.CustomEntity;
import io.github.berehum.customentity.utils.nms.INMSUtils;
import io.github.berehum.customentity.utils.nms.IWolfAlpha;
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
        IWolfAlpha customEntity = (IWolfAlpha) nmsUtils.createCustomEntity(CustomEntity.CustomEntityType.ALPHA_WOLF, player.getLocation());
        nmsUtils.spawnCustomEntity(customEntity);
        customEntity.createMembers().forEach(nmsUtils::spawnCustomEntity);
        return true;
    }
}
