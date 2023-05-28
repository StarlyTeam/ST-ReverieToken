package net.starly.reverietoken.command;

import net.starly.reverietoken.ReverieToken;
import net.starly.reverietoken.context.MessageContent;
import net.starly.reverietoken.context.MessageType;
import net.starly.reverietoken.util.ReverieTokenUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReverieTokenExecutor implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        MessageContent content = MessageContent.getInstance();

        if (args.length > 0 && ("리로드".equalsIgnoreCase(args[0]) || "reload".equalsIgnoreCase(args[0]))) {

            if (!sender.hasPermission("starly.reverietoken.reload")) {
                content.getMessageAfterPrefix(MessageType.ERROR, "notAnOperator").ifPresent(sender::sendMessage);
                return false;
            }

            ReverieToken.getInstance().reloadConfig();
            content.initialize(ReverieToken.getInstance().getConfig());
            content.getMessageAfterPrefix(MessageType.NORMAL, "reloadComplete").ifPresent(sender::sendMessage);
            return true;
        }

        if (!(sender instanceof Player)) {
            content.getMessageAfterPrefix(MessageType.ERROR, "noConsoleCommand").ifPresent(sender::sendMessage);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            content.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
            return true;
        }

        if ("설정".equalsIgnoreCase(args[0]) || "set".equalsIgnoreCase(args[0])) {

            if (!player.hasPermission("starly.reverietoken.set")) {
                content.getMessageAfterPrefix(MessageType.ERROR, "notAnOperator").ifPresent(sender::sendMessage);
                return false;
            }

            if (args.length != 1) {
                content.getMessageAfterPrefix(MessageType.ERROR, "wrongCommand").ifPresent(sender::sendMessage);
                return false;
            }

            ReverieTokenUtil.setItemInHand(player);
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> tabList = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("starly.reverietoken.set")) tabList.add("설정");
            if (sender.hasPermission("starly.reverietoken.reload")) tabList.add("리로드");
            return StringUtil.copyPartialMatches(args[0], tabList, new ArrayList<>());
        }
        return Collections.emptyList();
    }
}